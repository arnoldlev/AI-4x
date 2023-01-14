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
        //int row = i - 65, col = j - 1;
        int row = i, col = j;
        if ( (row > 7 || row < 0) || (col > 7 || col < 0) || board[row][col] != '-' ) {
            return false;
        }
        board[row][col] = 'X';
        setTurn('H');
        moves--;
        return true;
    }



    public int evaluate(char c) {
        char other = (c == 'X') ? 'O' : 'X';
        int score2 = checkTwos(c) * 500, score3 = checkThrees(c) * 5000;
        int other2 = checkTwos(other) * 500, other3 = checkThrees(other) * 5000;
        return score2 + score3 - other2 - other3;
    }

    public boolean isTerminalState() {
        return (moves <= 0 || winner() != 0);
    }


    public int scoreTwo(char c) {
        int score = 0;
        for (int j = 0; j < 5; j++ ) {
            for (int i = 0; i < 8; i++) { 
                if (   this.board[i][j] == c && this.board[i][j+1] == c && this.board[i][j+2] == '-' && this.board[i][j+3] == '-'
                    || this.board[i][j] == c && this.board[i][j+1] == '-' && this.board[i][j+2] == '-' && this.board[i][j+3] == c
                    || this.board[i][j] == '-' && this.board[i][j+1] == '-' && this.board[i][j+2] == c && this.board[i][j+3] == c 
                    || this.board[i][j] == '-' && this.board[i][j+1] == c && this.board[i][j+2] == c && this.board[i][j+3] == '-') {
                        score++;
                    }
            }
        }

        for (int i = 0; i< 5 ; i++ ){
            for (int j = 0; j< 8; j++){
                if (   this.board[i][j] == c && this.board[i+1][j] == c && this.board[i+2][j] == '-' && this.board[i+3][j] == '-'
                    || this.board[i][j] == c && this.board[i+1][j] == '-' && this.board[i+2][j] == '-' && this.board[i+3][j] == c
                    || this.board[i][j] == '-' && this.board[i+1][j] == '-' && this.board[i+2][j] == c && this.board[i+3][j] == c 
                    || this.board[i][j] == '-' && this.board[i+1][j] == c && this.board[i+2][j] == c && this.board[i+3][j] == '-') {
                        score++;
                    }          
            }
        }

        return score;
    }

    public int scoreThrees(char c) {
        int score = 0;
        for (int j = 0; j < 5; j++ ) {
            for (int i = 0; i < 8; i++) { 
                if (   this.board[i][j] == c && this.board[i][j+1] == c && this.board[i][j+2] == c && this.board[i][j+3] == '-'
                    || this.board[i][j] == c && this.board[i][j+1] == c && this.board[i][j+2] == '-' && this.board[i][j+3] == c
                    || this.board[i][j] == c && this.board[i][j+1] == '-' && this.board[i][j+2] == c && this.board[i][j+3] == c 
                    || this.board[i][j] == '-' && this.board[i][j+1] == c && this.board[i][j+2] == c && this.board[i][j+3] == c) {
                        score++;
                    }
            }
        }

        for (int i = 0; i< 5 ; i++ ){
            for (int j = 0; j< 8; j++){
                if (   this.board[i][j] == c && this.board[i+1][j] == c && this.board[i+2][j] == c && this.board[i+3][j] == '-'
                    || this.board[i][j] == c && this.board[i+1][j] == c && this.board[i+2][j] == '-' && this.board[i+3][j] == c
                    || this.board[i][j] == c && this.board[i+1][j] == '-' && this.board[i+2][j] == c && this.board[i+3][j] == c 
                    || this.board[i][j] == '-' && this.board[i+1][j] == c && this.board[i+2][j] == c && this.board[i+3][j] == c) {
                        score++;
                    }          
            }
        }

        return score;
    }

    public int winner() {
        // Check horizontal win for Human
        for (int j = 0; j<  5; j++ ){
            for (int i = 0; i < 8; i++){
                if (this.board[i][j] == 'O' && this.board[i][j+1] == 'O' && this.board[i][j+2] == 'O' && this.board[i][j+3] == 'O'){
                    return 500000;
                }           
            }
        }
       
        // Check vertical win for Human
        for (int i = 0; i< 5 ; i++ ){
            for (int j = 0; j< 8; j++){
                if (this.board[i][j] == 'O' && this.board[i+1][j] == 'O' && this.board[i+2][j] == 'O' && this.board[i+3][j] == 'O'){
                    return 500000;
                }           
            }
        }

        // Check horizontal win for Computer
        for (int j = 0; j<  5; j++ ){
            for (int i = 0; i < 8; i++){
                if (this.board[i][j] == 'X' && this.board[i][j+1] == 'X' && this.board[i][j+2] == 'X' && this.board[i][j+3] == 'X'){
                    return -500000;
                }           
            }
        }

        // Check vertical win for Computer
        for (int i = 0; i< 5 ; i++ ){
            for (int j = 0; j< 8; j++){
                if (this.board[i][j] == 'X' && this.board[i+1][j] == 'X' && this.board[i+2][j] == 'X' && this.board[i+3][j] == 'X') {
                    return -500000;
                }           
            }
        }

        return 0;
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


    private int checkTwos(char c){
        // System.out.println("Turn "+c);
          int score = 0;
          //check row
          for(int col=0;col<5;col++){
              for(int row=0;row<board.length;row++){
                  if( (board[row][col]==c && board[row][col]==board[row][col+1] &&
                       board[row][col+2]=='-' && board[row][col+3]=='-') ||
                      (board[row][col]==c && board[row][col]==board[row][col+2] &&
                       board[row][col+1]=='-' && board[row][col+3]=='-') ||
                      (board[row][col]==c && board[row][col]==board[row][col+3] &&
                       board[row][col+1]=='-' && board[row][col+2]=='-') ){
                      if(col>1 && board[row][col-2]=='-' && board[row][col-1]=='-') score+=2;
                      else score++;

                  }
                  if(col<4){
                    if( (board[row][col+2]==c && board[row][col+2]==board[row][col+3] &&
                         board[row][col]=='-' && board[row][col+1]=='-') ){
                           score++;
                      }
                  }
                  if(col==4){
                      if( (board[row][col+1]==c && board[row][col+1]==board[row][col+2] &&
                           board[row][col]=='-' && board[row][col+3]=='-') ||
                          (board[row][col+1]==c && board[row][col+1]==board[row][col+3] &&
                           board[row][col]=='-' && board[row][col+2]=='-') ||
                          (board[row][col+2]==c && board[row][col+2]==board[row][col+3] &&
                           board[row][col]=='-' && board[row][col+1]=='-') ){
                             score++;
                            // System.out.println(board[row][col]+" "+board[row][col+1]+" "+board[row][col+2]+" "+board[row][col+3]);
                      }
                  }
              }
          }
        //check columns
        for(int row=0;row<5;row++){
            for(int col=0;col<board.length;col++){
                if( (board[row][col]==c && board[row][col]==board[row+1][col] &&
                     board[row+2][col]=='-' && board[row+3][col]=='-') ||
                    (board[row][col]==c && board[row][col]==board[row+2][col] &&
                     board[row+1][col]=='-' && board[row+3][col]=='-') ||
                    (board[row][col]==c && board[row][col]==board[row+3][col] &&
                     board[row+1][col]=='-' && board[row+2][col]=='-') ){
                       // System.out.println(board[row][col]+" "+board[row+1][col]+" "+board[row+2][col]+" "+board[row+3][col]);
                       if(row>1 && board[row-2][col]=='-' && board[row-1][col]=='-'){
                         // System.out.println("YES "+board[row-2][col]+" "+board[row-1][col]+" "+board[row][col]+" "+board[row+1][col]+" "+board[row+2][col]+" "+board[row+3][col]);
                         score+=2;
                       }
                       else score++;
                }
                if(row<4){
                  if( (board[row+2][col]==c && board[row+2][col]==board[row+3][col] &&
                       board[row][col]=='-' && board[row+1][col]=='-') ){
                         score++;
                    }
                }
                if(row==4){
                    if( (board[row+1][col]==c && board[row+1][col]==board[row+2][col] &&
                         board[row][col]=='-' && board[row+3][col]=='-') ||
                        (board[row+1][col]==c && board[row+1][col]==board[row+3][col] &&
                         board[row][col]=='-' && board[row][col]=='-') ||
                        (board[row+2][col]==c && board[row+2][col]==board[row+3][col] &&
                         board[row][col]=='-' && board[row+1][col]=='-') ){
                           score++;
                    }
                }
            }
        }
        // System.out.println(score+" twos");
        return score;
    }

    private int checkThrees(char c){
        int score = 0;
        //check rows
        for(int col=0;col<5;col++){
            for(int row=0;row<board.length;row++){

                if( (board[row][col]==c && board[row][col]==board[row][col+1] &&
                     board[row][col+1]==board[row][col+2] && board[row][col+3]=='-') ||
                    (board[row][col]==c && board[row][col]==board[row][col+1] &&
                     board[row][col+1]==board[row][col+3] && board[row][col+2]=='-') ||
                    (board[row][col]==c && board[row][col]==board[row][col+2] &&
                     board[row][col+2]==board[row][col+3] && board[row][col+1]=='-') ){
                         // System.out.println(board[row][col]+" "+board[row][col+1]+" "+board[row][col+2]+" "+board[row][col+3]);
                    if(col>0 && board[row][col-1]=='-') score+=2;
                    else score++;
                }
                if(col==4){
                    if(board[row][col+1]==c && board[row][col+1]==board[row][col+2] &&
                       board[row][col+2]==board[row][col+3] && board[row][col]=='-'){
                         score++;
                    }
                }
            }
        }
        //check columns
        for(int row=0;row<5;row++){
            for(int col=0;col<board.length;col++){
                if( (board[row][col]==c && board[row][col]==board[row+1][col] &&
                     board[row+1][col]==board[row+2][col] && board[row+3][col]=='-') ||
                    (board[row][col]==c && board[row][col]==board[row+1][col] &&
                     board[row+1][col]==board[row+3][col] && board[row+2][col]=='-') ||
                    (board[row][col]==c && board[row][col]==board[row+2][col] &&
                     board[row+2][col]==board[row+3][col] && board[row+1][col]=='-') ){
                       if(row>0 && board[row-1][col]=='-') score+=2;
                       else score++;
                }
                if(row==4){
                    if(board[row+1][col]==c && board[row+1][col]==board[row+2][col] &&
                       board[row+2][col]==board[row+3][col] && board[row][col]=='-'){
                         score++;
                    }
                }
            }
        }
        // System.out.println(score+" threes");
        return score;
    }

}
