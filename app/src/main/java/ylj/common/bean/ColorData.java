package ylj.common.bean;

public class ColorData {
	protected int number=0;
	protected int count=0;
	protected int color;
	
	public float getValue() {
		return 0;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	public int getNumber() {
		return number;
	}
	public void incNumber(){
		number++;
	}
	public int getCount() {
		return count;
	}
	public void incCount(){
		count++;
	}
}
