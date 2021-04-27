package com.dakai.android.lib_download.download

import android.app.DownloadManager
import android.content.Context
import android.content.IntentFilter
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.dakai.android.lib_database.DbManager
import com.dakai.android.lib_database.dao.DownloadEntityDao
import com.dakai.android.lib_database.entity.DownloadEntity
import java.lang.ref.WeakReference

/**
 * DownloadManager下载封装。
 * @author      ： CuiYancey <cuiyuancheng0322@gmail.com>
 * @version     ： 1.0
 * @date        ： 2019-11-12 11:22
 */
class Downloader private constructor(private val activity: FragmentActivity) :
    DownloadCompleteCallback {

    override fun onDownloadSuccess(downloadId: Long, downloadUrl: String) {
        if (listeners.isNotEmpty())
            listeners.forEach { it.onSuccess(downloadId, downloadUrl) }
    }

    override fun onCancel(downloadId: Long, downloadUrl: String, currentSize: Int, totalSize: Int) {
        if (listeners.isNotEmpty())
            listeners.forEach { it.onFailed(downloadId, if (downloadUrl.isNotEmpty()) downloadUrl else DownloadRecord.instance.downloadUrlRecord[downloadId]?:"", currentSize, totalSize, "取消下载") }
    }

    companion object {
        /**下载任务handler信息的what标记*/
        const val DOWNLOAD_HANDLER_WHAT = 1101
        @Volatile
        private var instance: Downloader? = null

        @JvmStatic
        fun getInstance(activity: FragmentActivity) =
            instance ?: synchronized(this) {
                instance ?: Downloader(activity).also { instance = it }
            }
    }

    private val handler: Handler = Handler(Handler.Callback {
        if (it.what == DOWNLOAD_HANDLER_WHAT) {
            Log.e("cui_", "收到handler消息")
            val downloadEntity: DownloadEntity = it.obj as DownloadEntity
            //下载状态&进度回调
            statusHandle(
                downloadEntity.status,
                downloadEntity.downloadId,
                downloadEntity.downloadUrl,
                downloadEntity.currentSize,
                downloadEntity.totalSize,
                downloadEntity.reason
            )
        }
        false
    })

    /**activity弱引用*/
    private var activityReference: WeakReference<FragmentActivity> = WeakReference(activity)
    /**下载管理器*/
    private var downloadManager: DownloadManager? =
        activityReference.get()?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
    /**下载进度监听*/
    private var progressObserver: DownloadProgressObserver =
        DownloadProgressObserver(handler, downloadManager)
    /**下载通知栏广播接收者*/
    private val downloadReceiver = DownloadCompleteReceiver(this)
    /**进度监听集合*/
    private var listeners: ArrayList<DownloadListener> = ArrayList()

    fun addDownloadListener(listener: DownloadListener) {
        listeners.add(listener)
        progressObserver.addDownloadListener(listener)
    }

    /**
     * 恢复下载记录。
     * 因为本地数据库也对下载数据进行了记录，所以当APP在进程被杀死之后也能下载成功，这就导致了本地数据库数据下载状态更新不及时，因此需要在每次恢复下载记录时去同步系统下载库中的状态
     */
    fun restoreDownload() {
        DownloadRecord.instance.downloadUrlRecord.clear()
        //恢复所有没有下载成功的下载记录
        (DbManager.getInstance().queryMultiObjects(
            DownloadEntity::class.java,
            listOf("notEq"),
            listOf(DownloadEntityDao.Properties.Status),
            listOf(DownloadManager.STATUS_SUCCESSFUL)
        ) as ArrayList<DownloadEntity>?)?.forEach {
            //根据downloadId查询系统下载库中的数据
            activityReference.get()?.let { activity ->
                recordHandle(activity, it)
            }
        }
        Log.e(
            "cui_",
            "resume，downloadUrlRecord长度：${DownloadRecord.instance.downloadUrlRecord.size}"
        )
    }

    /**
     * 下载记录处理
     */
    private fun recordHandle(activity: FragmentActivity, it: DownloadEntity) {
        val downloadManager: DownloadManager? =
            activity.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
        val query = DownloadManager.Query().setFilterById(it.downloadId)
        val c: Cursor? = downloadManager?.query(query)
        if (c != null && c.moveToFirst()) {
            //下载成功||取消下载，更新本地数据库数据，移除数据的工作在Downloader中统一处理
            (DbManager.getInstance().queryByProperty(
                DownloadEntity::class.java,
                DownloadEntityDao.Properties.DownloadId,
                it.downloadId
            ) as DownloadEntity?)?.apply {
                //更新下载状态
                status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))
                DbManager.getInstance().save(DownloadEntity::class.java, this)
                //缓存下载记录到内存
                DownloadRecord.instance.downloadUrlRecord[it.downloadId] = it.downloadUrl
            }
        }
    }

    /**
     * 开始下载
     * [request]    DownloadRequest是DownloadManager.Request的包装类
     */
    fun startDownload(request: DownloadRequest) {
        if (activityReference.get() == null) {
            activityReference = WeakReference(activity)
        }
        activityReference.get()?.let {
            if (!DownloadUtils.downLoadMangerIsEnable(it)) {
                //如果下载管理器没有开启，那么就跳转到下载管理器设置页去开启
                DownloadUtils.openDownloadManager(it)
            } else {
                //downloadManager判空
                if (downloadManager == null) {
                    downloadManager =
                        activityReference.get()?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
                }
                downloadManager?.apply {
                    try {
                        //开始执行下载任务
                        val downloadId = enqueue(request.getDownloadRequest())
                        Log.e(
                            "cui_",
                            "保存下载记录，downloadId：$downloadId    url：${request.getDownloadUrl()}"
                        )
                        //保存下载记录
                        DownloadRecord.instance.downloadUrlRecord[downloadId] =
                            request.getDownloadUrl()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    /**
     * 下载状态&进度处理
     * [status]         下载状态
     * [downloadId]     下载id
     * [url]            下载url
     * [currentSize]    当前下载长度
     * [totalSize]      总长度
     * [reason]         下载出错|暂停的原因
     */
    private fun statusHandle(
        status: Int,
        downloadId: Long,
        url: String,
        currentSize: Int,
        totalSize: Int,
        reason: String
    ) {
        when (status) {
            DownloadManager.STATUS_PENDING -> {
                //回调进度
                if (listeners.isNotEmpty())
                    listeners.forEach { it.onPending(downloadId, url, "等待中...") }
                //更新记录的下载任务的状态
//                DownloadRecord.instance.downloadEntities[url]?.status = DownloadManager.STATUS_PENDING
            }
            DownloadManager.STATUS_RUNNING -> {
                if (listeners.isNotEmpty())
                    listeners.forEach { it.onRunning(downloadId, url, currentSize, totalSize) }
                //更新记录的下载任务的状态
//                DownloadRecord.instance.downloadEntities[url]?.status = DownloadManager.STATUS_RUNNING
            }
            DownloadManager.STATUS_PAUSED -> {
                if (listeners.isNotEmpty())
                    listeners.forEach {
                        it.onPause(
                            downloadId,
                            url,
                            currentSize,
                            totalSize,
                            reason
                        )
                    }
                //更新记录的下载任务的状态
//                DownloadRecord.instance.downloadEntities[url]?.status = DownloadManager.STATUS_PAUSED
            }
            DownloadManager.STATUS_SUCCESSFUL -> {
                if (listeners.isNotEmpty())
                    listeners.forEach { it.onSuccess(downloadId, url) }
                //下载成功，删除下载记录
                DownloadRecord.instance.downloadUrlRecord.remove(downloadId)
//                DownloadRecord.instance.downloadEntities.remove(url)
            }
            DownloadManager.STATUS_FAILED -> {
                if (listeners.isNotEmpty())
                    listeners.forEach {
                        it.onFailed(
                            downloadId,
                            url,
                            currentSize,
                            totalSize,
                            reason
                        )
                    }
                //下载失败，删除下载记录
                DownloadRecord.instance.downloadUrlRecord.remove(downloadId)
//                DownloadRecord.instance.downloadEntities.remove(url)
            }
            else -> {
            }
        }
    }

    /**
     * 注册下载内容提供者监听&下载通知栏广播接收者
     */
    fun registerContentObserver() {
        activityReference.get()?.apply {
            contentResolver.registerContentObserver(
                Uri.parse("content://downloads/"),
                true,
                progressObserver
            )
            val filter = IntentFilter()
            filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
            filter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED)
            //注册广播接收者
            Log.e("cui_", "注册下载完毕广播")
            registerReceiver(downloadReceiver, filter)
        }
    }

    /**
     * 注销下载内容提供者监听&下载通知栏广播接收者
     */
    fun unregisterContentObserver() {
        activityReference.get()?.apply {
            contentResolver.unregisterContentObserver(progressObserver)
            //注销广播接收者
            Log.e("cui_", "注销下载完毕广播")
            unregisterReceiver(downloadReceiver)
        }
    }

    interface DownloadListener {
        fun onPending(downloadId: Long, url: String, reason: String)
        fun onRunning(downloadId: Long, url: String, currentSize: Int, totalSize: Int)
        fun onPause(downloadId: Long, url: String, currentSize: Int, totalSize: Int, reason: String)
        fun onSuccess(downloadId: Long, url: String)
        fun onFailed(
            downloadId: Long,
            url: String,
            currentSize: Int,
            totalSize: Int,
            reason: String
        )
    }
}