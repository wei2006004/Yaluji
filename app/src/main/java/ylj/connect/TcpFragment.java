package ylj.connect;

import java.util.ArrayList;

import ylj.connect.ConnectStateHandler.OnStateChangedListener;

import ylj.MainActivity;
import com.ylj.R;
import ylj.common.TagAdapter;
import ylj.common.bean.TagItem;
import ylj.common.bean.DeviceInfo;

import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

public class TcpFragment extends Fragment implements OnStateChangedListener
{
	public static final boolean D=true;
	public static final String TAG="TcpFragment";

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
	private Button connectButton;
	private Button disconnectButton;
	//private View lineView;
	
	private static DeviceInfo deviceInfo=new DeviceInfo();
	
	private static TcpCom tcpCom;
	
	private boolean isListen=false;
	private static boolean isConnect=false;
	private static boolean isConnected=false;
	
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
			tcpCom.sendDeviceMessage();
			connected();
			isConnected=false;
			isConnect = true;				
			break;
		case ComConst.STATE_CONNECTING:
			showText(R.string.com_connecting);
			break;
		case ComConst.STATE_CONNECT_FAIL:
			showText(R.string.com_fail);
			isConnect = false;
			isConnected=false;
			break;
		case ComConst.STATE_CONNECT_LOST:
			showText(R.string.com_lost);
			isConnect = false;
			isConnected=false;
			deviceInfo=new DeviceInfo();
			break;
		}
	}
	
//    private final Handler ctrlHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//			// if (msg.what==BluetoothChatService.MESSAGE_STATE_CHANGE) {
//			if (D)
//				Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.what);
//			switch (msg.what) {
//			case ComConst.STATE_CONNECTED:
//				tcpCom.sendDeviceMessage();
//				connected();
//				isConnected=false;
//				isConnect = true;				
//				break;
//			case ComConst.STATE_CONNECTING:
//				showText(R.string.com_connecting);
//				break;
//			case ComConst.STATE_CONNECT_FAIL:
//				showText(R.string.com_fail);
//				isConnect = false;
//				isConnected=false;
//				break;
//			case ComConst.STATE_CONNECT_LOST:
//				showText(R.string.com_lost);
//				isConnect = false;
//				isConnected=false;
////				if(onConnectLostListener!=null)
////					onConnectLostListener.onConnectLost();
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
    	tcpCom.stop();
    }
    
    private void showText(int resId)
	{
    	if(isAdded())
    		showText(getString(resId));
	}
	
	private void showText(String text)
	{
		if(getActivity()!=null)
			Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
	}
	
	public static Com getCom()
	{
		return tcpCom;
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
		
		View view=inflater.inflate(R.layout.fragment_tcp, null);
		
		noConnectTextView=(TextView)view.findViewById(R.id.textview_no_connect);
		noConnectTextView.setText(R.string.device_no_connect);
		
		noconnectLayout=(LinearLayout)view.findViewById(R.id.layout_no_connect);
		connectButton=(Button)view.findViewById(R.id.button_connect);
		connectButton.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				if(!isWifiConnected(getActivity())){
					Toast.makeText(getActivity(), getString(R.string.com_no_wifi_promt), Toast.LENGTH_LONG).show();
					return;
				}
				if(isListen){
					tcpCom.stop();
					connectButton.setText(getString(R.string.tcp_open));
					isListen=false;
				}else{
					connectButton.setText(getString(R.string.tcp_close));
					tcpCom.connect();
					isListen=true;
				}
			}
		});
		
		disconnectButton=(Button)view.findViewById(R.id.button_disconnect);
		disconnectButton.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View arg0) {
				//tcpCom.sendStartMessage();
				tcpCom.stop();
				unConnected();
			}
		});
		
		if(tcpCom==null)
			tcpCom=new TcpCom(getActivity(), infoHandler, MainActivity.stateHandler);
		
		infoLayout=(LinearLayout)view.findViewById(R.id.ll_device_info);
		//lineView=view.findViewById(R.id.line_view);

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
		
		//unConnected();
		return view;
	}
	
	public boolean isWifiConnected(Context context) {
		if (context != null) {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context
		.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWiFiNetworkInfo = mConnectivityManager
		.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (mWiFiNetworkInfo != null) {
		return mWiFiNetworkInfo.isAvailable();
		}
		}
		return false;
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
	
	public void connected()
	{			
		noconnectLayout.setVisibility(View.GONE);
		infoLayout.setVisibility(View.VISIBLE);
	}
	
	public void setDeviceInfo(DeviceInfo info)
	{
		if(D)Log.d(TAG, "connected+info");
		deviceInfo=info;
		initTags();
		infoAdapter.notifyDataSetChanged();
		stateAdapter.notifyDataSetChanged();
		
		connected();
	}
	
	public void unConnected()
	{
		tcpCom.stop();
		connectButton.setText(getString(R.string.tcp_open));
		infoLayout.setVisibility(View.GONE);
		noconnectLayout.setVisibility(View.VISIBLE);
	}


}
