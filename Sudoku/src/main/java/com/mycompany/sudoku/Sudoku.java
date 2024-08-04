/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.mycompany.sudoku;

import java.util.Random;

/**
 *
 * @author brontybuutveld
 */

public class Sudoku {
    
    static Random rand = new Random();
    public static int genRand() {
        return rand.nextInt(9) + 1;
    }
    public static int toCoord(int num) {
        return (num - 1) / 3;
    }
    public static boolean check(int[][] board, int x, int y) {
        int num = board[x][y];
        int xGrid = toCoord(x);
        int yGrid = toCoord(y);
        for (int i = xGrid * 3; i < xGrid * 3 + 3; i++) {
            for (int j = yGrid * 3; j < yGrid * 3 + 3; j++) {
                if (board[i][j] == num) {
                    if (i == x && j == y)
                        continue;
                    else
                        return true;
                }
            }
        }
        for (int i = 0; i < 9; i++) {
            if (board[i][y] == num) {
                if (i == x)
                    continue;
                else
                    return true;
            }
        }
        for (int i = 0; i < 9; i++) {
            if (board[x][i] == num) {
                if (i == y)
                    continue;
                else
                    return true;
            }
        }
        return false;
    }
    public static void main(String[] args) {
        int[][] board = new int[9][9];
        boolean[][] mask = new boolean[9][9];
        
        for(int i = 0; i < 81; i++) {
            boolean isRunning = true;
            while (isRunning) {
                int x = genRand() - 1;
                int y = genRand() - 1;
                if (board[x][y] == 0) {
                    while (isRunning) {
                        board[x][y] = genRand();
                        isRunning = check(board, x, y);
                    }

                }
            }
        }
        /*
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                //while ()
                board[i][j] = genRand();
            }
        }
        */
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                System.out.print(board[i][j]);
            }
            System.out.println("");
        }
        
    }
}
