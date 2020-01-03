package com.example.dialogfragmentlibrary.dialog.base;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.dialogfragmentlibrary.constant.Constant;
import com.example.dialogfragmentlibrary.constant.Temp;


public abstract class BaseDialogBuilder<T extends BaseDialogBuilder<T>> {

    private String mTag = Constant.View.CUSTOM_DIALOG;
    private int mRequestCode = Constant.RequestCode.DIALOG;
    protected final FragmentManager mFragmentManager;
    protected final Class<? extends BaseDialogFragment> mClass;
    private Fragment mTargetFragment;
    private boolean isCancelable = true;
    private boolean isCancelableOnTouchOutside = true;
    private boolean isUseDarkTheme = false;
    private boolean isUseLightTheme = false;
    private boolean isUseListener = false;



    public BaseDialogBuilder(FragmentManager fragmentManager, Class<? extends BaseDialogFragment> clazz) {
        mFragmentManager = fragmentManager;
        mClass = clazz;
    }

    protected abstract T self();

    protected abstract Bundle prepareArguments();

    public T setCancelable(boolean cancelable) {
        isCancelable = cancelable;
        return self();
    }

    public T setCancelableOnTouchOutside(boolean cancelable) {
        isCancelableOnTouchOutside = cancelable;
        if (cancelable) {
            isCancelable = cancelable;
        }
        return self();
    }

    public T setOnKeyListener(boolean useListener) {
        isUseListener = useListener;
        if (useListener) {
            isCancelable = useListener;
        }
        return self();
    }

    public T setTargetFragment(Fragment fragment, int requestCode) {
        mTargetFragment = fragment;
        mRequestCode = requestCode;
        return self();
    }

    public T setRequestCode(int requestCode) {
        mRequestCode = requestCode;
        return self();
    }

    public T setTag(String tag) {
        mTag = tag;
        return self();
    }

    public T useDarkTheme() {
        isUseDarkTheme = true;
        return self();
    }

    public T useLightTheme() {
        isUseLightTheme = true;
        return self();
    }

    private BaseDialogFragment create(Context ctx) {
        final Bundle bundle = prepareArguments();
        final BaseDialogFragment fragment = (BaseDialogFragment) Fragment.instantiate(ctx, mClass.getName(), bundle);
        bundle.putBoolean(Temp.CANCELABLE_ON_TOUCH_OUTSIDE.getContent(), isCancelableOnTouchOutside);
        bundle.putBoolean(Temp.USE_DARK_THEME.getContent(), isUseDarkTheme);
        bundle.putBoolean(Temp.USE_LIGHT_THEME.getContent(), isUseLightTheme);
        bundle.putBoolean(Temp.USE_LISTENER.getContent(), isUseListener);
        if (mTargetFragment != null) {
            fragment.setTargetFragment(mTargetFragment, mRequestCode);
        } else {
            bundle.putInt(Temp.REQUEST_CODE.getContent(), mRequestCode);
        }
        fragment.setCancelable(isCancelable);
        return fragment;
    }

    public DialogFragment show(Context ctx) {
        BaseDialogFragment fragment = create(ctx);
        fragment.show(mFragmentManager, mTag);

        return fragment;
    }

    public void dismiss(Context ctx) {
        create(ctx).dismiss();
    }

    public DialogFragment showAllowingStateLoss(Context ctx) {
        BaseDialogFragment fragment = create(ctx);
        fragment.showAllowingStateLoss(mFragmentManager, mTag);
        return fragment;
    }
}
