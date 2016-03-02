package ylj.common.bean;

public class SettingItem
{
	public final static int PREF_STYLE_NONE=-1;
	public final static int PREF_STYLE_INT=0;
	public final static int PREF_STYLE_FLOAT=1;
	public final static int PREF_STYLE_BOOL=2;
	public final static int PREF_STYLE_TEXT=3;
	public final static int PREF_STYLE_ENUM=4;
	public final static int PREF_STYLE_BOOL_DEBUG=5;
	public final static int PREF_STYLE_BOOL_BT=6;
	
	private boolean isGroup=false;
	private boolean isGroupItem=true;
	
	private String group;	
	private String title;
	private String prefenceKey;
	private int prefenceStyle=PREF_STYLE_NONE;
	
	private String textDefault;
	private boolean boolDefault;
	private float floatDefault;
	private int intDefault;
	private int enumDefalut;
	
	private String[] list;
	private float floatMax,floatMin;
	private int intMax,intMin;
	
	public static SettingItem createGroupTitle(String group)
	{
		SettingItem item=new SettingItem();
		item.isGroup=true;
		item.isGroupItem=false;
		item.group=group;
		return item;
	}
	
	public static SettingItem createGroupItem(String group,String title,String prefenceKey,int prefenceStyle)
	{
		SettingItem item=new SettingItem();
		item.isGroup=false;
		item.isGroupItem=true;
		item.group=group;
		item.title=title;
		item.prefenceKey=prefenceKey;
		item.prefenceStyle=prefenceStyle;
		return item;
	}
	
	public static SettingItem createSimpleItem(String title,String prefenceKey,int prefenceStyle)
	{
		SettingItem item=new SettingItem();
		item.isGroup=false;
		item.isGroupItem=false;
		item.title=title;
		item.prefenceKey=prefenceKey;
		item.prefenceStyle=prefenceStyle;
		return item;
	}
	
	public static SettingItem createGroupItem(String group,String title,String prefenceKey,int prefenceStyle,ToSummaryChanger changer)
	{
		SettingItem item=new SettingItem();
		item.isGroup=false;
		item.isGroupItem=true;
		item.group=group;
		item.title=title;
		item.prefenceKey=prefenceKey;
		item.prefenceStyle=prefenceStyle;
		item.changer=changer;
		return item;
	}
	
	public static SettingItem createSimpleItem(String title,String prefenceKey,int prefenceStyle,ToSummaryChanger changer)
	{
		SettingItem item=new SettingItem();
		item.isGroup=false;
		item.isGroupItem=false;
		item.title=title;
		item.prefenceKey=prefenceKey;
		item.prefenceStyle=prefenceStyle;
		item.changer=changer;
		return item;
	}
	
	public interface ToSummaryChanger
	{
		public String change(String value);
	}
	
	private ToSummaryChanger changer;
	
	public void setSummaryChanger(ToSummaryChanger changer)
	{
		this.changer=changer;
	}
	
	public String getDefaultSummary()
	{
		String value;
		switch (prefenceStyle) {
		case PREF_STYLE_INT:
			value=String.valueOf(intDefault);
			break;
		case PREF_STYLE_BOOL:
			value="";
			break;
		case PREF_STYLE_ENUM:
			value=list[enumDefalut];
			break;
		case PREF_STYLE_TEXT:
			value=textDefault;
			break;
		case PREF_STYLE_FLOAT:		
			value=String.valueOf(floatDefault);
			break;
		default:
			value="";
			break;
		}
		return getSummary(value);
	}
	
	public String getEnumSummary(int value)
	{
		if(changer==null)
			return list[value];
		return changer.change(list[value]);
	}
	
	public String getSummary(int value)
	{
		if(changer==null)
			return String.valueOf(value);
		return changer.change(String.valueOf(value));
	}
	
	public String getSummary(float value)
	{
		if(changer==null)
			return String.valueOf(value);
		return changer.change(String.valueOf(value));
	}
	
	public String getSummary(String value)
	{
		if(changer==null)
			return value;
		return changer.change(value);
	}
	
	public String getGroup()
	{
		return group;
	}
	
	public void setGroup(String group)
	{
		this.group = group;
	}
	
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public String getPrefenceKey()
	{
		return prefenceKey;
	}
	public void setPrefenceKey(String prefenceKey)
	{
		this.prefenceKey = prefenceKey;
	}
	public int getPrefenceStyle()
	{
		return prefenceStyle;
	}
	public void setPrefenceStyle(int prefenceStyle)
	{
		this.prefenceStyle = prefenceStyle;
	}

	public boolean isGroup()
	{
		return isGroup;
	}

	public void setGroup(boolean isGroup)
	{
		this.isGroup = isGroup;
	}

	public boolean isGroupItem()
	{
		return isGroupItem;
	}

	public void setGroupItem(boolean isGroupItem)
	{
		this.isGroupItem = isGroupItem;
	}

	public String getTextDefault()
	{
		return textDefault;
	}

	public void setTextDefault(String textDefault)
	{
		this.textDefault = textDefault;
	}

	public boolean isBoolDefault()
	{
		return boolDefault;
	}

	public void setBoolDefault(boolean boolDefault)
	{
		this.boolDefault = boolDefault;
	}

	public float getFloatDefault()
	{
		return floatDefault;
	}

	public void setFloatDefault(float floatDefault)
	{
		this.floatDefault = floatDefault;
	}

	public int getIntDefault()
	{
		return intDefault;
	}

	public void setIntDefault(int intDefault)
	{
		this.intDefault = intDefault;
	}

	public int getEnumDefalut()
	{
		return enumDefalut;
	}

	public void setEnumDefalut(int enumDefalut)
	{
		this.enumDefalut = enumDefalut;
	}

	public String[] getList()
	{
		return list;
	}

	public void setList(String[] list)
	{
		this.list = list;
	}

	public float getFloatMax()
	{
		return floatMax;
	}

	public void setFloatMax(float floatMax)
	{
		this.floatMax = floatMax;
	}

	public float getFloatMin()
	{
		return floatMin;
	}

	public void setFloatMin(float floatMin)
	{
		this.floatMin = floatMin;
	}

	public int getIntMax()
	{
		return intMax;
	}

	public void setIntMax(int intMax)
	{
		this.intMax = intMax;
	}

	public int getIntMin()
	{
		return intMin;
	}

	public void setIntMin(int intMin)
	{
		this.intMin = intMin;
	}
}
