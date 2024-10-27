package sudokupdc.UI;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import sudokupdc.DB.SudokuDB;

public class MenuUI extends JPanel {
    private static final TopUI top = TopUI.getInstance();
    private final SudokuDB sudokudb = SudokuDB.getInstance();
   
    public MenuUI() {
        JPanel div = new JPanel(new BorderLayout());
        setBorder(new EmptyBorder(200, 0, 0, 0));
        JPanel menu = new JPanel();
        JLabel title = new JLabel("SUDOKU");
        title.setFont(new Font("SansSerif", Font.PLAIN, 60));
        div.add(title, BorderLayout.CENTER);
        div.add(menu, BorderLayout.SOUTH);
        
        JButton playButton = new JButton("Play");
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                top.setRoot(new BoardUI());
            }
        });
        menu.add(playButton, BorderLayout.EAST);

        JButton selectButton = new JButton("Select");
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (sudokudb.isTable("PUZZLE")) {
                    SelectBoardUI select = new SelectBoardUI();
                    top.setSelect(select);
                    top.setRoot(select);
                } else {
                    JOptionPane.showMessageDialog(null, "No puzzles to select!", "Sudoku", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        menu.add(selectButton, BorderLayout.WEST);
        
        JButton importButton = new JButton("Import");
        importButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[][] cells = loadPuzzle();
                top.setRoot(new BoardUI(cells));
            }
        });
        menu.add(importButton, BorderLayout.WEST);
        add(div);
    }
    
    private int[][] loadPuzzle() {
        
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        
        int[][] cells = new int[9][9];
        
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (Scanner scanner = new Scanner(file)) {
                for (int row = 0; row < 9; row++) {
                    if (!scanner.hasNextLine()) break;
                    String[] values = scanner.nextLine().split("[ ,]+");
                    for (int col = 0; col < 9; col++) {
                        if (col < values.length) {
                            try {
                                cells[row][col] = Integer.parseInt(values[col]);
                            } catch (NumberFormatException e) {
                                cells[row][col] = 0;
                            }
                        }
                    }
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error loading file", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return cells;
    }
}