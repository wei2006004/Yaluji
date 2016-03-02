package ylj.connect;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import ylj.connect.ConnectStateHandler.OnStateChangedListener;
import ylj.MainActivity;
import com.ylj.R;
import ylj.common.TagAdapter;
import ylj.common.bean.TagItem;
import ylj.common.bean.DeviceInfo;

public class BtFragment extends Fragment implements OnStateChangedListener
{
	public static final boolean D=true;
	public static final String TAG="BtFragment";
	
    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 3;
    private static final int REQUEST_ENABLE_BT = 2;
	
	private BluetoothAdapter bluetoothAdapter;
	private static BtCom btCom;
	
	//private TextView scanTextView;
	private TextView noConnectTextView;
	private TextView deviceInfoTextView;
	private TextView deviceStateTextView;
	
	private ListView deviceInfoListView;
	private ListView deviceStateListView;
	
	private TagAdapter infoAdapter;
	private TagAdapter stateAdapter;
	
	private ArrayList<TagItem> infoItems=new ArrayList<TagItem>();
	private ArrayList<TagItem> stateItems=new ArrayList<TagItem>();
	
	private LinearLayout infoLayout;
	private LinearLayout noconnectLayout;
	private Button enableButton;
	private Button scanButton;
	private Button disconnectButton;
//	private View lineView;
	
	private static DeviceInfo deviceInfo=new DeviceInfo();
	
	private static  boolean isConnect=false;
	private static  boolean isConnected=false;
	
	private final Handler infoHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        	switch (msg.what) {
            case ComConst.COM_DEVICE_INFO:
            	if(D) Log.i(TAG, "COM_DEVICE_INFO: " + msg.arg1);
            	DeviceInfo info=(DeviceInfo)msg.obj;
            	setDeviceInfo(info);
            	isConnected=true;
				showText(R.string.com_connected);
            	//connectFragment.connected(info);
            	break;
            case ComConst.COM_READ_ERROR:
            	break;
            default:
            	break;           	
        	}
        }
	};
	
	@Override
	public void onStateChanged(int state) {
		if (D)
			Log.i(TAG, "MESSAGE_STATE_CHANGE: " + state);
		switch (state) {
		case ComConst.STATE_CONNECTED:
			btCom.sendDeviceMessage();
			connected();
			isConnected=false;
			isConnect = true;
			break;
		case ComConst.STATE_CONNECTING:
			showText(R.string.com_connecting);
			break;
		case ComConst.STATE_CONNECT_FAIL:
			showText(R.string.com_fail);
			if(BtFragment.this.isAdded())
				enableButton.setText(getString(R.string.bt_open));
			isConnect = false;
			isConnected=false;
			break;
		case ComConst.STATE_CONNECT_LOST:
			showText(R.string.com_lost);
			if(BtFragment.this.isAdded())
				enableButton.setText(getString(R.string.bt_open));				
			isConnect = false;
			isConnected=false;
			deviceInfo=new DeviceInfo();
			break;
		}
	}
//	
//    private final Handler ctrlHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//			// if (msg.what==BluetoothChatService.MESSAGE_STATE_CHANGE) {
//			if (D)
//				Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.what);
//			switch (msg.what) {
//			case ComConst.STATE_CONNECTED:
//				btCom.sendDeviceMessage();
//				connected();
//				isConnected=false;
//				isConnect = true;
//				break;
//			case ComConst.STATE_CONNECTING:
//				showText(R.string.com_connecting);
//				break;
//			case ComConst.STATE_CONNECT_FAIL:
//				showText(R.string.com_fail);
//				if(BtFragment.this.isAdded())
//					enableButton.setText(getString(R.string.bt_open));
//				isConnect = false;
//				isConnected=false;
//				break;
//			case ComConst.STATE_CONNECT_LOST:
//				showText(R.string.com_lost);
//				if(BtFragment.this.isAdded())
//					enableButton.setText(getString(R.string.bt_open));				
//				isConnect = false;
//				isConnected=false;
//				if(onConnectLostListener!=null)
//					onConnectLostListener.onConnectLost();
//				break;
//			}
//           // }
//        }
//    };
    
    public static boolean isConnected()
    {
    	return isConnected;
    }
    
    public static boolean isConnect()
    {
    	return isConnect;
    }
    
    public void disconnect()
    {   	
    	btCom.stop();
    }
    
    private void showText(int resId)
	{
		if(!isAdded())
			return;
    	showText(getString(resId));
	}
	
	private void showText(String text)
	{
		if(getActivity()!=null)
			Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
	}
	
	private void enableBluetooth(boolean isChecked)
	{
		if(isChecked){
			if(!bluetoothAdapter.isEnabled()){
				Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
	            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
			}
		}else{
			new Handler().postDelayed(new Runnable() {				
				@Override
				public void run()
				{
					bluetoothAdapter.disable();
					showText(R.string.bt_disabled);
				}
			}, 300);		
			enableButton.setText(getString(R.string.bt_open));
			unConnected();
		}
	}
	
	public static Com getCom()
	{
		return btCom;
	}
	
	@Override
	public void onStart ()
	{
		super.onStart();
		Log.d(TAG, "start");
		if(isConnect){
			connected();
			setDeviceInfo(deviceInfo);
		}else{
			unConnected();
		}
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		View view=inflater.inflate(R.layout.fragment_connect, null);
		
//		scanTextView =(TextView)view.findViewById(R.id.textview_scan);
//		scanTextView.setText(R.string.scan_devices);
		
		noConnectTextView=(TextView)view.findViewById(R.id.textview_no_connect);
		noConnectTextView.setText(R.string.device_no_connect);
		
		bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
		if (bluetoothAdapter == null) {
			showText(R.string.bt_unabled);
			return view;
        }
		
		if(btCom==null)
			btCom=new BtCom(getActivity(), infoHandler, MainActivity.stateHandler);
		
		noconnectLayout=(LinearLayout)view.findViewById(R.id.layout_no_connect);
		enableButton=(Button)view.findViewById(R.id.button_enable);
		enableButton.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				if(bluetoothAdapter.isEnabled()){
					enableButton.setText(getString(R.string.bt_open));
					enableBluetooth(false);					
				}else {
					enableButton.setText(getString(R.string.bt_close));
					enableBluetooth(true);
				}
			}
		});
		scanButton=(Button)view.findViewById(R.id.button_scan);
		scanButton.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				if(bluetoothAdapter.isEnabled()){
					Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
					startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
				}else{
					showText(R.string.bt_please_enable);
				}
			}
		});
		if(bluetoothAdapter.isEnabled()){
			enableButton.setText(getString(R.string.bt_close));
		}else{
			enableButton.setText(getString(R.string.bt_open));
		}
		
		disconnectButton=(Button)view.findViewById(R.id.button_disconnect);
		disconnectButton.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				btCom.stop();
				unConnected();
			}
		});
		
		infoLayout=(LinearLayout)view.findViewById(R.id.ll_device_info);
//		lineView=view.findViewById(R.id.line_view);

		deviceInfoTextView=(TextView)view.findViewById(R.id.textview_info);
		deviceInfoTextView.setText(R.string.device_info_title);
		deviceStateTextView=(TextView)view.findViewById(R.id.textview_state);
		deviceStateTextView.setText(R.string.device_state_title);
		
		deviceInfoListView=(ListView)view.findViewById(R.id.listview_info);
		deviceStateListView=(ListView)view.findViewById(R.id.listview_state);
		deviceInfoListView.setDivider(null);
		deviceStateListView.setDivider(null);
		
		initTags();
		if (infoAdapter == null) {
			infoAdapter = new TagAdapter(infoItems, getActivity()) {
				@Override
				public boolean isEnabled (int position)
				{
					return false;
				}
			};
		}
		if(stateAdapter==null){
			stateAdapter=new TagAdapter(stateItems, getActivity()){
				@Override
				public boolean isEnabled (int position)
				{
					return false;
				}
			};
		}
		deviceInfoListView.setAdapter(infoAdapter);
		deviceStateListView.setAdapter(stateAdapter);
		
		unConnected();
		return view;
	}
	
	private void initTags()
	{
		infoItems.clear();
		stateItems.clear();
		
		if(!isAdded())
			return;
		String[] infoTags=getResources().getStringArray(R.array.array_device_info);		
		String[] stateTags=getResources().getStringArray(R.array.array_device_state);
		String[] infoValues=new String[]{
			deviceInfo.getDeviceId(),
			deviceInfo.getVersion(),
			deviceInfo.getSoftVersion()
		};
		boolean[] states={
			deviceInfo.isTempState(),
			deviceInfo.isQuakeState(),
			deviceInfo.isDirState(),
			deviceInfo.isHuoerState()
		};
		
		TagItem item;			
		for(int i=0;i<infoTags.length;i++){
			item=new TagItem(infoTags[i],infoValues[i]);
			infoItems.add(item);
		}	
		
		String value;
		String okText=getString(R.string.device_state_ok);
		String errorText=getString(R.string.device_state_error);
		for(int i=0;i<stateTags.length;i++){
			value=states[i]?okText:errorText;
			item=new TagItem(stateTags[i],value);
			stateItems.add(item);
		}
	}	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		if(D) Log.d(TAG, "onActivityResult " + requestCode);
        switch (requestCode) {
        case REQUEST_CONNECT_DEVICE_SECURE://+65536:
            if (resultCode == Activity.RESULT_OK) {
            	if(D) Log.d(TAG, "onActivityResult:connect");           	
                connectDevice(data);
            }
            break;
        case REQUEST_ENABLE_BT:
           onEnabledBtResult(resultCode);
        }
	}
	
	private void connectDevice(Intent data)
	{
		String address = data.getExtras()
            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        if(D)Log.d(TAG, "connect:"+address);
        BluetoothDevice device= bluetoothAdapter.getRemoteDevice(address);
        btCom.setBluetoothDevice(device);
        btCom.connect();
	}
	
	private void onEnabledBtResult(int resultCode)
	{
		if (resultCode == Activity.RESULT_OK) {
			enableButton.setText(getString(R.string.bt_close));
            showText(R.string.bt_run);          
        } else {
        	enableButton.setText(getString(R.string.bt_open));
            showText(R.string.bt_fail);
        }
	}
	
	private void connected()
	{			
		noconnectLayout.setVisibility(View.GONE);
		infoLayout.setVisibility(View.VISIBLE);
	}
	
	private void setDeviceInfo(DeviceInfo info)
	{
		if(D)Log.d(TAG, "connected+info");
		deviceInfo=info;
		initTags();
		infoAdapter.notifyDataSetChanged();
		stateAdapter.notifyDataSetChanged();
		
		connected();
	}
	
	private  void unConnected()
	{
		if(bluetoothAdapter.isEnabled()){
			enableButton.setText(getString(R.string.bt_close));
		}else{
			enableButton.setText(getString(R.string.bt_open));
		}
		infoLayout.setVisibility(View.GONE);
		noconnectLayout.setVisibility(View.VISIBLE);
	}

}
