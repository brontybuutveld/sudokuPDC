package sudokupdc.UI;

import sudokupdc.*;
import sudokupdc.DB.SudokuDB;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;

public class BoardUI extends JPanel {
    private JTextField[][] cells = new JTextField[9][9];
    private boolean[][] mask = new boolean[9][9];
    private int[][] board;
    private int id;
    private SudokuDB sudokudb = SudokuDB.getInstance();
    private static final TopUI top = TopUI.getInstance();

    public BoardUI() {
        Stack<Integer> input = new Stack<>();
        input.push(-1);
        AlgorithmX ax = new AlgorithmX(input, new Stack<>());
        ax.search(0, false, false, false);

        int[][] data = ax.toArray(true);
        int[][] sol = ax.toArray(false);
        this.board = data;

        this.id = sudokudb.insertPuzzleTable(data, sol);
        makeUI();
    }

    public BoardUI(int id) {
        if (id == -1) {
            System.exit(-1);
        } else {
            this.id = id;
            ArrayList<String[]> puzzle = sudokudb.getPuzzle();
            int[][] data = new int[9][9];
            for (String[] pzl : puzzle) {
                char[] cData = pzl[1].toCharArray();
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        data[i][j] = cData[i * 9 + j] - '0';
                    }
                }
            }
            this.board = data;
        }
        makeUI();
    }

    public BoardUI(int[][] board) {
        this.board = board;
        Stack<Integer> input = new Stack<>();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == 0) continue;
                input.add(4 * (81 * i + 9 * j + (board[i][j] - 1)));
            }
        }
        AlgorithmX ax = new AlgorithmX(input, new Stack<>());
        ax.search(0, true, false, false);
        
        int[][] data = ax.toArray(true);
        int[][] sol = ax.toArray(false);
        
        this.id = sudokudb.insertPuzzleTable(data, sol);
        makeUI();
    }

    private void makeUI() {
        JPanel gridPanel = new JPanel();
        setLayout(new BorderLayout());

        Dimension size = new Dimension(510, 560);
        setMinimumSize(size);

        gridPanel.setLayout(new GridLayout(9, 9));
        gridPanel.setBackground(Color.WHITE);

        makeCells(gridPanel);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.add(gridPanel);

        JPanel buttonPanel = new JPanel(new BorderLayout());
        makeButtons(buttonPanel);

        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setSize(size);
        setVisible(true);
    }

    private void makeButtons(JPanel buttonPanel) {
        
        JPanel north = new JPanel();
        JPanel south = new JPanel();
        
        final JButton menuButton = new JButton("Main menu");
        final JButton exportButton = new JButton("Export");
        final JButton checkButton = new JButton("Check Solution");
        final JButton solveButton = new JButton("Solve");
        final JButton resetButton = new JButton("Reset");
        final JButton undoButton = new JButton("Undo");
        final JButton redoButton = new JButton("Redo");

        north.add(menuButton);
        north.add(exportButton);
        north.add(checkButton);
        north.add(solveButton);
        south.add(resetButton);
        south.add(undoButton);
        south.add(redoButton);

        menuButton.addActionListener(new MenuAction());
        exportButton.addActionListener(new ExportAction());
        checkButton.addActionListener(new CheckSolutionAction());
        solveButton.addActionListener(new SolveAction());
        resetButton.addActionListener(new ResetGridAction());
        undoButton.addActionListener(new UndoAction());
        redoButton.addActionListener(new RedoAction());
        
        buttonPanel.add(north, BorderLayout.NORTH);
        buttonPanel.add(south, BorderLayout.SOUTH);
    }

    private void makeCells(JPanel gridPanel) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                JTextField text = new JTextField();
                text.setFont(new Font("SansSerif", Font.PLAIN, 30));
                text.setForeground(Color.BLUE);

                if (this.board[row][col] != 0) {
                    text = new JTextField("" + this.board[row][col]);
                    text.setFont(new Font("SansSerif", Font.PLAIN, 30));
                    text.setEditable(false);
                }

                if (mask[row][col]) text.setBackground(Color.RED);

                text.setPreferredSize(new Dimension(50, 50));
                text.setHorizontalAlignment(JTextField.CENTER);
                Border border = setBorder(row, col);
                text.setBorder(border);
                ((AbstractDocument) text.getDocument()).setDocumentFilter(new SudokuInputFilter());

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

    private abstract class BaseAction implements ActionListener {
        protected void updateCell(int[] vals) {
            int newValue = vals[0];
            int row = vals[1];
            int col = vals[2];

            if (newValue >= 0) {
                board[row][col] = newValue;
                cells[row][col].setText(newValue != 0 ? String.valueOf(newValue) : "");
                checkConstraints();
            }
        }
    }

    private class UndoAction extends BaseAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            int[] vals = sudokudb.undo(id);
            updateCell(vals);
        }
    }

    private class RedoAction extends BaseAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            int[] vals = sudokudb.redo(id);
            updateCell(vals);
        }
    }

    private class ResetGridAction extends BaseAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            int newValue = 0;
            while (newValue >= 0) {
                int[] vals = sudokudb.undo(id);
                newValue = vals[0];
                updateCell(vals);
            }
        }
    }

    private class SolveAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int[][] temp = sudokudb.getSolution(id);
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    System.out.print(temp[i][j]);
                    if (board[i][j] != 0) continue;
                    int prev = board[i][j];
                    board[i][j] = temp[i][j];
                    int val = board[i][j];
                    cells[i][j].setText(String.valueOf(temp[i][j]));
                    sudokudb.insertMoveTable(id, i, j, val, prev);
                }
                System.out.println("");
            }
            checkConstraints();
        }
    }

    private class MenuAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            top.setRoot(new MenuUI());
        }
    }
    
    private class ExportAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            File file = new File("sudoku"+id+".txt");
            try (FileWriter writer = new FileWriter(file)) {
                for (int row = 0; row < 9; row++) {
                    for (int col = 0; col < 9; col++) {
                        writer.write(board[row][col] + (col < 8 ? " " : ""));
                    }
                    writer.write("\n");
                }
                JOptionPane.showMessageDialog(null, "Sudoku exported successfully to sudoku.txt!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e2) {
                JOptionPane.showMessageDialog(null, "Error saving file", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class CheckSolutionAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (isValidSudoku()) {
                JOptionPane.showMessageDialog(null, "Correct Solution!", "Sudoku", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Invalid Solution!", "Sudoku", JOptionPane.ERROR_MESSAGE);
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

    private boolean isValidSudoku() {
        try {
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    if (mask[row][col] || cells[row][col].getText().isEmpty()) {
                        return false;
                    }
                }
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private class SudokuInputFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (isValidInput(string) && (fb.getDocument().getLength() + string.length() <= 1)) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (isValidInput(text) && (fb.getDocument().getLength() - length + text.length() <= 1)) {
                super.replace(fb, offset, length, text, attrs);
            }
        }

        private boolean isValidInput(String text) {
            return text.isEmpty() || text.matches("[1-9]");
        }
    }

    private Border setBorder(int row, int col) {
        int top    = (row == 0) ? 4 : (row % 3 == 0) ? 2 : 1;
        int left   = (col == 0) ? 4 : (col % 3 == 0) ? 2 : 1;
        int bottom = (row == 8) ? 4 : (row % 3 == 2) ? 2 : 1;
        int right  = (col == 8) ? 4 : (col % 3 == 2) ? 2 : 1;
        return BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK);
    }
}