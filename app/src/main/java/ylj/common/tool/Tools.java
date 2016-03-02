package ylj.common.tool;

import android.content.Context;
import android.content.SharedPreferences;

import com.ylj.R;

public class Tools
{
	public static SharedPreferences getAppPreferences(Context context)
	{
		return context.getApplicationContext().
				getSharedPreferences(
						context.getString(R.string.pref_name),
						Context.MODE_PRIVATE);
	}
}
