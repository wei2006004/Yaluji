package ylj.common.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;

import com.ylj.R;
import ylj.common.bean.SettingItem;
import ylj.common.tool.Tools;

public class BoolSetting
{
	private View view;
	private TextView titleView;
	private Switch switch1;
	private Context context;
	SettingItem item;
	
	public BoolSetting(SettingItem item,Context context)
	{
		this.item=item;
		this.context=context;
		SharedPreferences preferences=Tools.getAppPreferences(context);
		boolean value=preferences.getBoolean(item.getPrefenceKey(), item.isBoolDefault());
		
		view=LayoutInflater.from(context).inflate(R.layout.layout_setting_bool, null);
		
		titleView=(TextView)view.findViewById(R.id.textview);
		switch1=(Switch)view.findViewById(R.id.switch_button);
		titleView.setText(item.getTitle());
		switch1.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				BoolSetting.this.onCheckedChanged(isChecked);
			}
		});
		switch1.setChecked(value);
	}
	
	protected void onCheckedChanged(boolean isChecked)
	{
		SharedPreferences preferences=Tools.getAppPreferences(BoolSetting.this.context);
		preferences.edit().putBoolean(BoolSetting.this.item.getPrefenceKey(), isChecked).commit();
	}

	public View getView()
	{			
		return view;
	}
}
