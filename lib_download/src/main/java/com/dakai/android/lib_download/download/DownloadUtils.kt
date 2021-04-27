package com.dakai.android.lib_download.download

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings

/**
 * @author      ： CuiYancey <cuiyuancheng0322@gmail.com>
 * @version     ： 1.0
 * @date        ： 2019-11-12 13:18
 */
class DownloadUtils {

    companion object {
        /**
         * 判断下载管理器是否可用
         */
        @JvmStatic
        fun downLoadMangerIsEnable(context: Context): Boolean {
            val state = context.applicationContext.packageManager
                .getApplicationEnabledSetting("com.android.providers.downloads")

            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                !(state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED ||
                        state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                        || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED)
            } else {
                !(state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER)
            }
        }

        /**
         * 打开 系统下载管理器 设置页面 或者 打开系统设置
         */
        @JvmStatic
        fun openDownloadManager(context: Context) {
            try {
                //Open the specific App Info page:
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:" + "com.android.providers.downloads")
                context.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
                //Open the generic Apps page:
                val intent = Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS)
                context.startActivity(intent)
            }
        }
    }
}