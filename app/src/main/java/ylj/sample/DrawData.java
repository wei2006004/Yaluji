package ylj.sample;

import java.util.ArrayList;

import android.graphics.Color;
import android.graphics.PointF;

import ylj.common.widget.TracePlantView;
import ylj.common.widget.TracePlantView.PlantData;

import ylj.service.control.TestCtrl;
import ylj.service.source.CommonData;

public class DrawData {
	
	public static class PlotData 
	{
		public double runtime;
		public long runtime_long;
		public float temp;
		public float quake;
		public float speed;
		public PointF position;
	}
	
	public static class TestData extends PlotData
	{		
		public float direction;
		public int color;
		public PlantData getPlantData(){
			return new PlantData(position, direction, color);
		}
	}
	
	private CommonData data;
	private TestCtrl ctrl;
	private TestData currentData;
	
	private ArrayList<TracePlantView.PlantData> plantDatas = new ArrayList<TracePlantView.PlantData>();
	private ArrayList<PlotData> plotDatas=new ArrayList<DrawData.PlotData>();
	
	private static DrawData drawData;
	
	public static DrawData instance(){
		if(drawData==null)
			drawData=new DrawData();
		return drawData;
	}
	
	private DrawData()
	{
		ctrl=TestCtrl.instance();
		data=ctrl.getCommonData();
	}
	
	public boolean isEmpty(){
		return plantDatas.isEmpty() && plotDatas.isEmpty();
	}
	
	public ArrayList<TracePlantView.PlantData> getPlantDatas(){
		return plantDatas;
	}
	
	public ArrayList<PlotData> getPlotDatas(){
		return plotDatas;
	}
	
	private int convertToColor(float value,float vcv)
	{
		int color=Color.RED;
		if(value>0.95*vcv){
			color=Color.GREEN;
		}else if(value>0.75*vcv){
			color=Color.BLUE;
		}else if(value>0.6*vcv){
			color=Color.YELLOW;
		}
		//Log.d("reslut", "co:"+color);
		return color;
	}
	
	public static final float VCV_DEFAULT_VALUE=10;

	public void refresh()
	{	
		refresh(VCV_DEFAULT_VALUE);
	}
	
	public void refresh(float vcv)
	{	
		TestData temp=new TestData();
		temp.position=data.getPosition();
		temp.direction=data.getDirection();
		temp.color=convertToColor(data.getQuake(),vcv);
		plantDatas.add(temp.getPlantData());
		
		temp.runtime_long=ctrl.runTime();
		temp.runtime=temp.runtime_long/2.0;
		temp.temp=data.getTemp();
		temp.quake=data.getQuake();
		temp.speed=data.getSpeed();
		plotDatas.add(temp);
		
		currentData=temp;
	}
	
	public void clear()
	{
		plantDatas.clear();
		plotDatas.clear();
	}
	
	public void clearPlotData()
	{
		plotDatas.clear();
	}
	
	public TestData getCurrentData()
	{
		return currentData;
	}
}
