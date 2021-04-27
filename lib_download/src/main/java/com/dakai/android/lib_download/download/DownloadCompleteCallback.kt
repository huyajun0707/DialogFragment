package com.dakai.android.lib_download.download

/**
 * 下载完成回调
 * @author      ： CuiYancey <cuiyuancheng0322@gmail.com>
 * @version     ： 1.0
 * @date        ： 2019-11-15 18:08
 */
interface DownloadCompleteCallback {
    fun onDownloadSuccess(downloadId: Long, downloadUrl: String)
    fun onCancel(downloadId: Long, downloadUrl: String, currentSize: Int, totalSize: Int)
}