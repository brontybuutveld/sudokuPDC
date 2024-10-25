package sudokupdc;

import java.awt.BorderLayout;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.*;
import javax.swing.JScrollPane;


public class SelectBoardUI {
    public SelectBoardUI() {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        Dimension size = new Dimension(500, 560);
        frame.setMinimumSize(size);
        JScrollPane scrollBar = new JScrollPane(panel);
        
        JPanel board = new JPanel();
        board.setSize(400,400);
        
        GridBagConstraints gbc = new GridBagConstraints();
        board.setLayout(new GridBagLayout());
        
        SudokuDB sudokudb = new SudokuDB();
        ArrayList<String[]> puzzle = sudokudb.getPuzzle();
        
        int x = 0, y = 0;
        for (String[] pzl : puzzle) {
            
            int[][] data = new int[9][9];
            char[] cData = pzl[1].toCharArray();
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    data[i][j] = cData[i * 9 + j] - '0';
                }
            }
            BoardUI1 boardUI = new BoardUI1(data, Integer.valueOf(pzl[0]), sudokudb);
            boardUI.setPreferredSize(new Dimension(142, 142));
            gbc.gridx = x;
            gbc.gridy = y;
            board.add(boardUI, gbc);
            if (x < 2) {
                x++;
            } else {
                y++;
                x = 0;
            }
        }
        
        panel.add(board);
        
        
        frame.add(scrollBar);        
        
        frame.revalidate();
        
        frame.setTitle("Sudoku");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
        
        frame.setSize(size);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    public static void main(String[] args) {
        new SelectBoardUI();
    }
}