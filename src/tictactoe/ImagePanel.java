package tictactoe;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JLayeredPane;

public class ImagePanel extends JLayeredPane {
	private Image image;

	public ImagePanel(Image image) {
		this.image = image;
	}
	
	public ImagePanel(Image image, Pair<Integer> d1) {
		this.image = image;
		setPreferredSize(new Dimension(d1.getV1(), d1.getV2()));
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, this);
	}
}
