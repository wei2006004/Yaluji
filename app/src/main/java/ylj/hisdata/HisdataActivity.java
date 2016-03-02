package ylj.hisdata;

import java.util.ArrayList;

import ylj.sample.ColorActivity;
import ylj.sample.ColorCalculator;
import com.ylj.R;
import ylj.common.TagAdapter;

import ylj.common.bean.TagItem;

import ylj.common.tool.FtpThread;
import ylj.common.tool.Tools;
import ylj.common.bean.Record;
import ylj.common.bean.RuntimeData;
import ylj.common.widget.ColorView;
import ylj.common.widget.ColorView.DrawEdit;
import ylj.common.widget.PlotView;
import ylj.model.RecordDbAccess;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PointF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;


public class HisdataActivity extends Activity
{
	public static final String TAG="HisdataActivity";
	public static final boolean D=true;
	
	public static final String TEST_FILE_NAME="test_file";
	
	public static final int COLOR_ROW_NUM=12;
	public static final int COLOR_COLUMN_NUM=20;
	
	private ListView listView;
	private TagAdapter listAdapter;
	private ArrayList<TagItem> tagItems=new ArrayList<TagItem>();
	
	private static RuntimeData runtimeData=null;
	
	private RecordDbAccess dbAccess=new RecordDbAccess();
	private RecordDbAccess.Reader reader;
	private HisdataFragment hisdataFragment;

	private ProgressDialog progressDialog;
	private FtpThread thread;
	
	private static final int CHANGE_PROGRESS=0;
	private static final int DISMISS_AND_SHOW_MESSAGE=1;
	private static final int SHOW_MESSAGE=2;
	
	private Handler ftpHandler;
	
	//private String file;
	
	private  static String fileName;
	private static boolean isGetData=false;
	
	public static void setFileName(String file)
	{
		fileName=file;
		isGetData=false;
		runtimeData=null;
	}
	
	private String getFileName()
	{
		return this.getApplicationContext().getFilesDir().getAbsolutePath()
				+ "/" + fileName;
	}
	
	private int[] titles={
		R.string.hd_device_id,
		R.string.hd_road_length,
		R.string.hd_road_width,
		R.string.hd_distance,
		R.string.hd_time,
		R.string.hd_unqualified,
		R.string.hd_near_qualified,
		R.string.hd_qualified,
		R.string.hd_fine
	};
	
	public static final float VCV_DEFAULT_VALUE=10;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hisdata);	
		
		
//		Intent intent=getIntent();
//		Bundle data=intent.getExtras();	
//		file = data.getString(TEST_FILE_NAME);
//		file = this.getApplicationContext().getFilesDir().getAbsolutePath()
//				+ "/" + file;
		
		ColorCalculator colorData=ColorCalculator.instance();
		SharedPreferences preferences=Tools.getAppPreferences(this);
		float vcv=preferences.getFloat(getString(R.string.pref_vcv), VCV_DEFAULT_VALUE);
		if (runtimeData == null || (!isGetData)) {
			String file = getFileName();
			if (D)
				Log.d(TAG, file);
			dbAccess.setDbName(file);
			reader = dbAccess.createReader();
			reader.open();
			runtimeData = reader.read();
			isGetData=true;			
			colorData.setGrid(COLOR_ROW_NUM, COLOR_COLUMN_NUM);
			colorData.setRuntimeData(runtimeData,vcv);			
		}
	
		if(hisdataFragment==null)
			hisdataFragment=new HisdataFragment();
		getFragmentManager().beginTransaction()
			.add(R.id.fragment_detail,hisdataFragment ).commit();
		
		new Handler().postDelayed(new Runnable() {		
			@Override
			public void run()
			{
				hisdataFragment.addData(runtimeData);
			}
		}, 300);
		
		initTags();
		if(listAdapter==null)
			listAdapter=new TagAdapter(tagItems,this);
		listAdapter.setViewLayout(R.layout.layout_test_list_item);
		listView=(ListView)findViewById(R.id.listview);
		listView.setAdapter(listAdapter);
		
		if(progressDialog==null){
			progressDialog=new ProgressDialog(this);
			progressDialog.setTitle(R.string.hisdata_progress_title);
			progressDialog.setMax(100);
			progressDialog.setProgress(0);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressDialog.setIndeterminate(false);
		}
		
		ftpHandler=new Handler(){
			@Override
			public void handleMessage(Message msg)
			{
				if(msg.what==CHANGE_PROGRESS){
					progressDialog.setProgress(thread.getProgressNum());
				}else if(msg.what==DISMISS_AND_SHOW_MESSAGE){
					progressDialog.dismiss();
					if(thread.getFtpText()!=null){
						Toast.makeText(HisdataActivity.this, thread.getFtpText(), Toast.LENGTH_LONG).show();
					}
				}else if(msg.what==SHOW_MESSAGE){
					showProgressDialogText(thread.getFtpText());
				}
			}
		};
	}
	
	public void initData()
	{
		runtimeData.clear();
		runtimeData.addRecord(new Record());
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
		ArrayList<String> valueList=new ArrayList<String>();
		Record record=runtimeData.getRecord(runtimeData.size()-1);
		
		valueList.add(runtimeData.getDeviceId());
		valueList.add(String.valueOf(runtimeData.getRoadLength())+"m");
		valueList.add(String.valueOf(runtimeData.getRoadWidth())+"m");
		valueList.add(String.valueOf(record.getDistance())+"m");
		valueList.add(String.valueOf(runtimeData.size()/2)+"s");
		
		ColorCalculator colorData=ColorCalculator.instance();
		colorData.countNumber();
		valueList.add(String.valueOf(colorData.getNotPassNum())+"块");
		valueList.add(String.valueOf(colorData.getNearNum())+"块");
		valueList.add(String.valueOf(colorData.getPassNum())+"块");
		valueList.add(String.valueOf(colorData.getGoodNum())+"块");
		
		TagItem item;
		for(int i=0;i<titles.length;i++){
			item=tagItems.get(i);
			item.setValue(valueList.get(i));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.hisdata, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();
		if (id == R.id.action_fullscreen) {
			Intent intent=new Intent(HisdataActivity.this,ColorActivity.class);
			startActivity(intent);
			return true;
		}else if(id==R.id.action_upload){
			if(isNetworkConnected(this)){
				upload(fileName);
			}else{
				Toast.makeText(this, getString(R.string.com_no_connect_promt), Toast.LENGTH_LONG).show();
			}					
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public boolean isNetworkConnected(Context context) {
		if (context != null) {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context
		.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
		if (mNetworkInfo != null) {
		return mNetworkInfo.isAvailable();
		}
		}
		return false;
	}
	
	private void showProgressDialogText(String text)
	{
		progressDialog.setMessage(text);
	}
	
	private void upload(String file)
	{
		if(thread!=null){			
			thread.cancel();
			thread=null;
		}
		progressDialog.show();
		showProgressDialogText(getString(R.string.hisdata_ftp_connect));		
		
		thread=new FtpThread(this,file,ftpHandler);
		thread.start();
	}

	private class HisdataFragment extends Fragment
	{
		public final static int MAX_QUAKE=13;
		public final static int MIN_QUAKE=-2;
		public final static int MAX_TEMP=250;
		public final static int MIN_TEMP=0;
		
		private PlotView quakeView;
		private PlotView tempView;
		private PlotView traceView;
		private PlotView speedView;
		private ColorView colorView;
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
		{
			View view=inflater.inflate(R.layout.fragment_hisdata_view, null);
			
			quakeView=(PlotView)view.findViewById(R.id.quakeview);
			tempView=(PlotView)view.findViewById(R.id.tempview);
			speedView=(PlotView)view.findViewById(R.id.speedview);
			traceView=(PlotView)view.findViewById(R.id.traceview);
			colorView=(ColorView)view.findViewById(R.id.colorview);
			//addData(runtimeData);
			
			return view;
		}
		
//		@Override
//		public void onStart()
//		{
//			super.onStart();
//			addData(runtimeData);
//		}
		
		public void addData(RuntimeData data)
		{
			if(data==null)
				return;
			initPlotAxes(data);
			
			Record record;
			PlotView.DrawEdit quakeEdit=quakeView.getEdit();
			PlotView.DrawEdit tempEdit=tempView.getEdit();
			PlotView.DrawEdit speedEdit=speedView.getEdit();
			PlotView.DrawEdit traceEdit=traceView.getEdit();

			for(int i=0;i<data.size();i++)
			{
				record=data.getRecord(i);
				quakeEdit.addPoint(new PointF(i,record.getQuake()));
				tempEdit.addPoint(new PointF(i,record.getTemp()));
				speedEdit.addPoint(new PointF(i,record.getSpeed()));
				traceEdit.addPoint(record.getPosition());
				//if(D)Log.d(TAG, "Point:"+record.getPosition());
			}
			quakeEdit.commit();
			tempEdit.commit();
			traceEdit.commit();
			speedEdit.commit();
			
			addColors(data);
		}
		
		private void initPlotAxes(RuntimeData data)
		{
			int size=data.size();
			quakeView.getEdit().setXAxes(0, size).setYAxes(MIN_QUAKE, MAX_QUAKE).commit();
			tempView.getEdit().setXAxes(0, size).setYAxes(MIN_TEMP, MAX_TEMP).commit();
			speedView.getEdit().setXAxes(0, size).setYAxes(0, 5).commit();
			traceView.getEdit().setGridEnable(false).setXAxes(0, data.getRoadLength()).setYAxes(0, data.getRoadWidth()).commit();		
		}
		
		
		private void addColors(RuntimeData data)
		{
			ColorCalculator colorData=ColorCalculator.instance();
			if(!colorData.isCounted()){
				SharedPreferences preferences=Tools.getAppPreferences(HisdataActivity.this);
				float vcv=preferences.getFloat(getString(R.string.pref_vcv), VCV_DEFAULT_VALUE);
				colorData.setGrid(COLOR_ROW_NUM, COLOR_COLUMN_NUM);
				colorData.setRuntimeData(data,vcv);
			}
			DrawEdit edit=colorView.getEdit();
			edit.clear();
			edit.setOrigin(true, colorData.getOrigin());
			edit.setGrid(COLOR_ROW_NUM, COLOR_COLUMN_NUM);
			for(int i=0;i<COLOR_ROW_NUM;i++){
				for(int j=0;j<COLOR_COLUMN_NUM;j++){
					edit.setColor(i, j, colorData.getColor(i, j));
					//if(D)Log.d(TAG, "color:"+i+":"+j+":"+colorData.getColor(i, j));
				}
			}
			edit.commint();
		}
	}
	
}
