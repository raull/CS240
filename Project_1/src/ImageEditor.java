import java.util.Scanner;
import java.io.*;

public class ImageEditor {
	
	public Pixel[][] imageData;
	public int maxValue = 0;
	
	
	public static void main(String[] args) {
		
		String inputFileName = args[0];
		String outputFileName = args[1];
		String imageFilterType = args[2];
		
		int motionBlur = 0;
		
		if (args.length > 3) {
			motionBlur = Integer.parseInt(args[3]);
		}
		
		ImageEditor imageEditor = new ImageEditor();
		imageEditor.readPPMImageFile(inputFileName);
		
		if(imageFilterType.equals("invert")) {
			System.out.println("You have chosen the invert filter");
			imageEditor.invertImage(imageEditor.imageData, outputFileName);
		}else if(imageFilterType.equals("grayscale")) {
			System.out.println("You have chosen the grayscale filter");
			imageEditor.grayScaleImage(imageEditor.imageData, outputFileName);
		}else if(imageFilterType.equals("emboss")) {
			System.out.println("You have chosen the emboss filter");
			imageEditor.embossImage(imageEditor.imageData, outputFileName);
		}else if(imageFilterType.equals("motionblur")) {
			System.out.println("You have chosen the motionblur filter");
			imageEditor.motionBlurImage(imageEditor.imageData, outputFileName, motionBlur);
		}else {
			System.out.println("You have chosen an invalid filter");
		}
	}
	
	public void invertImage(Pixel[][] data, String outputFileName) {
		System.out.println("After Invert Filter");
		
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				Pixel curPixel = data[i][j];
				curPixel.red = -(curPixel.red - this.maxValue);
				curPixel.green = -(curPixel.green - this.maxValue);
				curPixel.blue = -(curPixel.blue - this.maxValue);
				
				System.out.println(data[i][j].toString());
			}
		}
		
	}
	
	public void grayScaleImage(Pixel[][] data, String outputFileName) {
		System.out.println("After GrayScale Filter");
		
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				Pixel curPixel = data[i][j];
				int average = (curPixel.red + curPixel.green + curPixel.blue)/3;
				curPixel.red = average;
				curPixel.green = average;
				curPixel.blue = average;
				
				System.out.println(data[i][j].toString());
			}
		}
	}
	
	public void embossImage(Pixel[][] data, String outputFileName) {
		System.out.println("After Emboss Filter");
		
		for (int i = data.length - 1; i >= 0; i--) {
			for (int j = data[i].length - 1; j >= 0; j--) {
				Pixel curPixel = data[i][j];
				
				if (i == 0 || j == 0) {
					curPixel.red = 128;
					curPixel.green = 128;
					curPixel.blue = 128;
				}else {
					Pixel otherPixel = data[i-1][j-1];
					int redDiff = curPixel.red - otherPixel.red;
					int greenDiff = curPixel.green - otherPixel.green;
					int blueDiff = curPixel.blue - otherPixel.blue;
					
					int value = this.maxDifference(this.maxDifference(redDiff, greenDiff), blueDiff) + 128;
					
					if (value < 0 ) {
						value = 0;
					}
					if (value > 255) {
						value = 255;
					}
					
					curPixel.red = value;
					curPixel.green = value;
					curPixel.blue = value;
				}
				
				System.out.println(data[i][j].toString());
			}
		}
	}
	
	public void motionBlurImage(Pixel[][] data, String outputFileName, int motionBlur) {
		
	}
	
	public void readPPMImageFile(String inputFileName) {
		try {
			File filePath = new File(inputFileName);
			Scanner fileScanner = new Scanner(filePath);
			
			fileScanner.nextLine();
			fileScanner.nextLine();
			
			//Extract the dimensions of the image
			int width = fileScanner.nextInt();
			int height = fileScanner.nextInt();
			
			//Set the dimensions of the image
			this.imageData = new Pixel[width][height];
			
			this.maxValue = fileScanner.nextInt();
			
			//Iterate for every pixel
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					int red = fileScanner.nextInt();
					int green = fileScanner.nextInt();
					int blue = fileScanner.nextInt();
					
					Pixel newPixel = new Pixel(red, green, blue);
					System.out.println(newPixel.toString());
					
					this.imageData[i][j] = newPixel;
				}
			}
						
		} catch (FileNotFoundException e) {
			System.out.println("File not found: "+ e.getMessage());
		}
		
	}
	
	private int maxDifference(int arg0, int arg1) {
		if (Math.abs(arg0) >= Math.abs(arg1)) {
			return arg0;
		} else {
			return arg1;
		}
	}

}
