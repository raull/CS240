package client.batch.image;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JComponent;

@SuppressWarnings("serial")
public class BatchImageComponent extends JComponent {
	
	private Image batchImage;
	private ArrayList<DrawingShape> shapes = new ArrayList<DrawingShape>();
	
	private double scale;
	
	private DrawingImage drawingImage;
	
	//Mouse event Instance Fields
	private int xDragStart;
	private int yDragStart;
	
	private int deltaX;
	private int deltaY;
	
	private int worldOriginX;
	private int worldOriginY;
	
	private int xDragOriginStart;
	private int yDragOriginStart;
		
	private boolean isDragging = false;
	
	public BatchImageComponent(Image image) {
		
		scale = 1;
		worldOriginX = 0;
		worldOriginY = 0;
		
		resetDrag();
		
		batchImage = image;
		drawingImage = new DrawingImage(image, new Rectangle2D.Double(0, 0, 700, 500));
		
		this.setBackground(new Color(100, 100, 100));
				
		this.addMouseListener(mouseAdapter);
		this.addMouseMotionListener(mouseAdapter);
		this.addMouseWheelListener(mouseAdapter);
		
		shapes.add(drawingImage);
	}
	
	
	@Override
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		drawBackground(g2);
		
		g2.scale(scale, scale);
		g2.translate(-worldOriginX, -worldOriginY);
		
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
	
	//Getters and Setters
	
	public void setImage(Image image) {
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
			
			AffineTransform transform = new AffineTransform();
			transform.scale(scale, scale);
			transform.translate(-worldOriginX, -worldOriginY);
			
			Point2D devicePoint = new Point2D.Double(deviceX, deviceY);
			Point2D worldPoint = new Point2D.Double();
			
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
			}
			
			if (hitShape) {
				isDragging = true;
				xDragStart = worldX;
				yDragStart = worldY;
				xDragOriginStart = worldOriginX;
				yDragOriginStart = worldOriginY;
				
				System.out.println("Hit a shape on world Point: " + worldPoint + ", Device Point: " + devicePoint);
			}
			
		}
		
		
		@Override
		public void mouseDragged(MouseEvent e) {
			if (isDragging) {
				int deviceX = e.getX();
				int deviceY = e.getY();
				
				AffineTransform transform = new AffineTransform();
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
				
				worldOriginX = xDragOriginStart - deltaX;
				worldOriginY = yDragOriginStart - deltaY;
				
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
}
