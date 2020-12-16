package com.yidian.upgrade.listener

interface OnDownloadListener {
    fun onDownloadStart()
    fun onDownloadProgress(progress: Float, speed: Float)
    fun onDownloadFailed()
    fun onDownloadSuccess()
}