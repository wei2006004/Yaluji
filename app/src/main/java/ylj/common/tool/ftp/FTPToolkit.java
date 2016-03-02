package ylj.common.tool.ftp;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import it.sauronsoftware.ftp4j.FTPFile;

import java.io.File;
import java.util.List;

/**
 * TTP�ͻ��˹���
 * 
 * @author leizhimin 2009-11-30 10:20:17
 */
public final class FTPToolkit
{

	private FTPToolkit()
	{
	}

	/**
	 * ����FTP����
	 * 
	 * @param host
	 *            ��������IP
	 * @param port
	 *            ftp�˿�
	 * @param username
	 *            ftp�û���
	 * @param password
	 *            ftp����
	 * @return һ���ͻ���
	 */
	public static FTPClient makeFtpConnection(String host, int port,
			String username, String password)
	{
		FTPClient client = new FTPClient();
		try {
			client.connect(host, port);
			client.login(username, password);
		} catch (Exception e) {
			throw new FTPRuntimeException(e);
		}
		return client;
	}

	/**
	 * FTP�����ļ�������һ���ļ���,��������ļ��в����ڣ��򴴽���Ҫ��Ŀ¼�ṹ
	 * 
	 * @param client
	 *            FTP�ͻ���
	 * @param remoteFileName
	 *            FTP�ļ�
	 * @param localFolderPath
	 *            ��ı���Ŀ¼
	 */
	public static void download(FTPClient client, String remoteFileName,
			String localFolderPath)
	{
		int x = isExist(client, remoteFileName);
		MyFtpListener listener = MyFtpListener.instance(FTPOptType.UP);
		File localFolder = new File(localFolderPath);
		if (localFolder.isFile()) {
			throw new FTPRuntimeException("��Ҫ�����ر���ĵط���һ���ļ����޷����棡");
		} else {
			if (!localFolder.exists())
				localFolder.mkdirs();
		}
		if (x == FTPFile.TYPE_FILE) {
			String localfilepath = PathToolkit.formatPath4File(localFolderPath
					+ File.separator + new File(remoteFileName).getName());
			try {
				if (listener != null)
					client.download(remoteFileName, new File(localfilepath),
							listener);
				else
					client.download(remoteFileName, new File(localfilepath));
			} catch (Exception e) {
				throw new FTPRuntimeException(e);
			}
		} else {
			throw new FTPRuntimeException("��Ҫ���ص��ļ�" + remoteFileName + "�����ڣ�");
		}
	}

	/**
	 * FTP�ϴ������ļ���FTP��һ��Ŀ¼��
	 * 
	 * @param client
	 *            FTP�ͻ���
	 * @param localfile
	 *            �����ļ�
	 * @param remoteFolderPath
	 *            FTP�ϴ�Ŀ¼
	 */
	public static void upload(FTPClient client, File localfile,
			String remoteFolderPath)
	{
		remoteFolderPath = PathToolkit.formatPath4FTP(remoteFolderPath);
		MyFtpListener listener = MyFtpListener.instance(FTPOptType.UP);
		try {
			client.changeDirectory(remoteFolderPath);
			if (!localfile.exists())
				throw new FTPRuntimeException("��Ҫ�ϴ���FTP�ļ�"
						+ localfile.getPath() + "�����ڣ�");
			if (!localfile.isFile())
				throw new FTPRuntimeException("��Ҫ�ϴ���FTP�ļ�"
						+ localfile.getPath() + "��һ���ļ��У�");
			if (listener != null)
				client.upload(localfile, listener);
			else
				client.upload(localfile);
			client.changeDirectory("/");
		} catch (Exception e) {
			throw new FTPRuntimeException(e);
		}
	}
	
	public static void upload(FTPClient client, File localfile,
			String remoteFolderPath,FTPDataTransferListener listener)
	{
		remoteFolderPath = PathToolkit.formatPath4FTP(remoteFolderPath);
		try {
			client.changeDirectory(remoteFolderPath);
			if (!localfile.exists())
				throw new FTPRuntimeException("��Ҫ�ϴ���FTP�ļ�"
						+ localfile.getPath() + "�����ڣ�");
			if (!localfile.isFile())
				throw new FTPRuntimeException("��Ҫ�ϴ���FTP�ļ�"
						+ localfile.getPath() + "��һ���ļ��У�");
			if (listener != null)
				client.upload(localfile, listener);
			else
				client.upload(localfile);
			client.changeDirectory("/");
		} catch (Exception e) {
			throw new FTPRuntimeException(e);
		}
	}

	/**
	 * FTP�ϴ������ļ���FTP��һ��Ŀ¼��
	 * 
	 * @param client
	 *            FTP�ͻ���
	 * @param localfilepath
	 *            �����ļ�·��
	 * @param remoteFolderPath
	 *            FTP�ϴ�Ŀ¼
	 */
	public static void upload(FTPClient client, String localfilepath,
			String remoteFolderPath,FTPDataTransferListener listener)
	{
		File localfile = new File(localfilepath);
		upload(client, localfile, remoteFolderPath,listener);
	}
	
	public static void upload(FTPClient client, String localfilepath,
			String remoteFolderPath)
	{
		File localfile = new File(localfilepath);
		upload(client, localfile, remoteFolderPath);
	}

	/**
	 * �����ϴ������ļ���FTPָ��Ŀ¼��
	 * 
	 * @param client
	 *            FTP�ͻ���
	 * @param localFilePaths
	 *            �����ļ�·���б�
	 * @param remoteFolderPath
	 *            FTP�ϴ�Ŀ¼
	 */
	public static void uploadListPath(FTPClient client,
			List<String> localFilePaths, String remoteFolderPath)
	{
		remoteFolderPath = PathToolkit.formatPath4FTP(remoteFolderPath);
		try {
			client.changeDirectory(remoteFolderPath);
			MyFtpListener listener = MyFtpListener.instance(FTPOptType.UP);
			for (String path : localFilePaths) {
				File file = new File(path);
				if (!file.exists())
					throw new FTPRuntimeException("��Ҫ�ϴ���FTP�ļ�" + path + "�����ڣ�");
				if (!file.isFile())
					throw new FTPRuntimeException("��Ҫ�ϴ���FTP�ļ�" + path
							+ "��һ���ļ��У�");
				if (listener != null)
					client.upload(file, listener);
				else
					client.upload(file);
			}
			client.changeDirectory("/");
		} catch (Exception e) {
			throw new FTPRuntimeException(e);
		}
	}

	/**
	 * �����ϴ������ļ���FTPָ��Ŀ¼��
	 * 
	 * @param client
	 *            FTP�ͻ���
	 * @param localFiles
	 *            �����ļ��б�
	 * @param remoteFolderPath
	 *            FTP�ϴ�Ŀ¼
	 */
	public static void uploadListFile(FTPClient client, List<File> localFiles,
			String remoteFolderPath)
	{
		try {
			client.changeDirectory(remoteFolderPath);
			remoteFolderPath = PathToolkit.formatPath4FTP(remoteFolderPath);
			MyFtpListener listener = MyFtpListener.instance(FTPOptType.UP);
			for (File file : localFiles) {
				if (!file.exists())
					throw new FTPRuntimeException("��Ҫ�ϴ���FTP�ļ�" + file.getPath()
							+ "�����ڣ�");
				if (!file.isFile())
					throw new FTPRuntimeException("��Ҫ�ϴ���FTP�ļ�" + file.getPath()
							+ "��һ���ļ��У�");
				if (listener != null)
					client.upload(file, listener);
				else
					client.upload(file);
			}
			client.changeDirectory("/");
		} catch (Exception e) {
			throw new FTPRuntimeException(e);
		}
	}

	/**
	 * �ж�һ��FTP·���Ƿ���ڣ�������ڷ�������(FTPFile.TYPE_DIRECTORY=1��FTPFile.TYPE_FILE=0��
	 * FTPFile.TYPE_LINK=2) ����ļ������ڣ��򷵻�һ��-1
	 * 
	 * @param client
	 *            FTP�ͻ���
	 * @param remotePath
	 *            FTP�ļ����ļ���·��
	 * @return ����ʱ�򷵻�����ֵ(�ļ�0���ļ���1������2)���������򷵻�-1
	 */
	public static int isExist(FTPClient client, String remotePath)
	{
		remotePath = PathToolkit.formatPath4FTP(remotePath);
		int x = -1;
		FTPFile[] list = null;
		try {
			list = client.list(remotePath);
		} catch (Exception e) {
			return -1;
		}
		if (list.length > 1)
			return FTPFile.TYPE_DIRECTORY;
		else if (list.length == 1) {
			FTPFile f = list[0];
			if (f.getType() == FTPFile.TYPE_DIRECTORY)
				return FTPFile.TYPE_DIRECTORY;
			// ���������ж�
			String _path = remotePath + "/" + f.getName();
			try {
				int y = client.list(_path).length;
				if (y == 1)
					return FTPFile.TYPE_DIRECTORY;
				else
					return FTPFile.TYPE_FILE;
			} catch (Exception e) {
				return FTPFile.TYPE_FILE;
			}
		} else {
			try {
				client.changeDirectory(remotePath);
				return FTPFile.TYPE_DIRECTORY;
			} catch (Exception e) {
				return -1;
			}
		}
	}

	/**
	 * �ر�FTP���ӣ��ر�ʱ�������������һ���ر�����
	 * 
	 * @param client
	 *            FTP�ͻ���
	 * @return �رճɹ������������ѶϿ�����������Ϊnullʱ�򷵻�true��ͨ�����ιرն�ʧ��ʱ�򷵻�false
	 */

	public static boolean closeConnection(FTPClient client)
	{
		if (client == null)
			return true;
		if (client.isConnected()) {
			try {
				client.disconnect(true);
				return true;
			} catch (Exception e) {
				try {
					client.disconnect(false);
				} catch (Exception e1) {
					e1.printStackTrace();
					return false;
				}
			}
		}
		return true;
	}
}
