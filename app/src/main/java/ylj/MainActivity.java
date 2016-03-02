package ylj;

import java.util.ArrayList;

import ylj.adjust.AdjustLightActivity;
import ylj.connect.ConnectStateHandler;
import ylj.sample.DrawData;
import com.ylj.R;
import ylj.sample.TestActivity;
import ylj.service.control.TestCtrl;
import ylj.connect.Com;
import ylj.connect.BtFragment;
import ylj.hisdata.HisDataFragment;
import ylj.setting.SettingFragment;
import ylj.connect.TcpFragment;
import ylj.common.tool.Glogal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements SettingFragment.SwitchChangeListener
{
	
	public static final String TAG="MainActivity";
	public static final boolean D=true;
	
	public static ConnectStateHandler stateHandler=new ConnectStateHandler();
	
	private int mPosition=POSITION_SETTING;
	//private ListView listView;
	private LinearLayout listLayout;
	private ArrayList<View> listViews=new ArrayList<View>();
	//private MainAdapter mAdapter;

	private SettingFragment runtimeFragment;
	private BtFragment btFragment;
	private TcpFragment tcpFragment;
	private HisDataFragment hisDataFragment;

	public static final int POSITION_BT=0;
	public static final int POSITION_TCP=1;
	public static final int POSITION_SETTING=2;
	public static final int POSITION_HISDATA=3;
	public static final int POSITION_ADJUST=4;
	public static final int POSITION_TEST=5;
	
	private static final int ITEM_SELECTED=0;
	private static final int ITEM_NOT_SELECTED=1;
	private static final int ITEM_UNABLED=2;
	private static final int ITEM_ABLED=3;
	
	private int[] mTitles=new int[]{
			R.string.list_connect,
			R.string.list_tcp,
			R.string.list_runtime,
			R.string.list_hisdata,
			R.string.list_adjust,
			R.string.list_test
	};
	
	private int[] mImages=new int[]{
			R.drawable.bluetooth,
			R.drawable.connect,
			R.drawable.settings,
			R.drawable.storage,
			R.drawable.adjust,
			R.drawable.test,
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		listLayout=(LinearLayout)findViewById(R.id.listLayout);
		initList();
		if(Glogal.isBtConnect()){
			setItemState(POSITION_TCP, ITEM_UNABLED);
		}else{
			setItemState(POSITION_BT, ITEM_UNABLED);
		}
		onListItemSelect(mPosition);
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
	}
	
	private void initList()
	{
		View view;
		for(int i=0;i<mTitles.length;i++){
			view = LayoutInflater.from(MainActivity.this).inflate(
					R.layout.layout_title, null);
			ImageView imageView = (ImageView) view.findViewById(R.id.image);
			imageView.setBackgroundResource(mImages[i]);
			
			TextView textView=(TextView)view.findViewById(R.id.textview);
			textView.setText(mTitles[i]);
			
			view.setTag(Integer.valueOf(i));
			view.setBackgroundResource(R.drawable.radio_buttong_bg);
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v)
				{
					int position=(Integer)v.getTag();
					onListItemSelect(position);
				}
			});
			listViews.add(view);
			listLayout.addView(view);
		}
	}
	
	private void setItemState(int pos,int state)
	{
		View view=listViews.get(pos);
		TextView textView=(TextView)view.findViewById(R.id.textview);
		switch (state) {
		case ITEM_SELECTED:
			view.setSelected(true);
			textView.setTextColor(getResources().getColor(R.color.theme_white));
			break;
		case ITEM_NOT_SELECTED:
			view.setSelected(false);
			textView.setTextColor(getResources().getColor(R.color.theme_black));
			break;
		case ITEM_ABLED:
			textView.setTextColor(getResources().getColor(R.color.theme_black));
			break;
		case ITEM_UNABLED:
			textView.setTextColor(getResources().getColor(R.color.theme_grep));
			break;
		default:
			break;
		}
	}

	private void onListItemSelect(int position)
	{
		switch (position) {
		case POSITION_BT:
			if(!Glogal.isBtConnect()){
				showText(R.string.main_info_bt);
				return;
			}			
			if(btFragment==null){
				btFragment=new BtFragment();				
			}
			stateHandler.setOnStateChangedListener(btFragment);
			getFragmentManager().beginTransaction()
					.replace(R.id.fragment_detail, btFragment).commit();
			break;
		case POSITION_TCP:
			if(Glogal.isBtConnect()){
				showText(R.string.main_info_tcp);
				return;
			}
			if(tcpFragment==null){
				tcpFragment=new TcpFragment();				
			}
			stateHandler.setOnStateChangedListener(tcpFragment);
			getFragmentManager().beginTransaction().replace(R.id.fragment_detail, tcpFragment).commit();
			break;
		case POSITION_SETTING:
			if (runtimeFragment == null){
				runtimeFragment = new SettingFragment(this);
				runtimeFragment.setSwitchChangeListener(this);
			}
			getFragmentManager().beginTransaction()
					.replace(R.id.fragment_detail, runtimeFragment).commit();
			break;
		case POSITION_HISDATA:
			if (hisDataFragment == null)
				hisDataFragment = new HisDataFragment();
			getFragmentManager().beginTransaction()
					.replace(R.id.fragment_detail, hisDataFragment).commit();
			break;
		case POSITION_ADJUST:	
			if(Glogal.isDebug()){
				TestCtrl.instance().setDebug(true);
			}else{
				Com com=null;
				TestCtrl.instance().setDebug(false);
				if(Glogal.isBtConnect()){
					if(BtFragment.isConnected())
						com=BtFragment.getCom();
				}else{
					if(TcpFragment.isConnected())
						com=TcpFragment.getCom();
				}
				if(com==null ){
					showText(R.string.main_info_device);
					return;
				}
				TestCtrl.instance().setCom(com);	
			}
			TestCtrl.instance().start();
			Intent intent_a=new Intent(MainActivity.this,AdjustLightActivity.class);
			startActivity(intent_a);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			return;
		case POSITION_TEST:			
			if(Glogal.isDebug()){
				TestCtrl.instance().setDebug(true);
			}else{
				Com com=null;
				TestCtrl.instance().setDebug(false);
				if(Glogal.isBtConnect()){
					if(BtFragment.isConnected())
						com=BtFragment.getCom();
				}else{
					if(TcpFragment.isConnected())
						com=TcpFragment.getCom();
				}
				if(com==null ){
					showText(R.string.main_info_device);
					return;
				}
				TestCtrl.instance().setCom(com);	
			}
			TestCtrl.instance().start();
			DrawData.instance().clear();
			Intent intent=new Intent(MainActivity.this,TestActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			return;
		default:
			break;
		}
		setItemState(mPosition, ITEM_NOT_SELECTED);
		mPosition=position;
		setItemState(position, ITEM_SELECTED);
		invalidateOptionsMenu();
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		if(mPosition==POSITION_HISDATA)
			getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();
		if (id == R.id.action_refresh) {
			if(mPosition==POSITION_HISDATA)
				hisDataFragment.refresh();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void showText(int resId)
	{
		showText(getString(resId));
	}
	
	private void showText(String text)
	{
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onDebugChanged(boolean ischecked) {
		
	}

	@Override
	public void onBtConnectChanged(boolean ischecked) {
		if(ischecked){
			if(tcpFragment!=null && tcpFragment.isConnect()){
				tcpFragment.disconnect();
			}
			setItemState(POSITION_BT, ITEM_ABLED);
			setItemState(POSITION_TCP, ITEM_UNABLED);
		}else{
			if(btFragment!=null && btFragment.isConnect()){
				btFragment.disconnect();
			}
			setItemState(POSITION_BT, ITEM_UNABLED);
			setItemState(POSITION_TCP, ITEM_ABLED);
		}
	}
	
	private class MainAdapter extends BaseAdapter
	{

		@Override
		public int getCount()
		{
			return mTitles.length;
		}

		@Override
		public Object getItem(int position)
		{
			return mTitles[position];
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			View view=listViews.get(position);
			return view;
		}
		
	}


}
