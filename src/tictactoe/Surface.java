package tictactoe;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Class Surface: provides a simple way to build a surface
 * @author JP
 * @version 1.0 (12/27/2020)
 */
public abstract class Surface extends JFrame {
	/***** ATTRIBUTES *****/
	protected Image backgroundImage;
	protected ImagePanel panel;
	
	// default values
	protected Pair<Integer> surfaceDims = new Pair<Integer>(1000,1000);
	protected Color surfaceBackground = Color.BLACK;
	protected Color surfaceForeground = Color.WHITE;
	
	/***** CONSTRUCTORS *****/
	// Dimensions via 2 independent parameters
	public Surface(int d1, int d2) {
		super();
		surfaceDims = new Pair<Integer>(d1, d2);
		initFrame(d1, d2);
		centerView();
	}

	// Dimensions via a Pair
	public Surface(Pair<Integer> d1) {
		super();
		surfaceDims = d1;
		initFrame(d1.getV1(), d1.getV2());
	}
	
	/***** METHODS *****/
	protected void setColor(Color color) {
		getContentPane().setBackground(color);
		getRootPane().setBackground(color);
		setBackground(color);
		pack();
	}
	
	private void initFrame(int d1, int d2) {
		setPreferredSize(new Dimension(d1, d2));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void centerView() {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = getSize();
		setLocation((screen.width - frameSize.width) >> 1,
						(screen.height - frameSize.height) >> 1);
	}
	
	/***** GETTERS & SETTERS *****/	
	public Pair<Integer> getDimensions() { return surfaceDims; }
	
	public int getWidth() { return surfaceDims.getV1();	}
	
	public int getHeight() { return surfaceDims.getV2(); }

	public void setBackground(String imgName, Pair<Integer> size) {
		// panel
		backgroundImage = getImage(imgName, size.getV1(), size.getV2());
		if (backgroundImage != null) {
			panel = new ImagePanel(backgroundImage);
			setContentPane(panel);
		}
		else {						// no image could be found, so set it default colors
			System.err.println("The background image could not be set...");
			JPanel panel = new JPanel();
			panel.setBackground(surfaceBackground);
			panel.setForeground(surfaceForeground);
			setContentPane(panel);
		}
	}
	
	protected Image getImage(String imgName, int width, int height) {
		URL url = this.getClass().getResource(imgName);
		if (url != null) {
			Image image1 = new ImageIcon(url).getImage();
			Image scaled1 = image1.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			return scaled1;
		}
		else {
			System.err.println("Error in getImage(String, int, int). Image could not be found, or it could not be decoded");
			return null;
		}
	}
	
	protected Image getImage(String imgName) {
		URL url = this.getClass().getResource(imgName);
		if (url != null) {
			return new ImageIcon(url).getImage();
		}
		else {
			System.err.println("Error in getImage(String). Image could not be found, or it could not be decoded");
			return null;
		}
	}
	
	/***** TO BE OVERRIDDEN *****/
	public abstract void surfaceCreated();
	
	public abstract void surfaceChanged();
	
	public abstract void surfaceDestroyed();
	
	protected abstract void setLayout();
}
