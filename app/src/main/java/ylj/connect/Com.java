package ylj.connect;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.ylj.R;
import ylj.service.control.TestCtrl;
import ylj.common.tool.XmlTool;
import ylj.common.bean.DeviceData;
import ylj.common.bean.DeviceInfo;

public class Com {
	public static final String TAG="Com";
	public static final boolean D=true;
	
	private XmlTool xmlTool;
	protected Context context;
	protected Handler handler;
	
	private String message="";
	
	public final static String MESSAGE_DEVICE="1";
	public final static String MESSAGE_START="2";
	public final static String MESSAGE_STOP="3";
	
	public Com(Context context,Handler infoHandler,Handler ctrlHandler)
	{
		this.context=context;
		this.handler=infoHandler;
		xmlTool=new XmlTool(context);
	}
	
	public int getState(){
		return ComConst.STATE_NONE;
	}
	
	public void connect()
	{
	}
	
	public void stop()
	{
	}
	
	public void sendMessage(String msg)
	{
	}
	
	public void sendDeviceMessage()
	{
		sendMessage(MESSAGE_DEVICE);
	}
	
	public void sendStartMessage()
	{
		if(D)Log.d(TAG, "send start message!");
		sendMessage(MESSAGE_START);
	}
	
	public void sendStopMessage()
	{
		if(D)Log.d(TAG, "send stop message!");
		sendMessage(MESSAGE_STOP);
	}
	
	protected void handleReadMessage(String msg)
	{
		if(!combineMessage(msg))
			return;
		parseMessage(message);
		message="";
	}
	
	private boolean combineMessage(String msg)
	{
		if(msg.startsWith(""+ComConst.START_FLAG)&&msg.endsWith(""+ComConst.END_FLAG)){
			message+=msg.substring(1, msg.length()-1);
			return true;
		}
		if(msg.endsWith(""+ComConst.END_FLAG)){
			if(msg.contains(""+ComConst.START_FLAG)){
				message="";
				return false;
			}
			message+=msg.substring(0, msg.length()-1);
			return true;
		}		
		if(msg.startsWith(""+ComConst.START_FLAG)){
			message=msg.substring(1);
			return false;
		}		
		message+=msg;
		return false;
	}
	
	private void parseMessage(String msg)
	{
		if(D)Log.d(TAG, "parseMessage:"+msg);
		if(msg.contains(context.getString(R.string.xml_deviceInfo))){
			if(D)Log.d(TAG, "info");
			DeviceInfo info=xmlTool.getDeviceInfoFromString(msg);
			handler.obtainMessage(ComConst.COM_DEVICE_INFO, info).sendToTarget();
		}else if(msg.contains(context.getString(R.string.xml_deviceData))){
			if(D)Log.d(TAG, "data");
			DeviceData data=xmlTool.getDeviceDataFromString(msg);
			if(data!=null)
				TestCtrl.instance().setDeviceData(data);
		}else {
			handler.sendEmptyMessage(ComConst.COM_READ_ERROR);
		}
		
	}
}
