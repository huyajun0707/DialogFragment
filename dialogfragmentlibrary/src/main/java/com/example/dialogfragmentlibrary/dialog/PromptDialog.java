package com.example.dialogfragmentlibrary.dialog;

import android.text.TextUtils;
import android.view.View;

import androidx.fragment.app.FragmentManager;

import com.example.dialogfragmentlibrary.constant.Temp;
import com.example.dialogfragmentlibrary.dialog.base.BaseDialogFragment;
import com.example.dialogfragmentlibrary.dialog.listener.OnDialogNegativeListener;
import com.example.dialogfragmentlibrary.dialog.listener.OnDialogNeutralListener;
import com.example.dialogfragmentlibrary.dialog.listener.OnDialogPositiveListener;
import com.example.dialogfragmentlibrary.util.BundleUtil;


public class PromptDialog extends BaseDialogFragment {

    @Override
    protected Builder build(Builder builder) {
        CharSequence title = BundleUtil.getInstance().getCharSequenceData(getArguments(), Temp.DIALOG_TITLE.getContent());
        CharSequence prompt = BundleUtil.getInstance().getCharSequenceData(getArguments(), Temp.DIALOG_PROMPT.getContent());
        CharSequence positive = BundleUtil.getInstance().getCharSequenceData(getArguments(), Temp.DIALOG_BUTTON_POSITIVE.getContent());
        CharSequence negative = BundleUtil.getInstance().getCharSequenceData(getArguments(), Temp.DIALOG_BUTTON_NEGATIVE.getContent());
        CharSequence neutral = BundleUtil.getInstance().getCharSequenceData(getArguments(), Temp.DIALOG_BUTTON_NEUTRAL.getContent());
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(prompt)) {
            builder.setMessage(prompt);
        }
        if (!TextUtils.isEmpty(positive)) {
            builder.setPositiveButton(positive, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (OnDialogPositiveListener listener : getDialogListeners(OnDialogPositiveListener.class)) {
                        listener.onPositiveButtonClicked(mRequestCode);
                    }
                    dismiss();
                }
            });
        }
        if (!TextUtils.isEmpty(negative)) {
            builder.setNegativeButton(negative, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (OnDialogNegativeListener listener : getDialogListeners(OnDialogNegativeListener.class)) {
                        listener.onNegativeButtonClicked(mRequestCode);
                    }
                    dismiss();
                }
            });
        }
        if (!TextUtils.isEmpty(neutral)) {
            builder.setNeutralButton(neutral, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (OnDialogNeutralListener listener : getDialogListeners(OnDialogNeutralListener.class)) {
                        listener.onNeutralButtonClicked(mRequestCode);
                    }
                    dismiss();
                }
            });
        }
        return builder;
    }

    public static PromptDialogBuilder createBuilder(FragmentManager fragmentManager) {
        return new PromptDialogBuilder(fragmentManager, PromptDialog.class);
    }
}
