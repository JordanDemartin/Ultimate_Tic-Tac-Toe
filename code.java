import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);

        // game loop
        while (true) {
            int opponentRow = in.nextInt();
            int opponentCol = in.nextInt();
            int validActionCount = in.nextInt();
            int choosen_row = 0, choosen_col = 0;
            for (int i = 0; i < validActionCount; i++) {
                int row = in.nextInt();
                int col = in.nextInt();
                if(i == 0){
                    choosen_row = row;
                    choosen_col = col;
                }
            }

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");

            System.out.println(choosen_row + " " + choosen_col);
        }
    }
}
