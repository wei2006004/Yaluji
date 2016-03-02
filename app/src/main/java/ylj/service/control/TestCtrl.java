package ylj.service.control;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

import com.ylj.R;

import ylj.common.bean.HisdataItem;
import ylj.connect.Com;
import ylj.common.tool.Tools;
import ylj.common.bean.DeviceData;
import ylj.common.bean.DeviceInfo;
import ylj.common.bean.Record;
import ylj.common.bean.RuntimePara;
import ylj.service.source.CommonData;
import ylj.service.source.DebugSource;
import ylj.service.source.DeviceSource;
import ylj.model.RecordDbAccess;
import ylj.sample.DrawData;

public class TestCtrl
{
	public final static String TAG="TestCtrl";
	public final static boolean D=true;
	
	public final static String MESSAGE_START="2";
	public final static String MESSAGE_STOP="3";
	
	public final static int STATE_RUN=0;
	public final static int STATE_PAUSE=1;
	public final static int STATE_STOP=2;
	
	private long mRuntime=0;
	private boolean isDebug=false;
	
	private Com com;
	
	private DebugSource debugSource=new DebugSource();
	private DeviceSource deviceSource=new DeviceSource();
	private CommonData commonData;
	
	private RecordDbAccess dbAccess=new RecordDbAccess();
	private RecordDbAccess.Writer writer;
	private HisdataItem hisdataItem=new HisdataItem();
	private RuntimePara runtimePara=new RuntimePara();
	
	private float vcv_value=10;
	public static final float VCV_DEFAULT_VALUE=10;
	
	public RuntimePara getRuntimePara(Context context)
	{
		refreshPara(context);
		return runtimePara;
	}
	
	private void refreshPara(Context context)
	{
		SharedPreferences preferences=Tools.getAppPreferences(context);
		
		int huoer=preferences.getInt(context.getString(R.string.pref_huoer_num), 4);
		float diameter=preferences.getFloat(context.getString(R.string.pref_roll_diameter), 2);
		float step=(float)3.1415*diameter/huoer;
		
		vcv_value=preferences.getFloat(context.getString(R.string.pref_vcv), VCV_DEFAULT_VALUE);
		
		runtimePara.setHuoerNum(huoer);
		runtimePara.setRoadLength(preferences.getFloat(context.getString(R.string.pref_road_length), 50));
		runtimePara.setRoadWidth(preferences.getFloat(context.getString(R.string.pref_road_width), 30));
		runtimePara.setRollDiameter(diameter);
		runtimePara.setRollWidth(preferences.getFloat(context.getString(R.string.pref_roll_width), (float)2.4));
		runtimePara.setOrigin(preferences.getInt(context.getString(R.string.pref_origin), RuntimePara.ORIGIN_ANTI_CLOCKWISE));
		runtimePara.setStep(step);
		
		runtimePara.setStationIp(preferences.getString(
				context.getString(R.string.pref_ip),
				context.getString(R.string.rt_def_ip)));
		runtimePara.setPort(Integer.valueOf(preferences.getString(
				context.getString(R.string.pref_port),
				context.getString(R.string.rt_def_port))));
		runtimePara.setUser(preferences.getString(
				context.getString(R.string.pref_user),
				context.getString(R.string.rt_def_user)));
		runtimePara.setPassword(preferences.getString(
				context.getString(R.string.pref_password),
				context.getString(R.string.rt_def_password)));
	}
	
	public void setDeviceInfo(DeviceInfo info)
	{
		runtimePara.setDeviceId(info.getDeviceId());
		runtimePara.setSoftVersion(info.getSoftVersion());
		runtimePara.setVersion(info.getVersion());
		runtimePara.setProductDate(info.getProductDate());
	}
	
	public CommonData getCommonData()
	{
		return commonData;
	}
	
	public void setDebug(boolean isDebug)
	{
		if(this.isDebug==isDebug)
			return;
		this.isDebug=isDebug;
		if(isDebug){
			deviceSource.stop();
			debugSource.start();
			commonData.setDataSource(debugSource);
		}else{
			debugSource.stop();
			deviceSource.start();
			commonData.setDataSource(deviceSource);
		}
	}
	
	public void setDeviceData(DeviceData data)
	{
		deviceSource.setCurrentData(data);
	}
	
	public void setCom(Com com)
	{
		this.com=com;
	}
	
	public TestCtrl()
	{		
		if (isDebug) {
			commonData = new CommonData(debugSource);
		} else {
			commonData = new CommonData(deviceSource);
		}
	}
	
	private static TestCtrl mInstance=null;
	public static TestCtrl instance()
	{
		if(mInstance==null)
			mInstance=new TestCtrl();
		return mInstance;
	}
		
	private Timer mTimer=null;
	
	public void start()
	{
		mRuntime=0;
		commonData.reset();
		if(mTimer!=null)
			return;
		mTimer=new Timer();
		mTimer.scheduleAtFixedRate(new TimerTask() {			
			@Override
			public void run()
			{
				refresh();
			}
		}, 500, 500);
		
	}
	
	private int mState=STATE_STOP;
	
	public long runTime()
	{
		return mRuntime;
	}
	
	public int testState()
	{
		return mState;
	}
	
	private boolean isAdjust=false;
	public void startAdjust()
	{
		if(!isDebug){
			if(com!=null)
				com.sendStartMessage();
		}
		mRuntime=0;
		commonData.reset();
		isAdjust=true;
	}
	
	public void stopAdjust()
	{
		if(!isDebug){
			if(com!=null)
				com.sendStopMessage();
		}
		isAdjust=false;
	}
	
	public void startTest(Context context)
	{
		if(!isDebug){
			if(com!=null)
				com.sendStartMessage();
		}
		mRuntime=0;
		commonData.reset();
		
		refreshPara(context);
		hisdataItem.setDeviceId(runtimePara.getDeviceId());
		hisdataItem.setTestTime(new Date());
		
		String fileString=context.getApplicationContext().getFilesDir().getAbsolutePath();
		fileString=fileString+"/"+hisdataItem.getFileName();
		if(D)Log.d(TAG, "file:"+fileString);
		dbAccess.setDbName(fileString);
		writer=dbAccess.createWriter();
		writer.create();
		writer.addParas(runtimePara);
		
		mState=STATE_RUN;
		
		DrawData.instance().clear();
	}
	
	public void restartTest()
	{		
		if(mState==STATE_PAUSE){
			mState=STATE_RUN;
			
			if (!isDebug) {
				if(com!=null)
					com.sendStartMessage();
			}
		}
	}
	
	public void pauseTest()
	{
		if(mState==STATE_RUN){
			mState=STATE_PAUSE;
			if(!isDebug){
				if(com!=null)
					com.sendStopMessage();
			}
		}
	}
	
	public boolean stopTest()
	{
		if(!isDebug){
			if(com!=null)
				com.sendStopMessage();
		}
		mState=STATE_STOP;
		return true;
	}
	
	private void refresh()
	{		
		if(isAdjust){
			mRuntime++;
			commonData.refresh();
			if(!adjustHandlers.isEmpty()){
				for(Handler handler:adjustHandlers){
					handler.sendEmptyMessage(0);
				}
			}
			return;
		}
		if(mState==STATE_RUN){
			mRuntime++;
			commonData.refresh();
			DrawData.instance().refresh(vcv_value);
			
			if(!handlers.isEmpty()){
				for(Handler handler:handlers){
					handler.sendEmptyMessage(0);
				}
			}
			
			Record record=new Record();
			record.set(commonData.getDistance(), 
					commonData.getSpeed(), 
					commonData.getDirection(),
					commonData.getState(),
					commonData.getTemp(), 
					commonData.getQuake(),
					commonData.getPosition().x, 
					commonData.getPosition().y);
			writer.addRecord(record);
		
		}
	}
	
	private ArrayList<Handler> adjustHandlers=new ArrayList<Handler>();
	
	public void addAdjustHandler(Handler handler)
	{
		adjustHandlers.add(handler);
	}

	private ArrayList<Handler> handlers=new ArrayList<Handler>();
	
	public void addRefreshHandler(Handler handler)
	{
		handlers.add(handler);
	}
	
}
