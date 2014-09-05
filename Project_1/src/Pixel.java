
public class Pixel {
	public int red = 0;
	public int green = 0;
	public int blue = 0;
	
	public Pixel(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	
	public String toString() {
		return "(" + this.red + ", " + this.green + ", " + this.blue + ")";
	}
}
