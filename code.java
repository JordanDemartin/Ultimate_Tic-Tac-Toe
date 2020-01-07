import java.util.*;
import java.io.*;
import java.math.*;

class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        boolean start = true;
        Noeud best_arbre = null;

        /*Case grille[][] = new Case[3][3];
        for(int j = 0; j < 3 ; j++){
            for(int k = 0; k < 3 ; k++){
                grille[j][k] = new Case(false, false);
            }
        }*/
        
        
        // game loop
        while (true) {
            int opponentRow = in.nextInt();
            int opponentCol = in.nextInt();
            int best_score = -10000000;
            int max_depth = 5;
            Noeud arbre = null, new_best_arbre = null;
            /*if(opponentRow != -1 && opponentCol != -1){
                grille[opponentRow][opponentCol].play(false);
            }*/
            
            int validActionCount = in.nextInt();
            int choosen_row = 0, choosen_col = 0;
            
            if(!start && best_arbre != null){
                arbre = best_arbre.getChild(opponentRow,opponentCol);
                if(arbre == null){
                    best_arbre.setMaxDepth(3);
                    best_arbre.createChilds();
                    arbre = best_arbre.getChild(opponentRow,opponentCol);
                }
                best_arbre = arbre;
            }
            System.err.println("best "+best_arbre+" ar:"+arbre);
            

            for (int i = 0; i < validActionCount; i++) {
                int row = in.nextInt();
                int col = in.nextInt();
                
                if(start && opponentCol==-1){
                    arbre = new Noeud(null,row,col,true,1,max_depth,-1,-1);
                    if(arbre.getScore() > best_score){
                        new_best_arbre = arbre;
                        best_score = arbre.getScore();
                    }
                }else if(start && opponentCol!=-1){
                    arbre = new Noeud(null,row,col,true,2,max_depth,opponentRow,opponentCol);
                    if(arbre.getScore() > best_score){
                        new_best_arbre = arbre;
                        best_score = arbre.getScore();
                    }
                }else if(!start){
                    arbre = best_arbre.getChild(row,col);
                    System.err.println(arbre.getScore()+" > "+best_score);
                    if(arbre.getScore() > best_score){
                        new_best_arbre = arbre;
                        best_score = arbre.getScore();
                    }
                }
            }
            start = false;
            best_arbre = new_best_arbre;
            System.err.println(best_score + " ");
            //System.err.println(choosen_row + " " + choosen_col);

            //grille[choosen_row][choosen_col].play(true);
            
            /*for(int j = 0; j < 3 ; j++){
                for(int k = 0; k < 3 ; k++){
                    System.err.print(grille[j][k].print());
                }
                System.err.println("");
            }*/
            
            System.out.println(best_arbre.getX() + " " + best_arbre.getY());
            
        }
    }
}

class Case{
    private boolean played;
    private boolean played_by_me;
    
    public Case(boolean played, boolean played_by_me){
        this.played = played;
        this.played_by_me = played_by_me;
    }
    
    public void play(boolean played_by_me){
        this.played = true;
        this.played_by_me = played_by_me;
    }
    
    public boolean getPlayed(){
        return this.played;
    }
    
    public boolean getPlayedByMe(){
        return this.played_by_me;
    }
    
    /*public String print(){
        if(this.played == true){
            return played_by_me+"";
        }else{
            return "void";
        }
    }*/
}

class Noeud{
    private boolean my_turn;
    private Case grille[][];
    private Noeud childs[];
    private Noeud previous_move;
    private int step;
    private int x_move;
    private int y_move;
    private int score;
    private int max_depth;
    
    public Noeud(Noeud previous_move,int x_move,int y_move,boolean my_turn,int step,int max_depth,int start_opponent_x,int start_opponent_y){
        this.previous_move = previous_move;
        this.x_move = x_move;
        this.y_move = y_move;
        this.my_turn = my_turn;
        this.step = step;
        this.childs = new Noeud[(9-this.step)];
        this.max_depth = max_depth;

        if(this.previous_move == null){

            this.grille = new Case[3][3];
            for(int j = 0; j < 3 ; j++){
                for(int k = 0; k < 3 ; k++){
                    this.grille[j][k] = new Case(false, false);
                }
            }
            if(my_turn && step == 2 && start_opponent_x != -1 && start_opponent_y != -1){
                this.grille[start_opponent_x][start_opponent_y].play(false);
            }

        }else{

            this.grille = new Case[3][3];
            for(int j = 0; j < 3 ; j++){
                for(int k = 0; k < 3 ; k++){
                    this.grille[j][k] = new Case(this.previous_move.getCase(j,k).getPlayed(),this.previous_move.getCase(j,k).getPlayedByMe());
                }
            }

        }

        this.grille[x_move][y_move].play(my_turn);

        /*System.err.println(childs.length + " step : "+ this.step + " max_depth = " + max_depth + " previous_move ? " + (previous_move != null));
        for(int j = 0; j < 3 ; j++){
                for(int k = 0; k < 3 ; k++){
                    System.err.print(grille[j][k].print());
                }
                System.err.println("");
            }*/

        updateScore(calculateScore());

        if((score > -1000 && score < 1000) && max_depth > 1){
            createChilds();
        }

    }

    public boolean getMyTurn(){
        return this.my_turn;
    }

    public Case getCase(int x, int y){
        return this.grille[x][y];
    }  

    public int getStep(){
        return this.step;
    }

    public Noeud getChild(int x, int y){
        if(this.childs[0] != null){
            for(int i = 0 ; i < this.childs.length ; i++){
                if(this.childs[i].getX() == x && this.childs[i].getY() == y){
                    return this.childs[i];
                }
            }
        }
        return null;
    }

    public void setMaxDepth(int max_depth){
        this.max_depth = max_depth;
    }

    public int getX(){
        return this.x_move;
    }

    public int getY(){
        return this.y_move;
    }

    public int getScore(){
        return this.score;
    }

    public void updateScore(int score){
        this.score = score + this.score;
        if(previous_move != null){
            previous_move.updateScore(this.score);
        }
    }

    public void createChilds(){
        int i = 0;
        for(int j = 0; j < 3 ; j++){
            for(int k = 0; k < 3 ; k++){
                if(!this.grille[j][k].getPlayed()){
                    childs[i] = new Noeud(this,j,k,!my_turn,step+1,max_depth-1,-1,-1);
                    i++;
                }

            }
        }

    }

    private int calculateScore() { // 0 rien, 1 victoire moi, 2 défaite
        int score = 0;

        for (int i = 0; i < 3; i++) {

            // check rows victory
            if(grille[i][0].getPlayed() && grille[i][1].getPlayed() && grille[i][2].getPlayed()){

                if(grille[i][0].getPlayedByMe() && grille[i][1].getPlayedByMe() && grille[i][2].getPlayedByMe()){
                    return 1000;
                }else if(!grille[i][0].getPlayedByMe() && !grille[i][1].getPlayedByMe() && !grille[i][2].getPlayedByMe()){
                    return -1000;
                }

            }

            if(grille[i][0].getPlayed() && grille[i][1].getPlayed()){
                if(grille[i][0].getPlayedByMe() && grille[i][1].getPlayedByMe()){
                    return 10;
                }else if(!grille[i][0].getPlayedByMe() && !grille[i][1].getPlayedByMe()){
                    return -10;
                }
            }

            if(grille[i][0].getPlayed() && grille[i][2].getPlayed()){
                if(grille[i][0].getPlayedByMe() && grille[i][2].getPlayedByMe()){
                    return 10;
                }else if(!grille[i][0].getPlayedByMe() && !grille[i][2].getPlayedByMe()){
                    return -10;
                }
            }

            if(grille[i][1].getPlayed() && grille[i][2].getPlayed()){
                if(grille[i][1].getPlayedByMe() && grille[i][2].getPlayedByMe()){
                    return 10;
                }else if(!grille[i][1].getPlayedByMe() && !grille[i][2].getPlayedByMe()){
                    return -10;
                }
            }

            // check cols
            if(grille[0][i].getPlayed() && grille[1][i].getPlayed() && grille[2][i].getPlayed()){

                if(grille[0][i].getPlayedByMe() && grille[1][i].getPlayedByMe() && grille[2][i].getPlayedByMe()){
                    return 1000;
                }else if(!grille[0][i].getPlayedByMe() && !grille[1][i].getPlayedByMe() && !grille[2][i].getPlayedByMe()){
                    return -1000;
                }

            }

            if(grille[0][i].getPlayed() && grille[1][i].getPlayed()){
                if(grille[0][i].getPlayedByMe() && grille[1][i].getPlayedByMe()){
                    return 10;
                }else if(!grille[0][i].getPlayedByMe() && !grille[1][i].getPlayedByMe()){
                    return -10;
                }
            }

            if(grille[0][i].getPlayed() && grille[2][i].getPlayed()){
                if(grille[0][i].getPlayedByMe() && grille[2][i].getPlayedByMe()){
                    return 10;
                }else if(!grille[0][i].getPlayedByMe() && !grille[2][i].getPlayedByMe()){
                    return -10;
                }
            }

            if(grille[1][i].getPlayed() && grille[2][i].getPlayed()){
                if(grille[1][i].getPlayedByMe() && grille[2][i].getPlayedByMe()){
                    return 10;
                }else if(!grille[1][i].getPlayedByMe() && !grille[2][i].getPlayedByMe()){
                    return -10;
                }
            }
        }

        // check diag 1
        if(grille[0][0].getPlayed() && grille[1][1].getPlayed() && grille[2][2].getPlayed()){

            if (grille[0][0].getPlayedByMe() && grille[1][1].getPlayedByMe() && grille[2][2].getPlayedByMe()) {
                return 1000;
            }else if(!grille[0][0].getPlayedByMe() && !grille[1][1].getPlayedByMe() && !grille[2][2].getPlayedByMe()){
                return -1000;
            }

        }

        if(grille[0][0].getPlayed() && grille[1][1].getPlayed()){
            if (grille[0][0].getPlayedByMe() && grille[1][1].getPlayedByMe()) {
                return 10;
            }else if(!grille[0][0].getPlayedByMe() && !grille[1][1].getPlayedByMe()){
                return -10;
            }
        }

        if(grille[0][0].getPlayed() && grille[2][2].getPlayed()){
            if (grille[0][0].getPlayedByMe() && grille[2][2].getPlayedByMe()) {
                return 10;
            }else if(!grille[0][0].getPlayedByMe() && !grille[2][2].getPlayedByMe()){
                return -10;
            }
        }

        if(grille[1][1].getPlayed() && grille[2][2].getPlayed()){
            if (grille[1][1].getPlayedByMe() && grille[2][2].getPlayedByMe()) {
                return 10;
            }else if(!grille[1][1].getPlayedByMe() && !grille[2][2].getPlayedByMe()){
                return -10;
            }
        }

        // check diag 2
        if(grille[2][0].getPlayed() && grille[1][1].getPlayed() && grille[0][2].getPlayed()){

            if (grille[2][0].getPlayedByMe() && grille[1][1].getPlayedByMe() && grille[0][2].getPlayedByMe()) {
                return 1000;
            }else if(!grille[2][0].getPlayedByMe() && !grille[1][1].getPlayedByMe() && !grille[0][2].getPlayedByMe()){
                return -1000;
            }

        }

        if(grille[2][0].getPlayed() && grille[1][1].getPlayed()){
            if (grille[2][0].getPlayedByMe() && grille[1][1].getPlayedByMe()) {
                return 10;
            }else if(!grille[2][0].getPlayedByMe() && !grille[1][1].getPlayedByMe()){
                return -10;
            }
        }

        if(grille[2][0].getPlayed() && grille[0][2].getPlayed()){
            if (grille[2][0].getPlayedByMe() && grille[0][2].getPlayedByMe()) {
                return 10;
            }else if(!grille[2][0].getPlayedByMe() && !grille[0][2].getPlayedByMe()){
                return -10;
            }
        }

        if(grille[1][1].getPlayed() && grille[0][2].getPlayed()){
            if (grille[1][1].getPlayedByMe() && grille[0][2].getPlayedByMe()) {
                return 10;
            }else if(!grille[1][1].getPlayedByMe() && !grille[0][2].getPlayedByMe()){
                return -10;
            }
        }

        return 0;
    }
}
