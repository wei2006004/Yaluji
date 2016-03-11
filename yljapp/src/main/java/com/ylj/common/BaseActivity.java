package com.ylj.common;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.x;

/**
 * Created by wyouflf on 15/11/4.
 */
public class BaseActivity extends AppCompatActivity implements IAlertable, IDialogEditable {

    private AlertDialog mAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLongToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showAlert(String Title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(Title);
        builder.setMessage(message);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mAlertDialog = builder.create();
        mAlertDialog.show();
    }

    @Override
    public void showAlert(String Title, String message,
                          DialogInterface.OnClickListener okButtonListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(Title);
        builder.setMessage(message);
        builder.setPositiveButton("Confirm", okButtonListener);
        mAlertDialog = builder.create();
        mAlertDialog.show();
    }

    @Override
    public void showAlert(String Title, String message,
                          DialogInterface.OnClickListener okButtonListener,
                          DialogInterface.OnClickListener cancelButtonListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(Title);
        builder.setMessage(message);
        builder.setPositiveButton("Confirm", okButtonListener);
        builder.setNegativeButton("Cancel", cancelButtonListener);
        mAlertDialog = builder.create();
        mAlertDialog.show();
    }

    @Override
    public void showDoubleEditDialog(String title, double defValue, final OnButtonClick<Double> onButtonClick) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        final EditText editText = new EditText(this);
        editText.setText(String.valueOf(defValue));
        editText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        builder.setView(editText);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                Double value = Double.parseDouble(editText.getText().toString());
                onButtonClick.onConfirm(dialog, value);
                toggleSoftInput();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onButtonClick.onCancel(dialog);
                toggleSoftInput();
            }
        });
        mAlertDialog = builder.create();
        mAlertDialog.show();
    }

    @Override
    public void showIntEditDialog(String title, int defValue,final OnButtonClick<Integer> onButtonClick) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        final EditText editText = new EditText(this);
        editText.setText(String.valueOf(defValue));
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(editText);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                Integer value = Integer.parseInt(editText.getText().toString());
                onButtonClick.onConfirm(dialog, value);
                toggleSoftInput();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onButtonClick.onCancel(dialog);
                toggleSoftInput();
            }
        });
        mAlertDialog = builder.create();
        mAlertDialog.show();
    }

    @Override
    public void showStringEditDialog(String title, String defValue,final OnButtonClick<String> onButtonClick) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        final EditText editText = new EditText(this);
        editText.setText(defValue);
        builder.setView(editText);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                String value = editText.getText().toString();
                onButtonClick.onConfirm(dialog, value);
                toggleSoftInput();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onButtonClick.onCancel(dialog);
                toggleSoftInput();
            }
        });
        mAlertDialog = builder.create();
        mAlertDialog.show();
    }

    @Override
    public void showSingleChoiceDialog(String title, String[] list, int defItem, DialogInterface.OnClickListener onItemClick) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setSingleChoiceItems(list, defItem, onItemClick);
        mAlertDialog = builder.create();
        mAlertDialog.show();
    }

    public void toggleSoftInput(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
