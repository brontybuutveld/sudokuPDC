package sudokupdc.UI;

import sudokupdc.DB.SudokuDB;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Objects;

public class BoardUI1 extends JPanel {
    private JTextField[][] cells = new JTextField[9][9];
    private boolean[][] mask = new boolean[9][9];
    private final int[][] board;
    private final int id;
    private final SudokuDB sudokudb;
    
    public BoardUI1(int[][] board, int id, SudokuDB sudokudb) {
        this.board = board;
        this.id = id;
        this.sudokudb = sudokudb;
        
        if (id == -1) System.exit(-1);
        
        makeUI();
    }
    
    private void makeUI() {
        JPanel gridPanel = new JPanel();
        gridPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
        
        gridPanel.setLayout(new GridLayout(9, 9));
        gridPanel.setBackground(Color.WHITE);
        
        makeCells(gridPanel);
                
        add(gridPanel);
        
        setVisible(true);
    }
    
    private void makeCells(JPanel gridPanel) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                JTextField text = new JTextField();
                text.setFont(new Font("SansSerif", Font.PLAIN, 10));
                text.setForeground(Color.BLUE);
                text.setEditable(false);
                
                if (this.board[row][col] != 0) {
                    text = new JTextField("" + this.board[row][col]);
                    text.setFont(new Font("SansSerif", Font.PLAIN, 10));
                }

                if (mask[row][col]) text.setBackground(Color.RED);
                
                text.setPreferredSize(new Dimension(15, 15));
                text.setHorizontalAlignment(JTextField.CENTER);
                Border border = setBorder(row, col);
                text.setBorder(border);
                
                JTextField finalText = text;
                int finalRow = row;
                int finalCol = col;
                finalText.addKeyListener(new KeyListener() {
                    @Override public void keyTyped(KeyEvent e) {}
                    @Override public void keyPressed(KeyEvent e) {}

                    @Override
                    public void keyReleased(KeyEvent e) {
                        int prev = board[finalRow][finalCol];
                        if (finalText.getText().isEmpty())
                            board[finalRow][finalCol] = 0;
                        else
                            board[finalRow][finalCol] = Integer.valueOf(finalText.getText());
                        int val = board[finalRow][finalCol];
                        sudokudb.insertMoveTable(id, finalRow, finalCol, val, prev);
                        checkConstraints();
                    }
                });

                cells[row][col] = finalText;
                gridPanel.add(cells[row][col]).setLocation(row, col);
            }
        }
    }

    private void checkConstraints() {
        mask = new boolean[9][9];
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] != 0) {
                    checkRowConstraints(row, col);
                    checkColumnConstraints(row, col);
                    checkBoxConstraints(row, col);
                }
            }
        }
        drawMask();
    }

    private void checkRowConstraints(int row, int col) {
        for (int i = 0; i < 9; i++) {
            if (Objects.equals(board[row][i], board[row][col]) && i != col) {
                mask[row][i] = true;
                mask[row][col] = true;
            }
        }
    }

    private void checkColumnConstraints(int row, int col) {
        for (int i = 0; i < 9; i++) {
            if (Objects.equals(board[i][col], board[row][col]) && i != row) {
                mask[i][col] = true;
                mask[row][col] = true;
            }
        }
    }

    private void checkBoxConstraints(int row, int col) {
        int boxRow = row - row % 3;
        int boxCol = col - col % 3;
        for (int i = boxRow; i < boxRow + 3; i++) {
            for (int j = boxCol; j < boxCol + 3; j++) {
                if (Objects.equals(board[i][j], board[row][col]) && i != row && j != col) {
                    mask[i][j] = true;
                    mask[row][col] = true;
                }
            }
        }
    }

    private void drawMask() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (mask[row][col])
                    cells[row][col].setBackground(Color.RED);
                else
                    cells[row][col].setBackground(Color.WHITE);
            }
        }
    }
    
    private Border setBorder(int row, int col) {
        int top    = 1;
        int left   = 1;
        int bottom = (row == 8) ? 1 : 0;
        int right  = (col == 8) ? 1 : 0;
        return BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK);
    }
}