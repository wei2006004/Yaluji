package ylj.setting;

import com.ylj.R;
import ylj.setting.SettingAdapter;
import ylj.common.tool.Constants;
import ylj.common.tool.Glogal;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.Switch;

public class SettingFragment extends Fragment
{
	private ListView listView;
	private SettingAdapter mAdapter;
	private Context mContext;
	
	private Switch debugSwitch;
	private Switch btSwitch;
	
	public SettingFragment(Context context)
	{
		mContext=context;
	}
	
	public interface SwitchChangeListener
	{
		void onDebugChanged(boolean ischecked);
		void onBtConnectChanged(boolean ischecked);
	}
	
	private SwitchChangeListener listener;
	
	public void setSwitchChangeListener(SwitchChangeListener listener)
	{
		this.listener=listener;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_setting, null);
		debugSwitch=(Switch)view.findViewById(R.id.switch_debug);
		debugSwitch.setChecked(Glogal.isDebug());
		debugSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				Glogal.setDebug(arg1);
				if(listener!=null)
					listener.onDebugChanged(arg1);
			}
		});
		
		btSwitch=(Switch)view.findViewById(R.id.switch_bt);
		btSwitch.setChecked(Glogal.isBtConnect());
		btSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				Glogal.setBtConnect(arg1);
				if(listener!=null)
					listener.onBtConnectChanged(arg1);
			}
		});
		
		listView=(ListView)view.findViewById(R.id.listview);
		if(mAdapter==null){
			mAdapter=new SettingAdapter(Constants.getSettingItems(mContext),mContext);
		}
		listView.setDivider(null);
		listView.setAdapter(mAdapter);
		return view;
	}
}
