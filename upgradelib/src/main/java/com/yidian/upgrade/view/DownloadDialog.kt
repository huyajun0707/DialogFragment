package com.yidian.upgrade.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.yidian.promptdialoglib.PromptDialog

/**
 * 提示框
 */
open class DownloadDialog : PromptDialog(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    companion object {
        private var mFragmentManager: FragmentManager? = null

        @JvmStatic
        @Synchronized
        fun create(fragmentManager: FragmentManager?): PromptDialog {
//        if (mSmsDiaolog == null) {
//            mSmsDiaolog = new SMSDiaolog();
//        }
            mFragmentManager = fragmentManager
            return PromptDialog()
        }
    }
}