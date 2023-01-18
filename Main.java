import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Board b = new Board('H');
        b.printBoard();

        while (!b.isTerminalState()) {
            if (b.getTurn() == 'H') {
                System.out.println("Enter your move: (EX: A1)");
                String in = scan.nextLine();

                while(in.length() != 2 || !b.humanMove(in.charAt(0), in.charAt(1) - '0')) {
                    System.out.println("(!) Error: Bad move entered or input:");
                    System.out.println("Enter your move: (EX: A1)");
                    in = scan.nextLine();
                }
            } else {
                int[] move = new int[2];
                long start = System.currentTimeMillis(); 
                for (int i = 1; i <= 5 && (System.currentTimeMillis() - start) < 5000; i++) {
                    start = System.nanoTime();
                    move = maxValue(move, b, -999999999,999999999, 0, i, System.currentTimeMillis() - start);
                }
                b.AIMove(move[0], move[1]);
            }
            b.printBoard();

        }

        scan.close();
    }

    public static int[] maxValue(int[] move, Board b, int alpha, int beta, int depth, int maxDepth, long time) {
        if (depth == maxDepth || b.isTerminalState()) {
            return move;
        }
        int bestValue = -999999999;
        int[] newMove = new int[2], childMove = new int[2];
        Board child = new Board(b);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (b.getBoard()[i][j] == '-') {
                    childMove[0] = i; childMove[1] = j;
                    child = new Board(b);
                    child.algoMove(i, j, 'X');
                    if (child.eval('X') > b.eval('X')) {
                        long newTime = System.currentTimeMillis() - time;
                        newMove = minValue(childMove, child, alpha, beta, depth + 1, maxDepth, newTime);
                        child.algoMove(newMove[0], newMove[1], 'X');
                        bestValue = Math.max(bestValue, child.eval('X'));
                        alpha = Math.max(alpha, bestValue);
                        if (beta <= alpha || newTime <= 0) {
                            return newMove;
                        }

                    }
                }
            }
        }
        return newMove;
    }

    public static int[] minValue(int[] move, Board b, int alpha, int beta, int depth, int maxDepth, long time) {
        if (depth == maxDepth || b.isTerminalState())  {
            return move;
        }
        int bestValue = 999999999;
        int[] newMove = new int[2], childMove = new int[2];
        Board child = new Board(b);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (b.getBoard()[i][j] == '-') {
                    childMove[0] = i; childMove[1] = j;
                    child = new Board(b);
                    child.algoMove(i, j, 'O');
                    if (child.eval('O') > b.eval('O')) {
                        long newTime = System.currentTimeMillis() - time;
                        newMove = maxValue(childMove, child, alpha, beta, depth + 1, maxDepth, newTime);
                        child.algoMove(newMove[0], newMove[1], 'O');
                        bestValue = Math.min(bestValue, child.eval('O'));
                        beta = Math.min(beta, bestValue);
                        if (beta <= alpha || newTime <= 0) {
                            return newMove;
                        }

                    }
                }
            }
        }
        return newMove;

    }
    
}
