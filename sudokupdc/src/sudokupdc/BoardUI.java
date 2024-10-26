package sudokupdc;

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
import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;

public class BoardUI extends JFrame {
    private JTextField[][] cells = new JTextField[9][9];
    private boolean[][] mask = new boolean[9][9];
    private final int[][] board;
    private final int id;
    private final SudokuDB sudokudb;
    
    public BoardUI(int[][] board, int id, SudokuDB sudokudb) {
        this.board = board;
        this.id = id;
        this.sudokudb = sudokudb;
        
        if (id == -1) System.exit(-1);
        
        makeUI();
    }
    
    public BoardUI(int id, SudokuDB sudokudb) {
        this.sudokudb = sudokudb;
        if (id == -1) System.exit(-1);
        if (id == -2) {
            ColumnNode head = new ColumnNode();
            MakeData md = new MakeData(head);
            ColumnNode[] columns = md.makeColumns(4 * 9 * 9);
            Node[] matrix = md.makeMatrix(columns);
            Stack<Integer> input = new Stack<>();
            input.push(-1);
            AlgorithmX ax = new AlgorithmX(columns[0], matrix, input, new Stack<>());
            ax.search(0, false, false, false, false, false);

            int[][] data = ax.toArray(true);
            int[][] sol = ax.toArray(false);
            this.board = data;

            this.id = sudokudb.insertPuzzleTable(data, sol);
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
    
    private void makeUI() {
        JPanel gridPanel = new JPanel();
        setTitle("Sudoku");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        Dimension size = new Dimension(510, 560);
        setMinimumSize(size);
        
        gridPanel.setLayout(new GridLayout(9, 9));
        gridPanel.setBackground(Color.WHITE);
        
        makeCells(gridPanel);
        
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.add(gridPanel);

        JPanel buttonPanel = new JPanel();
        makeButtons(buttonPanel);
        
        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        setSize(size);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void makeButtons(JPanel buttonPanel) {
        final JButton checkButton = new JButton("Check Solution");
        final JButton resetButton = new JButton("Reset");
        final JButton undoButton = new JButton("Undo");
        final JButton redoButton = new JButton("Redo");

        buttonPanel.add(checkButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(undoButton);
        buttonPanel.add(redoButton);

        checkButton.addActionListener(new CheckSolutionAction());
        resetButton.addActionListener(new ResetGridAction());
        undoButton.addActionListener(new UndoAction());
        redoButton.addActionListener(new RedoAction());
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