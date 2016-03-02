package ylj.sample;

import ylj.common.widget.ColorView;
import ylj.common.widget.ColorView.DrawEdit;
import ylj.common.tool.fullscreen.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.ylj.R;

public class ColorActivity extends Activity {

	private static final boolean AUTO_HIDE = true;
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
	private static final boolean TOGGLE_ON_CLICK = true;
	
//	private static final int COLOR_VIEW_ROW=12;
//	private static final int COLOR_VIEW_COLUMN=20;

	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	private SystemUiHider mSystemUiHider;
	
	private ColorView colorView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_color);
		colorView=(ColorView)findViewById(R.id.colorview);
		
		mSystemUiHider = SystemUiHider.getInstance(this, colorView,
				HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider
				.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {

					@Override
					@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
					public void onVisibilityChange(boolean visible)
					{
						if (visible && AUTO_HIDE) {
							delayedHide(AUTO_HIDE_DELAY_MILLIS);
						}
					}
				});
		
		colorView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view)
			{
				if (TOGGLE_ON_CLICK) {
					mSystemUiHider.toggle();
				} else {
					mSystemUiHider.show();
				}
			}
		});
		ColorCalculator colorData=ColorCalculator.instance();
		DrawEdit edit=colorView.getEdit();
		edit.setOrigin(true, colorData.getOrigin());
		edit.setGrid(colorData.getRowNum(), colorData.getColumnNum());
		edit.commint();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		new Handler().postDelayed(new Runnable() {			
			@Override
			public void run() {
				ColorCalculator colorData=ColorCalculator.instance();
//				colorData.setGrid(COLOR_VIEW_ROW, COLOR_VIEW_COLUMN);
//				colorData.commit();
				
				if(colorData.isCounted()){
					DrawEdit edit=colorView.getEdit();
					edit.setOrigin(true, colorData.getOrigin());
					edit.setGrid(colorData.getRowNum(), colorData.getColumnNum());
					edit.clear();
					for(int i=0;i<colorData.getRowNum();i++){
						for(int j=0;j<colorData.getColumnNum();j++){
							edit.setColor(i, j, colorData.getColor(i, j));
						}
					}
					edit.commint();
				}
			}
		}, 800);

	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);

		delayedHide(100);
	}

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run()
		{
			mSystemUiHider.hide();
		}
	};

	private void delayedHide(int delayMillis)
	{
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.color, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}
}
