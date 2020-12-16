package com.yidian.upgrade.task

import com.rayhahah.library.core.EGet
import com.yidian.upgrade.Constants
import com.yidian.upgrade.UpgradeApp
import com.yidian.upgrade.listener.CheckUpgradeListner
import com.yidian.upgrade.utils.LogUtil
import com.yidian.upgrade.utils.UPLog
import kotlin.concurrent.thread

/**
 * @description: TODO 类描述
 * @author: huyajun
 * @date:  2020/12/14
 **/


class CheckManager {

    fun checkUpgrade(upgradeApp: UpgradeApp?, checkUpgradeListner: CheckUpgradeListner?) {
        thread(start = true) {
            EGet(
                upgradeApp?.host!!,
                hashMapOf(
                    Constants.RequestKey.APP_ID to upgradeApp.appId!!,
                    Constants.RequestKey.APP_VERSION to upgradeApp.appVersion!!,
                    Constants.RequestKey.SYSTEM_VERSION to upgradeApp.systemVersion!!,
                    Constants.RequestKey.PLATFORM to upgradeApp.platform!!,
                    Constants.RequestKey.DEVICE_ID to upgradeApp.deviceId!!,
                    Constants.RequestKey.DEVICE_BRAND to upgradeApp.deviceBrand!!,
                    Constants.RequestKey.ANDROID_CHANNEL to upgradeApp.androidChannel!!
                )
            ).go<String> { data: String ->
                UPLog.print("data:$data")
                checkUpgradeListner?.onCheck(UpgradeInfo.parse(data))
            }
        }
    }

    companion object {
        @JvmStatic
        val INSTANCE: CheckManager by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { CheckManager() }
    }

}



