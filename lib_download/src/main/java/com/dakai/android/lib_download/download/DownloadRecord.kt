package com.dakai.android.lib_download.download

import java.util.concurrent.ConcurrentHashMap

/**
 * @author      ： CuiYancey <cuiyuancheng0322@gmail.com>
 * @version     ： 1.0
 * @date        ： 2019-11-12 16:46
 */
class DownloadRecord {

    companion object {
        val instance: DownloadRecord by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        DownloadRecord() }
    }

    /**下载url记录，key是DownloadManager的下载id*/
    val downloadUrlRecord: ConcurrentHashMap<Long, String> = ConcurrentHashMap(5)
    /**数据库中的所有未下载成功的任务, key是DownloadManager的下载url地址*/
//    val downloadEntities: ConcurrentHashMap<String, DownloadEntity> = ConcurrentHashMap()
}