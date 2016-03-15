package com.ylj.task.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class DeviceData implements Parcelable
{
	public final static int STATE_BACKING=-1;
	public final static int STATE_STOP=0;
	public final static int STATE_HEADING=1;
	
	protected int pulse; 
	protected float speed;
	
	protected int temp;
	protected int quake;	
	
	protected float compassHeading;
	protected float compassPitch;
	protected float compassRoll;
	
	protected int state=STATE_STOP;

	public DeviceData(){}

	protected DeviceData(Parcel in) {
		pulse = in.readInt();
		speed = in.readFloat();
		temp = in.readInt();
		quake = in.readInt();
		compassHeading = in.readFloat();
		compassPitch = in.readFloat();
		compassRoll = in.readFloat();
		state = in.readInt();
	}

	public static final Creator<DeviceData> CREATOR = new Creator<DeviceData>() {
		@Override
		public DeviceData createFromParcel(Parcel in) {
			return new DeviceData(in);
		}

		@Override
		public DeviceData[] newArray(int size) {
			return new DeviceData[size];
		}
	};

	@Override
	public String toString()
	{
		return "pulse:"+pulse+" temp:"+temp+" quake:"+quake+" roll:"+compassRoll;
	}
	
	public void setData(DeviceData data)
	{
		pulse=data.pulse;
		speed=data.speed;
		temp=data.temp;
		quake=data.quake;
		compassHeading=data.compassHeading;
		compassPitch=data.compassPitch;
		compassRoll=data.compassRoll;
		state=data.state;
	}

	public int getPulse()
	{
		return pulse;
	}

	public void setPulse(int pulse)
	{
		this.pulse = pulse;
	}

	public float getSpeed()
	{
		return speed;
	}

	public void setSpeed(float speed)
	{
		this.speed = speed;
	}

	public int getTemp()
	{
		return temp;
	}

	public void setTemp(int temp)
	{
		this.temp = temp;
	}

	public int getQuake()
	{
		return quake;
	}

	public void setQuake(int quake)
	{
		this.quake = quake;
	}

	public float getCompassHeading()
	{
		return compassHeading;
	}

	public void setCompassHeading(float compassHeading)
	{
		this.compassHeading = compassHeading;
	}

	public float getCompassPitch()
	{
		return compassPitch;
	}

	public void setCompassPitch(float compassPitch)
	{
		this.compassPitch = compassPitch;
	}

	public float getCompassRoll()
	{
		return compassRoll;
	}

	public void setCompassRoll(float compassRoll)
	{
		this.compassRoll = compassRoll;
	}

	public int getState()
	{
		return state;
	}

	public void setState(int state)
	{
		this.state = state;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(pulse);
		dest.writeFloat(speed);
		dest.writeInt(temp);
		dest.writeInt(quake);
		dest.writeFloat(compassHeading);
		dest.writeFloat(compassPitch);
		dest.writeFloat(compassRoll);
		dest.writeInt(state);
	}
}
