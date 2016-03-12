package com.ylj.adjust;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ylj.R;
import com.ylj.common.widget.PlotView;

import java.util.Random;


public class AdjustHeavyActivity extends Activity implements IAdjustCtrl.OnCtrlLister {
	@Override
	public void onAdjustStart(){

	}

	@Override
	public void onAdjustStop(){

	}
	public final static int MAX_QUAKE=15;
	public final static int MIN_QUAKE=-5;
	public final static int ADJUST_LENGTH=100;
	
	public static final int ADJUST_TYPE=2;
	
	private float[] quakeDatas;
	private int currentIndex=0;
	
	private PlotView plotView;

	private Button runButton;
	private Button stopButton;
	private Button autoButton;
	private Button nextButton;
	private Button exitButton;

	private EditText posText1;
	private EditText posText2;
	private EditText posText3;
	private EditText posText4;
	private EditText posText5;
	private EditText posText6;
	
	private EditText comText1;
	private EditText comText2;
	private EditText comText3;
	private EditText comText4;
	private EditText comText5;
	private EditText comText6;
	
	private IAdjustCtrl testCtrl;
	
	private Builder mBuilder;	
	private AlertDialog mDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_adjust_heavy);
		plotView=(PlotView)findViewById(R.id.plotview);
		plotView.getEdit().setXAxes(0, ADJUST_LENGTH).setYAxes(MIN_QUAKE, MAX_QUAKE).commit();
		if(quakeDatas==null){
			quakeDatas=new float[ADJUST_LENGTH];
			for(int i=0;i<ADJUST_LENGTH;i++){
				quakeDatas[i]=0;
				currentIndex=0;
			}
		}
		
		posText1=(EditText)findViewById(R.id.pos1);
		posText2=(EditText)findViewById(R.id.pos2);
		posText3=(EditText)findViewById(R.id.pos3);
		posText4=(EditText)findViewById(R.id.pos4);
		posText5=(EditText)findViewById(R.id.pos5);
		posText6=(EditText)findViewById(R.id.pos6);
		
		comText1=(EditText)findViewById(R.id.compact1);
		comText2=(EditText)findViewById(R.id.compact2);
		comText3=(EditText)findViewById(R.id.compact3);
		comText4=(EditText)findViewById(R.id.compact4);
		comText5=(EditText)findViewById(R.id.compact5);
		comText6=(EditText)findViewById(R.id.compact6);
		
		runButton=(Button)findViewById(R.id.button_start);
		runButton.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View arg0) {
				onRunButton();
			}
		});
		stopButton=(Button)findViewById(R.id.button_pause);
		stopButton.setEnabled(false);
		stopButton.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View arg0) {
				onStopButton();
			}
		});
		autoButton=(Button)findViewById(R.id.button_auto);
		autoButton.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View arg0) {
				onAutoButton();
			}
		});
		nextButton=(Button)findViewById(R.id.button_next);
		nextButton.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View arg0) {
				onNextButton();
			}
		});
		exitButton=(Button)findViewById(R.id.button_exit);
		exitButton.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View arg0) {
				onExitButton();
			}
		});
		
		testCtrl=AdjustCtrlImpl.instance();
		testCtrl.addOnRefreshListener(this);
		
		mBuilder=new Builder(this);
		mBuilder.setTitle(getString(R.string.ad_dialog_title));
		mBuilder.setMessage(getString(R.string.ad_dialog_text));
		mBuilder.setPositiveButton(getString(R.string.ad_ok), new DialogInterface.OnClickListener() {				
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				AdjustHeavyActivity.this.onStopButton();
				dialog.dismiss();
				AdjustHeavyActivity.this.finish();
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
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		if(testCtrl!=null) {
			testCtrl.deleteOnRefreshListener(this);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode == KeyEvent.KEYCODE_BACK) { 
	    	mDialog.show();	    				
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	private void onRunButton()
	{
		if(currentIndex>=100){
			//quakeDatas=new float[ADJUST_LENGTH];
			for(int i=0;i<ADJUST_LENGTH;i++){
				quakeDatas[i]=0;
				currentIndex=0;
			}
			plotView.getEdit().clear().commit();
		}
		testCtrl.startAdjust();
		runButton.setEnabled(false);
		runButton.setTextColor(getResources().getColorStateList(R.color.color_ad_button_font_grep));
		stopButton.setEnabled(true);
		stopButton.setTextColor(getResources().getColorStateList(R.color.color_ad_button_font));
	}
	
	private void onStopButton()
	{
		testCtrl.stopAdjust();
		runButton.setEnabled(true);
		runButton.setTextColor(getResources().getColorStateList(R.color.color_ad_button_font));
		stopButton.setEnabled(false);
		stopButton.setTextColor(getResources().getColorStateList(R.color.color_ad_button_font_grep));
	}
	
	private void onAutoButton()
	{
		onStopButton();
		Random random=new Random();
		for(int i=0;i<ADJUST_LENGTH;i++){
			quakeDatas[i]=random.nextFloat()*13;
		}
		PlotView.DrawEdit edit=plotView.getEdit();
		edit.clear();
		for(int i=0;i<ADJUST_LENGTH;i++){
			edit.addPoint(new PointF(i,quakeDatas[i]));
		}
		edit.commit();
		
		posText1.setText(String.valueOf(random.nextInt(ADJUST_LENGTH)));
		posText2.setText(String.valueOf(random.nextInt(ADJUST_LENGTH)));
		posText3.setText(String.valueOf(random.nextInt(ADJUST_LENGTH)));
		posText4.setText(String.valueOf(random.nextInt(ADJUST_LENGTH)));
		posText5.setText(String.valueOf(random.nextInt(ADJUST_LENGTH)));
		posText6.setText(String.valueOf(random.nextInt(ADJUST_LENGTH)));
		
		final int MAX_COM_VALUE=10;
		comText1.setText(String.format("%.1f",random.nextFloat()*MAX_COM_VALUE));
		comText2.setText(String.format("%.1f",random.nextFloat()*MAX_COM_VALUE));
		comText3.setText(String.format("%.1f",random.nextFloat()*MAX_COM_VALUE));
		comText4.setText(String.format("%.1f",random.nextFloat()*MAX_COM_VALUE));
		comText5.setText(String.format("%.1f",random.nextFloat()*MAX_COM_VALUE));
		comText6.setText(String.format("%.1f",random.nextFloat()*MAX_COM_VALUE));
	}
	
	private void onNextButton()
	{
		if(!saveData())
			return;
		Intent intent=new Intent(this,AdjustResultActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		finish();
	}
	
	private void onExitButton()
	{
		mDialog.show();
	}
	
	private boolean saveData()
	{
		if(posText1.getText().toString().equals("") || 
				posText2.getText().toString().equals("")|| 
				posText3.getText().toString().equals("")||
				posText4.getText().toString().equals("")|| 
				posText5.getText().toString().equals("")|| 
				posText6.getText().toString().equals("")||
				comText1.getText().toString().equals("")||
				comText2.getText().toString().equals("")||
				comText3.getText().toString().equals("")||
				comText4.getText().toString().equals("")||
				comText5.getText().toString().equals("")||
				comText6.getText().toString().equals("")){
			showText(R.string.ad_msg_null);
			return false;
		}
		AdjustData data=AdjustData.instance();		
		int pos=Integer.parseInt(posText1.getText().toString());
		if(pos<0 || pos>ADJUST_LENGTH){
			showText(R.string.ad_msg_outrange);
			return false;
		}		
		float quake=quakeDatas[pos];
		float compaction=Float.parseFloat(comText1.getText().toString());
		data.setData(ADJUST_TYPE, 0, quake, compaction);
		
		pos=Integer.parseInt(posText2.getText().toString());
		if(pos<0 || pos>ADJUST_LENGTH){
			showText(R.string.ad_msg_outrange);
			return false;
		}		
		quake=quakeDatas[pos];
		compaction=Float.parseFloat(comText2.getText().toString());
		data.setData(ADJUST_TYPE,1, quake, compaction);
		
		pos=Integer.parseInt(posText3.getText().toString());
		if(pos<0 || pos>ADJUST_LENGTH){
			showText(R.string.ad_msg_outrange);
			return false;
		}		
		quake=quakeDatas[pos];
		compaction=Float.parseFloat(comText3.getText().toString());
		data.setData(ADJUST_TYPE,2, quake, compaction);
		
		pos=Integer.parseInt(posText4.getText().toString());
		if(pos<0 || pos>ADJUST_LENGTH){
			showText(R.string.ad_msg_outrange);
			return false;
		}		
		quake=quakeDatas[pos];
		compaction=Float.parseFloat(comText4.getText().toString());
		data.setData(ADJUST_TYPE,3, quake, compaction);
		
		pos=Integer.parseInt(posText5.getText().toString());
		if(pos<0 || pos>ADJUST_LENGTH){
			showText(R.string.ad_msg_outrange);
			return false;
		}		
		quake=quakeDatas[pos];
		compaction=Float.parseFloat(comText5.getText().toString());
		data.setData(ADJUST_TYPE,4, quake, compaction);
		
		pos=Integer.parseInt(posText6.getText().toString());
		if(pos<0 || pos>ADJUST_LENGTH){
			showText(R.string.ad_msg_outrange);
			return false;
		}		
		quake=quakeDatas[pos];
		compaction=Float.parseFloat(comText6.getText().toString());
		data.setData(ADJUST_TYPE,5, quake, compaction);
		
		return true;
	}
	
	private void showText(int resId)
	{
		showText(getString(resId));
	}
	
	private void showText(String text)
	{
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}

	@Override
	public void refresh(double data) {
		if(currentIndex>=100){
			onStopButton();
			return;
		}
		quakeDatas[currentIndex]=(float)data;
		plotView.getEdit().addPoint(new PointF(currentIndex,quakeDatas[currentIndex])).commit();;
		currentIndex++;
	}
}








