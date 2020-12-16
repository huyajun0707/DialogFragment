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
import com.yidian.promptdialoglib.utils.ViewUtil

/**
 * 提示框
 */
open class PromptDialogFragment : DialogFragment(), View.OnClickListener {
    private var tvTitle: TextView? = null
    private var tvDialogSubTitle: TextView? = null
    private var tvContentTitle: TextView? = null
    private var tvContent: TextView? = null
    private var mTitle: String? = null
    private var mSubTitle: String? = null
    private var mContentTitle: String? = null
    private var mContent: String? = null
    private var mCenter: String? = null
    private var mNegative: String? = null
    private var mPositive: String? = null
    private var mRequstCode = 0
    private var llBottomCenter: LinearLayout? = null
    private var llBottom: LinearLayout? = null
    private var llNegative: LinearLayout? = null
    private var llPositive: LinearLayout? = null
    private var tvNegative: TextView? = null
    private var tvPositive: TextView? = null
    private var tvCenter: TextView? = null
    private var mContext: Context? = null

    //    private static SMSDiaolog mSmsDiaolog;
    private var mOnDialogPositiveListener: OnDialogPositiveListener? = null
    private var mOnDialogNegativeListener: OnDialogNegativeListener? = null
    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.llNegative) {
            if (mOnDialogNegativeListener != null && mRequstCode != 0) {
                mOnDialogNegativeListener!!.onNegativeButtonClicked(mRequstCode)
            }
            dialog!!.dismiss()
        } else if (id == R.id.llPositive) {
            if (mOnDialogPositiveListener != null && mRequstCode != 0) {
                mOnDialogPositiveListener!!.onPositiveButtonClicked(mRequstCode)
            }
            dialog!!.dismiss()
        } else if (id == R.id.llBottomCenter) {
            if (mOnDialogPositiveListener != null && mRequstCode != 0) {
                mOnDialogPositiveListener!!.onPositiveButtonClicked(mRequstCode)
            }
            dialog!!.dismiss()
        }
    }

    fun setTitle(title: String?): PromptDialogFragment {
        mTitle = title
        setText(tvTitle, mTitle)
        return this
    }

    fun setSubTitle(subTitle: String?): PromptDialogFragment {
        mSubTitle = subTitle
        setText(tvDialogSubTitle, mSubTitle)
        return this
    }

    fun setContentTitle(message: String?): PromptDialogFragment {
        mContentTitle = message
        setText(tvContentTitle, mContentTitle)
        return this
    }

    fun setContent(message: String?): PromptDialogFragment {
        mContent = message
        setText(tvContent, mContent)
        return this
    }

    fun setCenter(center: String?): PromptDialogFragment {
        mCenter = center
        setBottomVisibility(true)
        setText(tvCenter, mCenter)
        return this
    }

    fun setNegative(negative: String?): PromptDialogFragment {
        mNegative = negative
        setBottomVisibility(false)
        setText(tvNegative, mNegative)
        return this
    }

    private fun setText(textView: TextView?, text: String?) {
        if (textView != null) {
            textView.text = text
        }
    }

    private fun setBottomVisibility(isCenter: Boolean) {
        if (isCenter) {
            if (!TextUtils.isEmpty(mCenter)) {
//            LogUtil.getInstance().print("---mCenter");
                llBottom?.setVisibility(View.GONE)
                llBottomCenter?.setVisibility(View.VISIBLE)
            }
        } else {
            if (!TextUtils.isEmpty(mNegative) || !TextUtils.isEmpty(mPositive)) {
                llBottom?.setVisibility(View.VISIBLE)
                llBottomCenter?.setVisibility(View.GONE)
            }
        }
    }

    fun setPositive(positive: String?): PromptDialogFragment {
        mPositive = positive
        setText(tvPositive, mPositive)
        return this
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE) //去掉Dialog的标题部分
        val view = inflater.inflate(R.layout.dialog_custom_view, null)
        tvTitle = ViewUtil.getInstance().findView<TextView>(view, R.id.tvDialogTitle)
        tvDialogSubTitle = ViewUtil.getInstance().findView<TextView>(view, R.id.tvDialogSubTitle)
        tvContentTitle = ViewUtil.getInstance().findView<TextView>(view, R.id.tvDialogContentTitle)
        tvContent = ViewUtil.getInstance().findView<TextView>(view, R.id.tvDialogContent)
        tvNegative = ViewUtil.getInstance().findView<TextView>(view, R.id.tvNegative)
        tvPositive = ViewUtil.getInstance().findView<TextView>(view, R.id.tvPositive)
        llNegative =
            ViewUtil.getInstance().findViewAttachOnclick<LinearLayout>(view, R.id.llNegative, this)
        llPositive =
            ViewUtil.getInstance().findViewAttachOnclick<LinearLayout>(view, R.id.llPositive, this)
        llBottomCenter = ViewUtil.getInstance()
            .findViewAttachOnclick<LinearLayout>(view, R.id.llBottomCenter, this)
        llBottom =
            ViewUtil.getInstance().findViewAttachOnclick<LinearLayout>(view, R.id.llBottom, this)
        tvCenter = ViewUtil.getInstance().findView<TextView>(view, R.id.tvCenter)
        if (!TextUtils.isEmpty(mTitle)) {
            tvTitle?.setText(mTitle)
        } else {
            tvTitle?.setVisibility(View.GONE)
        }
        if (!TextUtils.isEmpty(mSubTitle)) {
            tvDialogSubTitle?.setText(mSubTitle)
        } else {
            tvDialogSubTitle?.setVisibility(View.GONE)
        }
        if (!TextUtils.isEmpty(mContentTitle)) {
            tvContentTitle?.setText(mContentTitle)
        } else {
            tvContentTitle?.setVisibility(View.GONE)
        }
        if (!TextUtils.isEmpty(mContent)) {
            tvContent?.setText(mContent)
        } else {
            tvContent?.setVisibility(View.GONE)
        }
        if (!TextUtils.isEmpty(mNegative)) {
            llBottom?.setVisibility(View.VISIBLE)
            llBottomCenter?.setVisibility(View.GONE)
            tvNegative?.setText(mNegative)
        }
        if (!TextUtils.isEmpty(mPositive)) {
            tvPositive?.setText(mPositive)
        }
        if (!TextUtils.isEmpty(mCenter)) {
//            LogUtil.getInstance().print("---mCenter");
            llBottom?.setVisibility(View.GONE)
            llBottomCenter?.setVisibility(View.VISIBLE)
            tvCenter?.setText(mCenter)
        } else {
            llBottom?.setVisibility(View.VISIBLE)
            llBottomCenter?.setVisibility(View.GONE)
        }
        if (mContext != null) {
            if (mRequstCode != 0 && mContext is OnDialogPositiveListener) {
                mOnDialogPositiveListener = mContext as OnDialogPositiveListener?
            }
            if (mRequstCode != 0 && mContext is OnDialogNegativeListener) {
                mOnDialogNegativeListener = mContext as OnDialogNegativeListener?
            }
        }
        //点击外部不消失
        isCancelable = false
        dialog!!.setOnKeyListener { dialog, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                true
            } else false
        }
        return view
    }

    fun setListener(any: Any?): PromptDialogFragment {
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
                .setLayout((dm.widthPixels * 0.80).toInt(), (dm.heightPixels * 0.5).toInt())
        }
    }

    fun setRequestCode(requestCode: Int): PromptDialogFragment {
        mRequstCode = requestCode
        return this
    }

    fun show(context: Context?) {
        mContext = context
        super.show(mFragmentManager!!, null)
    }

    companion object {
        private var mFragmentManager: FragmentManager? = null

        @JvmStatic
        @Synchronized
        fun create(fragmentManager: FragmentManager?): PromptDialogFragment {
//        if (mSmsDiaolog == null) {
//            mSmsDiaolog = new SMSDiaolog();
//        }
            mFragmentManager = fragmentManager
            return PromptDialogFragment()
        }
    }
}