package ylj.service.source;

import ylj.common.bean.DeviceData;

import android.graphics.PointF;

public class CommonData
{		
	private DataSource mDataSource;
	
	private static double tempOffset=(-250.0/4);
	private static double quakeOffset=0;
	
	private static double tempScale=(25.0*5000)/(65536.0*24*16);
	private static double quakeScale=1.0/4096;

	private float step=1;
		
	public CommonData(DataSource source)
	{
		mDataSource=source;
		
		lastPointF.set(0, 0);
		currentPointF.set(0, 0);
	}
	
	public void reset()
	{
		mDataSource.reset();
		lastPointF.set(0, 0);
		currentPointF.set(0, 0);
	}
	
	public void setTempPara(int tempOffset,float tempScale)
	{
		this.tempOffset = tempOffset;
		this.tempScale = tempScale;
	}

	public void setQuakePara(int quakeOffset,float quakeScale)
	{
		this.quakeOffset = quakeOffset;
		this.quakeScale = quakeScale;
	}
	
	public void setStep(float step)
	{
		this.step = step;
	}
	
	public void setDataSource(DataSource source)
	{
		mDataSource=source;
	}
	
	public int getState()
	{
		return mDataSource.getState();
	}
	
	public float getDistance()
	{
		return step*mDataSource.getPulse();
	}
	
	public float getSpeed()
	{
		return step*mDataSource.getSpeed();
	}
	
	public float getTemp()
	{
		return (float) (mDataSource.getTemp()*tempScale+tempOffset);
	}
	
	public float getQuake()
	{
		return (float)(mDataSource.getQuake()*quakeScale+quakeOffset);
	}
	
	public float getDirection()
	{
		return mDataSource.getCompassHeading()/(float)100.0;
	}
	
	private PointF lastPointF=new PointF(0,0);
	private PointF currentPointF=new PointF(0,0);
	private int lastPulse=0;
	
	public final PointF getPosition()
	{
		return currentPointF;
	}
	
	private PointF computePos()
	{
		PointF pointF=new PointF();
		
	    float speed=mDataSource.getPulse()-lastPulse;
	    double xspeed=speed*Math.cos(Math.toRadians(getDirection()));
	    double yspeed=speed*Math.sin(Math.toRadians(getDirection()));

	    if(getState()==DeviceData.STATE_BACKING){
	    	xspeed=-xspeed;
	    	yspeed=-yspeed;
	    }
	    pointF.x=(float) (lastPointF.x+xspeed)*step;
	    pointF.y=(float) (lastPointF.y+yspeed)*step;
			
		return pointF;
	}

	public boolean refresh()
	{
		lastPulse=mDataSource.getPulse();
		lastPointF.set(currentPointF);
		
		boolean flag=mDataSource.refresh();
		currentPointF=computePos();
		
		return flag;
	}


}
