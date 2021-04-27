package com.dakai.android.lib_download.download

import android.app.DownloadManager
import android.net.Uri
import java.io.File

/**
 * @author      ： CuiYancey <cuiyuancheng0322@gmail.com>
 * @version     ： 1.0
 * @date        ： 2019-11-12 15:30
 */
class DownloadRequest private constructor() {

    /**url下载地址*/
    private var downloadUrl: String = ""
    /**文件名*/
    private var fileName: String = ""
    /**文件路径*/
    private var file: File? = null
    /**通知栏标题*/
    private var title: String = ""
    /**通知栏描述*/
    private var description: String = ""
    /**允许移动网络下载*/
    private var allowedNetworkMobile: Boolean = false

    class Builder {
        val request: DownloadRequest = DownloadRequest()

        /**设置下载[url]*/
        fun setDownloadUrl(url: String): Builder {
            request.downloadUrl = url
            return this
        }

        /**设置[fileName]文件名*/
        fun setFileName(fileName: String): Builder {
            request.fileName = fileName
            return this
        }

        /**设置[file]保存文件，用于下载时获取uri*/
        fun setFile(file: File): Builder {
            request.file = file
            return this
        }

        /**设置[title]下载通知栏标题*/
        fun setTitle(title: String): Builder {
            request.title = title
            return this
        }

        /**设置[description]下载通知栏描述*/
        fun setDescription(description: String): Builder {
            request.description = description
            return this
        }

        /**设置[allowedNetworkMobile]允许移动网络下载，默认为false*/
        fun setAllowedNetworkMobile(allowedNetworkMobile: Boolean): Builder {
            request.allowedNetworkMobile = allowedNetworkMobile
            return this
        }

        /**通过DownloadRequest对象创建[DownloadManager.Request]对象*/
        fun create(): DownloadRequest {
            return request
        }
    }

    fun getDownloadUrl(): String{
        return downloadUrl
    }

    /**
     * 获取[DownloadManager.Request]
     */
    fun getDownloadRequest(): DownloadManager.Request {
        return DownloadManager.Request(Uri.parse(downloadUrl)).apply {
            if (file != null) {
                //指定本地保存路径
                setDestinationUri(Uri.fromFile(file))
            } else {
                //下载的本地路径，表示设置下载地址为SD卡的Download文件夹，文件名为single-test.apk
                setDestinationInExternalPublicDir("Download", fileName)
            }
            /**设置用于下载时的网络状态*/
            if (allowedNetworkMobile) {
                setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            } else {
                setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
            }
            /**设置通知栏是否可见*/
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            if (title.isNotEmpty()) {
                setTitle(title)
            }
            if (description.isNotEmpty()) {
                setDescription(description)
            }
            //添加请求头
            // downloadRequest.addRequestHeader()
            /**设置漫游状态下是否可以下载*/
            setAllowedOverRoaming(false)
            /**如果我们希望下载的文件可以被系统的Downloads应用扫描到并管理，我们需要调用Request对象的setVisibleInDownloadsUi方法，传递参数true.*/
//            setVisibleInDownloadsUi(true)
            /**设置文件保存路径  /Android/data/包名/file/phoenix/phoenix.apk*/
//          downloadRequest.setDestinationInExternalFilesDir(applicationContext,"phoenix", "phoenix.apk")
        }
    }
}