package ylj.common.bean;

import android.graphics.PointF;

public class Record
{
	private float distance;
	private float speed;
	private int state;
	private float direction;
	private float temp;
	private float quake;
	private PointF position=new PointF();

	public void set(float dist,float speed,float dir,int state,float temp,float quake,float x,float y)
	{
		this.distance=dist;
		this.speed=speed;
		this.direction=dir;
		this.temp=temp;
		this.quake=quake;
		this.state=state;
		position.x=x;
		position.y=y;
	}
	
	public void set(Record record)
	{
		set(record.distance,record.speed,record.direction,record.state,record.temp,record.quake,record.position.x,record.position.y);
	}
	
	@Override
	public String toString()
	{
		return "dist:"+distance+" dir:"+direction+" temp:"+temp+" quake:"+quake;
	}

	public float getDistance()
	{
		return distance;
	}

	public void setDistance(float distance)
	{
		this.distance = distance;
	}

	public float getSpeed()
	{
		return speed;
	}

	public void setSpeed(float speed)
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

	public float getDirection()
	{
		return direction;
	}

	public void setDirection(float direction)
	{
		this.direction = direction;
	}

	public float getTemp()
	{
		return temp;
	}

	public void setTemp(float temp)
	{
		this.temp = temp;
	}

	public float getQuake()
	{
		return quake;
	}

	public void setQuake(float quake)
	{
		this.quake = quake;
	}

	public PointF getPosition()
	{
		return position;
	}

	public void setPosition(PointF position)
	{
		this.position = position;
	}
}
