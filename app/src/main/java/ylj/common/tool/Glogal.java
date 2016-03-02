package ylj.common.tool;

public final class Glogal {
	private static boolean isDebug=true;
	private static boolean isBt=true;
	
	public static boolean isDebug(){
		return isDebug;
	}
	
	public static boolean isBtConnect(){
		return isBt;
	}
	
	public static void  setDebug(boolean isdebug){
		isDebug=isdebug;
	}
	
	public static void setBtConnect(boolean isbt){
		isBt=isbt;
	}
}
