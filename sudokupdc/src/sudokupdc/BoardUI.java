/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
import java.util.Objects;

/**
 *
 * @author brontybuutveld
 */
public class BoardUI extends JFrame {
    private JTextField[][] cells;
    private final JButton checkButton, resetButton;
    private boolean[][] mask = new boolean[9][9];
    private Integer[][] input = {
                    {0,0,3,0,2,0,6,0,0},
                    {9,0,0,3,0,5,0,0,1},
                    {0,0,1,8,0,6,4,0,0},
                    {0,0,8,1,0,2,9,0,0},
                    {7,0,0,0,0,0,0,0,8},
                    {0,0,6,7,0,8,2,0,0},
                    {0,0,2,6,0,9,5,0,0},
                    {8,0,0,2,0,3,0,0,9},
                    {0,0,5,0,1,0,3,0,0}
            };
    
    public BoardUI() {
        setTitle("Sudoku");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        Dimension size = new Dimension(500, 560);
        setMinimumSize(size);

        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridBagLayout());
        gridPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        //gbc.insets = new Insets(0, 0, 0, 0);

        cells = new JTextField[9][9];

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                JTextField text = new JTextField();
                text.setFont(new Font("SansSerif", Font.PLAIN, 30));
                text.setForeground(Color.BLUE);
                
                if (input[row][col] != 0) {
                    text = new JTextField("" + input[row][col]);
                    text.setFont(new Font("SansSerif", Font.PLAIN, 30));
                    text.setEditable(false);
                }

                if (mask[row][col]) {
                    text.setBackground(Color.RED);
                }
                
                text.setPreferredSize(new Dimension(50, 50));
                text.setHorizontalAlignment(JTextField.CENTER);
                Border border = setBorder(row, col);
                text.setBorder(border);
                ((AbstractDocument) text.getDocument()).setDocumentFilter(new SudokuInputFilter());
                
                JTextField finalText = text;
                int finalRow = row;
                int finalCol = col;
                finalText.addKeyListener(new KeyListener() {
                    @Override
                    public void keyTyped(KeyEvent e) {

                    }

                    @Override
                    public void keyPressed(KeyEvent e) {

                    }

                    @Override
                    public void keyReleased(KeyEvent e) {
                        if (finalText.getText().isEmpty())
                            input[finalRow][finalCol] = 0;
                        else
                            input[finalRow][finalCol] = Integer.valueOf(finalText.getText());
                        checkConstraints();
                    }

                    private void updateLabel() {
                        
                    }
                });

                gbc.gridx = col;
                gbc.gridy = row;
                cells[row][col] = finalText;
                gridPanel.add(cells[row][col], gbc);
            }
        }

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.add(gridPanel);

        JPanel buttonPanel = new JPanel();
        checkButton = new JButton("Check Solution");
        resetButton = new JButton("Reset");

        buttonPanel.add(checkButton);
        buttonPanel.add(resetButton);

        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        checkButton.addActionListener(new CheckSolutionAction());
        resetButton.addActionListener(new ResetGridAction());

        setSize(size);
        setLocationRelativeTo(null);
        setVisible(true);
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
                if (input[row][col] != 0) {
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
            if (Objects.equals(input[row][i], input[row][col]) && i != col) {
                mask[row][i] = true;
                mask[row][col] = true;
            }
        }
    }

    private void checkColumnConstraints(int row, int col) {
        for (int i = 0; i < 9; i++) {
            if (Objects.equals(input[i][col], input[row][col]) && i != row) {
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
                if (Objects.equals(input[i][j], input[row][col]) && i != row && j != col) {
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

    private class ResetGridAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    cells[row][col].setText("");
                }
            }
        }
    }

    private boolean isValidSudoku() {
        try {
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    String text = cells[row][col].getText();
                    if (text.isEmpty()) return false;
                    int num = Integer.parseInt(text);
                    if (num < 1 || num > 9) {
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
            return text.matches("[1-9]");
        }
    }
    
    private Border setBorder(int row, int col) {
        int top    = (row == 0) ? 4 : (row % 3 == 0) ? 2 : 1;
        int left   = (col == 0) ? 4 : (col % 3 == 0) ? 2 : 1;
        int bottom = (row == 8) ? 4 : (row % 3 == 2) ? 2 : 1;
        int right  = (col == 8) ? 4 : (col % 3 == 2) ? 2 : 1;
        return BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK);
    }

    public static void main(String[] args) {
        new BoardUI();
    }
}