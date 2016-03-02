package ylj.common.ui;


import com.ylj.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class SummaryItem
{
	private View view;
	private TextView titleView;
	private TextView summaryView;
	private Context context;
	
	public SummaryItem(Context context,String title,String summary)
	{
		view=LayoutInflater.from(context).inflate(R.layout.list_item_title_summary, null);
		
		titleView=(TextView)view.findViewById(R.id.text_title);
		summaryView=(TextView)view.findViewById(R.id.text_summary);
		titleView.setText(title);
		summaryView.setText(summary);	
		this.context=context;
	}

	public View getView()
	{			
		return view;
	}

	public TextView getTitleView()
	{
		return titleView;
	}

	public TextView getSummaryView()
	{
		return summaryView;
	}

	public Context getContext()
	{
		return context;
	}
	
}
