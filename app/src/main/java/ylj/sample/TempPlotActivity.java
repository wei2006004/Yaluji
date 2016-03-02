package ylj.sample;

import java.util.ArrayList;

import com.ylj.R;
import ylj.service.control.TestCtrl;

import ylj.common.widget.PlotView;
import ylj.common.tool.fullscreen.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class TempPlotActivity extends Activity {
	
	public final static String TAG ="TempPlotActivity";
	public final static boolean D=true;
	
	private int refreshTimes = 0;
	private static final int REFRESH_STEP = 50;
	
	public final static int MAX_VALUE=250;
	public final static int MIN_VALUE=0;
	
	private static final boolean AUTO_HIDE = true;
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
	private static final boolean TOGGLE_ON_CLICK = true;

	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	private SystemUiHider mSystemUiHider;
	
	private PlotView plotView;
	private TextView speedView;
	private TextView tempView;
	private TextView quakeView;
	
	private DrawData drawData;
	private Handler handler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_temp_plot);
		
		plotView=(PlotView)findViewById(R.id.plotview);
		speedView=(TextView)findViewById(R.id.speed_text);
		tempView=(TextView)findViewById(R.id.temp_text);
		quakeView=(TextView)findViewById(R.id.quake_text);
		
		mSystemUiHider = SystemUiHider.getInstance(this, plotView,
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

		plotView.setOnClickListener(new View.OnClickListener() {
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
		drawData=DrawData.instance();
		if(handler==null){
			handler=new Handler(){
				@Override
				public void handleMessage(Message msg)
				{
					refresh();
				}
			};
		}
		TestCtrl.instance().addRefreshHandler(handler);
		refreshTimes=(int)(TestCtrl.instance().runTime()/(REFRESH_STEP*2));
		initPlotAxes(plotView, MIN_VALUE, MAX_VALUE);
		new Handler().postDelayed(new Runnable() {			
			@Override
			public void run() {
				drawPlot();
			}
		}, 500);
	}
	
	private void initPlotAxes(PlotView plotView,float ymin,float ymax)
	{
		PlotView.DrawEdit edit=plotView.getEdit();			
		edit.setXAxes(REFRESH_STEP * refreshTimes,
				REFRESH_STEP * (refreshTimes + 1));
		edit.setYAxes(ymin, ymax).commit();
	}
	
	private void drawPlot()
	{
		ArrayList<DrawData.PlotData> dataList=drawData.getPlotDatas();
		PlotView.DrawEdit tempEdit = plotView.getEdit();
		tempEdit.clear();
		for (DrawData.PlotData data : dataList) {
			tempEdit.addPoint(new PointF((float)data.runtime, data.temp));
		}
		tempEdit.commit();
	}
	
	private void clearPlot(PlotView plotView)
	{
		if(plotView==null)
			return;
		PlotView.DrawEdit edit = plotView.getEdit();
		edit.clear();
		edit.setXAxes(REFRESH_STEP * refreshTimes, REFRESH_STEP
				* (refreshTimes + 1));
		edit.commit();
	}
	
	private void refresh()
	{		
		DrawData.TestData data=drawData.getCurrentData();
		long runTime=data.runtime_long;
		float temp=data.temp;
		
		if (runTime % (REFRESH_STEP*2) == 0) {			
			if (runTime != 0) {
				if(D)Log.d(TAG, "refresh:"+runTime);
				refreshTimes++;
				clearPlot(plotView);
			}
			drawData.clearPlotData();
		}
		
		if(runTime%2==0){
			speedView.setText(String.format("%.1fm/s", data.speed));
			tempView.setText(String.format("%.1f°C", data.temp));
			quakeView.setText(String.format("%.1fg", data.quake));
		}
		
		double run=data.runtime;
		PointF tempPointF=new PointF((float)run, temp);
		plotView.getEdit().addPoint(tempPointF).commit();

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

	private void delayedHide(int delayMillis)
	{
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.temp_plot, menu);
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
