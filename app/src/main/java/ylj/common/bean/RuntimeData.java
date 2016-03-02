package ylj.common.bean;

import java.util.ArrayList;

public class RuntimeData extends RuntimePara
{	
	private ArrayList<Record> mList=new ArrayList<Record>();
	
	public void addRecord(Record record)
	{
		mList.add(record);
	}
	
	public Record getRecord(int index)
	{
		return mList.get(index);
	}
	
	public int size()
	{
		return mList.size();
	}
	
	public void clear()
	{
		mList.clear();
	}

}
