package ylj.common.bean;

public class RuntimePara extends DeviceInfo
{
	public final static int ORIGIN_CLOCKWISE=0;
	public final static int ORIGIN_ANTI_CLOCKWISE=1;
	
	private float step=(float)0.8;
	private int huoerNum=4;
	private float roadWidth=30;
	private float roadLength=50;
	private float rollWidth=(float) 2.4;
	private float rollDiameter=2;
	private int origin=ORIGIN_ANTI_CLOCKWISE;
	
	private String stationIp="192.168.1.121";
	private int port=21;
	private String user="user";
	private String password="123";
	
	public float getStep()
	{
		return step;
	}
	public void setStep(float step)
	{
		this.step = step;
	}
	public int getHuoerNum()
	{
		return huoerNum;
	}
	public void setHuoerNum(int huoerNum)
	{
		this.huoerNum = huoerNum;
	}
	public float getRoadWidth()
	{
		return roadWidth;
	}
	public void setRoadWidth(float roadWidth)
	{
		this.roadWidth = roadWidth;
	}
	public float getRoadLength()
	{
		return roadLength;
	}
	public void setRoadLength(float roadLength)
	{
		this.roadLength = roadLength;
	}
	public float getRollWidth()
	{
		return rollWidth;
	}
	public void setRollWidth(float rollWidth)
	{
		this.rollWidth = rollWidth;
	}
	public float getRollDiameter()
	{
		return rollDiameter;
	}
	public void setRollDiameter(float rollDiameter)
	{
		this.rollDiameter = rollDiameter;
	}
	public int getOrigin()
	{
		return origin;
	}
	public void setOrigin(int origin)
	{
		this.origin = origin;
	}
	public String getStationIp()
	{
		return stationIp;
	}
	public void setStationIp(String stationIp)
	{
		this.stationIp = stationIp;
	}
	public int getPort()
	{
		return port;
	}
	public void setPort(int port)
	{
		this.port = port;
	}
	public String getUser()
	{
		return user;
	}
	public void setUser(String user)
	{
		this.user = user;
	}
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
}
