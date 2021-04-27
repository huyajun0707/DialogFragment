package com.dakai.android.lib_download.download

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.util.Log
import androidx.1core.content.ContextCompat.startActivity
import com.dakai.android.lib_database.DbManager
import com.dakai.android.lib_database.dao.DownloadEntityDao
import com.dakai.android.lib_database.entity.DownloadEntity

/**
 * DownloadManager下载成功广播接收者
 * @author      ： CuiYancey <cuiyuancheng0322@gmail.com>
 * @version     ： 1.0
 * @date        ： 2019-11-13 18:05
 */
class DownloadCompleteReceiver(private val callback: DownloadCompleteCallback) :
    BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        //下载完成后，下载管理会发出DownloadManager.ACTION_DOWNLOAD_COMPLETE这个广播，并传递downloadId作为参数
        val downloadId = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1) ?: -1
        Log.e("cui_", "下载完成，$downloadId-${DownloadRecord.instance.downloadUrlRecord[downloadId]}")

        when (intent?.action) {
            DownloadManager.ACTION_DOWNLOAD_COMPLETE -> {
                Log.e("cui_", "下载完成通知")
                context?.let {
                    val downloadManager: DownloadManager? =
                        it.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
                    val query = DownloadManager.Query().setFilterById(downloadId)
                    val c: Cursor? = downloadManager?.query(query)
                    if (c != null) {
                        if (c.moveToFirst()) {
                            //下载成功，更新本地数据库数据，移除数据的工作在Downloader中统一处理
                            downloadSuccessHandle(downloadId, c)
                        } else {
                            Log.e("cui_", "用户手动取消下载")
                            //用户手动取消下载
                            callback.onCancel(downloadId, "", 0, 100)
                        }
                    }
                }
            }
            DownloadManager.ACTION_NOTIFICATION_CLICKED -> {
                //do your work
                context?.let { goDownloadPage(it) }
            }
            else -> {
            }
        }

    }

    private fun downloadSuccessHandle(downloadId: Long, c: Cursor) {
        (DbManager.getInstance().queryByProperty(
            DownloadEntity::class.java,
            DownloadEntityDao.Properties.DownloadId,
            downloadId
        ) as DownloadEntity?)?.apply {
            //更新下载状态
            status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))
            DbManager.getInstance().save(DownloadEntity::class.java, this)
            Log.e("cui_", "下载成功")
            callback.onDownloadSuccess(
                downloadId,
                c.getString(c.getColumnIndexOrThrow(DownloadManager.COLUMN_URI))
            )
        }
    }

    /**
     * 隐式跳转到系统下载页
     */
    private fun goDownloadPage(context: Context) {
        val intent = Intent(DownloadManager.ACTION_VIEW_DOWNLOADS)
        startActivity(context, intent, null)
    }

}