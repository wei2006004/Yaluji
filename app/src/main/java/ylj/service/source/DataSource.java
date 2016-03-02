package ylj.service.source;

import ylj.common.bean.DeviceData;

public abstract class DataSource extends DeviceData
{
	public abstract boolean start();
	public abstract boolean stop();
	public abstract boolean refresh();
	public abstract void reset();
}
