package ylj.common.bean;

public class TagItem
{
	private String tag;
	private String value;
	
	public TagItem()
	{		
	}
	
	public TagItem(String tag,String value)
	{
		this.tag=tag;
		this.value=value;
	}
	
	public String getTag()
	{
		return tag;
	}
	
	public void setTag(String tag)
	{
		this.tag=tag;
	}
	
	public String getValue()
	{
		return value;
	}
	
	public void setValue(String value)
	{
		this.value=value;
	}
}
