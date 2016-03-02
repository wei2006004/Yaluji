package ylj.hisdata;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ylj.R;
import ylj.common.bean.HisdataItem;

public class HisdataAdapter extends BaseAdapter
{
	ArrayList<HisdataItem> items;
	Context context;
	
	public HisdataAdapter(ArrayList<HisdataItem> items,Context context)
	{
		this.context=context;
		this.items=items;
	}

	@Override
	public int getCount()
	{
		return items.size();
	}

	@Override
	public Object getItem(int position)
	{
		return items.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}
	
	private class ViewHolder
	{
		TextView deviceView;
		TextView timeView;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View view=convertView;
		ViewHolder holder;
		if(view==null){
			holder=new ViewHolder();
			view=LayoutInflater.from(context).inflate(R.layout.layout_tag_item, null);
			holder.deviceView=(TextView)view.findViewById(R.id.textview_tag);
			holder.timeView=(TextView)view.findViewById(R.id.textview_value);
			view.setTag(holder);
		}
		holder=(ViewHolder)view.getTag();
		holder.deviceView.setText(items.get(position).getDeviceId());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String timeString=sdf.format(items.get(position).getTestTime());
		holder.timeView.setText(timeString);
		
		return view;
	}

}
