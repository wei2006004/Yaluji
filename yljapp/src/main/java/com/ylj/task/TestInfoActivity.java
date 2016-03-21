package com.ylj.task;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ylj.R;
import com.ylj.common.BaseActivity;
import com.ylj.common.bean.Task;
import com.ylj.common.bean.Test;

import org.xutils.view.annotation.ContentView;

@ContentView(R.layout.activity_test_info)
public class TestInfoActivity extends BaseActivity {

    public static final String EXTRA_TEST = "EXTRA_TEST";

    Test mTest = new Test();

    public static void startNewActivity(Context context, Test test) {
        Intent intent = new Intent(context, TaskActivity.class);
        intent.putExtra(EXTRA_TEST, test);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initExtraData(getIntent());
        initToolBar();
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initExtraData(Intent intent) {
        mTest = intent.getParcelableExtra(EXTRA_TEST);
    }

}
