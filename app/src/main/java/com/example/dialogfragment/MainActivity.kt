package com.example.dialogfragment

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.yidian.promptdialoglib.Orientation
import com.yidian.promptdialoglib.PromptDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btTest.setOnClickListener(View.OnClickListener { showCustom() })
        println("---->bran:${Build.BRAND}")

    }


    fun showEditDialog() {
        val editNameDialog = EditNameDialogFragment()
        editNameDialog.show(supportFragmentManager, "EditNameDialog")
        supportFragmentManager

    }

    fun showLoginDialog() {
        val dialog = LoginDialogFragment()
        dialog.show(supportFragmentManager, "loginDialog")

    }

    fun showPromptDialog() {
//        PromptDialog.createBuilder(supportFragmentManager)
//            .setTitle(getString(R.string.dialog_prompt))
//            .setPrompt(getString(R.string.prompt))
//            .setPositiveButtonText(this, R.string.dialog_known)
//            .setCancelable(true)
//            .setRequestCode(110)
//            .setCancelableOnTouchOutside(false)
//            .setOnKeyListener(true)
//            .showAllowingStateLoss(this)
        PromptDialog.create(supportFragmentManager)
            .setTitle("标题")
            .setSubTitle("小标题")
            .setContent("内容内容内容内容内容内容内容内容内容内容内容")
            .setOrientationr(Orientation.VERTICAL)
            .setPositive("立即更新")
            .setNegative("取消")

            .setWidthRatio(0.8f)
            .setHightRatio(0.5f)
            .setRequestCode(10)
            .show(this)

    }

    fun showCustom() {

        showPromptDialog()


//        var upgrade = YDUpgrade(UpgradeApp {
//            context = this@MainActivity
//            host = "http://open-platform.test.yidian-inc.com/open-platform/get-update-package"
////            var fileName: String = "/test.apk"
////            var filePath: String? =
////                Environment.getExternalStoragePublicDirectory("YiDian").path + this.fileName
//            appId = "com.example.upgrade"
//            appVersion = "1"
//            systemVersion = "10.0"
//            platform = "android"
//            deviceId = ""
//            deviceBrand = Build.BRAND
//            androidChannel = "yd"
//        })
//        upgrade.checkUpgrade()

//       VoiceInteractor.Prompt.create(supportFragmentManager).setTitle("发现新版本")
//            .setSubTitle("V1.0.1")
//            .setContent("版本更新以下功能： \n1 . 修复了浏览器账号密码记录同步问题； \n2 . 修复了几个其他已知问题")
//            .setNegative("稍后提醒")
//            .setPositive("立即更新")
//            .setRequestCode(2)
//            .show(this)
//
//        var APK_NAME = "/test.apk"
//        var filePath: String = Environment.getExternalStoragePublicDirectory("YiDian").path + APK_NAME
    }
}
