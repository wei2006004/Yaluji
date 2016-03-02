package ylj.common.bean;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

public class HisdataItem
{
	public static final String TAG="HisdataItem";
	public static final boolean D=true;
	
	private String deviceId;
	private Date testTime;
	private int timeLength;
	
	public static HisdataItem fromFileName(String file)
	{
		String name=file.substring(0,file.length()-3);
		String deviceId=name.substring(0,name.indexOf('_'));
		String testTime=name.substring(name.indexOf('_')+1);
		if(D)Log.d(TAG, "id:"+deviceId+" time:"+testTime);
		
		HisdataItem item=new HisdataItem();
		item.setDeviceId(deviceId);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		try {
			item.setTestTime(sdf.parse(testTime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return item;
	}
	
	public String getFileName()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		String timeString=sdf.format(testTime);
		return deviceId+"_"+timeString+".db";
	}
	
	public String getDeviceId()
	{
		return deviceId;
	}
	public void setDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
	}

	public int getTimeLength()
	{
		return timeLength;
	}
	public void setTimeLength(int timeLength)
	{
		this.timeLength = timeLength;
	}

	public Date getTestTime()
	{
		return testTime;
	}

	public void setTestTime(Date testTime)
	{
		this.testTime = testTime;
	}
	
	
}
