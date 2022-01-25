package tictactoe;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Image;

import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

public class TicTacToeMenu extends Surface implements MouseListener {
	/***** ATTRIBUTES *****/
	private final int BUTTON_COUNT = 3,
					  TITLE_PAD = 75,
					  SUBTITLE_PAD = 50;
	
	// Lists
	protected ArrayList<Image> imageList = new ArrayList<Image>();		// title, play, quit
	protected ArrayList<ImageButton> buttonList = new ArrayList<ImageButton>();
	
	// Components
	private ImagePanel panel;
	protected ImageButton title;
	protected ImageButton play;
	protected ImageButton quit;
	
	// default values
	protected Pair<Integer> dims = new Pair<Integer>(700, 800);			// default menu dimensions
	private String bkgdName;
	private String logoName;
	
	/***** CONSTRUCTORS *****/
	public TicTacToeMenu(int d1, int d2, String[] imgNames, String bkgdName, String logoName) {
		super(d1, d2);						// build base surface
		dims = new Pair<Integer>(d1, d2);
		loadImages(imgNames);				// get the images required, set in the necessary interface
		this.bkgdName = bkgdName;
		this.logoName = logoName;
		surfaceCreated();					// create surface
	}
	
	public TicTacToeMenu(Pair<Integer> dims, String[] imgNames, String bkgdName, String logoName) {
		super(dims);						// build base surface
		this.dims = dims;
		loadImages(imgNames);				// get the images required, set in the necessary interface
		this.bkgdName = bkgdName;
		this.logoName = logoName;
		surfaceCreated();					// create surface
	}
	
	/***** METHODS *****/
	// loads the images in for the buttons
	private void loadImages(String[] imgNames) {
		for (int i = 0; i < BUTTON_COUNT; i++) {
			if (this.getClass().getResource(imgNames[i]) != null) {
				Image img = new ImageIcon(this.getClass().getResource(imgNames[i])).getImage();
				imageList.add(img);
			} else {
				imageList.add(null);
				System.err.println("Menu Surface Error: Could not access image.");
			}
		}
	}
	
	@Override
	public void surfaceCreated() {
		// set icon
		ImageIcon icon = new ImageIcon(getClass().getResource(logoName));
		setIconImage(icon.getImage());
		
		title = new ImageButton(imageList.get(0));
		title.addMouseListener(this);
		play = new ImageButton(imageList.get(1));
		play.addMouseListener(this);
		quit = new ImageButton(imageList.get(2));
		quit.addMouseListener(this);
		
		// add to list
		buttonList.add(title);
		buttonList.add(play);
		buttonList.add(quit);
		
		backgroundImage = getImage(bkgdName, dims.getV1(), dims.getV2());
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
				
		add(title);
		add(play);
		add(quit);
		setLayout();
		setTitle("TicTacToe!");
		pack();
	}
	
	@Override
	protected void setLayout() {
		SpringLayout l1 = new SpringLayout();
		
		l1.putConstraint(SpringLayout.NORTH, title, TITLE_PAD, SpringLayout.NORTH, this);
		l1.putConstraint(SpringLayout.HORIZONTAL_CENTER, title, 0, SpringLayout.HORIZONTAL_CENTER, this);
		
		l1.putConstraint(SpringLayout.NORTH, buttonList.get(1), TITLE_PAD, SpringLayout.SOUTH, buttonList.get(0));
		l1.putConstraint(SpringLayout.WEST, buttonList.get(1), 0, SpringLayout.WEST, buttonList.get(0));
		for (int i = 2; i < buttonList.size(); i++) {
			l1.putConstraint(SpringLayout.NORTH, buttonList.get(i), SUBTITLE_PAD, SpringLayout.SOUTH, buttonList.get(i - 1));
			l1.putConstraint(SpringLayout.WEST, buttonList.get(i), 0, SpringLayout.WEST, buttonList.get(i - 1));
		}
		setLayout(l1);
	}
	
	@Override
	public void mousePressed(MouseEvent m) {
		if (m.getSource() == play) {			// play, initialize Game Surface
			System.out.println("playing...");
			setVisible(false);
			new TicTacToeDifficulty(this, dims);
			
		}
		else if (m.getSource() == quit) {	// quit
			System.out.println("quitting...");
			System.exit(0);
		}
	}
	
	@Override
	public void surfaceChanged() { }

	@Override
	public void surfaceDestroyed() { }

	@Override
	public void mouseClicked(MouseEvent e) { }

	@Override
	public void mouseEntered(MouseEvent m) { }

	@Override
	public void mouseExited(MouseEvent m) { }

	@Override
	public void mouseReleased(MouseEvent e) { }
}
