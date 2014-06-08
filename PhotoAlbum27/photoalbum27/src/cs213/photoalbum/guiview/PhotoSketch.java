package cs213.photoalbum.guiview;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * Draws the full sized photo for the PhotoPanel
 * 
 * @author Rich Gerdes
 *
 */
public class PhotoSketch extends JPanel {

	private BufferedImage image;
	private BufferedImage raw;
	MessageDisplay parent;
	
	/**
	 * Constructor 
	 * Creates a new PhotoSketch object
	 * 
	 * @param parent JFrame object that contains the PhotoSketch object
	 */
	public PhotoSketch(MessageDisplay parent){
		this.parent = parent;
		this.setMaximumSize(new Dimension(50, 50));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (raw != null) {
			int imageWidth = raw.getWidth();
			int imageHeight = raw.getHeight();

			double ratio = (double) imageWidth / imageHeight;

			int maxWidth = this.getWidth();
			int maxHeight = this.getHeight();

			int maxW = (int) (maxHeight * ratio);
			int maxH = (int) (maxWidth * (1 / ratio));

			if (maxW < maxWidth)
				maxWidth = maxW;
			else
				maxHeight = maxH;

			double scaleX = (double) maxWidth / imageWidth;
			double scaleY = (double) maxHeight / imageHeight;
			AffineTransform scaleTransform = AffineTransform.getScaleInstance(
					scaleX, scaleY);
			AffineTransformOp bilinearScaleOp = new AffineTransformOp(
					scaleTransform, AffineTransformOp.TYPE_BICUBIC);
			image = bilinearScaleOp.filter(raw, new BufferedImage(maxWidth,
					maxHeight, raw.getType()));
			g.drawImage(image, this.getWidth() / 2
					- (image.getWidth() / 2), this.getHeight() / 2 - image.getHeight() / 2, null);
			parent.sendMessage("Image Loaded");
		} else {
			parent.sendError("No Photo Avaliable");
		}
	}

	/**
	 * Sets the image to be displayed in the panel
	 * 
	 * @param image image object to be placed on the photo sketch 
	 */
	public void setImage(BufferedImage image) {
		this.raw = image;		
	}
}