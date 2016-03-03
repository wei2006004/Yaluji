package com.ylj.adjust;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ylj.R;
import com.ylj.common.widget.PlotView;


public class AdjustResultActivity extends Activity {
	
	private final static int MIN_VCV_VALUE=-5;
	private final static int MAX_VCV_VALUE=15;
	private final static int MIN_X_VALUE=-5;
	private final static int MAX_X_VALUE=15;

	private PlotView plotview;
	
	private Button okButton;
	private Button exitButton;
	private EditText editText;
	
	private TextView rTextView;
	private TextView aTextView;
	private TextView bTextView;
	private TextView vcvTextView;
	private TextView promtTextView;
	
	private Builder mBuilder;	
	private AlertDialog mDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_adjust_result);
		plotview=(PlotView)findViewById(R.id.plotview);
		PlotView.DrawEdit edit=plotview.getEdit();
		edit.setGridEnable(false);
		edit.setXAxes(MIN_X_VALUE, MAX_X_VALUE);
		edit.setYAxes(MIN_VCV_VALUE, MAX_VCV_VALUE);
		edit.commit();
		
		okButton=(Button)findViewById(R.id.button_ok);
		okButton.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				onOkButton();
			}
		});
		
		exitButton=(Button)findViewById(R.id.button_exit);
		exitButton.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				onExitButton();
			}
		});
		
		editText=(EditText)findViewById(R.id.edittext);
		editText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				try {
					float value=Float.valueOf(editText.getText().toString());
					vcvTextView.setText(String.format("%.2f", value*b+a));
				} catch (Exception e) {
				}			
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {	
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});
		
		rTextView=(TextView)findViewById(R.id.rtext);
		aTextView=(TextView)findViewById(R.id.atext);
		bTextView=(TextView)findViewById(R.id.btext);
		vcvTextView=(TextView)findViewById(R.id.vcvtext);
		promtTextView=(TextView)findViewById(R.id.text_promt);
		
		mBuilder=new Builder(this);
		mBuilder.setTitle(getString(R.string.ad_dialog_title));
		mBuilder.setMessage(getString(R.string.ad_dialog_exit));
		mBuilder.setPositiveButton(getString(R.string.ad_ok), new DialogInterface.OnClickListener() {				
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				AdjustResultActivity.this.finish();
			}
		});
		mBuilder.setNegativeButton(getString(R.string.ad_cancel), new DialogInterface.OnClickListener() {				
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});		
		mDialog=mBuilder.create();
		countParas();
		if(r<0.7){
			promtTextView.setTextColor(Color.RED);
			promtTextView.setText(R.string.ad_promt_fail);
		}else{
			promtTextView.setText(R.string.ad_promt_pass);
		}
	}
	
	private float a,b,r;
	
	private void countParas()
	{
		AdjustData.Data[] datas=AdjustData.instance().getDatas();
		float qavg,cavg,qsum=0,csum=0;
		for(AdjustData.Data data : datas) {
			qsum+=data.quake;
			csum+=data.compaction;
		}
		qavg=qsum/datas.length;
		cavg=csum/datas.length;
		
		float qc=0,q2=0,c2=0;
		float quake,com;
		for(AdjustData.Data data : datas) {
			quake=data.quake;
			com=data.compaction;
			qc+=(quake-qavg)*(com-cavg);
			q2+=(quake-qavg)*(quake-qavg);
			c2+=(com-cavg)*(com-cavg);
		}
		b=qc/c2;
		a=qavg-b*cavg;
		r=qc/(float)Math.sqrt(q2*c2);
		r=r<0?-r:r;
		
		rTextView.setText(String.format("%.2f", r));
		aTextView.setText(String.format("%.2f", a));
		bTextView.setText(String.format("%.2f", b));
		//vcvTextView.setText(String.format("%.2f", 0));
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		new Handler().postDelayed(new Runnable() {	
			@Override
			public void run() {
				PlotView.DrawEdit edit=plotview.getEdit();
				AdjustData.Data[] datas=AdjustData.instance().getDatas();
				for (AdjustData.Data data : datas) {
					edit.addDrawPoint(new PointF(data.compaction,data.quake));
				}
				edit.addPoint(new PointF(MIN_X_VALUE,a+b*MIN_X_VALUE));
				edit.addPoint(new PointF(MAX_X_VALUE,a+b*MAX_X_VALUE));
				edit.addPoint(new PointF((MIN_VCV_VALUE-a)/b,MIN_VCV_VALUE));
				edit.addPoint(new PointF((MAX_VCV_VALUE-a)/b,MAX_VCV_VALUE));
				edit.commit();
			}
		}, 300);	
	}
	
	private void onOkButton()
	{
		if(editText.getText().toString().equals("")){
			showText(R.string.ad_msg_x_null);
			return;
		}
		try {
//			float value=Float.valueOf(editText.getText().toString());
//			SharedPreferences preferences=Tools.getAppPreferences(this);
//			preferences.edit().putFloat(getString(R.string.pref_vcv), value*b+a).commit();
		} catch (Exception e) {
			showText(R.string.ad_msg_x_notformat);
			return;
		}	
		finish();
	}
	
	private void onExitButton()
	{
		if(r>0.7){
			mDialog.show();
		}else{
			finish();
		}
		
	}
	
	private void showText(int resId)
	{
		showText(getString(resId));
	}
	
	private void showText(String text)
	{
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}
	
}
	






