package ylj.common.tool.ftp;


public enum FTPOptType { 
        UP("�ϴ�"), 
        DOWN("����"), 
        LIST("���"), 
        DELFILE("ɾ���ļ�"), 
        DELFOD("ɾ���ļ���"), 
        RENAME("�ϴ�"); 

        private String optname; 

        FTPOptType(String optname) { 
                this.optname = optname; 
        } 

        public String getOptname() { 
                return optname; 
        } 
}