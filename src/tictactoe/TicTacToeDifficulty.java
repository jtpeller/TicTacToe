package tictactoe;

import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.SpringLayout;

public class TicTacToeDifficulty extends Surface implements MouseListener {
	private JLabel difficultyPrompt = new JLabel("Choose your difficulty:");
	private ArrayList<ImageButton> buttonList = new ArrayList<ImageButton>();
	private TicTacToeMenu menu;
	
	private String[] buttonText = {"2-Player", "Easy AI", "Normal AI", "Hard AI", "Unbeatable AI", "Unbeatable vs Unbeatable", "Back to Menu"};
	
	// Fonts
	private Font f1 = new Font("Serif", Font.BOLD, 56),
				 f2 = new Font("Serif", Font.PLAIN, 36);
	
	
	private String backgroundImageName = "bkgd.png";
	
	private final int MULTIPLAYER = 0,
					  EASY = 1,
			  		  NORMAL = 2,
			  		  HARD = 3,
			  		  AI = 4,
			  		  AIvsAI = 5,
			  		  TITLE_PAD = 50,
			  		  SUBTITLE_PAD = 25;
	
	/***** CONSTRUCTORS *****/
	public TicTacToeDifficulty(TicTacToeMenu menu, Pair<Integer> d1) {
		super(d1);
		System.out.println("difficulty...");
		this.menu = menu;
		surfaceCreated();	
	}
	
	@Override
	public void surfaceCreated() {
		// colors
		setBackground(backgroundImageName, surfaceDims);
		setForeground(surfaceForeground);
		
		// initialize prompt
		difficultyPrompt.setFont(f1);
		add(difficultyPrompt);
		
		// initialize difficulty buttons
		for (int i = 0; i < buttonText.length; i++) {
			buttonList.add(new ImageButton(buttonText[i]));		// create buttons
			buttonList.get(i).addMouseListener(this);
			buttonList.get(i).setFont(f2);
			add(buttonList.get(i));								// add to this
		}
		
		// layout & pack
		setLayout();
		setTitle("TicTacToe!");
		pack();
	}
	
	@Override
	protected void setLayout() {
		SpringLayout l1 = new SpringLayout();
		
		l1.putConstraint(SpringLayout.NORTH, difficultyPrompt, TITLE_PAD, SpringLayout.NORTH, this);
		l1.putConstraint(SpringLayout.HORIZONTAL_CENTER, difficultyPrompt, 0, SpringLayout.HORIZONTAL_CENTER, this);
		
		l1.putConstraint(SpringLayout.NORTH, buttonList.get(0), TITLE_PAD, SpringLayout.SOUTH, difficultyPrompt);
		l1.putConstraint(SpringLayout.WEST, buttonList.get(0), SUBTITLE_PAD, SpringLayout.WEST, this);
		for (int i = 1; i < buttonList.size(); i++) {
			l1.putConstraint(SpringLayout.NORTH, buttonList.get(i), SUBTITLE_PAD, SpringLayout.SOUTH, buttonList.get(i - 1));
			l1.putConstraint(SpringLayout.WEST, buttonList.get(i), 0, SpringLayout.WEST, buttonList.get(i - 1));
		}
		setLayout(l1);
	}
	
	@Override
	public void mousePressed(MouseEvent m) {
		if (buttonList.contains(m.getSource())) {
			System.out.print("Difficulty chosen: ");
			int diff = -1;
			int idx = getIndex(m, buttonList);
			switch (idx) {
			case 0:
				System.out.println("Multiplayer...");
				diff = MULTIPLAYER;
				break;
			case 1:
				System.out.println("Easy...");
				diff = EASY;
				break;
			case 2:
				System.out.println("Normal...");
				diff = NORMAL;
				break;
			case 3:
				System.out.println("Hard...");
				diff = HARD;
				break;
			case 4:
				System.out.println("Impossible...");
				diff = AI;
				break;
			case 5:
				System.out.println("Unbeatable AI vs Unbeatable AI");
				diff = AIvsAI;
				break;
			case 6:
				System.out.println("Back to menu...");
				menu.setVisible(true);
				dispose();
				return;
			default:
				System.err.println("Difficulty Error: Improper difficulty setting...");
				System.out.println("Setting to difficulty to normal");
				diff = NORMAL;
				break;
			}
			if (diff != -1) {
				dispose();
				new TicTacToeSurface(menu, surfaceDims, diff);
			}
		}
	}
	
	private int getIndex(MouseEvent m, ArrayList<ImageButton> list) {
		for (int i = 0; i < list.size(); i++) {
			if (m.getSource() == list.get(i)) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public void mouseClicked(MouseEvent m) { }

	@Override
	public void mouseEntered(MouseEvent m) { }

	@Override
	public void mouseExited(MouseEvent m) { }

	@Override
	public void mouseReleased(MouseEvent m) { }

	@Override
	public void surfaceChanged() { }

	@Override
	public void surfaceDestroyed() { }
}
