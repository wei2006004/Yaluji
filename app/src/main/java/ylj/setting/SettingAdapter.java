package ylj.setting;

import java.util.ArrayList;

import com.ylj.R;
import ylj.common.bean.SettingItem;
import ylj.common.tool.Glogal;
import ylj.common.ui.BoolSetting;
import ylj.common.ui.FloatSetting;
import ylj.common.ui.IntSetting;
import ylj.common.ui.ListSetting;
import ylj.common.ui.TextSetting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SettingAdapter extends BaseAdapter
{
	private Context mContext;
	private ArrayList<SettingItem> list;
	
	public SettingAdapter(ArrayList<SettingItem> list,Context context)
	{
		mContext=context;
		this.list=list;
	}
	
	@Override
	public boolean isEnabled(int position)
	{
		return !list.get(position).isGroup();
	}
	
	@Override
	public int getCount()
	{
		return list.size();
	}

	@Override
	public Object getItem(int position)
	{
		return list.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View view=convertView;
		SettingItem item=list.get(position);
		if(item.isGroup()){
			view=LayoutInflater.from(mContext).inflate(R.layout.layout_st_group, null);
			TextView textView=(TextView)view.findViewById(R.id.textview_group);
			textView.setText(item.getGroup());
		}else{
			view=createItemView(position);
		}
		return view;
	}
	
	private View createItemView(int position)
	{
		View view;
		SettingItem item=list.get(position);
		switch (item.getPrefenceStyle()) {
		case SettingItem.PREF_STYLE_INT:
			IntSetting intSetting=new IntSetting(item, mContext);
			view=intSetting.getView();
			break;
		case SettingItem.PREF_STYLE_BOOL_DEBUG:
			view=new BoolSetting(item, mContext){
				@Override
				protected void onCheckedChanged(boolean isChecked)
				{
					Glogal.setDebug(isChecked);
				}
			}.getView();
			break;
		case SettingItem.PREF_STYLE_BOOL_BT:
			view =new BoolSetting(item, mContext){
				@Override
				protected void onCheckedChanged(boolean isChecked){
					Glogal.setBtConnect(isChecked);
				}
			}.getView();
			break;
		case SettingItem.PREF_STYLE_ENUM:
			view=new ListSetting(item, mContext).getView();
			break;
		case SettingItem.PREF_STYLE_TEXT:
			view=new TextSetting(item, mContext).getView();
			break;
		case SettingItem.PREF_STYLE_FLOAT:		
			view=new FloatSetting(item, mContext).getView();
			break;
		default:
			view=null;
			break;
		}
		return view;
	}

}
