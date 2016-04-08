package com.ylj.common;

import android.content.DialogInterface;
import android.view.View;

/**
 * Created by Administrator on 2016/3/9 0009.
 */
public interface IAlertable {
    void showToast(String message);
    void showToast(int resId);

    void showLongToast(String message);

    void showAlert(String Title,String message);

    void showAlert(String Title,String message,DialogInterface.OnClickListener okButtonListener);

    void showAlert(String Title,String message,DialogInterface.OnClickListener okButtonListener,DialogInterface.OnClickListener cancelButtonListener);
}
