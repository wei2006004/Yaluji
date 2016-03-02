package ylj.common.tool.ftp;

import it.sauronsoftware.ftp4j.FTPDataTransferListener;

/**
 * FTP������,���˼�ʵ�֣�����ʹ��commons logger�滻System.out.println
 * 
 * @author leizhimin 2009-11-30 11:05:33
 */
public class MyFtpListener implements FTPDataTransferListener
{
	private FTPOptType optType;

	public static MyFtpListener instance(FTPOptType optType)
	{
		return new MyFtpListener(optType);
	}

	private MyFtpListener(FTPOptType optType)
	{
		this.optType = optType;
	}

	public void started()
	{
		System.out.println(optType.getOptname() + "��FTP����ඡ�����������");
	}

	public void transferred(int length)
	{
		System.out.println(optType.getOptname() + "��FTP����ඡ�����������");

	}

	public void completed()
	{
		System.out.println(optType.getOptname() + "��FTP���ඡ�����������");
	}

	public void aborted()
	{
		System.out.println(optType.getOptname() + "��FTP��ֹඡ�����������");
	}

	public void failed()
	{
		System.out.println(optType.getOptname() + "��FTP�ҵ�ඡ�����������");
	}
}
