package gameai;

import java.io.*;

public class Game2 {
    public static final int BOARD_SIZE = 3;
    public static final int WIN_SIZE = 3;
    private char board[][];
    public char comp = 'X'; // tietokoneen merkit
    public char me = 'O';   // omat
    
    public Game2() throws IOException {
        // Pelilaudan alkutilanne
        board = new char[][] { 
            {'X',' ',' '},
            {' ',' ',' '},  
            {' ',' ',' '}};  
        
        String line = "";
        InputStreamReader converter = new InputStreamReader(System.in);
        BufferedReader in = new BufferedReader(converter);
        
        System.out.println("quit lopettaa pelin");
        printBoard(board);
        
        int[] move;
       
        while (!(line.equals("quit"))){
            System.out.print("syötä koordinaatit x,y: ");
            
            line = in.readLine();

            if (!(line.equals("quit"))){
                
                String[] s=line.split( ",");
                if(s.length == 2) {
                    int x = new Integer(s[0]);
                    int y = new Integer(s[1]);
                    if(x < 0 || x > BOARD_SIZE - 1 && y < 0 || y > BOARD_SIZE - 1) {
                        System.out.println("koordinaatti pitää olla välillä [0,"+(BOARD_SIZE-1)+"]");
                    } else if(canPlay(x,y,board)) {
                        board[y][x] = me; // oma siirto
                        printBoard(board);
                        if(wins(board,x,y) != 0) { 
                            System.out.println("voitto!");
                            printBoard(board);
                            line = "quit";
                        } else if(boardIsFull(board)) {
                            System.out.println("tasapeli");
                            printBoard(board);
                            line = "quit";
                        } else {
                            // tietokoneen vuoro
                            move = max(board,-1,-1,me, Integer.MIN_VALUE, Integer.MAX_VALUE); 
                            board[move[2]][move[1]] = comp;
                            System.out.println("tietokone pelaa x:"+move[1]+
                                    " y: "+move[2]+" arvioitu pelin lopputulos:"+move[0]);
                            if(wins(board,move[1],move[2]) != 0) { 
                                System.out.println("tappio!");
                                printBoard(board);
                                line = "quit";
                            } else if(boardIsFull(board)) {
                                System.out.println("tasapeli");
                                printBoard(board);
                                line = "quit";
                            } else { 
                                printBoard(board);
                            }
                            
                        }      
                    } else {
                        System.out.println("paikka ei ole tyhjä");
                    }
                }
            }
        }

    }
    
    //3x3
    void printBoard(char[][] b) {
        System.out.println(" 0  1  2 ");
        System.out.println(" +-+-+-+");
        for(int i = 0; i < 3;i++){
            System.out.print(i+"|");
            for(int j = 0; j < 3;j++) {
                if(b[i][j]!='X'&& b[i][j]!='O')
                    System.out.print(" "+"|");
                else
                    System.out.print(b[i][j]+"|");
            }
            System.out.println("");
            System.out.println(" +-+-+-+");
        }
        
    }
    
    boolean canPlay(int x, int y, char[][] b) {
        if(b[y][x] != 'X' && b[y][x] != 'O') return true;
        
        return false;
    }

    boolean boardIsFull(char[][] b) {
        for(int i = 0; i < BOARD_SIZE; i++) {
            for(int j = 0; j < BOARD_SIZE; j++) {
                if(b[i][j]!='X'&& b[i][j]!='O') {
                   return false;
                }
            }
        }
        return true;
    }
    
    int wins(char[][] board, int x, int y) {
        char current = board[y][x];
        
        int counter = 0;
 
        // row
        
        for(int j = 0; j < BOARD_SIZE ; j++) {
            if(board[y][j] == current) {
                counter++;
            } else {
                counter = 0;
            }
        }
      
        if(counter >= WIN_SIZE) {
            return (current == comp) ? 1 : -1;
        }
        
        counter = 0;
        
        // col
        
        for(int i = 0; i < BOARD_SIZE ; i++) {
            if(board[i][x] == current) {
                counter++;
            } else {
                counter = 0;
            }
        }
        
        if(counter >= WIN_SIZE) {
            return (current == comp) ? 1 : -1;
        }
        
        counter = 0;

        // diagonal
        
        int i = 0,j = 0;
        
        if(x > y) {
            j = x - y;
        } else if (x < y) {
            i = y - x;
        }
        
        for(;i < BOARD_SIZE && j < BOARD_SIZE ; i++, j++) {
            if(board[i][j] == current) {
                counter++;
            } else {
                counter = 0;
            }
        }
        
        if(counter >= WIN_SIZE) {
            return (current == comp) ? 1 : -1;
        }
        
        counter = 0;

        // antidiagonal
        
        i = BOARD_SIZE - 1; j = 0;
        
        int b = y + x;
        
        if(b > BOARD_SIZE - 1) {
           j = b - (BOARD_SIZE - 1);
        } else {
            i = b;
        }

        for(;i >= 0 && j < BOARD_SIZE; i--, j++) {
            if(board[i][j] == current) {
                counter++;
            } else {
                counter = 0;
            }
        }
        
        if(counter >= WIN_SIZE) {
            return (current == comp) ? 1 : -1;
        }
        
        counter = 0;
        
        return 0; // ei voittajaa vielä
    }
    
    int[] max(char[][] b, int x, int y, char m, int alpha, int beta) {
        // tarkista pelitilanne
        int w;
        
        if(x == -1 || y == -1) {
           w = 0;   
        } else {
            w = wins(b, x , y);
        }
        
        if (w == 1 || w == -1) {
            return new int[] {w,x,y};
        }
        
        if(boardIsFull(b)) {
            return new int[] {0,x,y};
        }
        
        // vaihda pelivuoro
        if (m == 'X')
            m = 'O';
        else if (m == 'O')
            m = 'X';
        
        int col = -1, row = -1;
        
        // etsitään vapaat siirrot
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                if(b[i][j] != 'X' && b[i][j] != 'O') {
                    // kopioi pelilauta
                    char[][] nb = new char[3][3];
                    for(int k = 0; k < 3;k++){
                         for(int l = 0; l < 3;l++){
                            nb[k][l] = b[k][l];
                         }
                    }
                    
                    // tee siirto
                    nb[i][j] = m;
  
                    // päivitä 
                    int newval = min(nb,j,i,m,alpha,beta)[0];
                    
                    if (newval > alpha) {
                        alpha = newval; 
                        col = j; 
                        row = i;
                    }
                    
                    // karsitaan turhat siirrot
                    if (alpha >= beta) {
                        break;
                    }
                }
            }
        }
        
        // palauta alphan:n arvo ja siirto
        return new int[] {alpha,col,row};
    }
    
    int[] min(char[][] b, int x, int y, char m, int alpha, int beta) {
        // tarkista pelitilanne
        int w = wins(b, x , y);
        
        if (w == 1 || w == -1) {
            return new int[] {w,x,y};
        }
        
        if(boardIsFull(b)) {
            return new int[] {0,x,y};
        }
        
        // vaihda pelivuoro
        if (m == 'X')
            m = 'O';
        else if (m == 'O')
            m = 'X';
        
        int col = -1, row = -1;
        
        // etsitään vapaat siirrot
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                if(b[i][j] != 'X' && b[i][j] != 'O') {
                    // kopioi pelilauta
                    char[][] nb = new char[3][3];
                    for(int k = 0; k < 3;k++){
                         for(int l = 0; l < 3;l++){
                            nb[k][l] = b[k][l];
                         }
                    }
                    // tee siirto
                    nb[i][j] = m;
                    
                    // päivitä beta
                    int newval = max(nb,j,i,m,alpha,beta)[0];

                    if (newval < beta) {
                        beta = newval; 
                        col=j;
                        row=i;
                    }
                    
                    // karsitaan turhat siirrot
                    if (alpha >= beta) {
                        break;
                    }
                }
            }
        }
        
        // palauta beta:n arvo ja siirto
        return new int[] {beta,col,row};
    }
}