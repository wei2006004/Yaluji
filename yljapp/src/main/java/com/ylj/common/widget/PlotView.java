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
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

public class PlotView extends View
{
	private float mXmin = 0;
	private float mXmax = 50;
	private float mYmin = 0;
	private float mYmax = 30;

	private boolean mGridEnable = true;
	private int mRow = 5;
	private int mColumn = 4;

	private final int originX = 60;
	private final int originY = 40;
	private final int paddingX = 30;
	private final int paddingY = 20;
	
	private int mWidth;
	private int mHeight;
	
	private int wpane;
	private int hpane;
	
	public class DrawEdit
	{		
		public DrawEdit setXAxes(float xmin,float xmax)
		{
			if(xmax<=xmin)
				return DrawEdit.this;
			mXmax=xmax;
			mXmin=xmin;
			return DrawEdit.this;
		}
		
		public DrawEdit setYAxes(float ymin,float ymax)
		{
			if(ymax<=ymin)
				return DrawEdit.this;
			mYmax=ymax;
			mYmin=ymin;
			return DrawEdit.this;
		}
		
		public DrawEdit setGridEnable(boolean enable)
		{
			mGridEnable=enable;
			return DrawEdit.this;
		}
		
		public DrawEdit setGrid(int row,int column)
		{
			mRow=row;
			mColumn=column;
			return DrawEdit.this;
		}
		
		public DrawEdit clear()
		{
			//mPath.reset();
			mPath.clear();
			drawPoints.clear();
			return DrawEdit.this;
		}
		
		public DrawEdit addPoint(PointF pointF)
		{
			if(!isPointAvailable(pointF))
				return DrawEdit.this;
			
			Point point=convertToAxesPoint(pointF);				
//			if(mPath.isEmpty()){
//				mPath.moveTo(point.x, point.y);
//				return DrawEdit.this;
//			}else{
//				mPath.lineTo(point.x, point.y);
//			}
			mPath.add(point);
			
			return DrawEdit.this;
		}
		
		public DrawEdit addDrawPoint(PointF pointF)
		{
			if(!isPointAvailable(pointF))
				return DrawEdit.this;			
			Point point=convertToAxesPoint(pointF);	
			drawPoints.add(point);
			return DrawEdit.this;
		}
		
		public void commit()
		{
			PlotView.this.invalidate();
		}
	}
	
	public DrawEdit getEdit()
	{
		return new DrawEdit();
	}
	
	private ArrayList<Point> drawPoints=new ArrayList<Point>();
	
	public PlotView(Context context)
	{
		super(context);	
		init();	
	}

	public PlotView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	public PlotView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		init();
	}
	
	private Paint linePaint=new Paint();
	private Paint pointPaint=new Paint();
	private Paint gridPaint=new Paint();
	private Paint textPaint=new Paint();
	private Paint rectPaint=new Paint();
	private Paint bitmapPaint=new Paint();
	
	private Canvas cacheCanvas=new Canvas();
	private Bitmap cacheBitmap;
	
	private void init()
	{		
		linePaint.setStrokeWidth(2);
		linePaint.setStyle(Paint.Style.STROKE);
		linePaint.setAntiAlias(true);
		linePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		linePaint.setPathEffect(null);
		linePaint.setARGB(255, 0, 128, 200);
		
		pointPaint.setStrokeWidth(5);
		pointPaint.setStyle(Paint.Style.STROKE);
		
		rectPaint.setStrokeWidth(2);
		rectPaint.setStyle(Paint.Style.STROKE);
		
		gridPaint.setStrokeWidth(2);
		gridPaint.setStyle(Paint.Style.STROKE);
		gridPaint.setPathEffect(new DashPathEffect(
				new float[]{25,10,5,10}, 20));
		
		textPaint.setTextAlign(Align.CENTER);
		textPaint.setTextSize(18);
		textPaint.setAntiAlias(true);
		textPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
	}

	//private Path mPath=new Path();
	private ArrayList<Point> mPath=new ArrayList<Point>();
	
	private Point convertToAxesPoint(final PointF pointF)
	{		
		Point point=new Point();
		point.x=(int)((pointF.x-mXmin)/(mXmax-mXmin)*wpane);
		point.y=(int)((pointF.y-mYmin)/(mYmax-mYmin)*hpane);
		
		point.y=hpane-point.y;
		return point;
	}
	
	private boolean isPointAvailable(final PointF pointF)
	{
		if(pointF.x<=mXmax && pointF.x>=mXmin){
			if(pointF.y<=mYmax && pointF.y>=mYmin){
				return true;
			}
		}
		return false;
	}	
	
	@Override
	public void onSizeChanged(int w,int h,int oldw,int oldh)
	{
		mWidth=w;
		mHeight=h;
		wpane=mWidth-originX-paddingX;
		hpane=mHeight-originY-paddingY;
		
		cacheBitmap=Bitmap.createBitmap(w, h, Config.ARGB_8888);
		cacheCanvas.setBitmap(cacheBitmap);
	}
	
	@Override
	public synchronized void onDraw(Canvas canvas)
	{
		cacheBitmap.eraseColor(Color.TRANSPARENT);
		paintBitmap();		
		canvas.drawBitmap(cacheBitmap, 0, 0, bitmapPaint);
	}
	
	private void paintBitmap()
	{
		paintBackground(cacheCanvas);
		paintLine(cacheCanvas);
		paintPoints(cacheCanvas);
	}
	
	private void paintPoints(Canvas canvas)
	{
		if(drawPoints.isEmpty())
			return;
		for (Point point : drawPoints) {
			canvas.drawPoint(point.x, point.y, pointPaint);
		}
	}
	
	private void paintBackground(Canvas canvas)
	{
		canvas.drawRect(originX, paddingY, mWidth-paddingX, mHeight-originY, rectPaint);
		paintAxes(canvas);
		if(mGridEnable)
			paintGrid(canvas);
	}
	
	private void paintAxes(Canvas canvas)
	{
		final float textPadding=30;
		
		float wstep=(mWidth-originX-paddingX)/mColumn;
		float hstep=(mHeight-originY-paddingY)/mRow;	
		float width=originX;
		float height=paddingY+10;
		
		float xstep=(mXmax-mXmin)/mColumn;
		float ystep=(mYmax-mYmin)/mRow;
		float xvalue=mXmin;
		float yvalue=mYmax;
		
		canvas.drawText(String.valueOf(mXmin), width, mHeight-originY+textPadding, textPaint);
		for(int i=0;i<mColumn-1;i++){
			width+=wstep;
			xvalue+=xstep;
			canvas.drawText(String.valueOf(xvalue), width, mHeight-originY+textPadding, textPaint);
		}
		canvas.drawText(String.valueOf(mXmax), width+wstep, mHeight-originY+textPadding, textPaint);
		
		canvas.drawText(String.valueOf(mYmax), originX-textPadding, height, textPaint);
		for(int i=0;i<mRow-1;i++){
			height+=hstep;
			yvalue-=ystep;
			canvas.drawText(String.valueOf(yvalue), originX-textPadding, height, textPaint);
		}
		canvas.drawText(String.valueOf(mYmin), originX-textPadding, height+hstep, textPaint);
	}
	
	private Path gridPath=new Path();
	private void paintGrid(Canvas canvas)
	{
		gridPath.reset();
		float wstep=(mWidth-originX-paddingX)/mColumn;
		float hstep=(mHeight-originY-paddingY)/mRow;		
		float width=originX;
		float height=paddingY;
		for(int i=0;i<mColumn-1;i++){
			width+=wstep;
			gridPath.moveTo(width, paddingY);
			gridPath.lineTo(width,mHeight-originY);
			canvas.drawPath(gridPath, gridPaint);
			gridPath.reset();
		}
		for(int i=0;i<mRow-1;i++){
			height+=hstep;
			gridPath.moveTo(originX, height);
			gridPath.lineTo( mWidth-paddingX, height);
			canvas.drawPath(gridPath, gridPaint);
			gridPath.reset();
		}
	}
	
	private void paintLine(Canvas canvas)
	{		
		if(mPath.isEmpty())
			return;
		canvas.translate(originX, paddingY);
		//canvas.drawPath(mPath, linePaint);
		Point point1;
		Point point2=mPath.get(0);
		for(int i=1;i<mPath.size();i++){
			point1=point2;
			point2=mPath.get(i);
			canvas.drawLine(point1.x, point1.y, point2.x, point2.y, linePaint);
		}
		canvas.translate(-originX, -paddingY);
	}
}
