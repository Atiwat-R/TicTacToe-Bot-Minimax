/**
 * Atiwat Rachatawrn
 * 29/3/2021
 */

import java.util.Scanner;

class Game2 {

    char[][] board;
    int nsize = 4; // Board size
    private static final char SYMBOL_X = '▒'; // ascii 177
    private static final char SYMBOL_O = '█'; // ascii 219
    private static final int MAX_DEPTH = 12;
    char turn = SYMBOL_X; // Decides who starts first 

    /**
     * Class constructor.
     */
    public Game2() {
        createBoard();
        System.out.println("Welcome to the game! Call newGame() to start playing.");
    }

    /**
     * Print board in a fashionable manner.
     */
    public void printBoard() {
        for (int row=0 ; row<nsize ; row++) 
        {
            System.out.print("| ");
            for (int col=0 ; col<nsize ; col++) 
            {
                System.out.print(this.board[row][col]);
                System.out.print(" | ");
            }
            System.out.print('\n');
        }
        System.out.print('\n');
    }

    /**
     * Replace current board with empty nsize x nsize board
     */
    public void createBoard() {
        char[][] startBoard = 
        {
        {'a','b','c','d'},
        {'e','f','g','h'},
        {'i','j','k','l'},
        {'m','n','o','p'}
        };
        this.board = startBoard;
    }

    /**
     * Replace posi with SYMBOL_X or SYMBOL_O. Return true if successful
     * Mostly for player's convenience. Minimax still inserts using {row,column} index
     */
    public boolean insert(String posiStr) {
        int posi = ((int) posiStr.charAt(0)) - 96;
        
        if (posi < 1 || posi > (nsize*nsize)) {
            System.out.println("\nIllegitimate value");
            return false;
        }
        int row = 0;
        if (posi > nsize*0) row = 0;        
        if (posi > nsize*1) row = 1;
        if (posi > nsize*2) row = 2;
        if (posi > nsize*3) row = 3;
        int index = (posi-1) % (nsize);
        if (this.board[row][index] != SYMBOL_X && this.board[row][index] != SYMBOL_O) {
            this.board[row][index] = this.turn;
            return true;
        }
        else {
            System.out.println("\nPosition already filled");
            return false;
        }
    }

    /**
     * Check if the game is over.
     * @return 10 if SYMBOL_X wins, -10 if SYMBOL_O wins, 0 if there's no victory (whether draw or the game is ongoing)
     */
    public int is_over() {
        // Horizontal win
        for (int i=0 ; i<nsize ; i++) {
            char is_same = board[i][0];
            if (board[i][0] == is_same && 
                board[i][1] == is_same && 
                board[i][2] == is_same &&
                board[i][3] == is_same) {
                    if (is_same == SYMBOL_X) return 10;
                    else return -10;
                }
        }
        // Vertical win
        for (int i=0 ; i<nsize ; i++) {
            char is_same = board[0][i];
            if (board[0][i] == is_same && 
                board[1][i] == is_same && 
                board[2][i] == is_same &&
                board[3][i] == is_same) {
                    if (is_same == SYMBOL_X) return 10;
                    else return -10;   
                }
        }
        // Diagonal win
        char is_same1 = board[0][0];
        if (board[0][0] == is_same1 &&
            board[1][1] == is_same1 &&
            board[2][2] == is_same1 &&
            board[3][3] == is_same1) {
                if (is_same1 == SYMBOL_X) return 10;
                else return -10;      
            }
        char is_same2 = board[0][2];
        if (board[0][3] == is_same2 &&
            board[1][2] == is_same2 &&
            board[2][1] == is_same2 &&
            board[3][0] == is_same2) {
                if (is_same2 == SYMBOL_X) return 10;
                else return -10;      
            }

        // No winner yet, return 0. Whether its a draw or its simply not over yet is irrelavant.
        return 0;
    }

    /**
     * Check if the board is full.
     * @return boolean
     */
    public boolean is_full() {
        for (int row=0 ; row<nsize ; row++) {
            for (int col=0 ; col<nsize ; col++) {
                if (board[row][col] != SYMBOL_X && board[row][col] != SYMBOL_O) {
                    return false; // If a blank spot is found, it's not over yet
                }
            }
        }
        return true;
    }

    /**
     * Minimax algorithm with Alpha-Beta pruning.
     * Used to find next optimal move.
     * @param depth
     * @param alpha
     * @param beta
     * @param isMax
     * @return
     */
    public int minimax(int depth, int alpha, int beta, boolean isMax) {
        int result = this.is_over();
        if (Math.abs(result) == 10 || depth == 0 || !this.is_full()) return result;

        if (isMax) 
        {
            int maxheu = Integer.MIN_VALUE;
            for (int row=0 ; row<nsize ; row++) {
                for (int col=0 ; col<nsize ; col++) {
                    char temp = board[row][col];
                    if (temp != SYMBOL_X && temp != SYMBOL_O) {
                        board[row][col] = SYMBOL_X;
                        maxheu = Math.max(maxheu, minimax(depth-1, alpha, beta, false));
                        board[row][col] = temp;
                        
                        alpha = Math.max(alpha, maxheu);
                        if (alpha >= beta) return maxheu;
                    }
                }
            }
            return maxheu;
        }
        else 
        {
            int minheu = Integer.MAX_VALUE;
            for (int row=0 ; row<nsize ; row++) {
                for (int col=0 ; col<nsize ; col++) {
                    char temp = board[row][col];
                    if (temp != SYMBOL_X && temp != SYMBOL_O) {
                        board[row][col] = SYMBOL_O;
                        minheu = Math.min(minheu, minimax(depth-1, alpha, beta, true));
                        board[row][col] = temp;
                        
                        alpha = Math.min(alpha, minheu);
                        if (beta <= alpha) return minheu;
                    }
                }
            }
            return minheu;
        }
    }

    /**
     * Use Minimax to obtain optimal next value.
     * @return int[] containing {optimal_x , optimal_y}
     */
    public int[] getNextMove() {
        int[] nextMove = new int[]{-1,-1}; // Keeps optimal play
        int[] viableMove = new int[]{-1,-1}; // Contingent for when nextmove = {-1,-1}. Always keep moves that are legal, though not optimal.
        int bestheu = Integer.MIN_VALUE;

        for (int row=0 ; row<nsize ; row++) {
            for (int col=0 ; col<nsize ; col++) {
                char temp = board[row][col];

                // If spot [row,col] has yet to be filled, proceed
                if (temp != SYMBOL_X && temp != SYMBOL_O) {
                    board[row][col] = SYMBOL_X;
                    int heu = minimax(MAX_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
                    board[row][col] = temp;

                    // Constantly update viableMove.
                    viableMove[0] = row;
                    viableMove[1] = col;
                    
                    // When found better move, update nextMove
                    if (heu > bestheu) {
                        nextMove[0] = row;
                        nextMove[1] = col;
                        bestheu = heu;
                    }
                }
            }
        }
        if (nextMove[0] == -1 || nextMove[1] == -1) return viableMove; // If optimal play is not found
        return nextMove; // return optimal move
    }

    /**
     * Start new game upon calling this function.
     */
    public void newGame() {
        createBoard();        
        System.out.println("Game Start!");            
        while (true) {

            // Give state of the game. Is always 0 if the winner has yet to be decided, or board is not full.
            int result = this.is_over();

            // Case the game is over. 
            if (result != 0) {
                printBoard();
                switch(result) {
                    case 10: 
                        System.out.print("Winner is ");
                        System.out.println(SYMBOL_X);
                        break;
                    case -10: 
                        System.out.print("Winner is ");
                        System.out.println(SYMBOL_O);
                        break;
                }
                break; // Finish game
            }

            // Case of draw. If the board is full and there's a winner, the above block would catch it. Reaching this would mean it is a draw.
            if (is_full()) {
                printBoard();
                System.out.println("It's a draw!");
                break;
            }

            // Player X's turn (human)
            if (this.turn == SYMBOL_X) {
                while (true) {
                    printBoard();
                    Scanner scan = new Scanner(System.in);
                    System.out.print("Select your move: ");
                    String input = scan.nextLine(); 
                    boolean act = this.insert(input);
                    if (act) break;
                }
                this.turn = SYMBOL_O;
            }
            // Player O's turn (Minimax)
            else {
                int[] nextMove = getNextMove();
                board[nextMove[0]][nextMove[1]] = SYMBOL_O;
                this.turn = SYMBOL_X;
            }
        }
    }
}


public class TicTacToe {
    public static void main(String args[]) {
        Game2 save1 = new Game2();
        save1.newGame();
    }
}





