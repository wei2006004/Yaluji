package ylj.connect;

import java.util.ArrayList;
import java.util.Set;

import ylj.common.bean.DeviceItem;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.ylj.R;

public class DeviceListActivity extends Activity
{
	private static final String TAG = "DeviceListActivity";
	private static final boolean D = true;

	public final static String EXTRA_DEVICE_ADDRESS = "device_address";

	private ListView listView;
	private DeviceListAdapter deviceListAdapter;
	
	private BluetoothAdapter mBtAdapter;
	private ArrayList<DeviceItem> mPairedDevicesArray=new ArrayList<DeviceItem>();
	private ArrayList<DeviceItem> mNewDevicesArray=new ArrayList<DeviceItem>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_devicelist);

		setResult(Activity.RESULT_CANCELED);

		mBtAdapter = BluetoothAdapter.getDefaultAdapter();
		initPairDevices();
		
		listView= (ListView) findViewById(R.id.listview);
		if(deviceListAdapter==null)
			deviceListAdapter=new DeviceListAdapter();
		listView.setAdapter(deviceListAdapter);
		listView.setOnItemClickListener(mDeviceClickListener);

		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		this.registerReceiver(mReceiver, filter);
		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		this.registerReceiver(mReceiver, filter);
		
		doDiscovery();
	}
	
	private void initPairDevices()
	{
		mPairedDevicesArray.clear();
		Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

		if (pairedDevices.isEmpty()) 
			return;
		
		DeviceItem item;
		for (BluetoothDevice device : pairedDevices) {
			if(D)Log.d(TAG,device.getName());
			item=new DeviceItem(device.getName(), device.getAddress());
			mPairedDevicesArray.add(item);
		}		
	}

	@Override
	protected void onDestroy()
	{
		if (D)Log.d(TAG, "onDestroy()");
		super.onDestroy();
		if (D)Log.d(TAG, "after()");
		if (mBtAdapter != null) {
			mBtAdapter.cancelDiscovery();
		}

		this.unregisterReceiver(mReceiver);
	}

	private void doDiscovery()
	{
		if (D)Log.d(TAG, "doDiscovery()");

		setProgressBarIndeterminateVisibility(true);
		setTitle(R.string.scanning);

		if (mBtAdapter.isDiscovering()) {
			mBtAdapter.cancelDiscovery();
		}
		mBtAdapter.startDiscovery();
	}

	private OnItemClickListener mDeviceClickListener = new OnItemClickListener() 
	{
		public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3)
		{
			mBtAdapter.cancelDiscovery();

			TextView addressView=(TextView)v.findViewById(R.id.textview_address);
			String address = addressView.getText().toString();

			Intent intent = new Intent();
			intent.putExtra(EXTRA_DEVICE_ADDRESS, address);
			if(D)Log.d(TAG, "connect:"+address);
			setResult(Activity.RESULT_OK, intent);
			finish();
			listView.setAdapter(null);
		}
	};

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent)
		{
			String action = intent.getAction();

			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
					if(D)Log.d(TAG, "new:"+device.getName());
					DeviceItem item=new DeviceItem(device.getName(), device.getAddress());
					mNewDevicesArray.add(item);
					deviceListAdapter.notifyDataSetChanged();
				}
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
					.equals(action)) {
				setProgressBarIndeterminateVisibility(false);
				setTitle(R.string.select_device);
			}
		}
	};
	
	private class DeviceListAdapter extends BaseAdapter
	{
		@Override
		public boolean isEnabled(int position)
		{
			if(position==0){
				return false;
			}
			int newPos=mPairedDevicesArray.isEmpty()?2:1+mPairedDevicesArray.size();
			if(position==newPos){
				return false;
			}
			return true;
		}
		
		@Override
		public int getCount()
		{
			int count=1;
			if(mPairedDevicesArray.isEmpty()){
				count++;
			}else{
				count+=mPairedDevicesArray.size();
			}
			count++;
			if(mNewDevicesArray.isEmpty()){
				count++;
			}else{
				count+=mNewDevicesArray.size();
			}			
			return count;
		}

		@Override
		public Object getItem(int position)
		{
			return null;
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
			
			
			if(position==0){
				view=LayoutInflater.from(DeviceListActivity.this).inflate(R.layout.layout_device_title, null);
				TextView textView=(TextView)view.findViewById(R.id.textview);
				textView.setText(R.string.title_paired_devices);
				return view;
			}
			
			if(mPairedDevicesArray.isEmpty() && position==1){
				view=LayoutInflater.from(DeviceListActivity.this).inflate(R.layout.layout_no_device, null);
				TextView textView=(TextView)view.findViewById(R.id.textview);
				textView.setText(R.string.none_paired);
				return view;
			}
			
			int newPos=mPairedDevicesArray.isEmpty()?2:1+mPairedDevicesArray.size();
			if(position==newPos){
				view=LayoutInflater.from(DeviceListActivity.this).inflate(R.layout.layout_device_title, null);
				TextView textView=(TextView)view.findViewById(R.id.textview);
				textView.setText(R.string.title_other_devices);
				return view;
			}
			
			if(mNewDevicesArray.isEmpty() && position==1+newPos){
				view=LayoutInflater.from(DeviceListActivity.this).inflate(R.layout.layout_no_device, null);
				TextView textView=(TextView)view.findViewById(R.id.textview);
				textView.setText(R.string.none_found);
				return view;
			}
			
			DeviceItem item;
			if(position<=mPairedDevicesArray.size()){
				view=LayoutInflater.from(DeviceListActivity.this).inflate(R.layout.layout_device_item, null);
				TextView nameView = (TextView) view.findViewById(R.id.textview_name);
				TextView addressView = (TextView) view.findViewById(R.id.textview_address);
				item=mPairedDevicesArray.get(position-1);
				//if(D)Log.d(TAG, "name:"+item.getName());
				nameView.setText(item.getName());
				addressView.setText(item.getAddress());
			}else{
				view=LayoutInflater.from(DeviceListActivity.this).inflate(R.layout.layout_device_item, null);
				TextView nameView = (TextView) view.findViewById(R.id.textview_name);
				TextView addressView = (TextView) view.findViewById(R.id.textview_address);
				if(D)Log.d(TAG, "pos:"+position);
				if(D)Log.d(TAG, "newpos:"+newPos);
				item=mNewDevicesArray.get(position-newPos-1);
				nameView.setText(item.getName());
				addressView.setText(item.getAddress());
			}
			
			return view;
		}
		
	}
}
