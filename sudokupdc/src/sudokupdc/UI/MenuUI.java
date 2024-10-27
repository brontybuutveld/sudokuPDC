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

public class MenuUI extends JPanel {
    private static final TopUI top = TopUI.getInstance();
   
    public MenuUI() {
        JPanel div = new JPanel(new BorderLayout());
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
                SelectBoardUI select = new SelectBoardUI();
                top.setSelect(select);
                top.setRoot(select);
            }
        });
        menu.add(selectButton, BorderLayout.WEST);
        
        JButton importButton = new JButton("Import");
        importButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[][] cells = loadPuzzle();
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