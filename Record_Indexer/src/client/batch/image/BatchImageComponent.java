package client.batch.image;

import image.editor.ImageEditor;
import image.editor.Pixel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import shared.modal.Batch;
import shared.modal.Field;
import shared.modal.Project;
import client.batch.state.BatchState;
import client.batch.state.BatchStateListener;
import client.batch.state.Cell;

@SuppressWarnings("serial")
public class BatchImageComponent extends JComponent {
	
	private BufferedImage batchImage;
	private ArrayList<DrawingShape> shapes = new ArrayList<DrawingShape>();
		
	private DrawingImage drawingImage;
	private DrawingRect selectionRect;
	
	//State 
	private Boolean highlight = true;
	private double scale;
	private Boolean inverted = false;
	
	//Mouse event Instance Fields
	private int xDragStart;
	private int yDragStart;
	
	private int deltaX;
	private int deltaY;
	
	private int w_center_X;
	private int w_center_Y;
	
	private int xDragOriginStart;
	private int yDragOriginStart;
		
	private boolean isDragging = false;
		
	public BatchImageComponent() {
		
		super();
				
		//Set Defaults
		scale = 0.5;
		
		resetDrag();
		
		//Draw Emty placeholders
		batchImage = null;
						
		drawingImage = new DrawingImage(null, new Rectangle2D.Double(0, 0, 100, 100));
		selectionRect = new DrawingRect(new Rectangle2D.Double(0, 0, 0, 0), new Color(41,153,240,80));
				
		w_center_X = (int)drawingImage.rect.getCenterX();
		w_center_Y = (int)drawingImage.rect.getCenterY();
		
		this.setBackground(new Color(100, 100, 100));
				
		//Set Up listeners
		this.addMouseListener(mouseAdapter);
		this.addMouseMotionListener(mouseAdapter);
		this.addMouseWheelListener(mouseAdapter);
		
		shapes.add(drawingImage);
		shapes.add(selectionRect);
				
		BatchState.addBatchStateListener(new BatchListener());
		
	}
	
	//Custom implementation of the paintComponent
	@Override
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		drawBackground(g2);
		
		//Update the graphics context
		g2.translate(this.getWidth()/2, this.getHeight()/2);
		g2.scale(scale, scale);
		g2.translate(-w_center_X, -w_center_Y);
		
		drawShapes(g2);
	}
	
	private void drawShapes(Graphics2D g2) {
		//Do not draw shapes if there is no image
		if (batchImage != null) {
			for (DrawingShape drawingShape : shapes) {
				drawingShape.draw(g2);
			}
		}
	}
	
	private void drawBackground(Graphics2D g2) {
		g2.setColor(getBackground());
		g2.fillRect(0, 0, getWidth(), getHeight());
		
	}
	
	//Restart Drag Parameters
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
		
		//Toggle inverting instance field
		inverted = !inverted;
		BatchState.setInverted(inverted);
		
		repaint();
	}
	
	//Handles the click to determine what row and column was selected on the image
	public void imageClicked(Point2D point) {
		if (BatchState.getBatch() == null) {
			return;
		}
		
		int x = (int)point.getX();
		int y = (int)point.getY();
		int width = 0;
		int heigth = BatchState.getProject().getRecordHeight();
		int drawX = 0;
		int drawY = 0;
		int row = 0;
		int column = 0;
		Project currentProject = BatchState.getProject();
		
		//Determine the row
		if (y < currentProject.getFirstYCood() || y > currentProject.getFirstYCood() + currentProject.getRecordsPerBatch()*currentProject.getRecordHeight()) {
			return;
		} else {
			row = (int)((y-currentProject.getFirstYCood())/currentProject.getRecordHeight());
			drawY = row * currentProject.getRecordHeight() + currentProject.getFirstYCood();
		}
		
		//Determine the column
		for (Field field : BatchState.getProject().getFields()) {
			if (field.getxCoord() < x && field.getxCoord() + field.getWidth() > x) {
				width = field.getWidth();
				drawX = field.getxCoord();
				column = field.getColNumber();
			}
		}
		
		selectionRect.rect = new Rectangle2D.Double(drawX, drawY, width, heigth);
		BatchState.setSelectedCell(new Cell(column, row));
	}
	
	public void setSelectedCell(Cell selectedCell) {
		
		//Just in Case it has submitted a batch
		if (BatchState.getBatch() != null) {
			int drawX = 0;
			int drawY = selectedCell.getRow() * BatchState.getProject().getRecordHeight()  + BatchState.getProject().getFirstYCood();
			int width = 0;
			int height = BatchState.getProject().getRecordHeight();
			Cell newCell = new Cell(selectedCell.getColumn(), selectedCell.getRow());
			
			if (newCell.getColumn() > 0) {
				newCell.setColumn(selectedCell.getColumn() - 1);
			}
			
			for (Field field : BatchState.getProject().getFields()) {
				if (field.getColNumber() == (newCell.getColumn() + 1)) {
					width = field.getWidth();
					drawX = field.getxCoord();
				}
			}
			
			selectionRect.rect = new Rectangle2D.Double(drawX, drawY, width, height);
			
			repaint();
		}
		
	}
	
	
	///////////////////////
	//Getters and Setters//
	///////////////////////
	
	public void setImage(BufferedImage image) {
		this.batchImage = image;
		drawingImage.image = image;
				
		drawingImage.rect = new Rectangle2D.Double(0, 0, image.getWidth(), image.getHeight());
		
		if (BatchState.getBatchImageCenter() != null) {
			w_center_X = BatchState.getBatchImageCenter().x;
			w_center_Y = BatchState.getBatchImageCenter().y;
		} else {
			w_center_X = this.batchImage.getWidth()/2;
			w_center_Y = this.batchImage.getHeight()/2;
		}
		
		if (BatchState.getScale() != 0) {
			scale = BatchState.getScale();
		} else {
			scale = 0.5;
		}
		
		repaint();
	}
	
	public Image getImage() {
		return this.batchImage;
	}
	
	public void setScale(double scale) {
		
		if (scale >= .2 && scale <= 3 ) {
			this.scale = scale;
			this.repaint();
		}
	}
	
	public double getScale() {
		return this.scale;
	}
	
	public void toggleHighlight () {
		highlight = !highlight;
		BatchState.setHighlight(highlight);
		repaint();
	}
	
	
	////////////////
	//Mouse Events//
	////////////////
	
	private MouseAdapter mouseAdapter = new MouseAdapter() {
		
		@Override
		public void mouseClicked(MouseEvent e) {
			int deviceX = e.getX();
			int deviceY = e.getY();
			
			//Apply transformations to jeep up to date in the Affine Transform
			Point2D devicePoint = new Point2D.Double(deviceX, deviceY);
			Point2D worldPoint = new Point2D.Double();
			
			AffineTransform transform = new AffineTransform();
			
			transform.translate(getWidth()/2, getHeight()/2);
			transform.scale(scale, scale);
			transform.translate(-w_center_X, -w_center_Y);
			
			//Transform to world coordinates
			try {
				transform.inverseTransform(devicePoint, worldPoint);
			} catch (Exception e2) {
				return;
			}
			
			Graphics2D g2 = (Graphics2D)getGraphics();
			
			int worldX = (int)worldPoint.getX();
			int worldY = (int)worldPoint.getY();
			
			//Determine if any of the shapes was clicked so we can handle the click
			boolean hitShape = false;
			
			for (DrawingShape drawingShape : shapes) {
				hitShape = drawingShape.contains(g2, worldX, worldY);
				if (hitShape) {
					break;
				}
			}
			
			if (hitShape) {
				imageClicked(worldPoint);
			}
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			
			int deviceX = e.getX();
			int deviceY = e.getY();
			
			//Update Transform
			Point2D devicePoint = new Point2D.Double(deviceX, deviceY);
			Point2D worldPoint = new Point2D.Double();
			
			AffineTransform transform = new AffineTransform();
			
			transform.translate(getWidth()/2, getHeight()/2);
			transform.scale(scale, scale);
			transform.translate(-w_center_X, -w_center_Y);
			
			//To world Coordinates
			try {
				transform.inverseTransform(devicePoint, worldPoint);
			} catch (Exception e2) {
				return;
			}
						
			Graphics2D g2 = (Graphics2D)getGraphics();
			
			int worldX = (int)worldPoint.getX();
			int worldY = (int)worldPoint.getY();
			
			//DEtermine if a shape was clicked so a drag is allowed
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
				
				transform.translate(getWidth()/2, getHeight()/2);
				transform.scale(scale, scale);
				transform.translate(-xDragOriginStart, -yDragOriginStart);
				
				Point2D devicePoint = new Point2D.Double(deviceX, deviceY);
				Point2D worldPoint = new Point2D.Double();
				
				
				try {
					transform.inverseTransform(devicePoint, worldPoint);
				} catch (Exception e2) {
					return;
				}
				
				//Update coordinates for image
				int worldX = (int)worldPoint.getX();
				int worldY = (int)worldPoint.getY();
				
				
				deltaX = worldX - xDragStart;
				deltaY = worldY - yDragStart;
				
				w_center_X = xDragOriginStart - deltaX;
				w_center_Y = yDragOriginStart - deltaY;
				
				BatchState.setBatchImageCenter(new Point(w_center_X, w_center_Y));
				
				repaint();
			}
		}
		
		
		@Override
		public void mouseReleased(MouseEvent e) {
			resetDrag();
		}
		
		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			
			//Only allow a threshold of scalng (Max and Min)
			if ((e.getWheelRotation() > 0 && scale <= 3) || e.getWheelRotation() < 0 && scale >= 0.2) {
				scale += (double)(e.getWheelRotation())/8;
				BatchState.setScale(scale);
			}
			
			repaint();
		}
		
	};
	
	
	//////////////////////////////////////////
	// Drawing Shapes classes and interfaces//
	//////////////////////////////////////////
	
	
	private interface DrawingShape {
		boolean contains(Graphics2D g2, double x, double y);
		void draw(Graphics2D g2);
		Rectangle2D getBounds(Graphics2D g2);
	}
	
	private class DrawingImage implements DrawingShape {

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
	
	private class DrawingRect implements DrawingShape {

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
			if (highlight) {
				Color newColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), 80);
				color = newColor;
			} else {
				Color newColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), 0);
				color = newColor;
			}
			
			g2.setColor(color);
			g2.fill(rect);
		}
		
		@Override
		public Rectangle2D getBounds(Graphics2D g2) {
			return rect.getBounds2D();
		}
	}
	
	////////////////////////
	//Batch State Listener//
	////////////////////////
	
	private class BatchListener implements BatchStateListener {

		@Override
		public void selectedCellChanged(Cell newSelectedCell) {
			setSelectedCell(newSelectedCell);
		}

		@Override
		public void newBatchLoaded(Batch newBatch, Project newProject) {
			try {
				
				if (newBatch == null) {
					//Erase the image
					drawingImage.rect = new Rectangle2D.Double(0, 0, 0, 0);
					batchImage = null;
					repaint();
				} else {
					//Retrieve new Image
					System.out.println("Got to this point");
					URL url = new URL(newBatch.getFilePath());
					BufferedImage batchImage = ImageIO.read(url);
					highlight = BatchState.getHighlight();
					scale = BatchState.getScale();
					setImage(batchImage);
					if (BatchState.getInverted() != inverted) {
						invertImage();
					}
				}
				
			} catch (Exception e) {
			}
		}

		@Override
		public void valueChanged(Cell editedCell, String value) {
			//Do nothing
		}
		
	}
	
}
