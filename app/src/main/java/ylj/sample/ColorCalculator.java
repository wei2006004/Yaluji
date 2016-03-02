package ylj.sample;

import android.graphics.Color;
import android.graphics.PointF;
import android.util.Log;

import ylj.common.bean.ColorData;
import ylj.common.bean.Record;
import ylj.common.bean.RuntimeData;
import ylj.common.bean.RuntimePara;

class ColorResult extends ColorData
{
	private float sum=0;
	
	public float getValue() {
		if(number==0)
			return 0;
		return sum/number;
	}
	
	public void addValue(float value) {
		sum+=value;
		number++;
	}
	
	public void clear()
	{
		sum=0;
		number=0;
	}
}

public class ColorCalculator {
	public static final String TAG="ColorCalculator";
	public static final boolean D=true;
	
	private int passNum=0;
	private int nearNum=0;
	private int notPassNum=0;
	private int goodNum=0;
	
	private boolean mOrigin=true;
	private float roadLength=50;
	private float roadWidth=30;
	
	private static ColorCalculator colorCalculator=null;
	public static ColorCalculator instance()
	{
		if(colorCalculator==null)
			colorCalculator=new ColorCalculator();
		return colorCalculator;
	}
	
	public boolean getOrigin()
	{
		return mOrigin;
	}
	
	public int getPassNum()
	{
		return passNum;
	}
	
	public int getNearNum(){
		return nearNum;
	}
	
	public int getGoodNum(){
		return goodNum;
	}
	
	public int getNotPassNum(){
		return notPassNum;
	}
	
	public int getRowNum()
	{
		return rowNum;
	}
	
	public int getColumnNum()
	{
		return columnNum;
	}
	
	private int currentRow=0;
	private int currentColumn=0;

	private ColorResult[][] list=null;
	private int rowNum=8;
	private int columnNum=15;
	private boolean isCounted=false;
	
	public int getColor(int row,int column)
	{
		if((row<0 || row >=rowNum) || (column<0||column>=columnNum) ||list==null)
			return Color.WHITE;
		return list[row][column].getColor();
	}
	
	public int getCount(int row,int column)
	{
		if((row<0 || row >=rowNum) || (column<0||column>=columnNum) ||list==null)
			return 0;
		return list[row][column].getCount();
	}
	
	public ColorData getData(int row,int column)
	{
		if((row<0 || row >=rowNum) || (column<0||column>=columnNum) ||list==null)
			return null;
		return list[row][column];
	}
	
	public ColorData getCurrentData(){
		return list[currentRow][currentColumn];
	}
	
	public void setGrid(int row,int column)
	{
		rowNum=row;
		columnNum=column;
		isCounted=false;
	}
	
	public boolean isCounted()
	{
		return isCounted;
	}

	public void setRuntimePata(RuntimePara pata)
	{
		roadLength=pata.getRoadLength();
		roadWidth=pata.getRoadWidth();
		mOrigin=pata.getOrigin()==0?false:true;
		isCounted=false;
	}
	
	public void addData(PointF point,float value,float vcv)
	{
		isCounted=true;
		int row=0,column=0;
		column=(int)(point.x/(roadLength/columnNum));
		row=(int)(point.y/(roadWidth/rowNum));			
		if((row<0 || row >=rowNum) || (column<0||column>=columnNum))
			return;
		ColorResult result=list[row][column];
		if(row==currentRow && column== currentColumn){
			if(D)Log.d(TAG, "add:"+row+":"+column+":"+value);
			result.addValue(value);
		}else{			
			if(result.getCount()!=0){
				result.clear();
			}
			result.incCount();
			result.addValue(value);
			currentColumn=column;
			currentRow=row;
		}
		result.setColor(convertToColor(result.getValue(),vcv));
	}
	
	public void init()
	{
		list=new ColorResult[rowNum][columnNum];
		for(int i=0;i<rowNum;i++){
			for(int j=0;j<columnNum;j++)
				list[i][j]=new ColorResult();
		}
		isCounted=false;
	}
	
	public void setRuntimeData(RuntimeData runtimeData,float vcv)
	{
		if(runtimeData==null)
			return;
		setRuntimePata(runtimeData);
		init();
		addDatas(runtimeData,vcv);
		isCounted=true;
	}
	
	private void addDatas(RuntimeData runtimeData,float vcv)
	{
		Record record=null;
		currentRow=0;
		currentColumn=0;
		for(int i=0;i<runtimeData.size();i++){
			record=runtimeData.getRecord(i);
			addData(record.getPosition(), record.getQuake(),vcv);
		}
	}
	
	public void countNumber()
	{
		int color;
		if(!isCounted)
			return;
		notPassNum=0;passNum=0;
		goodNum=0;nearNum=0;
		for(int i=0;i<rowNum;i++){
			for(int j=0;j<columnNum;j++){
				color=list[i][j].getColor();
				switch (color) {
				case Color.RED:
					notPassNum++;
					break;
				case Color.YELLOW:
					nearNum++;
					break;
				case Color.BLUE:
					passNum++;
					break;
				case Color.GREEN:
					goodNum++;
				default:
					break;
				}
			}
		}
	}
	
	private int convertToColor(float value,float vcv)
	{
		int color=Color.RED;
		if(value>0.95*vcv){
			color=Color.GREEN;
		}else if(value>0.75*vcv){
			color=Color.BLUE;
		}else if(value>0.6*vcv){
			color=Color.YELLOW;
		}
		//Log.d("reslut", "co:"+color);
		return color;
	}
	
	public static final float VCV_DEFAULT_VALUE=10;

	public int getCurrentRow() {
		return currentRow;
	}

	public int getCurrentColumn() {
		return currentColumn;
	}
}
