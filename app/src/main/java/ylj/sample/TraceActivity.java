package ylj.sample;

import java.lang.reflect.Method;
import java.util.ArrayList;

import ylj.sample.DrawData.TestData;

import com.ylj.R;
import ylj.service.control.TestCtrl;

import ylj.common.bean.RuntimePara;
import ylj.common.widget.TracePlantView;
import ylj.common.widget.TracePlantView.PlantData;
import ylj.common.tool.fullscreen.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class TraceActivity extends Activity
{
	public static final String TAG="TraceActivity";
	public static final boolean D=true;
	
	private static final boolean AUTO_HIDE = true;
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
	private static final boolean TOGGLE_ON_CLICK = true;

	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	private SystemUiHider mSystemUiHider;
	
	private Handler handler;

	TracePlantView traceView;
	private TestCtrl testCtrl=TestCtrl.instance();
	//private CommonData data=TestCtrl.instance().getCommonData();
	
	//private ArrayList<TracePlantView.PlantData> plantDatas =TestFragment.plantDatas;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_trace);

		traceView = (TracePlantView)findViewById(R.id.traceview);

		mSystemUiHider = SystemUiHider.getInstance(this, traceView,
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

		traceView.setOnClickListener(new View.OnClickListener() {
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
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		if(D)Log.d(TAG, "onstart");
		
		RuntimePara para=TestCtrl.instance().getRuntimePara(this);
		traceView.getEdit().setField(para.getRoadLength(), para.getRoadWidth()).setOrigin(para.getOrigin()).commit();		
		
		TracePlantView.DrawEdit edit=traceView.getEdit();
		ArrayList<PlantData> plantDatas=DrawData.instance().getPlantDatas();
		for(PlantData data:plantDatas){
			edit.addPlant(data);
		}
		edit.commit();
		
		menuFlag=testCtrl.testState()==TestCtrl.STATE_RUN?false:true;
		invalidateOptionsMenu();
		
	}
	
	private DrawData drawData=DrawData.instance();
	
	private void refresh()
	{		
		//if(D)Log.d(TAG, "refresh:"+data.getDirection());
		TestData data=drawData.getCurrentData();
		traceView.getEdit().addPlant(data.getPlantData()).commit();
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

	private boolean menuFlag=true;

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		setIconEnable(menu, true);
		return super.onCreateOptionsMenu(menu);
	}
	
	private void setIconEnable(Menu menu, boolean enable)  
    {  
        try   
        {  
            Class<?> clazz = Class.forName("com.android.internal.view.menu.MenuBuilder");  
            Method m = clazz.getDeclaredMethod("setOptionalIconsVisible", boolean.class);  
            m.setAccessible(true);  
            m.invoke(menu, enable);  
              
        } catch (Exception e)   
        {  
            e.printStackTrace();  
        }  
    } 
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		//menuFlag=testCtrl.testState()==TestCtrl.STATE_RUN?false:true;
		menu.clear();
		if (menuFlag/*true*/) {
			getMenuInflater().inflate(R.menu.menu_trace_run, menu);
		} else {
			getMenuInflater().inflate(R.menu.menu_trace_pause, menu);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();
		if (id == R.id.action_run) {
			if(TestCtrl.instance().testState()==TestCtrl.STATE_PAUSE){
				restart();
			}else{
				start();
			}		
			menuFlag = false;
			invalidateOptionsMenu();
			return true;
		} else if (id == R.id.action_pause) {
			pause();
			menuFlag = true;
			invalidateOptionsMenu();
			return true;
		} else if (id == R.id.action_stop) {
			stop();
			menuFlag = true;
			invalidateOptionsMenu();
			return true;
		}else if(id==R.id.action_fullscreen){
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void start()
	{
		traceView.getEdit().clear().commit();
		testCtrl.startTest(this);
	}
	
	private void restart()
	{
		testCtrl.restartTest();
	}
	
	private void pause()
	{
		testCtrl.pauseTest();
	}
	
	private void stop()
	{
		testCtrl.stopTest();
	}
}
