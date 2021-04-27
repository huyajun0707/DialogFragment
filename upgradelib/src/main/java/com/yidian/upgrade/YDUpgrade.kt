package com.yidian.upgrade

import androidx.appcompat.app.AppCompatActivity
import com.yidian.promptdialoglib.PromptDialog
import com.yidian.promptdialoglib.listener.OnDialogNegativeListener
import com.yidian.promptdialoglib.listener.OnDialogPositiveListener
import com.yidian.upgrade.listener.CheckUpgradeListner
import com.yidian.upgrade.listener.OnDownloadListener
import com.yidian.upgrade.task.CheckManager
import com.yidian.upgrade.task.DownloadManager
import com.yidian.upgrade.task.InstallManager
import com.yidian.upgrade.task.UpgradeInfo
import com.yidian.upgrade.view.DownloadDialog


/**
 * @description: TODO 类描述
 * @author: huyajun
 * @date:  2020/12/15
 **/

class YDUpgrade(upgradeapp: UpgradeApp) : OnDialogPositiveListener, OnDialogNegativeListener,
    OnDownloadListener {
    private var downloadDialog: PromptDialog? = null
    private var upgradeInfo: UpgradeInfo? = null
    private var status = 0;
    private var DOWNLOAD_STATUS_DEFAULT = 0;
    private var DOWNLOAD_STATUS_SUCCESS = 1
    private var DOWNLOAD_STATUS_FAILED = 2
    private var DOWNLOAD_STATUS_PAUSE = 3
    private var upgradeApp = upgradeapp
    private var context = upgradeApp.context
    var activity = context as AppCompatActivity

    fun checkUpgrade() {
        CheckManager.INSTANCE.checkUpgrade(upgradeApp, object : CheckUpgradeListner {
            override fun onCheck(upgrade: UpgradeInfo) {
                upgradeInfo = upgrade
                upgradeInfo?.appUrl?.let {
                    if (downloadDialog == null) {
                        downloadDialog = DownloadDialog.create(activity.supportFragmentManager)
                            .setTitle("发现新版本")
                            .setSubTitle(upgradeInfo?.appVersion)
                            .setContent(upgradeInfo?.description)
                            .setListener(this)
                            .setRequestCode(2)
                        if (upgradeInfo?.isMandatory!!) {
                            //强制升级
                            downloadDialog?.setPositive("立即更新")
                        } else {
                            downloadDialog?.setNegative("稍后提醒")
                                ?.setPositive("立即更新")
                        }
                    }
                    downloadDialog?.show(context)
                }
            }

        })
    }

    override fun onPositiveButtonClicked(requestCode: Int) {
        //立即更新
        when (status) {
            DOWNLOAD_STATUS_DEFAULT, DOWNLOAD_STATUS_FAILED
            -> {
                upgradeInfo?.appUrl?.let {
                    DownloadManager.init(upgradeApp, it, this)
                    DownloadManager.download(it)
                }
            }
            DOWNLOAD_STATUS_SUCCESS -> {
                InstallManager.INSTANCE.install(upgradeApp)
            }
        }

    }

    override fun onNegativeButtonClicked(requestCode: Int) {
        //稍后

    }

    override fun onDownloadStart() {

    }

    override fun onDownloadProgress(progress: Float, speed: Float) {

    }

    override fun onDownloadFailed() {
        status = DOWNLOAD_STATUS_FAILED
        downloadDialog?.setPositive("下载失败，重试")
    }

    override fun onDownloadSuccess() {
        status = DOWNLOAD_STATUS_SUCCESS
        downloadDialog?.setPositive("安装")
    }

}