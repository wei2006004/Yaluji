package ylj.connect;

import ylj.connect.TcpService.IOListener;

import android.content.Context;
import android.os.Handler;

public class TcpCom  extends Com{
	public static final boolean CLIENT_MODE=true;
	
	public static final String TAG="TcpCom";
	public static final boolean D=true;
	
	private TcpService tcpService;
	
	public TcpCom(Context context, Handler infoHandler,Handler ctrlHandler) {
		super(context, infoHandler,ctrlHandler);
		tcpService=new TcpService(ctrlHandler);
		tcpService.setIOLister(new IOListener() {			
			@Override
			public void write(String msg) {
				
			}
			
			@Override
			public void read(String msg) {
				handleReadMessage(msg);
			}
		});
	}
	
	@Override
	public int getState()
	{
		return tcpService.getState();
	}
	
	@Override
	public void connect()
	{
		if(CLIENT_MODE){
			tcpService.connect();
		}else{
			tcpService.start();
		}		
	}
	
	@Override
	public void stop()
	{
		tcpService.stop();
	}
	
	@Override
	public void sendMessage(String msg)
	{
		if(tcpService.getState()==ComConst.STATE_CONNECTED)
			tcpService.write(msg);
	}
}
