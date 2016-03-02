package ylj.connect;

import ylj.connect.ComConst;

import android.os.Handler;
import android.os.Message;

public class ConnectStateHandler extends Handler {
	@Override
    public void handleMessage(Message msg) {
		if(onStateChangedListener!=null)
			onStateChangedListener.onStateChanged(msg.what);
		if(onConnectLostListener!=null && msg.what ==ComConst.STATE_CONNECT_LOST)
			onConnectLostListener.onConnectLost();
	}
	
	private OnStateChangedListener onStateChangedListener;
	
	public void setOnStateChangedListener(OnStateChangedListener listener){
		onStateChangedListener=listener;
	}
	
	public interface OnStateChangedListener
	{
		public void onStateChanged(int state);
	}
	
	private OnConnectLostListener onConnectLostListener;
	
	public void setOnConnectLostListener(OnConnectLostListener listener){
		onConnectLostListener=listener;
	}
	
	public interface OnConnectLostListener
	{
		public void onConnectLost();
	}
}
