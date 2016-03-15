package com.ylj.daemon.connect;

import android.os.Handler;
import android.util.Log;

import com.ylj.daemon.config.ConnectState;

import org.xutils.x;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpService {
	public static final boolean D=true;
	public static final String TAG="TcpService";
	
	public static void debug(String msg){
		Log.d(TAG, msg);
	}
	
	public static void error(String msg){
		Log.e(TAG, msg);
	}
	
	public static final boolean iS_DEVICE=true;
	
    private int state= ConnectState.STATE_NONE;
    
    private AcceptThread acceptThread=null;
    private ConnectThread connectThread=null;

	IConnector.OnStateChangeListener mOnStateChangeListener;

	public void setOnStateChangeListener(IConnector.OnStateChangeListener listener) {
		mOnStateChangeListener = listener;
	}
    
	public TcpService()
	{
	}
	
	public synchronized void start()
	{
		if(D)debug("start");
		if(acceptThread==null){
			acceptThread=new AcceptThread(port);
		}
		acceptThread.start();
	}

	public static int port=12345;
	public static String ip="192.168.0.99";
	public static String computerIp="192.168.1.109";
	
	private String mIp = "192.168.0.99";
	private int mPort = 12345;
	
	public void setAddress(String ip,int port){
		mIp=ip;
		mPort=port;
	}
	
	public synchronized void connect()
	{
		if(D)debug("start");
		if(connectThread==null){
			connectThread=new  ConnectThread(mIp,mPort);
		}
		connectThread.start();
	}
	
	public void write(String msg)
	{
//		AcceptThread r;
//		synchronized (this) {
//			if(state!=ConnectState.STATE_CONNECTED)return;
//			r=acceptThread;
//		}
		if(acceptThread!=null)
			acceptThread.write(msg);
		if(connectThread!=null)
			connectThread.write(msg.getBytes());
	}
	
	public int getState()
	{
		return state;
	}
	
	private void setState(final int state){
		this.state=state;
		
		if(mOnStateChangeListener == null)
			return;
		x.task().autoPost(new Runnable() {
			@Override
			public void run() {
				mOnStateChangeListener.onStateChange(state);
			}
		});
	}
	
	public void stop()
	{
		if(D)debug("stop");
		if(acceptThread!=null){
			acceptThread.cancel();
			acceptThread=null;
		}
		if(connectThread!=null){
			connectThread.cancel();
			connectThread=null;
		}
	}
	
	private class ConnectThread extends Thread
	{
		private Socket mSocket;
//		private BufferedReader reader;
//		private PrintWriter writer;
        private  InputStream mmInStream;
        private  OutputStream mmOutStream;
		private String host;
		private int port;
		
		public ConnectThread(String host,int port){
			this.host=host;
			this.port=port;
		}
		
		@Override
		public void run()
		{
			try{
				mSocket=new Socket(host,port);
				if(D)debug("connect");
				setState(ConnectState.STATE_CONNECTING);
			}catch(IOException e){
				if(D)debug("connect to server fail!");
				setState(ConnectState.STATE_CONNECT_FAIL);				
				return;
			}
			
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = mSocket.getInputStream();
                tmpOut = mSocket.getOutputStream();
            } catch (IOException e) {
            	if(D)debug("connect to server fail!");
				setState(ConnectState.STATE_CONNECT_FAIL);				
				return;
            }
            setState(ConnectState.STATE_CONNECTED);
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
            
            byte[] buffer = new byte[1024];
            int bytes;

            // Keep listening to the InputStream while connected
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    String msg=new String(buffer).substring(0,bytes);
                    if(D)debug("read:"+msg);
                    if(listener!=null){
                    	listener.read(msg);
                    }
                } catch (IOException e) {
                	debug("connect is lost");
    				setState(ConnectState.STATE_CONNECT_LOST);
                    break;
                }
            }

			try {
				if (mSocket != null)
					mSocket.close();
			} catch (Exception e) {
				if (D)
					error("cannot close unwanted socket!");
			}

		}
		
        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);
                if(D)Log.d(TAG, "write byte:"+buffer);
                if(listener!=null){
                	listener.write(new String(buffer));
                }
            } catch (IOException e) {
            	error("exception incur in writing!");
            }
        }
		
		public void cancel()
		{
			try {
				if(mSocket!=null)mSocket.close();
				if(D)debug("server socket closed");
			} catch (Exception e) {
				if(D)error("server socket close fail!");
			}
			
		}
	}
	
	private class AcceptThread extends Thread
	{
		private ServerSocket serverSocket=null;
		
		private Socket mSocket;
		private BufferedReader reader;
		private PrintWriter writer;
		
		public AcceptThread(int port) {
			try {				
				serverSocket=new ServerSocket(port);
				if(D)debug("server start!");	
				setState(ConnectState.STATE_LISTEN);
			} catch (IOException e) {
				if(D)debug("start server socket fail!");
				setState(ConnectState.STATE_CONNECT_FAIL);
			}
		}
		
		@Override
		public void run()
		{
			mSocket=null;
			while (state != ConnectState.STATE_CONNECTED) {
				try {
					mSocket = serverSocket.accept();
					if (D)
						debug("connect:" + mSocket.getInetAddress() + ":"
								+ mSocket.getPort());
					setState(ConnectState.STATE_CONNECTING);
				} catch (IOException e) {
					if (D)
						error("seversocket accept fail!");
					setState(ConnectState.STATE_CONNECT_FAIL);
					break;
				}
				if(mSocket!=null){
					synchronized (TcpService.this) {
						switch (state) {
						case ConnectState.STATE_LISTEN:
						case ConnectState.STATE_CONNECTING:							
							String msg;
							try {
								reader=new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
								writer=new PrintWriter(mSocket.getOutputStream(),true);
								setState(ConnectState.STATE_CONNECTED);
								while((msg=reader.readLine())!=null){
									if(listener!=null)
										listener.read(msg);
									if(D)debug(msg);
								}
							} catch (IOException e) {
								debug("connect is lost");
								setState(ConnectState.STATE_CONNECT_LOST);
							}
							break;
						case ConnectState.STATE_CONNECTED:
						case ConnectState.STATE_NONE:
							try {
								mSocket.close();
							} catch (Exception e) {
								if(D)error("cannot close unwanted socket!");
							}
							break;
						default:
							break;
						}
					}
				}
			}
		}
		
		public void write(String msg)
		{		
			try {
				writer.write(msg);
				writer.flush();
				if(D)debug("write:"+msg);
				if(listener!=null)
					listener.write(msg);
			} catch (Exception e) {			
				error("exception incur in writing!");
			}
		}
		
		public void cancel()
		{
			try {
				if(mSocket!=null)mSocket.close();
				if(serverSocket!=null)
					serverSocket.close();
				if(D)debug("server socket closed");
			} catch (Exception e) {
				if(D)error("server socket close fail!");
			}
			
		}
	}
	
    public interface IOListener
    {
    	public void read(String msg);
    	public void write(String msg);
    }
    
    private IOListener listener;
    
    public void setIOLister(IOListener listener)
    {
    	this.listener=listener;
    }
	
	
	
}
