package com.yidian.upgrade.task

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import com.yidian.upgrade.Constants
import com.yidian.upgrade.UpgradeApp
import com.yidian.upgrade.upgradelib.R
import com.yidian.upgrade.utils.ApplicationUtil
import com.yidian.upgrade.utils.FilePathUtil

/**
 * @description: 安装操作类
 * @author: huyajun
 * @date:  2020/12/15
 **/
class InstallManager {

    fun install(upgradeApp: UpgradeApp) {
        upgradeApp.filePath?.let {
            ApplicationUtil.INSTANCE.chmod(
                Constants.PERMISSION, upgradeApp.filePath
            )
            var flags: Int =
                Constants.FILEPROVIDER_DEFAULT_FLAG
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            }
            var activity = upgradeApp.context as Activity

            startActivityForResult(
                activity,
                Intent.ACTION_VIEW,
                FilePathUtil.INSTANCE
                    .getDownloadFileUri(true, upgradeApp.context, upgradeApp.filePath),
                flags,
                Constants.FILE_TYPE,
                Constants.INSTALL_APK,
                null
            )


        }

    }

    protected fun startActivityForResult(
        activity: Activity,
        act: String?,
        data: Uri?,
        flags: Int,
        type: String?,
        requestCode: Int,
        bundle: Bundle?
    ) {
        val intent = Intent()
        intent.action = act
        if (flags != Constants.FILEPROVIDER_DEFAULT_FLAG) {
            intent.addFlags(flags)
        }
        if (data != null && !TextUtils.isEmpty(type)) {
            intent.setDataAndType(data, type)
        }
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        activity.startActivityForResult(intent, requestCode)
    }

    companion object {
        @JvmStatic
        val INSTANCE: InstallManager by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { InstallManager() }
    }
}