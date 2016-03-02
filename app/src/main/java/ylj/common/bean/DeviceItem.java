package ylj.common.bean;

public class DeviceItem
{
	private String name;
	private String address;
	
	public DeviceItem(String name,String address)
	{
		this.name=name;
		this.address=address;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getAddress()
	{
		return address;
	}
}
