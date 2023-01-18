public class Board {

    private char[][] board;
    private char turn;
    private int moves;

    public Board(char turn) {
        board = new char[8][8];
        setTurn(turn); 
        moves = 64;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = '-';
            }
        }
    }

    public Board(Board b){
        board = new char[8][8];
        moves = b.getMoves();
  
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                 board[i][j] = b.getBoard()[i][j];
             }
         }
         turn = b.getTurn();
    }

    public final char[][] getBoard() {
        return board;
    }

    /**
     * Returns who's turn it is to move 
     * C = Computer | H = Human
     * @param c C or H
     */
    public char getTurn() {
        return turn;
    }

    public int getMoves() {
        return moves;
    }

    private void setTurn(char c) {
        turn = c;
    }

    public void algoMove(int i, int j, char c) {
        board[i][j] = c;
    }

    public boolean tryMove(int[] move, char c) {
        int row = move[0], col = move[1];
        if ( (row > 7 || row < 0) || (col > 7 || col < 0) || board[row][col] != '-' ) {
            return false;
        }
        return true;
    }

    public boolean humanMove(char i, int j) {
        int row = i - 65, col = j - 1;
        if ( (row > 7 || row < 0) || (col > 7 || col < 0) || board[row][col] != '-' ) {
            return false;
        }
        board[row][col] = 'O';
        setTurn('C');
        moves--;
        return true;
    }

    public boolean AIMove(int i, int j) {
        int row = i, col = j;
        if ( (row > 7 || row < 0) || (col > 7 || col < 0) || board[row][col] != '-' ) {
            return false;
        }
        board[row][col] = 'X';
        setTurn('H');
        moves--;
        return true;
    }


    public int eval(char c) {
        char other = (c == 'X') ? 'O' : 'X';
        if (hasWon(c)) {
            return 100000000;
        }

        if (hasWon(other)) {
            return -100000000;
        }

        int score2 = scoreTwo(c) * 10, score3 = scoreThree(c) * 10000;
        int other2 = scoreTwo(other) * 10, other3 = scoreThree(other) * 10000;
        return score2 + score3 - other3 - other2;
    }

    public boolean isTerminalState() {
        return (moves <= 0 || hasWon('O') || hasWon('X'));
    }


    public int scoreTwo(char c) {
        int score = 0;
        for (int j = 0; j < 5; j++) {
            for (int i = 0; i < 8; i++) { 
                if ( (this.board[i][j] == c && this.board[i][j+1] == c && this.board[i][j+2] == '-' && this.board[i][j+3] == '-') ||
                     (this.board[i][j] == c && this.board[i][j+1] == '-' && this.board[i][j+2] == '-' && this.board[i][j+3] == c) ||
                     (this.board[i][j] == '-' && this.board[i][j+1] == '-' && this.board[i][j+2] == c && this.board[i][j+3] == c) ||
                     (this.board[i][j] == '-' && this.board[i][j+1] == c && this.board[i][j+2] == c && this.board[i][j+3] == '-') ) 
                {
                    if (j > 1 && board[i][j-2] == '-' && board[i][j-1] == '-') {
                        score += 2;
                    } else {
                        score++;
                    }
                }

                if (j < 4 && (board[i][j+2] == c && board[i][j+3] == c && board[i][j] == '-' && board[i][j+1] == '-')) {
                    score++;
                }

                if (j == 4) {
                    if( (board[i][j+1] == c && board[i][j+2] == c && board[i][j] == '-' && board[i][j+3] == '-') ||
                      (board[i][j+1] == c && board[i][j+3] == c && board[i][j] == '-' && board[i][j+2] == '-') ||
                      (board[i][j+2] == c && board[i][j+3] == c && board[i][j] == '-' && board[i][j+1] == '-') ) 
                    {
                        score++;
                    }
                }
            }
        }

        for (int i = 0; i < 5 ; i++ ) {
            for (int j = 0; j < 8; j++) {
                if ( (this.board[i][j] == c && this.board[i+1][j] == c && this.board[i+2][j] == '-' && this.board[i+3][j] == '-') ||
                     (this.board[i][j] == c && this.board[i+1][j] == '-' && this.board[i+2][j] == '-' && this.board[i+3][j] == c) ||
                     (this.board[i][j] == '-' && this.board[i+1][j] == '-' && this.board[i+2][j] == c && this.board[i+3][j] == c) ||
                     (this.board[i][j] == '-' && this.board[i+1][j] == c && this.board[i+2][j] == c && this.board[i+3][j] == '-') ) 
                {
                    if (i > 1 && board[i-2][j] == '-' && board[i-1][j] == '-') {
                        score+=2;
                    } else {
                        score++;
                    }
                }
                

                if (i < 4 && (board[i+2][j] == c && board[i+3][j] == c && board[i][j] == '-' && board[i+1][j] == '-')) {
                    score++;
                }
                
                if (i == 4) {
                    if( (board[i+1][j] == c && board[i+2][j] == c && board[i][j] == '-' && board[i+3][j] == '-') ||
                        (board[i+1][j] == c && board[i+3][j] == c && board[i][j] == '-' && board[i][j] == '-') ||
                        (board[i+2][j] == c && board[i+3][j] == c && board[i][j] == '-' && board[i+1][j] == '-') )
                    {
                        score++;
                    }
                }
            }
        }

        return score;
    }

    public int scoreThree(char c) {
        int score = 0;
        for (int j = 0; j < 5; j++ ) {
            for (int i = 0; i < 8; i++) { 
                if ((this.board[i][j] == c && this.board[i][j+1] == c && this.board[i][j+2] == c && this.board[i][j+3] == '-') ||
                    (this.board[i][j] == c && this.board[i][j+1] == c && this.board[i][j+2] == '-' && this.board[i][j+3] == c) ||
                    (this.board[i][j] == c && this.board[i][j+1] == '-' && this.board[i][j+2] == c && this.board[i][j+3] == c) ||
                    (this.board[i][j] == '-' && this.board[i][j+1] == c && this.board[i][j+2] == c && this.board[i][j+3] == c)) 
                {
                    if (j > 0 && board[i][j-1] == '-') {
                        score += 2;
                    } else {
                        score++;
                    }
                }

                if (j == 4 && (board[i][j+1] == c && board[i][j+2] == c && board[i][j+3] == c && board[i][j] == '-')) {
                     score++;
                }
            }
        }

        for (int i = 0; i < 5 ; i++ ){
            for (int j = 0; j < 8; j++){
                if ((this.board[i][j] == c && this.board[i+1][j] == c && this.board[i+2][j] == c && this.board[i+3][j] == '-') ||
                    (this.board[i][j] == c && this.board[i+1][j] == c && this.board[i+2][j] == '-' && this.board[i+3][j] == c) ||
                    (this.board[i][j] == c && this.board[i+1][j] == '-' && this.board[i+2][j] == c && this.board[i+3][j] == c) ||
                    (this.board[i][j] == '-' && this.board[i+1][j] == c && this.board[i+2][j] == c && this.board[i+3][j] == c)) 
                {
                    if (i > 0 && board[i-1][j] == '-') {
                        score+=2;
                    } else {
                        score++;
                    }
                }
                
                if (i == 4 && (board[i+1][j] == c && board[i+2][j] == c && board[i+3][j] == c && board[i][j] =='-')) {
                    score++;
                }
            }
        }

        return score;
    }

    public boolean hasWon(char c) {
        // Check horizontal win 
        for (int j = 0; j<  5; j++ ){
            for (int i = 0; i < 8; i++){
                if (this.board[i][j] == c && this.board[i][j+1] == c && this.board[i][j+2] == c && this.board[i][j+3] == c) {
                    return true;
                }           
            }
        }
       
        // Check vertical win
        for (int i = 0; i< 5 ; i++ ){
            for (int j = 0; j< 8; j++){
                if (this.board[i][j] == c && this.board[i+1][j] == c && this.board[i+2][j] == c && this.board[i+3][j] == c) {
                    return true;
                }           
            }
        }
        
        return false;
    }
    
    public void printBoard() {
        System.out.println("  1 2 3 4 5 6 7 8");
        for (int i = 0; i < 8; i++) {
            System.out.print( (char) ('A' + i) + " ");
            for (int j = 0; j < 8; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

}
