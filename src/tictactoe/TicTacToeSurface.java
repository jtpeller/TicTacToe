package tictactoe;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SpringLayout;
import javax.swing.JOptionPane;

public class TicTacToeSurface extends Surface implements MouseListener {
	/***** ATTRIBUTES *****/
	// Game constants
	private char player = 'X',			// player's symbol, which is always X, because I'm lazy
				 opponent = 'O',		// AI's symbol, which is always O, because I'm lazy
				 turn;					// who's turn is it?
	private int diff;					// difficulty
	private int lastRow;				// row of last move, for undo functionality
	private int lastCol;				// column of last move, used for undo functionality
	private boolean gameEnd = false;	// decides if the game has been won/tied.
	private TicTacToeGrid grid;			// 3x3 grid for tic-tac-toe
	private TicTacToeMenu menu;			// menu of the game
	private TicTacToeAI ai;				// opponent for easy thru AI difficulties.
	private TicTacToeAI ai2;			// replaces player if diff == AIvsAI
	
	private final int MULTI = 0,
					  EASY = 1,
					  NORMAL = 2,
					  HARD = 3,
					  IMPOSSIBLE = 4,
					  AIvsAI = 5;
	
	private final int GAME_COUNT = 1000;	// number of games played between the AI.
	
	// Sizes & Dimensions
	private final int IMG_WIDTH = 40,	// width of the image buttons
			  		  IMG_HEIGHT = 40,	// height of the image buttons
			  		  GRID_SIZE = 3,	// # rows & cols of the grid
			  		  GRID_DIMS = 400,	// dimensions of the grid
			  		  SMALL_PAD = 10,	// small padding for layout
			  		  MEDIUM_PAD = 25;	// medium padding for layout
	// Labels
	private ArrayList<JLabel> labels = new ArrayList<JLabel>();
	private JLabel title = new JLabel("TicTacToe!"),			// main title label
			   	   subtitle = new JLabel(),						// displays difficulty
			   	   infoTitle = new JLabel();					// gives info about the game
	// Images
	private ArrayList<ImageButton> imgList = new ArrayList<ImageButton>();					// image button list
	private String[] imgNames = {"undo.png", "retry.png", "toMenu.png"},					// images for image buttons
					 toolTips = {"Undo Last Move", "Play another game", "Back to Menu"};	// tooltips to set
	// Fonts
	private Font f1 = new Font("Serif", Font.BOLD, 40),			// large font, for title
			 	 f2 = new Font("Serif", Font.PLAIN, 28),		// medium font, for difficulty
			 	 f3 = new Font("Serif", Font.ITALIC, 24);		// small font, for info
	// Strings
	private String backgroundImageName = "bkgd.png",			// background image name
				   iconName = "logo.png";						// logo image name
	
	/***** CONSTRUCTORS *****/
	// Takes a menu, dimension pair, and K, the difficulty
	public TicTacToeSurface(TicTacToeMenu menu, Pair<Integer> dimensions, int K) {
		super(dimensions);
		this.menu = menu;
		this.diff = K;
		surfaceCreated();
		setLayout();
		if (diff == AIvsAI)
			runSimulation();
	}
	
	/***** METHODS *****/
	@Override
	public void surfaceCreated() {
		// primary frame stuff
		setBackground(backgroundImageName, surfaceDims);
		ImageIcon icon = new ImageIcon(getClass().getResource(iconName));
		setIconImage(icon.getImage());
		
		// initialize grid
		grid = new TicTacToeGrid(GRID_SIZE, GRID_DIMS);
		if (diff != AIvsAI)
			grid.addMouseListener(this);
		
		// set difficulty & decide who goes first
		decideWhoGoesFirst();
		setDifficulty();
		
		// initialize appearance
		setLabels();
		setImages();
		
		// Add everything to panel
		panel.add(grid);								// add the grid
		for (int i = 0; i < imgList.size(); i++)		// add images
			panel.add(imgList.get(i));
		for (int i = 0; i < labels.size(); i++)			// add labels
			panel.add(labels.get(i));
		
		// frame & stuff
		setLayout();
		setResizable(false);
		setTitle("TicTacToe!");
		pack();
	}
	
	@Override
	public void mousePressed(MouseEvent m) {
		if (m.getSource() == grid)
			gridClicked(m);
		if (imgList.contains(m.getSource()))
			imageClicked(m);
	}
	
	/***** IMPORTANT GAME METHODS *****/
	/**
	 * Performs behaviors based on where the grid was clicked.
	 * @param m
	 */
	private void gridClicked(MouseEvent m) {
		if (gameEnd == false) {
			// Initializations
			System.out.println("Click on the grid occurred...");
			int row = grid.rowAtPoint(m.getPoint());
			int col = grid.columnAtPoint(m.getPoint());
			
			// Check if cell is a viable choice
			if (!isEditable(row, col)) {
				infoTitle.setText("Choose a valid location");
				return;
			}
			else if (row >= 0 && col >= 0) { 			// set that value
				grid.setValueAt(turn, row, col);
				lastRow = row;
				lastCol = col;
			}
			
			// Check for winners
			checkGameEnd();
			if (gameEnd == false) {			// if the game has yet to end, allow another move.
				if (ai == null)					// 2-player mode. no AI exists
					turn = toggle(turn);
				else {							// AI mode. perform a turn.
					ai.doTurn();
					checkGameEnd();
				}
			}
		}
	}
	
	/**
	 * Executes behavior based on which image was clicked.
	 * @param m		mouse event
	 */
	private void imageClicked(MouseEvent m) {
		int idx = getIndex(m, imgList);
		switch (idx) {
		case 0:
			undo();
			break;
		case 1:
			retry();
			break;
		case 2:
			menu.setVisible(true);
			dispose();
			break;
		default:
			System.err.println("Error: Problem encountered with the image set...");
			break;
		}
	}
	
	// decides (by RNG) whether X or O shall go first.
	private void decideWhoGoesFirst() {
		Random r = new Random();
		boolean which = r.nextBoolean();		// figuratively flip a coin.
		if (which)
			turn = 'X';
		else 
			turn = 'O';
		infoTitle.setText("It's " + turn + "'s turn");
	}
	
	private void runSimulation() {
		int tieCount = 0;
		int playerWins = 0;
		int opponentWins = 0;
		for (int i = 1; i <= GAME_COUNT; i++) {
			while (grid.checkWinner() == ' ' && grid.checkCatsGame() == false) {
				if (turn == opponent) {
					ai.doTurn();
					turn = toggle(turn);
				}
				else if (diff == AIvsAI && turn == player) {
					ai2.doTurn();
					turn = toggle(turn);
				}
			}
			if (grid.checkCatsGame()) {
				System.out.println("Game #" + i + " resulted in a tie.");
				tieCount++;
			}
			if (grid.checkWinner() == player) {
				System.out.println("Game #" + i + " resulted in a win by " + player + ".");
				playerWins = 0;
			}
			if (grid.checkWinner() == opponent) {
				System.out.println("Game #" + i + " resulted in a win by " + player + ".");
				opponentWins = 0;
			}
		}
		
		// format stats of the simulation
		String out = String.format("Game count -- %d. Games won by %s -- %d. Games won by %s -- %d. Games tied -- %d.",
		 GAME_COUNT, opponent, opponentWins, player, playerWins, tieCount);
		JOptionPane.showMessageDialog(null, out);
	}
		
	
	/***** UTILITY METHODS *****/
	// checks if a cell is "editable"; as in, it has yet to be set.
	private boolean isEditable(int row, int col) {
		Object[][] puzzle = grid.getData();
		if (puzzle[row][col] != null)		// location has already been changed. do not allow edits
			return false;
		else								// location is "editable"
			return true;
	}
	
	// simple method to change t from X to O or O to X
	private char toggle(char t) {
		if (t == 'X') {
			infoTitle.setText("It's O's turn");
			return 'O';
		}
		else {
			infoTitle.setText("It's X's turn");
			return 'X';
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
	
	// creates an AI opponent
	private TicTacToeAI generateAI(char aiSymbol) {
		TicTacToeAI ai = new TicTacToeAI(grid, diff, aiSymbol);
		if (turn == opponent) {
			ai.doTurn();
			turn = toggle(turn);
		}
		return ai;
	}
	
	// undoes the last turn.
	private void undo() {
		if (lastRow != -1 && lastCol != -1) {
			grid.setValueAt(null, lastRow, lastCol);
			turn = toggle(turn);
			infoTitle.setText("It is " + turn + "'s turn");
			gameEnd = false;			// in case it was the last turn of the game
			return;
		}
		infoTitle.setText("A move must be made first");
	}
	
	// resets the game board for a new game.
	private void retry() {
		grid.clear();
		gameEnd = false;
		decideWhoGoesFirst();
		setDifficulty();
	}
	
	// checks if the game has been completed
	private void checkGameEnd() {
		char winner = grid.checkWinner();
		if (winner != ' ') {
			infoTitle.setText(winner + " won the game");
			gameEnd = true;
		}
		else if (grid.checkCatsGame()) {
			infoTitle.setText("Cat's Game! No winner.");
			gameEnd = true;
		}
	}
	
	/***** GETTERS & SETTERS *****/
	private void setLabels() {
		// set fonts
		title.setFont(f1);
		subtitle.setFont(f2);
		infoTitle.setFont(f3);
		
		// add to list
		labels.add(title);
		labels.add(subtitle);
		labels.add(infoTitle);
	}
	
	private void setImages() {
		// get circle images
 		for (int i = 0; i < imgNames.length; i++) {
 			Image temp = getImage(imgNames[i], IMG_WIDTH, IMG_HEIGHT);
			if (temp != null) {
				imgList.add(new ImageButton(temp));
				imgList.get(i).setToolTipText(toolTips[i]);
				imgList.get(i).addMouseListener(this);
				imgList.get(i).setVisible(true);
			}
			else {
				System.err.println("Error in initImages(). Image could not be found, or it could not be decoded");
			}
 		}
	}
	
	@Override
	protected void setLayout() {
		SpringLayout l1 = new SpringLayout();
		
		// layout labels
		l1.putConstraint(SpringLayout.NORTH, title, SMALL_PAD, SpringLayout.NORTH, panel);
		l1.putConstraint(SpringLayout.HORIZONTAL_CENTER, title, 0, SpringLayout.HORIZONTAL_CENTER, panel);
		for (int i = 1; i < labels.size(); i++) {
			l1.putConstraint(SpringLayout.NORTH, labels.get(i), SMALL_PAD, SpringLayout.SOUTH, labels.get(i - 1));
			l1.putConstraint(SpringLayout.HORIZONTAL_CENTER, labels.get(i), 0, SpringLayout.HORIZONTAL_CENTER, labels.get(i - 1));			
		}
		
		// layout grid
		l1.putConstraint(SpringLayout.HORIZONTAL_CENTER, grid, 0, SpringLayout.HORIZONTAL_CENTER, labels.get(0) );
		l1.putConstraint(SpringLayout.NORTH, grid, MEDIUM_PAD, SpringLayout.SOUTH, labels.get(labels.size() - 1));
		
		// layout images
		for (int i = 0; i < imgList.size(); i++) {
			l1.putConstraint(SpringLayout.NORTH, imgList.get(i), MEDIUM_PAD, SpringLayout.SOUTH, grid);
			l1.putConstraint(SpringLayout.HORIZONTAL_CENTER, imgList.get(i), i * GRID_DIMS / GRID_SIZE + grid.getRowHeight() / 2, SpringLayout.WEST, grid);
		}
		
		// commit to panel
		panel.setLayout(l1);
	}
	
	// performs some initializations based on difficulty
	// the side effect of this method is that it generates the AIs. For AIvsAI, it will generate 
	private void setDifficulty() {
		switch (diff) {
		case MULTI:
			subtitle.setText("2-player game");
			break;
		case EASY:
			subtitle.setText("Difficulty: Easy AI");
			ai = generateAI(opponent);
			break;
		case NORMAL:
			subtitle.setText("Difficulty: Normal AI");	
			ai = generateAI(opponent);
			break;
		case HARD:
			subtitle.setText("Difficulty: Hard AI");
			ai = generateAI(opponent);
			break;
		case IMPOSSIBLE:
			subtitle.setText("Difficulty: Unbeatable AI");
			ai = generateAI(opponent);
			break;
		case AIvsAI:
			subtitle.setText("Two AIs face off!");
			ai = generateAI(opponent);
			ai2 = generateAI(player);
			break;
		default:
			subtitle.setText("Difficulty: Easy");
			diff = EASY;
			generateAI(opponent);
			break;
		}
	}
	
	/***** USELESS OVERRIDDEN METHODS *****/
	@Override
	public void surfaceChanged() { }

	@Override
	public void surfaceDestroyed() { }

	@Override
	public void mouseClicked(MouseEvent m) { }

	@Override
	public void mouseEntered(MouseEvent m) { }

	@Override
	public void mouseExited(MouseEvent m) { }
	
	@Override
	public void mouseReleased(MouseEvent m) { }
}
