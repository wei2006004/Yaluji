package ylj.common.tool.ftp;
import java.io.File; 

/** 
* ·�������ߣ�����ϵͳ����Ӧ 
* 
* @author leizhimin 2009-11-30 16:01:34 
*/ 
public final class PathToolkit { 
        private PathToolkit() { 
        } 

        /** 
         * ��ʽ���ļ�·���������в��淶�ķָ�ת��Ϊ��׼�ķָ���,����ȥ��ĩβ���ļ�·���ָ����� 
         * ����������ϵͳ����Ӧ 
         * 
         * @param path �ļ�·�� 
         * @return ��ʽ������ļ�·�� 
         */ 
        public static String formatPath4File(String path) { 
                String reg0 = "\\\\+"; 
                String reg = "\\\\+|/+"; 
                String temp = path.trim().replaceAll(reg0, "/"); 
                temp = temp.replaceAll(reg, "/"); 
                if (temp.length() > 1 && temp.endsWith("/")) { 
                        temp = temp.substring(0, temp.length() - 1); 
                } 
                temp = temp.replace('/', File.separatorChar); 
                return temp; 
        } 

        /** 
         * ��ʽ���ļ�·���������в��淶�ķָ�ת��Ϊ��׼�ķָ��� 
         * ����ȥ��ĩβ��"/"����(������FTPԶ���ļ�·������Web��Դ�����·��)�� 
         * 
         * @param path �ļ�·�� 
         * @return ��ʽ������ļ�·�� 
         */ 
        public static String formatPath4FTP(String path) { 
                String reg0 = "\\\\+"; 
                String reg = "\\\\+|/+"; 
                String temp = path.trim().replaceAll(reg0, "/"); 
                temp = temp.replaceAll(reg, "/"); 
                if (temp.length() > 1 && temp.endsWith("/")) { 
                        temp = temp.substring(0, temp.length() - 1); 
                } 
                return temp; 
        } 

        /** 
         * ��ȡFTP·���ĸ�·����������·����Ч������� 
         * 
         * @param path FTP·�� 
         * @return ��·�������û�и�·�����򷵻�null 
         */ 
        public static String genParentPath4FTP(String path) { 
                String pp = new File(path).getParent(); 
                if (pp == null) return null; 
                else return formatPath4FTP(pp); 
        } 
}
