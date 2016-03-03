package com.ylj.adjust;

public class AdjustData {

	public static final int ADJUST_POINT_NUM=6;
	
	public static final int ADJUST_TYPE_LIGHT=0;
	public static final int ADJUST_TYPE_MIDDLE=1;
	public static final int ADJUST_TYPE_HEAVY=2;
	
	public static class Data{
		public float quake;
		public float compaction;
	}
	
	private static AdjustData adjustData=null;
	public static AdjustData instance(){
		if(adjustData==null){
			adjustData=new AdjustData();
		}
		return adjustData;
	}
	
	private Data[]  list;
	
	public AdjustData(){
		list=new Data[ADJUST_POINT_NUM*3];
		for(int i=0;i<ADJUST_POINT_NUM*3;i++){
			list[i]=new Data();
		}
	}
	
	public void setData(int type,int index,float quake,float compaction)
	{
		list[type*ADJUST_POINT_NUM+index].quake=quake;
		list[type*ADJUST_POINT_NUM+index].compaction=compaction;
	}
	
	public Data[] getDatas()
	{
		return list;
	}
}
