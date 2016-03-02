package ylj.common.ui;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import ylj.common.bean.SettingItem;
import ylj.common.tool.Tools;

public class TextSetting extends SummaryItem
{
	private Builder mBuilder;	
	private AlertDialog mDialog;
	private SettingItem item;
	private EditText mTextView;
	protected String mText;
	
	public TextSetting(SettingItem item,Context context)
	{
		super(context, item.getTitle(), item.getDefaultSummary());
		this.item =item;
		mText=item.getTextDefault();
		SharedPreferences preferences=Tools.getAppPreferences(context);
		mText=preferences.getString(item.getPrefenceKey(), mText);
		getSummaryView().setText(mText);
		
		mTextView=new EditText(context);
		mTextView.setText(mText);
		
		mBuilder=new Builder(context);
		mBuilder.setTitle(item.getTitle());
		mBuilder.setView(mTextView);
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
				mText=mTextView.getText().toString();
				onOkButtonClick();
				dialog.dismiss();
			}
		});
		mDialog=mBuilder.create();
		
		getView().setOnClickListener(new OnClickListener() {				
			@Override
			public void onClick(View v)
			{
				mDialog.show();
			}
		});
		
		
	}
	
	protected void onOkButtonClick()
	{
		getSummaryView().setText(mText);
		
		SharedPreferences preferences=Tools.getAppPreferences(getContext());
		preferences.edit().putString(item.getPrefenceKey(), mText).commit();
	}	

}
