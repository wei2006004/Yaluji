package ylj.sample;

import java.util.ArrayList;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import ylj.sample.ColorRunActivity;
import ylj.sample.DrawData;
import ylj.sample.DrawData.PlotData;
import ylj.sample.DrawData.TestData;
import ylj.sample.QuakePlotActivity;
import com.ylj.R;
import ylj.sample.SpeedPlotActivity;
import ylj.sample.TempPlotActivity;
import ylj.service.control.TestCtrl;

import ylj.common.bean.RuntimePara;
import ylj.common.widget.PlotView;
import ylj.common.widget.TracePlantView;
import ylj.common.widget.TracePlantView.PlantData;

public class TestFragment extends Fragment 
{
	public final static String TAG ="TestFragment";
	public final static boolean D=true;
	
	private int refreshTimes = 0;
	private static final int REFRESH_STEP = 50;
	
	private PlotView quakeView;
	private PlotView tempView;
	private PlotView speedView;
	
	public final static int MAX_QUAKE=13;
	public final static int MIN_QUAKE=-2;
	public final static int MAX_TEMP=250;
	public final static int MIN_TEMP=0;
	public final static int MAX_SPEED=5;
	public final static int MIN_SPEED=0;
	
	private TracePlantView mPlantView;
	
	private Button colorButton;
	private Button tempButton;
	private Button quakeButton;
	private Button speedButton;

	private DrawData drawData;
	
	public TestFragment()
	{
		drawData=DrawData.instance();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View view=inflater.inflate(R.layout.fragment_test, null);
		
		mPlantView=(TracePlantView)view.findViewById(R.id.traceview);
		quakeView=(PlotView)view.findViewById(R.id.quakeview);
		tempView=(PlotView)view.findViewById(R.id.tempview);
		speedView=(PlotView)view.findViewById(R.id.speedview);
		
		initPlotAxes(quakeView,MIN_QUAKE, MAX_QUAKE);
		initPlotAxes(tempView, MIN_TEMP, MAX_TEMP);
		initPlotAxes(speedView, MIN_SPEED, MAX_SPEED);
		
		colorButton=(Button)view.findViewById(R.id.button_color);
		colorButton.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(getActivity(),ColorRunActivity.class);
				startActivity(intent);
			}
		});
		tempButton=(Button)view.findViewById(R.id.button_temp);
		tempButton.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(getActivity(),TempPlotActivity.class);
				startActivity(intent);
			}
		});
		quakeButton=(Button)view.findViewById(R.id.button_quake);
		quakeButton.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(getActivity(),QuakePlotActivity.class);
				startActivity(intent);
			}
		});
		speedButton=(Button)view.findViewById(R.id.button_speed);
		speedButton.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(getActivity(),SpeedPlotActivity.class);
				startActivity(intent);
			}
		});
		
		refreshTimes=(int)(TestCtrl.instance().runTime()/(REFRESH_STEP*2));
		clearPlot(quakeView);
		clearPlot(tempView);
		clearPlot(speedView);
		
		return view;
	}

	private void initPlotAxes(PlotView plotView,float ymin,float ymax)
	{
		PlotView.DrawEdit edit=plotView.getEdit();			
		edit.setXAxes(REFRESH_STEP * refreshTimes,
				REFRESH_STEP * (refreshTimes + 1));
		edit.setYAxes(ymin, ymax).commit();
	}
	
	@Override
	public void onStart()
	{
		super.onStart();	
		
		RuntimePara para=TestCtrl.instance().getRuntimePara(getActivity());
		mPlantView.getEdit()
				.setField(para.getRoadLength(), para.getRoadWidth())
				.setPlant(para.getRollWidth(), 5).setOrigin(para.getOrigin()).commit();
		if(D)Log.d(TAG, "orgin:"+para.getOrigin());
		if(!drawData.isEmpty())
			draw();
	}
	
	private void draw()
	{
		drawTrace();
		//drawPlot();
		if(TestCtrl.instance().testState()!=TestCtrl.STATE_RUN){
			new Handler().postDelayed(new Runnable() {			
				@Override
				public void run() {
					drawPlot();
				}
			}, 500);
		}else{
			isRedrawPlot=true;
		}
	}
	
	private void drawTrace()
	{
		ArrayList<PlantData> plantDatas=drawData.getPlantDatas();
		if(D)Log.d(TAG, "plant:"+plantDatas.size());
		TracePlantView.DrawEdit edit=mPlantView.getEdit();
		edit.clear();
		for(TracePlantView.PlantData data:plantDatas){
			edit.addPlant(data);
		}
		edit.commit();		
	}
	
	boolean isRedrawPlot=false;
	
	private void drawPlot()
	{
		ArrayList<PlotData> dataList=drawData.getPlotDatas();
		PlotView.DrawEdit tempEdit = tempView.getEdit();
		PlotView.DrawEdit quakeEdit = quakeView.getEdit();
		PlotView.DrawEdit speedEdit = speedView.getEdit();
		tempEdit.clear();
		quakeEdit.clear();
		speedEdit.clear();
		for (PlotData data : dataList) {
			tempEdit.addPoint(new PointF((float)data.runtime, data.temp));
			quakeEdit.addPoint(new PointF((float)data.runtime, data.quake));
			speedEdit.addPoint(new PointF((float)data.runtime, data.speed));
		}
		tempEdit.commit();
		quakeEdit.commit();
		speedEdit.commit();
	}
	
	public void clear()
	{
		refreshTimes=0;
		
		if(isAdded()){
			clearPlot(quakeView);
			clearPlot(tempView);
			clearPlot(speedView);
			
			mPlantView.getEdit().clear().commit();
		}
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
	
	public void refresh()
	{
		TestData data=drawData.getCurrentData();
		refreshTrace(data.getPlantData());
		refreshPlot(data);
	}
	
	private void refreshTrace(PlantData data)
	{
		if(isAdded()){			
			mPlantView.getEdit().addPlant(data)
					.commit();
		}
	}
	
	private void refreshPlot(PlotData data)
	{
		long runTime=data.runtime_long;
		float temp=data.temp;
		float quake=data.quake;
		float speed=data.speed;
		
		if (runTime % (REFRESH_STEP*2) == 0) {			
			if (runTime != 0) {
				if(D)Log.d(TAG, "refresh:"+runTime);
				refreshTimes++;
				clearPlot(quakeView);
				clearPlot(tempView);
				clearPlot(speedView);
			}
			drawData.clearPlotData();
		}
		
		double run=data.runtime;
		PointF quakePointF=new PointF((float)run, quake);
		PointF tempPointF=new PointF((float)run, temp);
		PointF speedPointF=new PointF((float)run, speed);
		if(isAdded()){
			if(isRedrawPlot){
				drawPlot();
				isRedrawPlot=false;
			}
			quakeView.getEdit().addPoint(quakePointF).commit();
			tempView.getEdit().addPoint(tempPointF).commit();
			speedView.getEdit().addPoint(speedPointF).commit();
		}
	}
}
