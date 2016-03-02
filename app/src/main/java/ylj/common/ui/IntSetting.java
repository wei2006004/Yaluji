package ylj.common.ui;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.ylj.R;
import ylj.common.bean.SettingItem;
import ylj.common.tool.Tools;

public class IntSetting extends SummaryItem
{
	private final static String FAIL_SETTING_TEXT="设置失败";

	private SettingItem item;
	protected Builder mBuilder;	
	private AlertDialog mDialog;
	
	private int value;

	private EditText mEditText;
	private SeekBar mSeekBar;

	public IntSetting(SettingItem item,Context context)
	{
		super(context, item.getTitle(), item.getDefaultSummary());

		this.item=item;
		value=item.getIntDefault();
		SharedPreferences preferences=Tools.getAppPreferences(context);
		value=preferences.getInt(item.getPrefenceKey(), value);
		getSummaryView().setText(item.getSummary(value));
		
		mBuilder=new Builder(context);
		mBuilder.setTitle(item.getTitle());		

		mBuilder.setView(createDialogView());
		mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {		
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {				
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				int value=Integer.valueOf(mEditText.getText().toString());
				if(value<IntSetting.this.item.getIntMin() || value>IntSetting.this.item.getIntMax()){
					Toast.makeText(getContext().getApplicationContext(), FAIL_SETTING_TEXT, Toast.LENGTH_SHORT).show();
				}else{
					IntSetting.this.value=value;
					onOkButtonClick();
				}					
				dialog.dismiss();
			}
		});
		mDialog=mBuilder.create();
		
		getView().setOnClickListener(new OnClickListener() {				
			@Override
			public void onClick(View v)
			{
				mEditText.setText(String.valueOf(value));
				mSeekBar.setProgress(value);
				mDialog.show();
			}
		});
		
		
	}
	
	private View createDialogView()
	{
		View view = LayoutInflater.from(getContext()).inflate(
				R.layout.dialog_int_input, null);
		mEditText = (EditText) view.findViewById(R.id.edittext);
		mEditText.setText(String.valueOf(value));

		mSeekBar = (SeekBar) view.findViewById(R.id.seekbar);

		mSeekBar.setMax(item.getIntMax());
		mSeekBar.setProgress(value);

		mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar)
			{
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar)
			{
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser)
			{
				mEditText.setText(String.valueOf(progress));
			}
		});
		
		return view;
	}
	
	protected void onOkButtonClick()
	{
		getSummaryView().setText(item.getSummary(value));
		
		SharedPreferences preferences=Tools.getAppPreferences(getContext());
		preferences.edit().putInt(item.getPrefenceKey(), value).commit();
	}
}
