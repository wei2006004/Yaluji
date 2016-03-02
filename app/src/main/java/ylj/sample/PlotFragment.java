package ylj.sample;

import java.util.ArrayList;

import com.ylj.R;
import ylj.common.widget.PlotView;

import android.app.Fragment;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PlotFragment extends Fragment
{
	public final static String TAG ="PlotFragment";
	public final static boolean D=false;
	
	private int refreshTimes = 0;
	private static final int REFRESH_STEP = 50;
	
	public final static int MAX_QUAKE=35;
	public final static int MIN_QUAKE=-35;
	public final static int MAX_TEMP=250;
	public final static int MIN_TEMP=0;
	
	private PlotView quakeView;
	private PlotView tempView;

	private ArrayList<PointF> quakeList = new ArrayList<PointF>();
	private ArrayList<PointF> tempList = new ArrayList<PointF>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View view=inflater.inflate(R.layout.fragment_plotview, null);
		
		quakeView=(PlotView)view.findViewById(R.id.quakeview);
		tempView=(PlotView)view.findViewById(R.id.tempview);
		
		initPlotAxes(quakeView, MIN_QUAKE, MAX_QUAKE);
		initPlotAxes(tempView,MIN_TEMP, MAX_TEMP);
		
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
		
		if(D)Log.d(TAG, "start:"+quakeList.size());
	
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run()
			{
				draw(quakeView, quakeList);
				draw(tempView, tempList);
			}
		}, 1000);	
	}
	
	private void draw(PlotView plotView,ArrayList<PointF> valueList)
	{
		PlotView.DrawEdit edit = plotView.getEdit();
		edit.clear();
		for (PointF point : valueList) {
			edit.addPoint(point);
			if(D)Log.d(TAG, "point:"+point);
		}
		edit.commit();
	}

	public void clear()
	{
		refreshTimes=0;
		
		if(isAdded()){
			clearPlot(quakeView);
			clearPlot(tempView);
		}

		quakeList.clear();
		tempList.clear();
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

	public void refresh(long runTime,float temp,float quake)
	{
		
		if (runTime % (REFRESH_STEP*2) == 0) {			
			if (runTime != 0) {
				if(D)Log.d(TAG, "refresh:"+runTime);
				refreshTimes++;
				clearPlot(quakeView);
				clearPlot(tempView);
			}	
			tempList.clear();
			quakeList.clear();
		}
		
		double run=runTime/2.0;
		PointF quakePointF=new PointF((float)run, quake);
		PointF tempPointF=new PointF((float)run, temp);
		if(isAdded()){
			quakeView.getEdit().addPoint(quakePointF).commit();
			tempView.getEdit().addPoint(tempPointF).commit();
		}
		tempList.add(quakePointF);
		quakeList.add(tempPointF);
	}

}
