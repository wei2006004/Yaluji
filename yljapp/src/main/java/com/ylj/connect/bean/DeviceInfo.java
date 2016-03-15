package com.ylj.connect.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class DeviceInfo implements Parcelable
{
	private String deviceId="001";
	private String version="V1.0";
	private String softVersion="V1.0";
	private Date productDate=new Date(2014, 12, 1);
	
	private boolean huoerState;
	private boolean dirState;
	private boolean tempState;
	private boolean quakeState;

	public DeviceInfo(){}

	protected DeviceInfo(Parcel in) {
		deviceId = in.readString();
		version = in.readString();
		softVersion = in.readString();
		huoerState = in.readByte() != 0;
		dirState = in.readByte() != 0;
		tempState = in.readByte() != 0;
		quakeState = in.readByte() != 0;
	}

	public static final Creator<DeviceInfo> CREATOR = new Creator<DeviceInfo>() {
		@Override
		public DeviceInfo createFromParcel(Parcel in) {
			return new DeviceInfo(in);
		}

		@Override
		public DeviceInfo[] newArray(int size) {
			return new DeviceInfo[size];
		}
	};

	public boolean isHuoerState()
	{
		return huoerState;
	}
	public void setHuoerState(boolean huoerState)
	{
		this.huoerState = huoerState;
	}
	
	public boolean isDirState()
	{
		return dirState;
	}
	
	public void setDirState(boolean dirState)
	{
		this.dirState = dirState;
	}
	
	public boolean isTempState()
	{
		return tempState;
	}
	
	public void setTempState(boolean tempState)
	{
		this.tempState = tempState;
	}
	
	public boolean isQuakeState()
	{
		return quakeState;
	}
	
	public void setQuakeState(boolean quakeState)
	{
		this.quakeState = quakeState;
	}
	
	public String getDeviceId()
	{
		return deviceId;
	}
	
	public void setDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
	}
	
	public String getVersion()
	{
		return version;
	}
	
	public void setVersion(String version)
	{
		this.version = version;
	}
	
	public String getSoftVersion()
	{
		return softVersion;
	}
	
	public void setSoftVersion(String softVersion)
	{
		this.softVersion = softVersion;
	}
	
	public Date getProductDate()
	{
		return productDate;
	}
	
	public void setProductDate(Date productDate)
	{
		this.productDate = productDate;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(deviceId);
		dest.writeString(version);
		dest.writeString(softVersion);
		dest.writeByte((byte) (huoerState ? 1 : 0));
		dest.writeByte((byte) (dirState ? 1 : 0));
		dest.writeByte((byte) (tempState ? 1 : 0));
		dest.writeByte((byte) (quakeState ? 1 : 0));
	}
}
