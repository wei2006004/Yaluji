package com.ylj.task.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Record implements Parcelable
{
	private double distance;
	private double speed;
	private int state;
	private double direction;
	private double temp;
	private double quake;
	private double positionX;
    private double positionY;

    protected Record(Parcel in) {
        distance = in.readDouble();
        speed = in.readDouble();
        state = in.readInt();
        direction = in.readDouble();
        temp = in.readDouble();
        quake = in.readDouble();
        positionX = in.readDouble();
        positionY = in.readDouble();
    }

    public static final Creator<Record> CREATOR = new Creator<Record>() {
        @Override
        public Record createFromParcel(Parcel in) {
            return new Record(in);
        }

        @Override
        public Record[] newArray(int size) {
            return new Record[size];
        }
    };

    public void set(double dist,double speed,double dir,int state,double temp,double quake,double x,double y)
	{
		this.distance=dist;
		this.speed=speed;
		this.direction=dir;
		this.temp=temp;
		this.quake=quake;
		this.state=state;
		positionX =x;
		positionY =y;
	}
	
	public void set(Record record)
	{
		set(record.distance,record.speed,record.direction,record.state,record.temp,record.quake,record.positionX,record.positionY);
	}
	
	@Override
	public String toString()
	{
		return "dist:"+distance+" dir:"+direction+" temp:"+temp+" quake:"+quake;
	}

	public double getDistance()
	{
		return distance;
	}

	public void setDistance(double distance)
	{
		this.distance = distance;
	}

	public double getSpeed()
	{
		return speed;
	}

	public void setSpeed(double speed)
	{
		this.speed = speed;
	}

	public int getState()
	{
		return state;
	}

	public void setState(int state)
	{
		this.state = state;
	}

	public double getDirection()
	{
		return direction;
	}

	public void setDirection(double direction)
	{
		this.direction = direction;
	}

	public double getTemp()
	{
		return temp;
	}

	public void setTemp(double temp)
	{
		this.temp = temp;
	}

	public double getQuake()
	{
		return quake;
	}

	public void setQuake(double quake)
	{
		this.quake = quake;
	}

    public double getPositionX() {
        return positionX;
    }

    public void setPositionX(double positionX) {
        this.positionX = positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public void setPositionY(double positionY) {
        this.positionY = positionY;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(distance);
        dest.writeDouble(speed);
        dest.writeInt(state);
        dest.writeDouble(direction);
        dest.writeDouble(temp);
        dest.writeDouble(quake);
        dest.writeDouble(positionX);
        dest.writeDouble(positionY);
    }
}
