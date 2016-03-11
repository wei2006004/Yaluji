package com.ylj.common;

import android.content.DialogInterface;

/**
 * Created by Administrator on 2016/3/11 0011.
 */
public interface IDialogEditable {
    interface OnButtonClick<T> {
        void onConfirm(DialogInterface dialog, T result);

        void onCancel(DialogInterface dialog);
    }

    void showDoubleEditDialog(String title, double defValue, final OnButtonClick<Double> onButtonClick);

    void showIntEditDialog(String title, int defValue, final OnButtonClick<Integer> onButtonClick);

    void showStringEditDialog(String title, String defValue, final OnButtonClick<String> onButtonClick);

    void showSingleChoiceDialog(String title, String[] list, int defItem, DialogInterface.OnClickListener onItemClick);
}
