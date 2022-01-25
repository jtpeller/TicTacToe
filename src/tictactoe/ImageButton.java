package tictactoe;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import java.awt.image.*;
import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class ImageButton extends JButton {
	private BufferedImage buttonIcon;
	
	/***** CONSTRUCTORS *****/
	public ImageButton() {
		super();
		buttonIcon = null;
		setIcon(null);
		setBorder(BorderFactory.createEmptyBorder());
		setContentAreaFilled(false);
	}
	
	public ImageButton(String str) {
		super(str);
		setBorder(BorderFactory.createEmptyBorder());
		setContentAreaFilled(false);
	}
	
	public ImageButton(Image img) {
		buttonIcon = processImage(img);					// convert to BufferedImage
		setIcon(new ImageIcon(buttonIcon));				// set the button's icon
		setBorder(BorderFactory.createEmptyBorder());	// no border
		setContentAreaFilled(false);					// no fill
	}
	
	public ImageButton(BufferedImage img) {
		buttonIcon = img;								// save img
		setIcon(new ImageIcon(buttonIcon));				// set button's icon
		setBorder(BorderFactory.createEmptyBorder());	// no border
		setContentAreaFilled(false);					// no fill
	}
	
	/***** METHODS *****/
	public void colorImage(Color c) {
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();
		
		int width = buttonIcon.getWidth();
		int height = buttonIcon.getHeight();
		WritableRaster raster = buttonIcon.getRaster();

		for (int xx = 0; xx < width; xx++) {
			for (int yy = 0; yy < height; yy++) {
				int[] pixels = raster.getPixel(xx, yy, (int[]) null);
				pixels[0] = r;
				pixels[1] = g;
				pixels[2] = b;
				raster.setPixel(xx, yy, pixels);
			}
		}
	}
	
	/***** UTILITY FUNCTIONS *****/
	private BufferedImage processImage(Image image) {
		if (image instanceof BufferedImage)				// simply cast it over if it already is a BImage
			return (BufferedImage) image;
		
		image = new ImageIcon(image).getImage();		// ensures all pixels are loaded
		boolean hasAlpha = hasAlpha(image);				// does it have transparent pixels?
		
		BufferedImage bimage = null;					// new buffered image to be returned
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		try {											// determine transparency type
			int transparency = Transparency.OPAQUE;			
			if (hasAlpha == true)
				transparency = Transparency.BITMASK;
			
			GraphicsDevice gs = ge.getDefaultScreenDevice();	// create BImage
			GraphicsConfiguration gc = gs.getDefaultConfiguration();

			bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
		} 
		catch (HeadlessException e) {
			System.err.println("HeadlessException in processImage(Image)...");
			e.printStackTrace();
		}

		if (bimage == null) {
			int type = BufferedImage.TYPE_INT_RGB;		// use default color model
			if (hasAlpha == true)
				type = BufferedImage.TYPE_INT_ARGB;
			bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
		}
		
		Graphics g = bimage.createGraphics();			// Copy image to buffered image
		g.drawImage(image, 0, 0, null);					// Paint the image onto the buffered image
		g.dispose();

		return bimage;
	}
	
	private static boolean hasAlpha(Image image) {
		// If buffered image, the color model is readily available
		if (image instanceof BufferedImage)
			return ((BufferedImage) image).getColorModel().hasAlpha();

		// Use a pixel grabber to retrieve the image's color model;
		// grabbing a single pixel is usually sufficient
		PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
		try {
			pg.grabPixels();
		} catch (InterruptedException e) {
		}

		// Get the image's color model
		return pg.getColorModel().hasAlpha();
	}
	
	/***** GETTERS & SETTERS *****/
	public BufferedImage getImage() {
		return buttonIcon;
	}
	
	public void setImage(Image img) {
		buttonIcon = processImage(img);
		setIcon(new ImageIcon(buttonIcon));
	}
}
