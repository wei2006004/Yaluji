package ylj.common.tool;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

import java.io.IOException;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.ylj.R;
import ylj.service.control.TestCtrl;
import ylj.common.bean.RuntimePara;
import ylj.common.tool.ftp.FTPToolkit;

public class FtpThread extends Thread implements FTPDataTransferListener
{
	public static final int CHANGE_PROGRESS=0;
	public static final int DISMISS_AND_SHOW_MESSAGE=1;
	public static final int SHOW_MESSAGE=2;
	
	public static final String TAG="FtpThread";
	public static final boolean D=true;
	
	String file;
	FTPClient client;
	Handler handler;
	Context context;
	
	String ftpText;
	int progressNum;
	
	public FtpThread(Context context,String file,Handler handler)
	{
		this.context=context;
		this.file=file;
		this.handler=handler;
	}
	
	@Override
	public void run()
	{			
		
		try {
			RuntimePara para=TestCtrl.instance().getRuntimePara(context);
			client=FTPToolkit.makeFtpConnection(
					para.getStationIp(), para.getPort(), para.getUser(), para.getPassword());	
			if(D)Log.d(TAG, "IP:"+para.getStationIp());
			if(D)Log.d(TAG, "Port:"+para.getPort());
		} catch (Exception e) {
			showText(context.getString(R.string.hisdata_ftp_connect_fail));
			Log.e(TAG, "error:"+e.getMessage());
			return;
		}
		
		try {
			FTPToolkit.upload(client, file, "/data/", this);
		} catch (Exception e) {
			showText(context.getString(R.string.hisdata_upload_fail));
		}
	}
	
	private void showText(String text)
	{
		ftpText=text;
		handler.sendEmptyMessage(SHOW_MESSAGE);
	}
	
	public void cancel()
	{
		if(client!=null){
			try {
				client.disconnect(true);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (FTPIllegalReplyException e) {
				e.printStackTrace();
			} catch (FTPException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void aborted()
	{
		handler.sendEmptyMessage(DISMISS_AND_SHOW_MESSAGE);
		this.cancel();
	}

	@Override
	public void completed()
	{
		ftpText=context.getString(R.string.hisdata_upload_down);
		handler.sendEmptyMessage(DISMISS_AND_SHOW_MESSAGE);
		this.cancel();
	}

	@Override
	public void failed()
	{
		handler.sendEmptyMessage(DISMISS_AND_SHOW_MESSAGE);
		this.cancel();
	}

	@Override
	public void started()
	{
	}

	@Override
	public void transferred(int arg0)
	{
		progressNum+=arg0;
		handler.sendEmptyMessage(CHANGE_PROGRESS);
	}

	public int getProgressNum()
	{
		return progressNum;
	}

	public String getFtpText()
	{
		return ftpText;
	}

}
