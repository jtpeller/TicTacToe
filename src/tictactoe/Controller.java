package tictactoe;

public class Controller {
    private static final Pair<Integer> dims = new Pair<Integer>(700, 700);

    private static final String[] IMG_PATHS = {"title.png", "play.png", "quit.png"};

    /***** MAIN *****/
    public static void main(String[] args) {
        new TicTacToeMenu(dims, IMG_PATHS, "bkgd.png", "logo.png");
    }
}
