package com.ylj.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import org.xutils.x;

/**
 * Created by wyouflf on 15/11/4.
 */
public class BaseFragment extends Fragment implements IAlertable,IDialogEditable{

    private boolean injected = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        injected = true;
        return x.view().inject(this, inflater, container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!injected) {
            x.view().inject(this, this.getView());
        }
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLongToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    private AlertDialog mAlertDialog;

    @Override
    public void showAlert(String Title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(Title);
        builder.setMessage(message);
        builder.setPositiveButton("Confirm", okButtonListener);
        builder.setNegativeButton("Cancel", cancelButtonListener);
        mAlertDialog = builder.create();
        mAlertDialog.show();
    }

    @Override
    public void showDoubleEditDialog(String title, double defValue, final OnButtonClick<Double> onButtonClick) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        final EditText editText = new EditText(getActivity());
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        final EditText editText = new EditText(getActivity());
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        final EditText editText = new EditText(getActivity());
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setSingleChoiceItems(list, defItem, onItemClick);
        mAlertDialog = builder.create();
        mAlertDialog.show();
    }

    public void toggleSoftInput(){
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
