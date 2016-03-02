package ylj.common.tool;

import java.util.ArrayList;

import android.content.Context;

import com.ylj.R;
import ylj.common.bean.SettingItem;
import ylj.common.bean.SettingItem.ToSummaryChanger;

class LengthSummaryCharger implements ToSummaryChanger
{
	@Override
	public String change(String value)
	{
		return value+"米";
	}	
}

class NumSummaryCharger implements ToSummaryChanger
{
	@Override
	public String change(String value)
	{
		return value+"个";
	}	
}

public class Constants
{
	public static ArrayList<SettingItem> getSettingItems(Context context)
	{
		ArrayList<SettingItem> list=new ArrayList<SettingItem>();
		SettingItem item;
//		SettingItem item=SettingItem.createSimpleItem(
//				context.getString(R.string.rt_title_bt), 
//				null,
//				SettingItem.PREF_STYLE_BOOL_BT);
//		item.setBoolDefault(Glogal.isDebug());
//		list.add(item);
//		
//		item=SettingItem.createSimpleItem(
//				context.getString(R.string.rt_title_debug), 
//				null,
//				SettingItem.PREF_STYLE_BOOL_DEBUG);
//		item.setBoolDefault(Glogal.isBtConnect());
//		list.add(item);
		
		item=SettingItem.createSimpleItem(
				context.getString(R.string.rt_title_origin), 
				context.getString(R.string.pref_origin),
				SettingItem.PREF_STYLE_ENUM,
				null);
		item.setEnumDefalut(0);
		item.setList(context.getResources().getStringArray(R.array.rt_heading_text));
		list.add(item);
		
		item=SettingItem.createGroupTitle(context.getString(R.string.rt_group_road));
		list.add(item);
		item=SettingItem.createGroupItem(
				context.getString(R.string.rt_group_road), 
				context.getString(R.string.rt_title_road_length), 
				context.getString(R.string.pref_road_length), 
				SettingItem.PREF_STYLE_FLOAT, 
				new LengthSummaryCharger());
		item.setFloatDefault(50);
		item.setFloatMax(500);
		item.setFloatMin(0);
		list.add(item);
		item=SettingItem.createGroupItem(
				context.getString(R.string.rt_group_road), 
				context.getString(R.string.rt_title_road_width), 
				context.getString(R.string.pref_road_width), 
				SettingItem.PREF_STYLE_FLOAT, 
				new LengthSummaryCharger());
		item.setFloatDefault(30);
		item.setFloatMax(300);
		item.setFloatMin(0);
		list.add(item);
		
		item=SettingItem.createGroupTitle(context.getString(R.string.rt_group_roll));
		list.add(item);
		item=SettingItem.createGroupItem(
				context.getString(R.string.rt_group_roll), 
				context.getString(R.string.rt_title_roll_diameter), 
				context.getString(R.string.pref_roll_diameter), 
				SettingItem.PREF_STYLE_FLOAT, 
				new LengthSummaryCharger());
		item.setFloatDefault(2);
		item.setFloatMax(3);
		item.setFloatMin(0);
		list.add(item);
		item=SettingItem.createGroupItem(
				context.getString(R.string.rt_group_roll), 
				context.getString(R.string.rt_title_roll_width), 
				context.getString(R.string.pref_roll_width), 
				SettingItem.PREF_STYLE_FLOAT, 
				new LengthSummaryCharger());
		item.setFloatDefault((float)2.4);
		item.setFloatMax(5);
		item.setFloatMin(0);
		list.add(item);
		
		item=SettingItem.createSimpleItem(
				context.getString(R.string.rt_title_huoer_num), 
				context.getString(R.string.pref_huoer_num),
				SettingItem.PREF_STYLE_INT,
				new NumSummaryCharger());
		item.setIntDefault(4);
		item.setIntMax(10);
		item.setIntMin(0);
		list.add(item);
		
		item=SettingItem.createGroupTitle(context.getString(R.string.rt_group_ftp));
		list.add(item);
		item=SettingItem.createGroupItem(
				context.getString(R.string.rt_group_ftp), 
				context.getString(R.string.rt_title_ip), 
				context.getString(R.string.pref_ip), 
				SettingItem.PREF_STYLE_TEXT, 
				null);
		item.setTextDefault(context.getString(R.string.rt_def_ip));
		list.add(item);
		item=SettingItem.createGroupItem(
				context.getString(R.string.rt_group_ftp), 
				context.getString(R.string.rt_title_port), 
				context.getString(R.string.pref_port), 
				SettingItem.PREF_STYLE_TEXT, 
				null);
		item.setTextDefault(context.getString(R.string.rt_def_port));
		list.add(item);
		item=SettingItem.createGroupItem(
				context.getString(R.string.rt_group_ftp), 
				context.getString(R.string.rt_title_user), 
				context.getString(R.string.pref_user), 
				SettingItem.PREF_STYLE_TEXT, 
				null);
		item.setTextDefault(context.getString(R.string.rt_def_password));
		list.add(item);
		item=SettingItem.createGroupItem(
				context.getString(R.string.rt_group_ftp), 
				context.getString(R.string.rt_title_password), 
				context.getString(R.string.pref_password), 
				SettingItem.PREF_STYLE_TEXT, 
				null);
		item.setTextDefault(context.getString(R.string.rt_def_password));
		list.add(item);
		
		return list;
	}
}
