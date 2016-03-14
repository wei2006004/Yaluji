package com.ylj.common.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class ColorView extends View
{
	private int mRow=7;
	private int mColumn=10;
	private int[][] colors;
	
	private boolean xOrigin=true;
	private boolean yOrigin=false;
	
	public class DrawEdit
	{
		public DrawEdit setOrigin(boolean xorigin, boolean yorigin)
		{
			xOrigin = xorigin;
			yOrigin = yorigin;
			return DrawEdit.this;
		}

		public DrawEdit setGrid(int row, int column)
		{
			mRow = row;
			mColumn = column;
			colors = new int[row][column];
			for (int i = 0; i < row; i++) {
				for (int j = 0; j < column; j++) {
					colors[i][j] = Color.WHITE;
				}
			}
			return DrawEdit.this;
		}

		public DrawEdit setColor(int row, int column, int color)
		{
			if (row >= mRow || row < 0)
				return DrawEdit.this;
			if (column >= mColumn || column < 0)
				return DrawEdit.this;
			colors[row][column] = color;
			return DrawEdit.this;
		}

		public DrawEdit clear()
		{
			for (int i = 0; i < mRow; i++) {
				for (int j = 0; j < mColumn; j++) {
					colors[i][j] = Color.WHITE;
				}
			}
			return DrawEdit.this;
		}
		
		public void commint()
		{
			invalidate();
		}
	}
	
	public DrawEdit getEdit()
	{
		return new DrawEdit();
	}
	
	private Paint colorPaint=new Paint();
	private Paint linePaint=new Paint();
	private Paint bmpPaint=new Paint();
	
	private Canvas cacheCanvas=new Canvas();
	private Bitmap cacheBitmap;
	
	private void init()
	{				
		colors=new int[mRow][mColumn];
		for(int i=0;i<mRow;i++){
			for(int j=0;j<mColumn;j++){
				colors[i][j]=Color.WHITE;
			}
		}
		
		linePaint.setStrokeWidth(2);
		linePaint.setStyle(Paint.Style.STROKE);
		
		colorPaint.setStyle(Paint.Style.FILL);
		colorPaint.setColor(Color.WHITE);
	}
	
	@Override
	public void onDraw(Canvas canvas)
	{
		paintBitmap();
		canvas.drawBitmap(cacheBitmap,0,0, bmpPaint);
	}
	
	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		cacheBitmap=Bitmap.createBitmap(w, h, Config.ARGB_8888);
		cacheCanvas.setBitmap(cacheBitmap);
	}
	
	private void paintBitmap()
	{		
		cacheBitmap.eraseColor(Color.TRANSPARENT);
		paintColors(cacheCanvas);
		paintLines(cacheCanvas);
	}
	
	private void paintColors(Canvas canvas)
	{
		float vwidth=getWidth();
	    float vheight=getHeight();
	    float wstep=vwidth/mColumn;
	    float hstep=vheight/mRow;
	    int rowIndex,columnIndex;
	    for(int i=0;i<mRow;i++){
	        rowIndex=i;
	        if(yOrigin){
	            rowIndex=mRow-i-1;
	        }
	        for(int j=0;j<mColumn;j++){
	            columnIndex=j;
	            if(!xOrigin){
	                columnIndex=mColumn-j-1;
	            }
	            colorPaint.setColor(colors[rowIndex][columnIndex]);
	            canvas.drawRect(wstep*j,hstep*i,wstep*(j+1),hstep*(i+1),colorPaint);
	        }
	    }
	}
	
	private void paintLines(Canvas canvas)
	{
		float vwidth=getWidth();
	    float vheight=getHeight();
	    canvas.drawRect(1,1,vwidth-1,vheight-1,linePaint);
	    float wstep=vwidth/mColumn;
	    float hstep=vheight/mRow;
	    for(int i=1;i<mColumn;i++){
	        canvas.drawLine(wstep*i,0,wstep*i,vheight,linePaint);
	    }
	    for(int i=1;i<mRow;i++){
	        canvas.drawLine(0,hstep*i,vwidth,hstep*i,linePaint);
	    }
	}
	
	public ColorView(Context context)
	{
		super(context);
		init();
	}

	public ColorView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	public ColorView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		init();
	}

}
