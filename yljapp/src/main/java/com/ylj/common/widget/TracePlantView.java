package com.ylj.common.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

public class TracePlantView extends View
{	
	public final static int ORIGIN_CLOCKWISE=0;
	public final static int ORIGIN_ANTI_CLOCKWISE=1;
	
	private boolean mOrigin=false;
	
	private int mWidth;
	private int mHeight;
	
	static private final String METER_UNIT="米";
	
	static private final int PADDING_X=60;
	static private final int PADDING_Y=50;
	
	private int xPane;
	private int yPane;
	
	private double xRoad=100;
	private double yRoad=50;
	
	private double plantWidth=2;
	private double plantHeight=5;

	public static class PlantData
	{
		PointF point;
		double dir;
		int color;
		
		public PlantData(PointF pointF,double dir,int color)
		{
			set(pointF, dir, color);
		}
		
		public void set(PointF pointF,double dir,int color)
		{
			this.point=pointF;
			this.dir=dir;
			this.color=color;
		}			
	}
	
	private Point convertPoint(PointF pointF)
	{
		Point point = new Point();
		point.x = (int) (xPane * (pointF.x / xRoad) + PADDING_X);
		if(mOrigin){
			point.y = (int) (yPane * (pointF.y / yRoad) + PADDING_Y);
		}else {
			point.y = (int) (yPane * (1-pointF.y / yRoad) + PADDING_Y);
		}		
		return point;
	}
	
	public class DrawEdit
	{
		public DrawEdit setPlant(double width,double height)
		{
			plantWidth=width;
			plantHeight=height;
			return DrawEdit.this;
		}
		
		public DrawEdit setField(double x,double y)
		{
			xRoad=x;
			yRoad=y;
			return DrawEdit.this;
		}
		
		public DrawEdit setOrigin(int origin)
		{
			mOrigin=origin==ORIGIN_CLOCKWISE?true:false;
			return DrawEdit.this;
		}
		
		public DrawEdit addPlant(PointF pointF,double dir)
		{
			return addPlant(pointF, dir, Color.GREEN);
		}

		public DrawEdit addPlant(double x,double y,double dir,int color)
		{
			PlantData data=new PlantData(new PointF((float)x,(float)y),dir,color);
			plantDatas.add(data);
			return DrawEdit.this;
		}
		
		public DrawEdit addPlant(PointF pointF,double dir,int color)
		{	
			PlantData data=new PlantData(pointF,dir,color);
			plantDatas.add(data);
			return DrawEdit.this;
		}
		
		public DrawEdit addPlant(PlantData data)
		{
			plantDatas.add(data);
			return DrawEdit.this;
		}
		
		public DrawEdit clear()
		{
			plantDatas.clear();
			return DrawEdit.this;
		}
		
		public void commit()
		{
			invalidate();
		}
	}
	
	public PlantData createPlantData(PointF pointF,double dir)
	{
		return new PlantData(pointF, dir, Color.GREEN);
	}
	
	public PlantData createPlantData(PointF pointF,double dir,int color)
	{
		return new PlantData(pointF, dir, color);
	}
	
	private DrawEdit drawEdit;
	
	public DrawEdit getEdit()
	{
		if(drawEdit==null){
			drawEdit=new DrawEdit();
		}
		return drawEdit;
	}
	
	public TracePlantView(Context context)
	{
		super(context);
		initPaints();
	}

	public TracePlantView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initPaints();
	}

	public TracePlantView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		initPaints();
	}
	
	private Paint textPaint=new Paint();
	private Paint gridPaint=new Paint();
	private Paint rectPaint=new Paint();
	private Paint arrowPaint=new Paint();
	private Paint plantPaint=new Paint();
	private Paint plantLinePaint=new Paint();
	private Paint bitmapPaint=new Paint();
	
	private void initPaints()
	{
		rectPaint.setStrokeWidth(2);
		rectPaint.setStyle(Paint.Style.STROKE);
		
		arrowPaint.setStrokeWidth(2);
		arrowPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		arrowPaint.setColor(Color.BLACK);
		arrowPaint.setARGB(255, 0, 128, 200);
		
		gridPaint.setStrokeWidth(2);
		gridPaint.setStyle(Paint.Style.STROKE);
		gridPaint.setPathEffect(new DashPathEffect(
				new float[]{25,10,5,10}, 20));
		
		textPaint.setTextAlign(Align.CENTER);
		textPaint.setTextSize(18);
		textPaint.setAntiAlias(true);
		textPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		textPaint.setARGB(255, 0, 128, 200);
		
		plantPaint.setStyle(Paint.Style.FILL);
		plantPaint.setColor(Color.GREEN);
		
		plantLinePaint.setAntiAlias(true);
		plantLinePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		plantLinePaint.setStyle(Paint.Style.STROKE);
		plantLinePaint.setStrokeWidth(1);
		plantLinePaint.setColor(Color.BLACK);
	}
	
	
	private Canvas cacheCanvas=new Canvas();
	private Bitmap cacheBitmap;
	
	@Override
	public void onSizeChanged(int w,int h,int oldw,int oldh)
	{
		mWidth=w;
		mHeight=h;
		xPane=mWidth-PADDING_X-PADDING_X;
		yPane=mHeight-PADDING_Y-PADDING_Y;
		
		int xPlant=(int)((xPane/xRoad)*plantHeight/2);
		int yPlant=(int)((yPane/yRoad)*plantWidth/2);
		plantRect.set(-xPlant,-yPlant,xPlant,yPlant);
		
		cacheBitmap=Bitmap.createBitmap(w, h, Config.ARGB_8888);
		cacheCanvas.setBitmap(cacheBitmap);
	}
	
	@Override
	public void onDraw(Canvas canvas)
	{		
		cacheBitmap.eraseColor(Color.TRANSPARENT);
		paintBitmap();
		canvas.drawBitmap(cacheBitmap, 0, 0, bitmapPaint);
	}

	private void paintBitmap()
	{
		paintBackground(cacheCanvas);
		paintPlants(cacheCanvas);
	}
	
	private void paintBackground(Canvas canvas)
	{
		canvas.drawRect(new Rect(1,1,mWidth-1,mHeight-1), rectPaint);
		
		paintGrid(canvas);
		paintRoadInfo(canvas);
	}
	
	private void paintGrid(Canvas canvas)
	{
		drawGridLine(canvas,PADDING_X, 0, PADDING_X, mHeight);
		drawGridLine(canvas,mWidth-PADDING_X, 0, mWidth-PADDING_X, mHeight);
		drawGridLine(canvas,0, PADDING_Y, mWidth, PADDING_Y);
		drawGridLine(canvas,0, mHeight-PADDING_Y, mWidth, mHeight-PADDING_Y);
	}
	
	private Path gridPath=new Path();
	private void drawGridLine(Canvas canvas,int x1,int y1,int x2,int y2)
	{
		gridPath.reset();
		gridPath.moveTo(x1, y1);
		gridPath.lineTo(x2, y2);
		canvas.drawPath(gridPath, gridPaint);
	}
	
	private void paintRoadInfo(Canvas canvas)
	{		
		canvas.drawLine(PADDING_X-20, PADDING_Y, PADDING_X-20, mHeight-PADDING_Y, arrowPaint);		
		paintArrow(PADDING_X-20, PADDING_Y, 90, canvas);
		paintArrow(PADDING_X-20, mHeight-PADDING_Y,-90, canvas);
		paintText(String.valueOf(yRoad)+METER_UNIT, PADDING_X-25,mHeight/2, 270, canvas);
				
		canvas.drawLine(PADDING_X, PADDING_Y-20, mWidth-PADDING_X, PADDING_Y-20, arrowPaint);		
		paintArrow(PADDING_X, PADDING_Y-20, 0, canvas);
		paintArrow(mWidth-PADDING_X, PADDING_Y-20,180, canvas);
		paintText(String.valueOf(xRoad)+METER_UNIT, mWidth/2, PADDING_Y-25, 0, canvas);
	}
	
	private static Path arrowPath=new Path();
	private void paintArrow(int originX,int originY,double rotate,Canvas canvas)
	{
		if(arrowPath.isEmpty()){
			arrowPath.moveTo(0, 0);
			arrowPath.lineTo(15, -8);
			arrowPath.lineTo(15, 8);
			arrowPath.close();
		}
		canvas.translate(originX, originY);
		canvas.rotate((float)rotate);
		canvas.drawPath(arrowPath, arrowPaint);
		canvas.rotate((float)-rotate);
		canvas.translate(-originX, -originY);		
	}
	
	private void paintText(String text,int originX,int originY,double rotate,Canvas canvas)
	{
		canvas.translate(originX, originY);
		canvas.rotate((float)rotate);
		canvas.drawText(text, 0,0, textPaint);
		canvas.rotate((float)-rotate);
		canvas.translate(-originX, -originY);
	}

	private ArrayList<PlantData> plantDatas=new ArrayList<PlantData>();
	
	private void paintPlants(Canvas canvas)
	{
		for (PlantData data : plantDatas) {
			paintPlant(convertPoint(data.point), data.dir, data.color, canvas);
		}
	}
	
	private Rect plantRect=new Rect();
	
	private void paintPlant(Point point,double dir,int color,Canvas canvas)
	{
		canvas.translate(point.x, point.y);
		canvas.rotate((float)dir-90);
		plantPaint.setColor(color);
		canvas.drawRect(plantRect, plantPaint);
		canvas.drawRect(plantRect, plantLinePaint);
		canvas.rotate(90-(float)dir);
		canvas.translate(-point.x, -point.y);
	}

}
