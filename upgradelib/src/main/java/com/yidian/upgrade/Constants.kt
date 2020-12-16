package com.yidian.upgrade

/**
 * @description: TODO 类描述
 * @author: huyajun
 * @date:  2020/12/14
 **/
object Constants {
    const val CHMOD = "chmod "
    const val PERMISSION = "777"
    const val SPACE = " "
    const val PERCENT = "%"
    const val FILE_HEAD = "file://"
    const val FILE_TYPE = "application/vnd.android.package-archive"

    const val FILEPROVIDER_DEFAULT_FLAG = -1
    const val INSTALL_APK = 0x5099

    object RequestKey {

        const val APP_ID = "app_id"
        const val APP_VERSION = "app_version"
        const val SYSTEM_VERSION = "system_version"
        const val PLATFORM = "platform"
        const val DEVICE_ID = "device_id"
        const val DEVICE_BRAND = "device_brand"
        const val ANDROID_CHANNEL = "android_channel"
    }

    object ResponseKey {
        const val APP_URL = "app_url"
        const val APP_VERSION = "app_version"
        const val UPDATE_TIME = "update_time"
        const val DESCRIPTION = "description"
        const val IS_MANDATORY = "is_mandatory"

    }
}