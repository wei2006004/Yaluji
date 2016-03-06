package com.ylj.staff;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.ylj.R;
import com.ylj.common.BaseActivity;
import com.ylj.common.bean.Staff;
import com.ylj.db.AccountLet;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_staff_modify)
public class StaffModifyActivity extends BaseActivity {

    @ViewInject(R.id.et_name)
    EditText mNameText;

    @ViewInject(R.id.et_company)
    EditText mCompany;

    @ViewInject(R.id.et_group)
    EditText mGroup;

    @Event(R.id.fab)
    private void onFabClick(View view){
        Staff staff=new Staff();
        staff.setStaffName(mNameText.getText().toString());
        staff.setCompany(mCompany.getText().toString());
        staff.setGroup(mGroup.getText().toString());
        AccountLet.addStaff(staff);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
