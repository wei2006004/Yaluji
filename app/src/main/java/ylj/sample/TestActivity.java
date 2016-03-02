package ylj.sample;

import java.util.ArrayList;

import com.ylj.R;
import ylj.service.control.TestCtrl;
import ylj.common.TagAdapter;
import ylj.common.bean.TagItem;

import ylj.common.bean.DeviceData;
import ylj.common.bean.RuntimePara;
import ylj.service.source.CommonData;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class TestActivity extends Activity
{
	public final static String TAG ="TestActivity";
	public final static boolean D=true;
	
	private ListView listView;
	private TagAdapter listAdapter;
	private ArrayList<TagItem> tagItems=new ArrayList<TagItem>();
	
	private TraceFragment traceFragment;
	private PlotFragment plotFragment;
	private TestFragment testFragment;
	
	private boolean isSwitchPlot=false;
	
	private TestCtrl testCtrl;
	
	private int[] titles={
		R.string.test_test_state,
		R.string.test_road_length,
		R.string.test_road_width,
		R.string.test_position_x,
		R.string.test_position_y,
		R.string.test_time,
		R.string.test_state,
		R.string.test_distance,
		R.string.test_speed,
		R.string.test_direction,
		R.string.test_quake,
		R.string.test_temp
	};
	
	private Handler handler;

	private Builder mBuilder;	
	private AlertDialog mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		initTags();
		if(listAdapter==null)
			listAdapter=new TagAdapter(tagItems,this);
		listAdapter.setViewLayout(R.layout.layout_test_list_item);
		listView=(ListView)findViewById(R.id.listview);
		listView.setAdapter(listAdapter);
		
//		if(traceFragment==null)
//			traceFragment=new TraceFragment();
//		if(plotFragment==null)
//			plotFragment=new PlotFragment();
		if(testFragment==null)
			testFragment=new TestFragment();
		getFragmentManager().beginTransaction()
			.add(R.id.fragment_detail, testFragment).commit();
		
		testCtrl=TestCtrl.instance();
		//commonData=testCtrl.getCommonData();
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
		
		mBuilder=new Builder(this);
		mBuilder.setTitle(getString(R.string.test_dialog_title));
		mBuilder.setMessage(getString(R.string.test_dialog_text));
		mBuilder.setPositiveButton(getString(R.string.test_ok), new DialogInterface.OnClickListener() {				
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				TestActivity.this.stop();
				dialog.dismiss();
				TestActivity.this.finish();
			}
		});
		mBuilder.setNegativeButton(getString(R.string.test_cancel), new DialogInterface.OnClickListener() {				
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});		
		mDialog=mBuilder.create();
		
//		menuFlag=testCtrl.testState()==TestCtrl.STATE_RUN?false:true;
//		
//		invalidateOptionsMenu();
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		if(D)Log.d(TAG, "onstart");
		if(D)Log.d(TAG, "create state:"+testCtrl.testState());
		if(D)Log.d(TAG, "create flag:"+menuFlag);
		menuFlag=testCtrl.testState()==TestCtrl.STATE_RUN?false:true;
		invalidateOptionsMenu();
	}
	
	private void refresh()
	{
		//if(D)Log.d(TAG, "refresh");
		refreshTagValue();
		listAdapter.notifyDataSetChanged();

//		traceFragment.refresh(commonData.getPosition(),
//				commonData.getDirection());
//		plotFragment.refresh(testCtrl.runTime(),commonData.getTemp(), commonData.getQuake());
		testFragment.refresh();
	}
	
	public void initTags()
	{
		tagItems.clear();
		TagItem item;
		for(int i=0;i<titles.length;i++){
			item=new TagItem(getString(titles[i]),"");
			tagItems.add(item);
		}
		refreshTagValue();
	}
	
	public void refreshTagValue()
	{
		TestCtrl ctrl=TestCtrl.instance();
		CommonData data=ctrl.getCommonData();
		RuntimePara para=ctrl.getRuntimePara(this);
		ArrayList<String> valueList=new ArrayList<String>();
		
		if(ctrl.testState()==TestCtrl.STATE_RUN){
			valueList.add(getString(R.string.test_state_run));
		}else if(ctrl.testState()==TestCtrl.STATE_STOP){
			valueList.add(getString(R.string.test_state_stop));
		}else{
			valueList.add(getString(R.string.test_state_pause));
		}
		valueList.add(String.valueOf(para.getRoadLength())+"m");
		valueList.add(String.valueOf(para.getRoadWidth())+"m");
		
		valueList.add(String.format("%.1fm",data.getPosition().x));
		valueList.add(String.format("%.1fm",data.getPosition().y));
		
		valueList.add(String.valueOf(ctrl.runTime()/2)+"s");
		if(data.getState()==DeviceData.STATE_BACKING){
			valueList.add(getString(R.string.test_back));
		}else if(data.getState()==DeviceData.STATE_HEADING){
			valueList.add(getString(R.string.test_head));
		}else{
			valueList.add(getString(R.string.test_stop));
		}
		
		valueList.add(String.format("%.1fm",data.getDistance()));
		valueList.add(String.format("%.1fm/s",data.getSpeed()));
		valueList.add(String.format("%.1f°",data.getDirection()));
		valueList.add(String.format("%.1fg",data.getQuake()));
		valueList.add(String.format("%.1f°C",data.getTemp()));

		TagItem item;
		for(int i=0;i<titles.length;i++){
			item=tagItems.get(i);
			item.setValue(valueList.get(i));
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode == KeyEvent.KEYCODE_BACK) { 
	    	if(testCtrl.testState()==TestCtrl.STATE_STOP){
	    		return super.onKeyDown(keyCode, event);
	    	}
	    	mDialog.show();	    				
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}

	private  boolean menuFlag=true;
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		menu.clear();
		if(D)Log.d(TAG, "flag:"+menuFlag);
		if (menuFlag) {
			getMenuInflater().inflate(R.menu.menu_test_run, menu);
		} else {
			getMenuInflater().inflate(R.menu.menu_test_pause, menu);
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
			//switchFragment();
			//invalidateOptionsMenu();
			Intent intent =new Intent(TestActivity.this,TraceActivity.class);
			startActivity(intent);
			return true;
		}else if(id==android.R.id.home){
			if(D)Log.d(TAG, "home");
			if(testCtrl.testState()==TestCtrl.STATE_STOP){
	    		return super.onOptionsItemSelected(item);
	    	}
	    	mDialog.show();	    				
	        return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void start()
	{
//		traceFragment.clear();
//		plotFragment.clear();
		testFragment.clear();
		testCtrl.startTest(this);
	}
	
	private void restart()
	{
		testCtrl.restartTest();
	}
	
	private void pause()
	{
		testCtrl.pauseTest();
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				refreshTagValue();
				listAdapter.notifyDataSetChanged();
			}
		}, 300);
		
	}
	
	private void stop()
	{
		testCtrl.stopTest();
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				refreshTagValue();
				listAdapter.notifyDataSetChanged();
			}
		}, 300);
	}
	
//	private void switchFragment()
//	{
//		if(isSwitchPlot){
//			getFragmentManager().beginTransaction()
//				.replace(R.id.fragment_detail, traceFragment).commit();
//			isSwitchPlot=false;
//		}else{
//			getFragmentManager().beginTransaction()
//				.replace(R.id.fragment_detail, plotFragment).commit();
//			isSwitchPlot=true;
//		}
//		
//	}
}