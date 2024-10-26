package sudokupdc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

public class SelectBoardUI {
    private static final int BOARD_SIZE = 142;
    private JPanel board;
    private JScrollPane scrollBar;
    SudokuDB sudokudb = new SudokuDB();

    public SelectBoardUI() {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        Dimension size = new Dimension(510, 560);
        frame.setMinimumSize(size);

        board = new JPanel(new GridLayout(0, 1));
        panel.add(board);

        scrollBar = new JScrollPane(panel);
        frame.add(scrollBar);

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
                }
            });
            boardContainer2.add(deleteButton, BorderLayout.WEST);

            board.add(boardContainer);
        }

        scrollBar.getViewport().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustGridColumns();
            }
        });

        frame.setTitle("Sudoku");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(size);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        adjustGridColumns();
    }

    private void onBoardSelected(int boardId) {
        System.out.println("Board " + boardId + " sel.");
    }
    
    private void deleteBoard(int boardId) {
        System.out.println("Board " + boardId + " del.");
        sudokudb.deletePuzzle(boardId);
    }

    private void adjustGridColumns() {
        int availableWidth = scrollBar.getViewport().getWidth();
        int columns = Math.max(1, availableWidth / (BOARD_SIZE + 20));
        ((GridLayout) board.getLayout()).setColumns(columns);
        board.revalidate();
    }

    public static void main(String[] args) {
        new SelectBoardUI();
    }
}