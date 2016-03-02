package ylj.common.ui;

import ylj.common.bean.SettingItem;
import ylj.common.tool.Tools;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;

public class ListSetting extends SummaryItem
{
	private Builder mBuilder;	
	private AlertDialog mDialog;
	private SettingItem item;
	
	public ListSetting(SettingItem item,Context context)
	{
		super(context, item.getTitle(), item.getDefaultSummary());
		this.item =item;
		SharedPreferences preferences=Tools.getAppPreferences(context);
		int index=preferences.getInt(item.getPrefenceKey(), item.getEnumDefalut());
		getSummaryView().setText(item.getEnumSummary(index));
		
		mBuilder=new Builder(context);
		mBuilder.setTitle(item.getTitle());
		mBuilder.setSingleChoiceItems(item.getList(),index/* item.getEnumDefalut()*/, new DialogInterface.OnClickListener() {				
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				onListItemClick(which);
				dialog.dismiss();
			}
		});
		mDialog=mBuilder.create();
		
		getView().setOnClickListener(new OnClickListener() {				
			@Override
			public void onClick(View v)
			{
				mDialog.show();
			}
		});
	}
	
	protected void onListItemClick(int index)
	{
		getSummaryView().setText(item.getEnumSummary(index));
		
		SharedPreferences preferences=Tools.getAppPreferences(getContext());
		preferences.edit().putInt(item.getPrefenceKey(), index).commit();
	}	

}
