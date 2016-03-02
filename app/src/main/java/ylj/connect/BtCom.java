package ylj.connect;

import ylj.connect.BluetoothChatService.IOListener;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Handler;

public class BtCom extends Com
{
	public static final String TAG="BtCom";
	public static final boolean D=true;
	
	private BluetoothChatService chatService;
	private BluetoothDevice device;
	
	public BtCom(Context context,Handler infoHandler,Handler ctrlHandler)
	{
		super(context, infoHandler,ctrlHandler);
		chatService=new BluetoothChatService(context, ctrlHandler);
		chatService.setIOLister(new IOListener() {			
			@Override
			public void write(String msg)
			{				
			}
			
			@Override
			public void read(String msg)
			{
				handleReadMessage(msg);
			}
		});
	}
	
	@Override
	public int getState()
	{
		return chatService.getState();
	}
	
	public void setBluetoothDevice(BluetoothDevice device)
	{
		this.device=device;
	}
	
	@Override
	public void connect()
	{
		if(device==null)
			return;
		chatService.connect(device, true);
	}
	
	@Override
	public void stop()
	{
		chatService.stop();
	}
	
	@Override
	public void sendMessage(String msg)
	{
		if(chatService.getState()!=ComConst.STATE_CONNECTED)
			return;
		chatService.write(msg.getBytes());
	}
}
