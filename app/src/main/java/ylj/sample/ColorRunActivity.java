package ylj.sample;

import java.util.ArrayList;

import com.ylj.R;
import ylj.service.control.TestCtrl;

import ylj.common.tool.Tools;
import ylj.common.bean.ColorData;
import ylj.common.widget.ColorView;
import ylj.common.widget.ColorView.DrawEdit;
import ylj.common.tool.fullscreen.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ColorRunActivity extends Activity {
	public static final String TAG="ColorRunActivity";
	public static final boolean D=true;
	
	private static final boolean AUTO_HIDE = true;
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
	private static final boolean TOGGLE_ON_CLICK = true;
	
	private static final int COLOR_VIEW_ROW=12;
	private static final int COLOR_VIEW_COLUMN=20;

	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	private SystemUiHider mSystemUiHider;
	
	private ColorView colorView;
	
	private ColorCalculator caluclator;
	
	private TextView rowText;
	private TextView columnText;
	private TextView countText;
	private TextView valueText;
	private TextView stateText;
	
	private Handler handler;
	private TestCtrl testCtrl=TestCtrl.instance();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_color_run);
		colorView=(ColorView)findViewById(R.id.colorview);
		
		rowText=(TextView)findViewById(R.id.test_row);
		columnText=(TextView)findViewById(R.id.test_column);
		countText=(TextView)findViewById(R.id.test_count);
		valueText=(TextView)findViewById(R.id.test_value);
		stateText=(TextView)findViewById(R.id.test_state);
		
		mSystemUiHider = SystemUiHider.getInstance(this, colorView,
				HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider
				.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {

					@Override
					@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
					public void onVisibilityChange(boolean visible)
					{
						if (visible && AUTO_HIDE) {
							delayedHide(AUTO_HIDE_DELAY_MILLIS);
						}
					}
				});
		
		colorView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view)
			{
				if (TOGGLE_ON_CLICK) {
					mSystemUiHider.toggle();
				} else {
					mSystemUiHider.show();
				}
			}
		});
		
		if(handler==null){
			handler=new Handler(){
				@Override
				public void handleMessage(Message msg)
				{
					refresh();
				}
			};
		}
		testCtrl.addRefreshHandler(handler);
		
		if(caluclator==null){
			caluclator=new ColorCalculator();
			caluclator.setGrid(COLOR_VIEW_ROW, COLOR_VIEW_COLUMN);
			caluclator.setRuntimePata(TestCtrl.instance().getRuntimePara(this));
		}
	}
	
	public static final float VCV_DEFAULT_VALUE=10;
	
	@Override
	protected void onStart()
	{
		super.onStart();
		if(D)Log.d(TAG, "onstart");
		caluclator.init();
		ArrayList<DrawData.PlotData> plotDatas=drawData.getPlotDatas();
		SharedPreferences preferences=Tools.getAppPreferences(this);
		float vcv=preferences.getFloat(getString(R.string.pref_vcv), VCV_DEFAULT_VALUE);
		for (DrawData.PlotData plotData : plotDatas) {
			caluclator.addData(plotData.position, plotData.quake,vcv);
		}
		ColorData data;
		DrawEdit edit=colorView.getEdit();
		edit.setGrid(caluclator.getRowNum(), caluclator.getColumnNum());
		edit.setOrigin(true,caluclator.getOrigin() );
		for(int i=0;i<caluclator.getRowNum();i++){
			for(int j=0;j<caluclator.getColumnNum();j++){
				data=caluclator.getData(i, j);
				edit.setColor(i, j, data.getColor());
			}
		}
		edit.commint();
		refreshTag(caluclator.getCurrentRow(), caluclator.getCurrentColumn(), caluclator.getCurrentData());
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);

		delayedHide(100);
	}

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run()
		{
			mSystemUiHider.hide();
		}
	};
	
	private DrawData drawData=DrawData.instance();
	
	private int time_flag=0;
	private void refresh()
	{		
		DrawData.TestData data=drawData.getCurrentData();
		SharedPreferences preferences=Tools.getAppPreferences(this);
		float vcv=preferences.getFloat(getString(R.string.pref_vcv), VCV_DEFAULT_VALUE);
		caluclator.addData(data.position, data.quake,vcv);
		ColorData colorData=caluclator.getCurrentData();
		colorView.getEdit().setColor(caluclator.getCurrentRow(), caluclator.getCurrentColumn(), colorData.getColor()).commint();
		
		time_flag++;
		if(time_flag%2==0){
			refreshTag(caluclator.getCurrentRow(), caluclator.getCurrentColumn(),colorData);
		}
	}
	
	private void refreshTag(int row,int column,ColorData colorData)
	{
		rowText.setText(String.valueOf(row));
		columnText.setText(String.valueOf(column));
		countText.setText(String.valueOf(colorData.getCount())+"块");
		valueText.setText(String.format("%.1f", colorData.getValue()));
		int resid=R.string.test_state_nopass;
		switch (colorData.getColor()) {
		case Color.RED:
			resid=R.string.test_state_nopass;
			break;
		case Color.YELLOW:
			resid=R.string.test_state_near;
			break;
		case Color.BLUE:
			resid=R.string.test_state_pass;
			break;
		case Color.GREEN:
			resid=R.string.test_state_good;
			break;
		}
		stateText.setText(resid);
	}

	private void delayedHide(int delayMillis)
	{
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}


//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.color_run, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}
}
