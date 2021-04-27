package com.dakai.android.lib_download.download

import android.app.DownloadManager
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import android.os.Message
import android.util.Log
import com.dakai.android.lib_database.DbManager
import com.dakai.android.lib_database.dao.DownloadEntityDao
import com.dakai.android.lib_database.entity.DownloadEntity
import java.lang.NumberFormatException

/**
 * 下载进度监听
 * @author      ： CuiYancey <cuiyuancheng0322@gmail.com>
 * @version     ： 1.0
 * @date        ： 2019-11-12 11:40
 */
class DownloadProgressObserver(
    private val handler: Handler,
    private val downloadManager: DownloadManager?
) : ContentObserver(handler) {

    /**进度监听集合*/
    private var listeners: ArrayList<Downloader.DownloadListener> = ArrayList()
    /**定时任务线程池*/
//    private var scheduledThreadPoolExecutor: ScheduledThreadPoolExecutor =
//        ScheduledThreadPoolExecutor(3)
    private val downloadEntity: DownloadEntity = DownloadEntity()

    private val task = Runnable {
        //        while (!Thread.currentThread().isInterrupted) {
//            try {
//                do your work
//                Thread.sleep(5000)
//            } catch (e: InterruptedException) {
//                e.printStackTrace()
//                //如果抛出异常则再次设置中断请求
//                Thread.currentThread().interrupt()
//            }
//        }
    }

    fun addDownloadListener(listener: Downloader.DownloadListener) {
        listeners.add(listener)
    }

    override fun onChange(selfChange: Boolean, uri: Uri?) {
        super.onChange(selfChange, uri)
        //uri为发生变化的数据库+id （）
        Log.e("cui_", "发生变化的uri：$uri")
        uri?.toString()?.let {
            //获取发生变化的下载id
            val id = it.substring(it.lastIndexOf("/") + 1)
            try {
                updateProgress(id.toLong())
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
        }
    }

    private fun updateProgress(downloadId: Long) {
        var c: Cursor? = null
        try {
            val query = DownloadManager.Query().setFilterById(downloadId)
            c = downloadManager?.query(query)
            if (c != null && c.moveToFirst()) {
                downloadEntity.downloadId = downloadId
                downloadEntity.downloadUrl =
                    c.getString(c.getColumnIndexOrThrow(DownloadManager.COLUMN_URI))
                downloadEntity.currentSize =
                    c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                downloadEntity.totalSize =
                    c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                downloadEntity.status =
                    c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))
                downloadEntity.reason =
                    parseReason(c.getInt(c.getColumnIndex(DownloadManager.COLUMN_REASON)))
                //DownloadManager.COLUMN_URI字段代表的是下载的url地址
//                    val uri = c.getString(c.getColumnIndexOrThrow(DownloadManager.COLUMN_URI))
                //本地文件保存地址，例如file:///storage/emulated/0/Download/文件名
//                    val localUri = c.getString(c.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_URI))

                //根据downloadId查询数据，如果为null就new一个，最后更新数据并保存到数据库
                dbHandle(downloadId, downloadEntity.downloadUrl, downloadEntity.status)
                //发送进度消息
                Message.obtain(handler, Downloader.DOWNLOAD_HANDLER_WHAT, downloadEntity)
                    .sendToTarget()
            }
        } finally {
            c?.close()
        }
    }

    private fun dbHandle(id: Long, url: String, status: Int) {
        ((DbManager.getInstance().queryByProperty(
            DownloadEntity::class.java,
            DownloadEntityDao.Properties.DownloadId,
            id
        ) as DownloadEntity?) ?: DownloadEntity()).apply {
            Log.e("cui_", "查出来的数据的id：${this.id}")
            downloadId = id
            downloadUrl = url
            this.status = status
            //更新数据
            DbManager.getInstance().save(DownloadEntity::class.java, this)
        }
    }

    private fun parseReason(reasonCode: Int): String {
        return when (reasonCode) {
            //some possibly transient error occurred but we can't resume the download
            DownloadManager.ERROR_CANNOT_RESUME -> "可能发生了某些瞬时错误，导致无法下载"
            //no external storage device was found. Typically, this is because the SD card is not mounted
            DownloadManager.ERROR_DEVICE_NOT_FOUND -> "未发现外部存储设备，SD卡可能未装载"
            //the requested destination file already exists (the download manager will not overwrite an existing file)
            DownloadManager.ERROR_FILE_ALREADY_EXISTS -> "文件已下载，请勿重复下载"
            //a storage issue arises which doesn't fit under any other error code
            DownloadManager.ERROR_FILE_ERROR -> "文件存储错误，原因未知"
            //an error receiving or processing data occurred at the HTTP level
            DownloadManager.ERROR_HTTP_DATA_ERROR -> "HTTP数据异常"
            //here was insufficient storage space. Typically, this is because the SD card is full
            DownloadManager.ERROR_INSUFFICIENT_SPACE -> "SD卡存储空间不足"
            //there were too many redirects
            DownloadManager.ERROR_TOO_MANY_REDIRECTS -> "重定向过多"
            //an HTTP code was received that download manager can't handle
            DownloadManager.ERROR_UNHANDLED_HTTP_CODE -> "未知的HTTP代码"
            //the download has completed with an error that doesn't fit under any other error code
            DownloadManager.ERROR_UNKNOWN -> "下载失败，原因未知"
            //the download exceeds a size limit for downloads over the mobile network and the download manager is waiting for a Wi-Fi connection to proceed
            DownloadManager.PAUSED_QUEUED_FOR_WIFI -> "超过移动网络下载的大小限制，请连接Wi-Fi后继续下载"
            //the download is paused for some other reason.
            DownloadManager.PAUSED_UNKNOWN -> "未知原因导致下载暂停"
            //the download is waiting for network connectivity to proceed
            DownloadManager.PAUSED_WAITING_FOR_NETWORK -> "未连接到网络"
            //the download is paused because some network error occurred and the download manager is waiting before retrying the request
            DownloadManager.PAUSED_WAITING_TO_RETRY -> "发生网络错误，等待重试"
            else -> ""
        }
    }
}