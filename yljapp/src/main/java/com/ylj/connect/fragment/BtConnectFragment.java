package com.ylj.connect.fragment;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;

import com.ylj.R;
import com.ylj.common.BaseFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ContentView(R.layout.fragment_bt_connect)
public class BtConnectFragment extends BaseFragment {

    private static final int REQUEST_ENABLE_BT = 2;

    private static final String TAG_DEVCIE_NAME = "TAG_DEVCIE_NAME";
    private static final String TAG_DEVCIE_ADDRESS = "TAG_DEVCIE_ADDRESS";

    @ViewInject(R.id.lv_device)
    private ListView mDeviceListView;

    @ViewInject(R.id.switch_bluetooth)
    private Switch mBtSwitch;

    @ViewInject(R.id.btn_refresh)
    private Button mRefreshBtn;

    private BluetoothAdapter mBluetoothAdapter;

    private OnBluetoothConnectListener mListener;

    public BtConnectFragment() {
    }

    @Event(R.id.btn_refresh)
    private void onRefreshClick(View view) {
        doDiscovery();
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Map<String, String> map = new HashMap<>();
                map.put(TAG_DEVCIE_NAME, device.getName());
                map.put(TAG_DEVCIE_ADDRESS, device.getAddress());
                mDeviceMaps.add(map);
                mDeviceAdapter.notifyDataSetChanged();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
                    .equals(action)) {
                showToast("scan finished");
            }
        }
    };


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initDeviceListView();
        initBtState();
    }

    SimpleAdapter mDeviceAdapter;
    List<Map<String, String>> mDeviceMaps = new ArrayList<>();

    private void initDeviceListView() {
        mDeviceAdapter = new SimpleAdapter(getActivity(),
                mDeviceMaps,
                R.layout.listview_bt_device_item,
                new String[]{TAG_DEVCIE_NAME, TAG_DEVCIE_ADDRESS},
                new int[]{R.id.tv_name, R.id.tv_address});
        mDeviceListView.setAdapter(mDeviceAdapter);
        mDeviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onDeviceItemClick(position);
            }
        });
    }

    private void onDeviceItemClick(int position) {
        String address = mDeviceMaps.get(position).get(TAG_DEVCIE_ADDRESS);
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        if (mListener != null) {
            mListener.onBluetoothConnect(device);
        }
    }

    private void doDiscovery() {
        showToast("start scan device");
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        mBluetoothAdapter.startDiscovery();
    }

    private void initBtState() {
        int state = mBluetoothAdapter.getState();

        if (state == BluetoothAdapter.STATE_ON) {
            mBtSwitch.setChecked(true);
            mRefreshBtn.setEnabled(true);
        } else {
            mBtSwitch.setChecked(false);
            mRefreshBtn.setEnabled(false);
        }
        mBtSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                enableBlueTooth(isChecked);
            }
        });
    }

    private void enableBlueTooth(boolean enable) {
        if (enable) {
            if (mBluetoothAdapter.enable()) {
                mRefreshBtn.setEnabled(true);
                doDiscovery();
            }
        } else {
            mBluetoothAdapter.disable();
            mDeviceMaps.clear();
            mRefreshBtn.setEnabled(false);
            mDeviceAdapter.notifyDataSetChanged();
        }
    }

    private void initData() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    Activity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnBluetoothConnectListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnWifiConnectListener");
        }

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        activity.registerReceiver(mReceiver, filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        activity.registerReceiver(mReceiver, filter);
        mActivity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity.unregisterReceiver(mReceiver);
        mActivity = null;
        mListener = null;
    }

    public interface OnBluetoothConnectListener {
        void onBluetoothConnect(BluetoothDevice device);
    }

}
