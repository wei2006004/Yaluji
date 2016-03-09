package com.ylj.common;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.xutils.x;

/**
 * Created by wyouflf on 15/11/4.
 */
public class BaseFragment extends Fragment implements IAlertable{

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

    }

    @Override
    public void showLongToast(String message) {

    }

    @Override
    public void showAlert(String Title, String message) {

    }

    @Override
    public void showAlert(String Title, String message, View.OnClickListener okButtonListener) {

    }

    @Override
    public void showAlert(String Title, String message, View.OnClickListener okButtonListener, View.OnClickListener cancelButtonListener) {

    }
}
