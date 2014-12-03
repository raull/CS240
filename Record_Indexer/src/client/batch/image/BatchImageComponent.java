package client.batch.image;

import image.editor.ImageEditor;
import image.editor.Pixel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JComponent;

@SuppressWarnings("serial")
public class BatchImageComponent extends JComponent {
	
	private BufferedImage batchImage;
	private ArrayList<DrawingShape> shapes = new ArrayList<DrawingShape>();
	
	private double scale;
	
	private DrawingImage drawingImage;
	private DrawingRect selectionRect;
		
	//Mouse event Instance Fields
	private int xDragStart;
	private int yDragStart;
	
	private int deltaX;
	private int deltaY;
	
	private int w_center_X;
	private int w_center_Y;
	
	private int panelCenterX;
	private int panelCenterY;
	
	private int xDragOriginStart;
	private int yDragOriginStart;
		
	private boolean isDragging = false;
		
	public BatchImageComponent(BufferedImage image , Dimension dimension) {
		
		super();
		
		scale = 1;
		
		resetDrag();
		
		batchImage = image;
		
		double aspectRatio = (double)image.getHeight()/(double)image.getWidth();
				
		drawingImage = new DrawingImage(image, new Rectangle2D.Double(0, 0, dimension.getWidth(), dimension.getWidth()*aspectRatio));
		selectionRect = new DrawingRect(new Rectangle2D.Double(0, 0, 0, 0), new Color(50,50,50,50));
		
		w_center_X = (int)drawingImage.rect.getWidth()/2;
		w_center_Y = (int)drawingImage.rect.getHeight()/2;
		
		panelCenterX = (int)dimension.width/2;
		panelCenterY = (int)dimension.height/2;
		
		this.setBackground(new Color(100, 100, 100));
				
		this.addMouseListener(mouseAdapter);
		this.addMouseMotionListener(mouseAdapter);
		this.addMouseWheelListener(mouseAdapter);
		
		shapes.add(drawingImage);
		shapes.add(selectionRect);
	}
	
	
	@Override
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		drawBackground(g2);
		
		g2.translate(panelCenterX, panelCenterY);
		g2.scale(scale, scale);
		g2.translate(-w_center_X, -w_center_Y);

		drawShapes(g2);
		
	}
	
	private void drawShapes(Graphics2D g2) {
		for (DrawingShape drawingShape : shapes) {
			drawingShape.draw(g2);
		}
	}
	
	private void drawBackground(Graphics2D g2) {
		g2.setColor(getBackground());
		g2.fillRect(0, 0, getWidth(), getHeight());
		
	}
	
	private void resetDrag() {
		isDragging = false;
		deltaX = 0;
		deltaY = 0;
		xDragStart = 0;
		yDragStart = 0;
	}
	
	public void invertImage() {
		
		BufferedImage image = (BufferedImage)drawingImage.image;
		
		int width = image.getWidth();
		int height = image.getHeight();
						
		for (int i = image.getMinY(); i < height; i++) {
			for (int j = image.getMinX(); j < width; j++) {
				int pixel = image.getRGB(j, i);
				Color rgbColor = new Color(pixel); 
				Pixel newPixel = new Pixel(rgbColor.getRed(), rgbColor.getGreen(), rgbColor.getBlue());
				ImageEditor.invertPixel(newPixel, 255);
				rgbColor = new Color(newPixel.red, newPixel.green, newPixel.blue);
				image.setRGB(j, i, rgbColor.getRGB());
			}
		}
		
		repaint();
	}
	
	///////////////////////
	//Getters and Setters//
	///////////////////////
	
	public void setImage(BufferedImage image) {
		this.batchImage = image;
	}
	
	public Image getImage() {
		return this.batchImage;
	}
	
	public void setScale(double scale) {
		
		if (scale >= .5 && scale <= 3 ) {
			this.scale = scale;
			this.repaint();
		}
	}
	
	public double getScale() {
		return this.scale;
	}
	
	
	////////////////
	//Mouse Events//
	////////////////
	
	private MouseAdapter mouseAdapter = new MouseAdapter() {
		
		@Override
		public void mousePressed(MouseEvent e) {
			
			int deviceX = e.getX();
			int deviceY = e.getY();
			
			
			Point2D devicePoint = new Point2D.Double(deviceX, deviceY);
			Point2D worldPoint = new Point2D.Double();
			
			AffineTransform transform = new AffineTransform();
			
			transform.translate(panelCenterX, panelCenterY);
			transform.scale(scale, scale);
			transform.translate(-w_center_X, -w_center_Y);
			
			
			try {
				transform.inverseTransform(devicePoint, worldPoint);
			} catch (Exception e2) {
				return;
			}
						
			Graphics2D g2 = (Graphics2D)getGraphics();
			
			int worldX = (int)worldPoint.getX();
			int worldY = (int)worldPoint.getY();
			
			
			boolean hitShape = false;
			
			for (DrawingShape drawingShape : shapes) {
				hitShape = drawingShape.contains(g2, worldX, worldY);
				if (hitShape) {
					break;
				}
			}
			
			if (hitShape) {
				isDragging = true;
				xDragStart = worldX;
				yDragStart = worldY;
				xDragOriginStart = w_center_X;
				yDragOriginStart = w_center_Y;
				
			}
			
		}
		
		
		@Override
		public void mouseDragged(MouseEvent e) {
			if (isDragging) {
				int deviceX = e.getX();
				int deviceY = e.getY();
				
				AffineTransform transform = new AffineTransform();
				
				transform.translate(panelCenterX, panelCenterY);
				transform.scale(scale, scale);
				transform.translate(-xDragOriginStart, -yDragOriginStart);
				
				Point2D devicePoint = new Point2D.Double(deviceX, deviceY);
				Point2D worldPoint = new Point2D.Double();
				
				
				try {
					transform.inverseTransform(devicePoint, worldPoint);
				} catch (Exception e2) {
					return;
				}
								
				int worldX = (int)worldPoint.getX();
				int worldY = (int)worldPoint.getY();
				
				
				deltaX = worldX - xDragStart;
				deltaY = worldY - yDragStart;
				
				w_center_X = xDragOriginStart - deltaX;
				w_center_Y = yDragOriginStart - deltaY;
				
				
				repaint();
			}
		}
		
		
		@Override
		public void mouseReleased(MouseEvent e) {
			resetDrag();
		}
		
		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			
			if ((e.getWheelRotation() > 0 && scale <= 3) || e.getWheelRotation() < 0 && scale >= 0.5) {
				scale += (double)(e.getWheelRotation())/8;
			}
			
			repaint();
		}
		
	};
	
	
	//////////////////////////////////////////
	// Drawing Shapes classes and interfaces//
	//////////////////////////////////////////
	
	
	interface DrawingShape {
		boolean contains(Graphics2D g2, double x, double y);
		void draw(Graphics2D g2);
		Rectangle2D getBounds(Graphics2D g2);
	}
	
	class DrawingImage implements DrawingShape {

		private Image image;
		private Rectangle2D rect;
		
		public DrawingImage(Image image, Rectangle2D rect) {
			this.image = image;
			this.rect = rect;
		}

		@Override
		public boolean contains(Graphics2D g2, double x, double y) {
			return rect.contains(x, y);
		}

		@Override
		public void draw(Graphics2D g2) {
			g2.drawImage(image, (int)rect.getMinX(), (int)rect.getMinY(), (int)rect.getMaxX(), (int)rect.getMaxY(),
							0, 0, image.getWidth(null), image.getHeight(null), null);
		}	
		
		@Override
		public Rectangle2D getBounds(Graphics2D g2) {
			return rect.getBounds2D();
		}
	}
	
	class DrawingRect implements DrawingShape {

		private Rectangle2D rect;
		private Color color;
		
		public DrawingRect(Rectangle2D rect, Color color) {
			this.rect = rect;
			this.color = color;
		}

		@Override
		public boolean contains(Graphics2D g2, double x, double y) {
			return rect.contains(x, y);
		}

		@Override
		public void draw(Graphics2D g2) {
			g2.setColor(color);
			g2.fill(rect);
		}
		
		@Override
		public Rectangle2D getBounds(Graphics2D g2) {
			return rect.getBounds2D();
		}
	}
	
}