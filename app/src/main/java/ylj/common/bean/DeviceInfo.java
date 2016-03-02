package ylj.common.bean;

import java.util.Date;

public class DeviceInfo
{
	private String deviceId="001";
	private String version="V1.0";
	private String softVersion="V1.0";
	private Date productDate=new Date(2014, 12, 1);
	
	private boolean huoerState;
	private boolean dirState;
	private boolean tempState;
	private boolean quakeState;

	
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
}
