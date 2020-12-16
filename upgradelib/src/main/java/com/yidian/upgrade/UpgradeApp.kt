package com.yidian.upgrade

import android.content.Context
import android.os.Environment

/**
 * @description: 持有全局初始化变量
 * @author: huyajun
 * @date: 2020/12/14
 */
class UpgradeApp(init: UpgradeApp.() -> Unit) {
    /**
    "app_id": 客户端包id
    "app_version": 客户端版本号
    "system_version": 系统版本号
    "platform": 手机操作系统平台iOS、android
    "device_id": 设备唯一id
    "device_brand": 手机运营商apple、vivo、oppo
    "android_channel": 渠道标识xiaomi、oppo、tencent

     */

    var context: Context? = null
    var resIcon: Int? = null
    var host: String? = null
    var fileName: String = "/test.apk"
    var filePath: String? =
        Environment.getExternalStoragePublicDirectory("YiDian").path + this.fileName
    var appId: String? = null
    var appVersion: String? = null
    var systemVersion: String? = null
    var platform: String? = null
    var deviceId: String? = null
    var deviceBrand: String? = null
    var androidChannel: String? = null

    init {
        this.apply(init)
    }

}