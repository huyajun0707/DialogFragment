package com.yidian.promptdialoglib

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.yidian.promptdialoglib.listener.OnDialogNegativeListener
import com.yidian.promptdialoglib.listener.OnDialogPositiveListener

/**
 * 提示框
 */
open class PromptDialog : DialogFragment(), View.OnClickListener {
    private var mTitle: String? = null
    private var mSubTitle: String? = null
    private var mContentTitle: String? = null
    private var mContent: String? = null
    private var mNegative: String? = null
    private var mPositive: String? = null
    private var mRequstCode = 0
    private var mContext: Context? = null
    private var widthRatio: Float = 0.5f
    private var heightRatio: Float = 0.5f
    private var canCancelable = false
    private var orientationMode = Orientation.HORIZONTAL
    private var llBottomHorizontal: LinearLayout? = null
    private var llBottomVertical: LinearLayout? = null
    private var tvTitle: TextView? = null
    private var tvSubTitle: TextView? = null
    private var tvContentTitle: TextView? = null
    private var tvContent: TextView? = null
    private var tvBottomVerticalNegative: TextView? = null
    private var tvBottomVerticalPositive: TextView? = null
    private var tvBottomHorizontalNegative: TextView? = null
    private var tvBottomHorizontalPositive: TextView? = null


    //    private static SMSDiaolog mSmsDiaolog;
    private var mOnDialogPositiveListener: OnDialogPositiveListener? = null
    private var mOnDialogNegativeListener: OnDialogNegativeListener? = null
    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.tvBottomHorizontalNegative) {
            if (mOnDialogNegativeListener != null && mRequstCode != 0) {
                mOnDialogNegativeListener!!.onNegativeButtonClicked(mRequstCode)
            }
            dialog!!.dismiss()
        } else if (id == R.id.tvBottomHorizontalPositive) {
            if (mOnDialogPositiveListener != null && mRequstCode != 0) {
                mOnDialogPositiveListener!!.onPositiveButtonClicked(mRequstCode)
            }
            dialog!!.dismiss()
        } else if (id == R.id.tvBottomVerticalNegative) {
            if (mOnDialogPositiveListener != null && mRequstCode != 0) {
                mOnDialogPositiveListener!!.onPositiveButtonClicked(mRequstCode)
            }
            dialog!!.dismiss()
        } else if (id == R.id.tvBottomVerticalPositive) {
            if (mOnDialogNegativeListener != null && mRequstCode != 0) {
                mOnDialogNegativeListener!!.onNegativeButtonClicked(mRequstCode)
            }
            dialog!!.dismiss()
        }
    }

    fun setOrientationr(mode: Orientation): PromptDialog {
        this.orientationMode = mode
        return this
    }

    fun setTitle(title: String?): PromptDialog {
        mTitle = title
        setText(tvTitle, mTitle)
        return this
    }

    fun setSubTitle(subTitle: String?): PromptDialog {
        mSubTitle = subTitle
        setText(tvSubTitle, mSubTitle)
        return this
    }

    fun setContentTitle(message: String?): PromptDialog {
        mContentTitle = message
        setText(tvContentTitle, mContentTitle)
        return this
    }

    fun setContent(message: String?): PromptDialog {
        mContent = message
        setText(tvContent, mContent)
        return this
    }


    fun setNegative(negative: String?): PromptDialog {
        mNegative = negative
        setBottomVisibility()
        return this
    }
//
//    fun setNeutral(center: String?): PromptDialog {
//        mBottomVerticalPositive = center
//        setBottomVisibility()
//        return this
//    }

    private fun setText(textView: TextView?, text: String?) {
        if (textView != null) {
            textView.text = text
        }
    }

    private fun setBottomVisibility() {
        if (orientationMode == Orientation.VERTICAL) {
            llBottomHorizontal?.setVisibility(View.GONE)
            llBottomVertical?.setVisibility(View.VISIBLE)
            if (!TextUtils.isEmpty(mNegative)) {
                tvBottomVerticalNegative?.visibility = View.VISIBLE
            } else {
                tvBottomVerticalNegative?.visibility = View.GONE
            }
            setText(tvBottomHorizontalPositive, mPositive)
            setText(tvBottomVerticalNegative, mNegative)
        } else {
            llBottomHorizontal?.setVisibility(View.VISIBLE)
            llBottomVertical?.setVisibility(View.GONE)
            setText(tvBottomHorizontalPositive, mPositive)
            setText(tvBottomHorizontalNegative, mNegative)
        }
    }

    fun setPositive(positive: String?): PromptDialog {
        mPositive = positive
        setBottomVisibility()
        return this
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE) //去掉Dialog的标题部分
        val view = inflater.inflate(R.layout.dialog_prompt_view, null)
        llBottomHorizontal = view.findViewById(R.id.llBottomHorizontal)
        llBottomVertical = view.findViewById(R.id.llBottomVertical)
        tvTitle = view.findViewById(R.id.tvDialogTitle)
        tvSubTitle = view.findViewById(R.id.tvDialogSubTitle)
        tvContentTitle = view.findViewById(R.id.tvDialogContentTitle)
        tvContent = view.findViewById(R.id.tvDialogContent)
        tvBottomVerticalNegative = view.findViewById(R.id.tvBottomVerticalNegative)
        tvBottomVerticalPositive = view.findViewById(R.id.tvBottomVerticalPositive)
        tvBottomHorizontalNegative = view.findViewById(R.id.tvBottomHorizontalNegative)
        tvBottomHorizontalPositive = view.findViewById(R.id.tvBottomHorizontalPositive)
        setTitle(mTitle)
        setSubTitle(mSubTitle)
        setContentTitle(mContentTitle)
        setContent(mContent)
        setBottomVisibility()
        tvBottomHorizontalNegative?.setOnClickListener(this)
        tvBottomHorizontalPositive?.setOnClickListener(this)
        tvBottomVerticalNegative?.setOnClickListener(this)
        tvBottomVerticalPositive?.setOnClickListener(this)
        if (mContext != null) {
            if (mRequstCode != 0 && mContext is OnDialogPositiveListener) {
                mOnDialogPositiveListener = mContext as OnDialogPositiveListener?
            }
            if (mRequstCode != 0 && mContext is OnDialogNegativeListener) {
                mOnDialogNegativeListener = mContext as OnDialogNegativeListener?
            }
        }
        //点击外部不消失
        isCancelable = canCancelable
        dialog!!.setOnKeyListener { dialog, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                true
            } else false
        }
        return view
    }

    fun setListener(any: Any?): PromptDialog {
        if (any != null) {
            if (mRequstCode != 0 && any is OnDialogPositiveListener) {
                mOnDialogPositiveListener = any
            }
            if (mRequstCode != 0 && any is OnDialogNegativeListener) {
                mOnDialogNegativeListener = any
            }
        }
        return this
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            getDialog()!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            /**
             * 设置Dialog的大小
             */
            val dm = DisplayMetrics()
            activity!!.windowManager.defaultDisplay.getMetrics(dm)
            dialog.window!!
                .setLayout(
                    (dm.widthPixels * widthRatio).toInt(),
                    (dm.heightPixels * heightRatio).toInt()
                )
        }
    }

    fun setWidthRatio(widthRatio: Float): PromptDialog {
        this.widthRatio = widthRatio
        return this
    }

    fun setHightRatio(heightRatio: Float): PromptDialog {
        this.heightRatio = heightRatio
        return this
    }

    fun setRequestCode(requestCode: Int): PromptDialog {
        mRequstCode = requestCode
        return this
    }

//    fun setCanCancelable(cancelable: Boolean): PromptDialog {
//        this.canCancelable = canCancelable
//        return this
//    }

    fun show(context: Context?) {
        mContext = context
        super.show(mFragmentManager!!, null)
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

enum class Orientation() {
    VERTICAL,
    HORIZONTAL

}