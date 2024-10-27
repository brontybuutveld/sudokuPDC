package sudokupdc.UI;

import sudokupdc.DB.SudokuDB;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SelectBoardUI extends JPanel {
    private static final int BOARD_SIZE = 142;
    private final JPanel board;
    private final TopUI top = TopUI.getInstance();
    private final SudokuDB sudokudb = SudokuDB.getInstance();

    public SelectBoardUI() {
        setLayout(new BorderLayout());
        JPanel buttons = new JPanel();
        
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                top.setSelect(null);
                top.setRoot(new MenuUI());
            }
        });
        buttons.add(backButton);
        
        JButton delButton = new JButton("DELETE ALL PUZZLES");
        delButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sudokudb.dropMoveTable();
                sudokudb.dropPuzzleTable();
                remove(board);
                revalidate();
                repaint();
            }
        });
        buttons.add(delButton);
        
        add(buttons, BorderLayout.NORTH);
        
        board = new JPanel(new GridLayout(0, 3));
        add(board, BorderLayout.CENTER);

        ArrayList<String[]> puzzle = sudokudb.getPuzzle();

        for (String[] pzl : puzzle) {
            int[][] data = new int[9][9];
            char[] cData = pzl[1].toCharArray();
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    data[i][j] = cData[i * 9 + j] - '0';
                }
            }

            JPanel boardContainer = new JPanel();
            boardContainer.setLayout(new BoxLayout(boardContainer, BoxLayout.PAGE_AXIS));
            BoardUI1 boardUI = new BoardUI1(data, Integer.valueOf(pzl[0]), sudokudb);
            boardContainer.setSize(new Dimension(BOARD_SIZE, BOARD_SIZE));
            boardContainer.add(boardUI);
            
            JPanel boardContainer2 = new JPanel(new BorderLayout());
            boardContainer.add(boardContainer2);
            
            JPanel boardContainer3 = new JPanel();
            boardContainer2.add(boardContainer3, BorderLayout.NORTH);

            JButton playButton = new JButton("Play");
            playButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onBoardSelected(Integer.valueOf(pzl[0]));
                }
            });
            boardContainer3.add(playButton);

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
            boardContainer3.add(deleteButton);

            board.add(boardContainer);
        }
        setVisible(true);
    }

    private void onBoardSelected(int boardId) {
        top.setSelect(null);
        top.setRoot(new BoardUI(boardId));
    }

    private void deleteBoard(int boardId) {
        sudokudb.deletePuzzle(boardId);
    }

    public void adjustGridColumns(int width) {
        System.out.println(Math.max(1, width / (BOARD_SIZE)));
        int columns = Math.max(1, width / (BOARD_SIZE + 30));
        ((GridLayout) board.getLayout()).setColumns(columns);
        board.revalidate();
        board.repaint();
    }
}