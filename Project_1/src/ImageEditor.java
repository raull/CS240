
public class ImageEditor {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Hello World");
		
		String inputFileName = args[0];
		String outputFileName = args[1];
		String imageFilterType = args[2];
		
		if(imageFilterType.equals("invert")) {
			System.out.println("You have chosen the invert filter");
		}else if(imageFilterType.equals("grayscale")) {
			System.out.println("You have chosen the grayscale filter");
		}else if(imageFilterType.equals("emboss")) {
			System.out.println("You have chosen the emboss filter");
		}else if(imageFilterType.equals("motionblur")) {
			System.out.println("You have chosen the motionblur filter");
		}else {
			System.out.println("You have chosen an invalid filter");
		}
	}
	
	public void invertImage(int[][] data, String outputFileName) {
		
	}
	
	public void grayScaleImage(int[][] data, String outputFileName) {
		
	}
	
	public void embossImage(int[][] data, String outputFileName) {
		
	}
	
	public void motionBlurImage(int[][] data, String outFileName) {
		
	}
	
	public int[][] readPPMImageFile(String inputFileName) {
		return null;
	}

}
