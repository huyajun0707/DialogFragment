package com.example.dialogfragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.dialogfragmentlibrary.constant.Constant
import com.example.dialogfragmentlibrary.dialog.PromptDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btTest.setOnClickListener(View.OnClickListener { showPromptDialog() })
    }



    fun showEditDialog(){
        val editNameDialog = EditNameDialogFragment()
        editNameDialog.show(supportFragmentManager, "EditNameDialog")
        supportFragmentManager

    }

    fun showLoginDialog(){
        val dialog = LoginDialogFragment()
        dialog.show(supportFragmentManager, "loginDialog")

    }

    fun showPromptDialog(){
        PromptDialog.createBuilder(supportFragmentManager)
            .setTitle(getString(R.string.dialog_prompt))
            .setPrompt(getString(R.string.prompt))
            .setPositiveButtonText(this, R.string.dialog_known)
            .setCancelable(true)
            .setRequestCode(110)
            .setCancelableOnTouchOutside(false)
            .setOnKeyListener(true)
            .showAllowingStateLoss(this)
    }
}
