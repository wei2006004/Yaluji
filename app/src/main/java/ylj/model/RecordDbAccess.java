package ylj.model;

import ylj.common.bean.Record;
import ylj.common.bean.RuntimeData;
import ylj.common.bean.RuntimePara;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PointF;
import android.provider.BaseColumns;
import android.util.Log;

public class RecordDbAccess
{
	private SQLiteDatabase mDatabase;
	
	private static abstract class ParaEntry implements BaseColumns
	{
		public static final String TABLE_NAME="para_table";
		public static final String COLUMN_TAG="para_name";
		public static final String COLUMN_VALUE="para_value";
		
		public static final String PARA_STEP="step";
		public static final String PARA_ROADW="road_width";
		public static final String PARA_ROADL="road_length";
		public static final String PARA_ROLLW="roll_width";
		public static final String PARA_ROLLD="roll_diameter";
		public static final String PARA_ORIGIN="origin";
		public static final String PARA_HUOERNUM="huoer_num";
		
		public static final String PARA_DEVICE_ID="device_id";
		public static final String PARA_DEVICE_VERSION="device_version";
		public static final String PARA_DEVICE_SOFT_VERSION="device_soft_version";
	}
	
	private static abstract class RecordEntry implements BaseColumns
	{
		public static final String TABLE_NAME="record_table";
		public static final String COLUMN_DIST="distance";
		public static final String COLUMN_SPEED="speed";
		public static final String COLUMN_STATE="state";
		public static final String COLUMN_DIR="direction";
		public static final String COLUMN_TEMP="temp";
		public static final String COLUMN_QUAKE="quake";
		public static final String COLUMN_POINT_X="point_x";
		public static final String COLUMN_POINT_Y="point_y";
	}
	
	private static final String TEXT_TYPE = " TEXT"; 
	private static final String REAL_TYPE = " REAL"; 
	private static final String INTEGER_TYPE = " INTEGER"; 
	private static final String COMMA_SEP = ","; 
	
	private static final String SQL_CREATE_PARA_TABLE = "CREATE TABLE "+ 
					ParaEntry.TABLE_NAME + " (" + 
					ParaEntry._ID+ " INTEGER PRIMARY KEY," +
					ParaEntry.COLUMN_TAG+ TEXT_TYPE + COMMA_SEP +
					ParaEntry.COLUMN_VALUE + TEXT_TYPE + " )";
	
	private static final String SQL_CREATE_RECORD_TABLE = 
			"CREATE TABLE "+ RecordEntry.TABLE_NAME + " (" + 
					RecordEntry._ID+ " INTEGER PRIMARY KEY," +
					RecordEntry.COLUMN_DIST+ REAL_TYPE + COMMA_SEP +
					RecordEntry.COLUMN_DIR + REAL_TYPE + COMMA_SEP +
					RecordEntry.COLUMN_STATE+ INTEGER_TYPE + COMMA_SEP +
					RecordEntry.COLUMN_SPEED + REAL_TYPE + COMMA_SEP +
					RecordEntry.COLUMN_TEMP + REAL_TYPE + COMMA_SEP +
					RecordEntry.COLUMN_QUAKE + REAL_TYPE + COMMA_SEP +
					RecordEntry.COLUMN_POINT_X + REAL_TYPE + COMMA_SEP +
					RecordEntry.COLUMN_POINT_Y + REAL_TYPE + " )";

	private static final String SQL_IF_PARA_TABLE_EXIT = 
			"select count(*) from sqlite_master where type='table' and name='"+ParaEntry.TABLE_NAME+"'";
	
	private static final String SQL_IF_RECORD_TABLE_EXIT = 
			"select count(*) from sqlite_master where type='table' and name='"+RecordEntry.TABLE_NAME+"'";
	
	private static final String SQL_SELECT_PARA_VALUE = 
			"select "+ParaEntry.COLUMN_VALUE+" from "+
					ParaEntry.TABLE_NAME+" where "+ParaEntry.COLUMN_TAG+"=";
	
	private static final String SQL_SELECT_RECORD_ALL = 
			"select * from "+RecordEntry.TABLE_NAME;
	
	private String databaseName;
	
	public RecordDbAccess(String name)
	{
		databaseName=name;
	}
	
	public RecordDbAccess()
	{
	}
	
	public void setDbName(String name)
	{
		databaseName=name;
	}
	
	private boolean openDb()
	{
		try {
			mDatabase=SQLiteDatabase.openDatabase(databaseName, null,SQLiteDatabase.OPEN_READONLY);		
		} catch (SQLException e) {
			Log.e("open db:",e.getMessage());
			return false;
		}	
		return true;
	}
	
	private boolean createDb()
	{		
		try {
			mDatabase=SQLiteDatabase.openOrCreateDatabase(databaseName, null);	
			Log.d("createdb","open");
		} catch (SQLException e) {
			Log.e("create db:",e.getMessage());
			return false;
		}	
		return true;
	}
	
	public Reader createReader()
	{
		return new Reader();
	}
	
	public Writer createWriter()
	{
		return new Writer();
	}
	
	public class Reader
	{
		public boolean open()
		{
			if(!openDb())
				return false;
			if(!isParaTableExist())
				return false;
			if(!isRecordTableExist())
				return false;
			return true;
		}
		
		public RuntimeData read()
		{
			RuntimeData data=new RuntimeData();
			if(getParas(data)){
				if(getRecords(data)){
					return data;
				}
			}
			return null;
		}
		
		private boolean isParaTableExist()
		{
			Cursor cursor;
			try {
				cursor=mDatabase.rawQuery(SQL_IF_PARA_TABLE_EXIT, null);
				if(cursor.moveToNext()){
					int count=cursor.getInt(0);
					if(count>0)
						return true;
				}
			} catch (Exception e) {
				Log.e("if para table exit:", e.getMessage());
			}
			
			return false;
		}
		
		private boolean isRecordTableExist()
		{
			Cursor cursor;
			try {
				cursor=mDatabase.rawQuery(SQL_IF_RECORD_TABLE_EXIT, null);
				if(cursor.moveToNext()){
					int count=cursor.getInt(0);
					if(count>0)
						return true;
				}
			} catch (Exception e) {
				Log.e("if record table exit:", e.getMessage());
			}
			
			return false;
		}
		
		private boolean getParas(RuntimeData data)
		{
			if(data==null)
				return false;
			
			data.setStep(Float.valueOf(getParaValue(ParaEntry.PARA_STEP)));
			data.setRoadLength(Float.valueOf(getParaValue(ParaEntry.PARA_ROADL)));
			data.setRoadWidth(Float.valueOf(getParaValue(ParaEntry.PARA_ROADW)));
			data.setRollDiameter(Float.valueOf(getParaValue(ParaEntry.PARA_ROLLD)));
			data.setRollWidth(Float.valueOf(getParaValue(ParaEntry.PARA_ROLLW)));
			data.setOrigin(Integer.valueOf(getParaValue(ParaEntry.PARA_ORIGIN)));
			data.setHuoerNum(Integer.valueOf(getParaValue(ParaEntry.PARA_HUOERNUM)));
			
			data.setDeviceId(getParaValue(ParaEntry.PARA_DEVICE_ID));
			data.setVersion(getParaValue(ParaEntry.PARA_DEVICE_VERSION));
			data.setSoftVersion(getParaValue(ParaEntry.PARA_DEVICE_SOFT_VERSION));

			return true;
		}
		
		private String getParaValue(String paraTag)
		{
			Cursor cursor;
			try {
				cursor=mDatabase.rawQuery(SQL_SELECT_PARA_VALUE+"'"+paraTag+"'",null);
				cursor.moveToNext();
				return cursor.getString(0);
			} catch (Exception e) {
				Log.e("getPara", e.getMessage());
			}
			return "0";
		}
		
		private boolean getRecords(RuntimeData data)
		{
			Cursor cursor;
			try {
				cursor=mDatabase.rawQuery(SQL_SELECT_RECORD_ALL, null);
				Record record;
				for(int i=0;i<cursor.getCount();i++){
					if(cursor.moveToNext()){
						record=getRecord(cursor, i);
						data.addRecord(record);
					}
				}
			} catch (Exception e) {
				Log.e("getRecords", e.getMessage());
				return false;
			}
			return true;
		}
		
		private Record getRecord(Cursor cursor,int index)
		{
			Record record=new Record();
			record.setDistance(cursor.getFloat(1));
			record.setDirection(cursor.getFloat(2));
			record.setState(cursor.getInt(3));
			record.setSpeed(cursor.getFloat(4));
			record.setTemp(cursor.getFloat(5));
			record.setQuake(cursor.getFloat(6));
			
			PointF point=new PointF();
			point.x=cursor.getFloat(7);
			point.y=cursor.getFloat(8);
			record.setPosition(point);

			return record;
		}
		
	}
	
	public class Writer
	{		
		public boolean create()
		{
			if(!createDb()){
				return false;
			}
			if(!createParaTable()){
				return false;
			}
			if(!createRecordTable()){
				return false;
			}
			return true;
		}
		
		public boolean addDatas(RuntimeData data)
		{
			if(!addParas(data)){
				return false;
			}
			if(!addRecords(data)){
				return false;
			}
			return true;
		}
		
		public boolean addRecord(Record record)
		{
			ContentValues values=new ContentValues();
			values.put(RecordEntry.COLUMN_DIR, record.getDirection());
			values.put(RecordEntry.COLUMN_POINT_X, record.getPosition().x);
			values.put(RecordEntry.COLUMN_POINT_Y, record.getPosition().y);
			values.put(RecordEntry.COLUMN_SPEED, record.getSpeed());
			values.put(RecordEntry.COLUMN_STATE, record.getState());
			values.put(RecordEntry.COLUMN_DIST, record.getDistance());
			values.put(RecordEntry.COLUMN_QUAKE, record.getQuake());
			values.put(RecordEntry.COLUMN_TEMP, record.getTemp());

			try{
				mDatabase.insertOrThrow(RecordEntry.TABLE_NAME, null, values);
			}catch(SQLException e){
				Log.e("insert record:", e.getMessage());
				return false;
			}
			return true;
		}
		
		public boolean addParas(RuntimePara para)
		{
			setParaValue(ParaEntry.PARA_STEP, String.valueOf(para.getStep()));
			setParaValue(ParaEntry.PARA_ORIGIN, String.valueOf(para.getOrigin()));
			setParaValue(ParaEntry.PARA_ROADL, String.valueOf(para.getRoadLength()));
			setParaValue(ParaEntry.PARA_ROADW, String.valueOf(para.getRoadWidth()));
			setParaValue(ParaEntry.PARA_ROLLD, String.valueOf(para.getRollDiameter()));
			setParaValue(ParaEntry.PARA_ROLLW, String.valueOf(para.getRollWidth()));
			setParaValue(ParaEntry.PARA_HUOERNUM, String.valueOf(para.getHuoerNum()));
			
			setParaValue(ParaEntry.PARA_DEVICE_ID, para.getDeviceId());
			setParaValue(ParaEntry.PARA_DEVICE_VERSION, para.getVersion());
			setParaValue(ParaEntry.PARA_DEVICE_SOFT_VERSION, para.getSoftVersion());
			
			return true;
		}
		
		private boolean addRecords(RuntimeData data)
		{
			Record record;
			for(int i=0;i<data.size();i++){
				record=data.getRecord(i);
				if(!addRecord(record))
					return false;				
			}
			return true;
		}

		private boolean createParaTable()
		{
			try {
				mDatabase.execSQL(SQL_CREATE_PARA_TABLE);				
			} catch (SQLException e) {
				Log.e("create para table:",e.getMessage());
				return false;
			}	
			return true;		
		}
		
		private boolean createRecordTable()
		{
			try {
				mDatabase.execSQL(SQL_CREATE_RECORD_TABLE);				
			} catch (SQLException e) {
				Log.e("create record table:",e.getMessage());
				return false;
			}	
			return true;
		}
		
		private void setParaValue(String paraTag,String value)
		{
			ContentValues values=new ContentValues();
			values.put(ParaEntry.COLUMN_TAG, paraTag);
			values.put(ParaEntry.COLUMN_VALUE, value);
			try{
				mDatabase.insertOrThrow(ParaEntry.TABLE_NAME, null, values);
			}catch(SQLException e){
				Log.e("insert para:", e.getMessage());
			}
		}
	}

}
