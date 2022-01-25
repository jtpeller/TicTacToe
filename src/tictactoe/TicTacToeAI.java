package tictactoe;

import java.util.Random;

/**
 * TicTacToeAI: Plays tic tac toe, interacting with the board to choose moves
 * 	Difficulty is set by the caller. Values for difficulty must be:
 * 		1 -> Easy
 * 		2 -> Normal
 * 		3 -> Hard
 * 		4 -> Machine Learning		// this is yet to be implemented
 * @author JP
 * @date January 03, 2021
 */
public class TicTacToeAI {
	/***** ATTRIBUTES *****/
	private final int EASY = 1,		// easy difficulty
					  NORMAL = 2,	// normal difficulty
					  HARD = 3,		// hard difficulty
					  AI = 4,		// AI difficulty
					  AIvsAI = 5;	// AI faces another AI
	private int diff;				// difficulty value passed into constructor
	private char symbol;			// symbol to denote the AI's turn
	private char player;			// symbol to denote the player
	
	private final int NORMAL_CHANCE = 4;		// 4/10 times it will look for better moves
	private final int HARD_CHANCE = 8;			// 8/10 times it will look for better moves
	
	private TicTacToeGrid grid;		// tic tac toe grid
	
	/***** CONSTRUCTORS *****/
	public TicTacToeAI(TicTacToeGrid grid, int diff, char symbol) {
		if (diff >= EASY && diff <= AI) {		// valid difficulties only
			this.diff = diff;
			this.grid = grid;
			this.symbol = symbol;
			this.player = toggle(symbol);
		}
		else if (diff == AIvsAI) {
			this.diff = diff;
			this.grid = grid;
			this.symbol = symbol;
			this.player = toggle(symbol);
		}
		else {								// invalid difficulty
			System.err.println("Error in TicTacToeAI: Difficulty must be 1, 2, 3, or 4");
			System.exit(0);
		}
	}
	
	/***** METHODS *****/
	// based on difficulty; the AI will perform its turn
	public void doTurn() {
		switch (diff) {
		case EASY: 			// randomly pick a valid spot
			easyTurn();
			break;
		case NORMAL: 		// randomly pick a valid spot. Searches for blocks & wins first
			normalTurn();
			break;
		case HARD:			// searches for blocks & plays "strategically"
			hardTurn();
			break;
		case AI:			// learns how to play properly. based on the minimax algorithm
		case AIvsAI:
			aiTurn();
			break;
		default:			// this shouldn't happen ever. But just in case...
			System.err.println("Error occurred in doTurn(). Invalid difficulty");
			System.exit(0);
		}
	}
	
	private void easyTurn() {
		System.out.println("easy turn performed...");
		Random r = new Random();
		int row;		// row to set value
		int col;		// col to set value
		do {
			row = r.nextInt(grid.getGridSize());
			col = r.nextInt(grid.getGridSize());
		} while (grid.getValueAt(row, col) != null);		// keep generating coords until that square is empty
		
		// set the value
		grid.setValueAt(symbol, row, col);
		
	}
	
	private void normalTurn() {
		System.out.println("normal turn performed...");
		
		// first decide if this turn it will search for blocks & wins. there is a modifiable percentage attribute
		Random r = new Random();
		int chance = r.nextInt(10);
		if (chance < NORMAL_CHANCE) {
			Pair<Integer> move = findBestMove();
			grid.setValueAt(symbol, move.getV1(), move.getV2());
		}
		else {
			easyTurn();
		}
	}
	
	private void hardTurn() {
		System.out.println("hard turn performed...");
		Random r = new Random();
		int chance = r.nextInt(10);
		if (chance < HARD_CHANCE) {
			Pair<Integer> move = findBestMove();
			grid.setValueAt(symbol, move.getV1(), move.getV2());
		}
		else {
			easyTurn();
		}
	}
	
	private void aiTurn() {
		System.out.println("AI turn performed...");
		Pair<Integer> move = findBestMove();
		grid.setValueAt(symbol, move.getV1(), move.getV2());
	}
	

	/***** HELPER METHODS *****/
	/**
	 * Returns the best possible move for the AI (denoted by symbol)
	 * @return	coordinates of the best move.
	 */
	private Pair<Integer> findBestMove() {
		int bestVal = -1000;
		Pair<Integer> bestMove = new Pair<Integer>(-1, -1);

		// search all cells & evaluate minimax to find optimal cell.
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {

				if (grid.getValueAt(i,j) == null) {		// Check if cell is empty
					grid.setValueAt(symbol, i, j);			// perform move
					
					int moveVal = minimax(0, false);		// evaluate minimax
					grid.setValueAt(null, i, j);			// undo move

					if (moveVal > bestVal) {				// find best move
						bestMove.setV1(i);
						bestMove.setV2(j);;
						bestVal = moveVal;
					}
				}
			}
		}

		System.out.printf("The value of the best Move " + "is : %d\n\n", bestVal);
		return bestMove;
	}
	
	/**
	 * Checks if there are moves remaining.
	 * @return	true if there are moves remaining. false otherwise
	 */
	private Boolean isMovesLeft() {
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				if (grid.checkValueAt(null, i, j))
					return true;
		return false;
	}
	
	/**
	 * Evaluates the "score" of a move. 
	 * @return	10 for a move benefitting the AI. -10 for a move benefitting the player. 0 otherwise.
	 */
	private int evaluate() {
		// Checking for Rows for X or O victory.
		for (int row = 0; row < 3; row++) {
			if (grid.checkRow(row)) {
				if (grid.getValueAt(row, 0) == (Object)symbol)
					return +10;
				else if (grid.getValueAt(row, 0) == (Object)player)
					return -10;
			}
		}

		// Checking for Columns for X or O victory.
		for (int col = 0; col < 3; col++) {
			if (grid.checkCol(col)) {
				if (grid.checkValueAt(symbol, 0, col))
					return +10;
				else if (grid.checkValueAt(player, 0, col))
					return -10;
			}
		}

		// Checking for Diagonals for X or O victory.
		if (grid.checkTopDiag()) {
			if (grid.checkValueAt(symbol, 0, 0))
				return +10;
			else if (grid.checkValueAt(player, 0, 0))
				return -10;
		}
		if (grid.checkBottomDiag()) {
			if (grid.checkValueAt(symbol, 0, 2))
				return +10;
			else if (grid.checkValueAt(player, 0, 2))
				return -10;
		}
		
		return 0;			// no winner. return 0 score.
	}
	
	/**
	 * Minimax method. Checks all possible ways the game can go.
	 * 
	 * @param depth		how deep to search
	 * @param isMax		is it the maximizer's turn?
	 * @return			the value of the board
	 */
	private int minimax(int depth, Boolean isMax) {
		int score = evaluate();

		// maximizer won the game. return evaluated score.
		if (score == 10)
			return score;
		// minimizer won the game. return evaluated score
		if (score == -10)
			return score;
		// no more moves, cat's game.
		if (isMovesLeft() == false)
			return 0;
		
		// If this maximizer's move
		if (isMax) {
			int best = -1000;

			// Traverse all cells
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					// Check if cell is empty
					if (grid.getValueAt(i,j) == null) {
						grid.setValueAt(symbol, i, j);		// perform move

						// choose next best value
						best = Math.max(best, minimax(depth + 1, !isMax));

						// Undo the move
						grid.setValueAt(null, i, j);
					}
				}
			}
			return best;
		}

		// If this minimizer's move
		else {
			int best = 1000;

			// Traverse all cells
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					// Check if cell is empty
					if (grid.checkValueAt(null, i, j)) {
						grid.setValueAt(player, i, j);			// Make the move
						best = Math.min(best, minimax(depth + 1, !isMax));	// choose min value
						grid.setValueAt(null, i, j);			// Undo the move
					}
				}
			}
			return best;
		}
	}
	
	/***** UTILITY METHODS *****/
	// toggles the symbol between 'X' & 'O'
	private char toggle(char t) {
		if (t == 'X')
			return 'O';
		else
			return 'X';
	}
}
