package ylj.hisdata;

import java.io.File;
import java.util.ArrayList;

import com.ylj.R;

import ylj.common.bean.HisdataItem;
import ylj.common.tool.FtpThread;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class HisDataFragment extends Fragment
{
	public static final String TAG="HisDataFragment";
	public static final boolean D=true;
	
//	public static final String TEST_FILE_NAME="test_file";
	
	private ListView listView;
	private HisdataAdapter hisdataAdapter;
	private ArrayList<HisdataItem> hisdataItems=new ArrayList<HisdataItem>();
	
	protected Builder mBuilder;	
	private AlertDialog mDialog;
	private ProgressDialog progressDialog;
	
	private int selectPos=0;
	
	private FtpThread thread;
	
	private static final int CHANGE_PROGRESS=0;
	private static final int DISMISS_AND_SHOW_MESSAGE=1;
	private static final int SHOW_MESSAGE=2;
	
	private Handler ftpHandler;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View view=inflater.inflate(R.layout.fragment_hisdata, null);
		listView=(ListView)view.findViewById(R.id.listview);
		refreshItems();
		if(hisdataAdapter==null)
			hisdataAdapter=new HisdataAdapter(hisdataItems,getActivity());
		
		mBuilder=new Builder(getActivity());
		mBuilder.setTitle(R.string.hisdata_title_device);
		String[] list=getResources().getStringArray(R.array.hisdata_list);
		mBuilder.setItems(list, new DialogInterface.OnClickListener() {				
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				onDialogItemClick(which);
				dialog.dismiss();
			}
		});
		mDialog=mBuilder.create();
		
		if(progressDialog==null){
			progressDialog=new ProgressDialog(getActivity());
			progressDialog.setTitle(R.string.hisdata_progress_title);
			progressDialog.setMax(100);
			progressDialog.setProgress(0);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressDialog.setIndeterminate(false);
		}
		
		listView.setAdapter(hisdataAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				selectPos=position;
				mDialog.show();
			}
		});
		
		ftpHandler=new Handler(){
			@Override
			public void handleMessage(Message msg)
			{
				if(msg.what==CHANGE_PROGRESS){
					progressDialog.setProgress(thread.getProgressNum());
				}else if(msg.what==DISMISS_AND_SHOW_MESSAGE){
					progressDialog.dismiss();
					if(thread.getFtpText()!=null){
						Toast.makeText(getActivity(), thread.getFtpText(), Toast.LENGTH_LONG).show();
					}
				}else if(msg.what==SHOW_MESSAGE){
					showProgressDialogText(thread.getFtpText());
				}
			}
		};
		
		return view;
	}
	
	private void showText(String text)
	{
		Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
	}
	
	private void onDialogItemClick(int which)
	{
		String fileString=hisdataItems.get(selectPos).getFileName();
		String path=getActivity().getApplicationContext().getFilesDir().getAbsolutePath()+"/";
		switch (which) {
		case 0:			
			Intent intent=new Intent(getActivity(),HisdataActivity.class);
//			Bundle data=new Bundle();
//			data.putString(TEST_FILE_NAME, fileString);
//			intent.putExtras(data);
			HisdataActivity.setFileName(fileString);
			getActivity().startActivity(intent);
			getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			break;
		case 1:			
			if(D)Log.d(TAG, "delete:"+path+fileString);
			try {
				new File(path+fileString).delete();
				new File(path+fileString+"-journal").delete();
				hisdataItems.remove(selectPos);
				hisdataAdapter.notifyDataSetChanged();
				showText(getString(R.string.hisdata_delete)+fileString);
			} catch (Exception e) {
				Log.d(TAG, "error:"+e.getMessage());
			}			
			
			break;
		case 2:
			if(isNetworkConnected(getActivity())){
				upload(path+fileString);
			}else{
				Toast.makeText(getActivity(), getString(R.string.com_no_connect_promt), Toast.LENGTH_LONG).show();
			}		
			break;
		default:
			break;
		}
	}
	
	public boolean isNetworkConnected(Context context) {
		if (context != null) {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context
		.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
		if (mNetworkInfo != null) {
		return mNetworkInfo.isAvailable();
		}
		}
		return false;
	}
	
	private void showProgressDialogText(String text)
	{
		progressDialog.setMessage(text);
	}
	
	private void upload(String file)
	{
		if(thread!=null){			
			thread.cancel();
			thread=null;
		}
		progressDialog.show();
		showProgressDialogText(getString(R.string.hisdata_ftp_connect));		
		
		thread=new FtpThread(getActivity(),file,ftpHandler);
		thread.start();
	}
	
	public void refresh()
	{
		refreshItems();
		hisdataAdapter.notifyDataSetChanged();
	}
	
	private void refreshItems()
	{
		hisdataItems.clear();
		
		String path=getActivity().getApplicationContext().getFilesDir().getAbsolutePath();
		File[] files=new File(path).listFiles();
		
		HisdataItem item;
		for(int i=0;i<files.length;i++){
			//if(D)Log.d(TAG, files[i].getName());
			if(!files[i].getName().contains(".db-journal")){
				item=HisdataItem.fromFileName(files[i].getName());
				//if(D)Log.d(TAG, files[i].getName());
				hisdataItems.add(item);
			}
		}
	}
	



}
