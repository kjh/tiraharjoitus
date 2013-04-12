
package gameai;

import java.io.*;

public class Game {
    public static final int BOARD_SIZE = 3;
    public static final int WIN_SIZE = 3;    
    private char board[][];
    
    public Game() throws IOException {
        board = new char[BOARD_SIZE][BOARD_SIZE]; 
        
        String line = "";
	InputStreamReader converter = new InputStreamReader(System.in);
	BufferedReader in = new BufferedReader(converter);
        System.out.println("quit lopettaa pelin");
        char m = 'O';
       
        
        int[] move;
        
        
        printBoard(board);
        
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
                        board[y][x] = 'X'; // omat
                        move = max(board,0,0,m);
                        board[move[2]][move[1]] = m;
                        printBoard(board);
                        
                    } else {
                        System.out.println("paikka ei ole tyhjä");
                    }  
                } 
            }
	}

    }
    
    //3x3
    void printBoard(char[][] b) {
        System.out.println("  0 1 2 ");
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
                if(canPlay(i,j,b)) {
                   return false;
                }
            }    
        }
        return true;
    }
    
    int wins(char[][] board, int x, int y, char current) {
        
        
        int counter = 0;
        
        if(current != 'X' && current != 'O') return 0;
        if(x == -1 || y == -1) return 0;
       
        // row
        
        for(int j = 0; j < BOARD_SIZE ; j++) {
            if(board[y][j] == current) {
                counter++;
            } else {
                counter = 0;
            }
        }
      
        if(counter >= WIN_SIZE) { 
            return (current == 'X') ? 1 : -1;
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
            return (current == 'X') ? 1 : -1;
        }
        
        counter = 0;
        
        
        // diagonal
        
        int i = 0,j = 0;
        
        if(x > y) { 
            j = x - y;
        } else if (x < y) { 
            i = y - x;
        }
        
        for(;i < BOARD_SIZE  && j < BOARD_SIZE ; i++, j++) {
            if(board[i][j] == current) {
                counter++;
            } else {
                counter = 0;
            }
        }
        
        if(counter >= WIN_SIZE) { 
            return (current == 'X') ? 1 : -1;
        }
        
        counter = 0;
             
        
        // antidiagonal
        
        i = BOARD_SIZE - 1; j = 0;
        
        int b = y + x;
        
        if(b > BOARD_SIZE - 1) { 
           j = b - (BOARD_SIZE - 1);
        } else  { 
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
            return (current == 'X') ? 1 : -1;
        }
        
        counter = 0;    
        
        return 0; // ei voittajaa vielä
    }
    
    int[] max(char[][] b, int x, int y, char m) {
        
        int w = wins(b, x , y, m);
        
        
        // terminal
        if ((boardIsFull(b) || w == 1 || w == -1)) {
            if(w == 1) w = Integer.MAX_VALUE; 
            return new int[] {w,x,y};
        } 
        
        int mybest = Integer.MIN_VALUE; 
      
        int col = x, row = y;
        
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                if(b[i][j] != 'X' && b[i][j] != 'O') {
                    // copy
                    char[][] nb = new char[3][3];
                    for(int k = 0; k  < 3;k++){
                         for(int l = 0; l  < 3;l++){
                            nb[k][l] = b[k][l];
                         }
                    }
                    
                    // move
                    nb[i][j] = m;
                
                    // min
                    int[] ret = min(nb,j,i,m);
                    
                    int newval = ret[0]; // eval 
                    int newcol = ret[1]; // x
                    int newrow = ret[2]; // y
                    
                    // päivitetään arvot
                    if (newval > mybest) { 
                        mybest = newval; // eval 
                        col = newcol;      // x
                        row = newrow;      // y 
                    }
                } 
            }    
        }
        
        return new int[] {mybest,col,row};
    }
    
    int[] min(char[][] b, int x, int y, char m) {
        // vaihda pelaaja
        if (m == 'X') 
                m = 'O';
        else 
                m = 'X';
        
        int w = wins(b, x , y, m);
        
        
        // terminal node
        if ((boardIsFull(b) || w == 1 || w == -1)) {
            return new int[] {w,x,y};
        } 
        
        int myworst = Integer.MAX_VALUE; 
        
        int col = x, row = y;
       
       
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                if(b[i][j] != 'X' && b[i][j] != 'O') {
                    // copy
                    char[][] nb = new char[3][3];
                    for(int k = 0; k  < 3;k++){
                         for(int l = 0; l  < 3;l++){
                            nb[k][l] = b[k][l];
                         }
                    }
                    
                    // move
                    nb[i][j] = m;
                
                    int[] ret = max(nb,j,i,m);
                    
                    int newval = ret[0]; // eval
                    int newcol = ret[1]; // x
                    int newrow = ret[2]; // y
                    
                    // päivitetään arvot
                    if (newval < myworst) { 
                        myworst = newval; // eval
                        col=newcol;       // x
                        row=newrow;       // y
                    }
                } 
            }    
        }
        
        return new int[] {myworst,col,row};
        
    }
     
    }
