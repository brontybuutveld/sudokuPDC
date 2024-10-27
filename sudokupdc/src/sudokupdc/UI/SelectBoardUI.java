package sudokupdc.UI;

import sudokupdc.DB.SudokuDB;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SelectBoardUI extends JPanel {
    private static final int BOARD_SIZE = 142;
    private JPanel board;
    private TopUI top = TopUI.getInstance();
    private SudokuDB sudokudb = SudokuDB.getInstance();

    public SelectBoardUI() {
        
        board = new JPanel(new GridLayout(0, 1));
        add(board);
        
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                top.setRoot(new MenuUI());
            }
        });
        board.add(backButton);

        ArrayList<String[]> puzzle = sudokudb.getPuzzle();

        for (String[] pzl : puzzle) {
            int[][] data = new int[9][9];
            char[] cData = pzl[1].toCharArray();
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    data[i][j] = cData[i * 9 + j] - '0';
                }
            }

            JPanel boardContainer = new JPanel(new BorderLayout());
            BoardUI1 boardUI = new BoardUI1(data, Integer.valueOf(pzl[0]), sudokudb);
            boardUI.setPreferredSize(new Dimension(BOARD_SIZE, BOARD_SIZE));
            boardContainer.add(boardUI, BorderLayout.CENTER);
            
            JPanel boardContainer2 = new JPanel(new BorderLayout());
            boardContainer.add(boardContainer2, BorderLayout.SOUTH);

            JButton playButton = new JButton("Play");
            playButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onBoardSelected(Integer.valueOf(pzl[0]));
                }
            });
            boardContainer2.add(playButton, BorderLayout.EAST);

            JButton deleteButton = new JButton("Delete");
            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    board.remove(boardContainer);
                    deleteBoard(Integer.valueOf(pzl[0]));
                    board.revalidate();
                    board.repaint();
                }
            });
            boardContainer2.add(deleteButton, BorderLayout.WEST);

            board.add(boardContainer);
        }
        adjustGridColumns(510);
        setVisible(true);
    }

    private void onBoardSelected(int boardId) {
        System.out.println("Board " + boardId + " selected.");
        new BoardUI(boardId);
    }

    private void deleteBoard(int boardId) {
        System.out.println("Board " + boardId + " deleted.");
        sudokudb.deletePuzzle(boardId);
    }

    public void adjustGridColumns(int width) {
        System.out.println(Math.max(1, width / (BOARD_SIZE)));
        int columns = Math.max(1, width / (BOARD_SIZE + 25));
        ((GridLayout) board.getLayout()).setColumns(columns);
        board.revalidate();
        board.repaint();
    }
}