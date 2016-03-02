package ylj.common;

import java.util.ArrayList;

import com.ylj.R;
import ylj.common.bean.TagItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TagAdapter extends BaseAdapter
{
	private ArrayList<TagItem> itemList;
	private Context mContext;
	
	public TagAdapter(ArrayList<TagItem> items,Context context)
	{
		itemList=items;
		mContext=context;
	}
	
	private int viewRid;
	
	public void setViewLayout(int resId)
	{
		viewRid=resId;
	}
	
	@Override
	public int getCount()
	{
		return itemList.size();
	}

	@Override
	public Object getItem(int position)
	{
		return itemList.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}
	
	private class ViewHolder
	{
		TextView tagView;
		TextView valueView;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		
		View view=convertView;
		ViewHolder holder;
		
		int layoutId=R.layout.layout_tag_item;
		if(viewRid!=0){
			layoutId=viewRid;
		}
		
		if(view==null){
			holder=new ViewHolder();
			view=LayoutInflater.from(mContext).inflate(layoutId, null);
			holder.tagView=(TextView)view.findViewById(R.id.textview_tag);
			holder.valueView=(TextView)view.findViewById(R.id.textview_value);
			view.setTag(holder);
		}
		holder=(ViewHolder)view.getTag();
		holder.tagView.setText(itemList.get(position).getTag());
		holder.valueView.setText(itemList.get(position).getValue());
		
		return view;
	}

}
