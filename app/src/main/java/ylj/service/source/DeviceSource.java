package ylj.service.source;

import ylj.common.bean.DeviceData;

public class DeviceSource extends DataSource
{
	private DeviceData source;
	
	public synchronized void setCurrentData(DeviceData data)
	{
		source=data;
	}
	
	@Override
	public boolean start()
	{
		return true;
	}

	@Override
	public boolean stop()
	{
		return true;
	}

	@Override
	public synchronized boolean refresh()
	{
		if(source!=null)
			setData(source);
		return true;
	}

	@Override
	public void reset() {

	}

}
