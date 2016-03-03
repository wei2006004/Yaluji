package com.ylj.task;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.ylj.R;
import com.ylj.adjust.AdjustLightActivity;
import com.ylj.common.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_new_task)
public class NewTaskActivity extends BaseActivity {

    @ViewInject(R.id.rl_origin)
    private RelativeLayout mOriginLayout;

    @Event(R.id.rl_origin)
    private void onOriginLayoutClick(View view){

    }

    @Event(R.id.fab_adjust)
    private void onAdjustButtonClick(View view){
        Intent intent=new Intent(x.app(), AdjustLightActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
