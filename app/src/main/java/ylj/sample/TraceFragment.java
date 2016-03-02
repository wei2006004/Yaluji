package ylj.sample;


import java.util.ArrayList;

import com.ylj.R;
import ylj.common.widget.TracePlantView;
import ylj.common.widget.TracePlantView.PlantData;

import android.app.Fragment;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TraceFragment extends Fragment
{
	
	private TracePlantView mPlantView;
	private ArrayList<TracePlantView.PlantData> mDatas = new ArrayList<TracePlantView.PlantData>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View view=inflater.inflate(R.layout.fragment_trace, null);
		mPlantView=(TracePlantView)view.findViewById(R.id.traceview);
		return view;
	}

	@Override
	public void onStart()
	{
		super.onStart();
		TracePlantView.DrawEdit edit=mPlantView.getEdit();
		edit.clear();
		for (PlantData data : mDatas) {
			edit.addPlant(data);
		}
		edit.commit();
	}
	
	public void clear()
	{
		if(isAdded()){
			mPlantView.getEdit().clear().commit();
		}	
		mDatas.clear();
	}

	public void refresh(PointF position,float dir)
	{
		if(isAdded()){			
			mPlantView.getEdit().addPlant(position, dir)
					.commit();
		}
		mDatas.add(mPlantView.createPlantData(position, dir));		
	}
}
