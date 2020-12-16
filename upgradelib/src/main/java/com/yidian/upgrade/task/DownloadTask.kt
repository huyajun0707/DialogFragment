package com.yidian.upgrade.task

import android.app.DownloadManager
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.FileUtils
import androidx.core.app.NotificationCompat
import com.rayhahah.library.core.EDownload
import com.yidian.upgrade.Constants
import com.yidian.upgrade.UpgradeApp
import com.yidian.upgrade.listener.OnDownloadListener
import com.yidian.upgrade.utils.ApplicationUtil
import com.yidian.upgrade.utils.FilePathUtil
import com.yidian.upgrade.utils.UPLog
import java.io.File
import kotlin.reflect.KParameter

/**
 * @description: 下载管理类
 * @author: huyajun
 * @date:  2020/12/15
 **/

class DownloadTask {


    var url: String? = null
    var file: File? = null
    var listener: OnDownloadListener? = null
    private var notificationManager: NotificationManager? = null
    private var builder: NotificationCompat.Builder? = null
    private val NOTIFICATION_ID = 0x0001
    private var time: Long = 0
    private var upgradeApp: UpgradeApp? = null

    fun init(upgradeApp: UpgradeApp, url: String, listener: OnDownloadListener) {
        this.url = url
        this.listener = listener
        this.upgradeApp = upgradeApp
    }

    init {
        file = File(upgradeApp?.filePath)
        time = System.currentTimeMillis()
        listener?.onDownloadStart()
        notificationManager =
            upgradeApp?.context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        builder = NotificationCompat.Builder(upgradeApp?.context)
        builder!!.setSmallIcon(upgradeApp?.resIcon!!)
        builder!!.setContentTitle("版本更新")
        builder!!.setContentText("准备下载...")
        builder!!.setContentIntent(
            PendingIntent.getActivity(
                upgradeApp?.context,
                0,
                Intent(),
                0
            )
        )
        notificationManager!!.notify(NOTIFICATION_ID, builder!!.build())
    }

    private fun onProgressUpdate(value: Float, total: Long) {
        UPLog.print("onProgressUpdate")
        if (listener != null) {
            var totalTime = (System.currentTimeMillis() - time) / 1000
            if (totalTime == 0L) {
                totalTime += 1
            }
            var temp = (value?.times(100.0f) ?: 0f)
            var progress = 0
            total?.let {
                progress = (temp / it).toInt()
            }
            if (value != null) {
                listener?.onDownloadProgress(progress.toFloat(), value / totalTime)
            }
            builder?.setContentText("当前进度：" + progress + Constants.PERCENT);
            notificationManager?.notify(NOTIFICATION_ID, builder?.build());
        }
    }


    private fun onDownloadExecute(result: Boolean) {
        UPLog.print("onPostExecute")
        if (result) {
            ApplicationUtil.INSTANCE.chmod(Constants.PERMISSION, file?.absolutePath)
            builder!!.setContentIntent(
                PendingIntent.getActivity(
                    upgradeApp?.context,
                    0,
                    Intent(Intent.ACTION_VIEW).setDataAndType(
                        Uri.parse(Constants.FILE_HEAD + file?.absolutePath),
                        Constants.FILE_TYPE
                    ),
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
            builder!!.setContentText("下载完成,点击安装...")
            builder!!.setDefaults(Notification.DEFAULT_ALL)
            notificationManager!!.notify(NOTIFICATION_ID, builder!!.build())
            listener?.onDownloadSuccess()
        } else {
            builder!!.setContentText("下载失败,稍后请重试...")
            notificationManager!!.notify(NOTIFICATION_ID, builder!!.build())
            listener?.onDownloadFailed()
        }
    }

    fun download(url: String) {
        FilePathUtil.INSTANCE.forceMkdir(file?.parentFile)
        EDownload(
            url,
            file?.absolutePath!!,
            upgradeApp?.fileName!!,
            success = {
                onDownloadExecute(true)
            },
            fail = { call, e ->
                onDownloadExecute(false)
            },
            progress = { value, total ->

                onProgressUpdate(value, total)
            }
        )
    }

    companion object {
        @JvmStatic
        val INSTANCE: DownloadTask by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { DownloadTask() }
    }
}

val DownloadManager: DownloadTask = DownloadTask.INSTANCE