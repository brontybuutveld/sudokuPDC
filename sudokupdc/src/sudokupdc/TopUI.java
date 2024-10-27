package sudokupdc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class TopUI {
    private static final int BOARD_SIZE = 142;
    private JPanel board;
    SudokuDB sudokudb = new SudokuDB();
    Boolean isSelect = false;
    SelectBoardUI select;
    

    public TopUI() {
        JFrame frame = new JFrame();
        JPanel div = new JPanel();
        JPanel menu = new JPanel();
        Dimension size = new Dimension(520, 560);
        frame.setMinimumSize(size);
        frame.add(div);
        div.add(menu);
        
        JButton playButton = new JButton("Play");
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                div.add(new BoardUI(-2, sudokudb));
                div.remove(menu);
                div.revalidate();
                div.repaint();
            }
        });
        menu.add(playButton, BorderLayout.EAST);

        JButton selectButton = new JButton("Select");
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isSelect = true;
                select = new SelectBoardUI(sudokudb);
                JScrollPane scrollBar = new JScrollPane(div);
                scrollBar.getViewport().addComponentListener(new ComponentAdapter() {
                    @Override
                    public void componentResized(ComponentEvent e) {
                        select.adjustGridColumns(scrollBar.getViewport().getWidth());
                    }
                });
                frame.add(scrollBar);
                
                div.setSize(size);
                div.add(select);
                div.remove(menu);
                frame.revalidate();
                frame.repaint();
            }
        });
        menu.add(selectButton, BorderLayout.WEST);
        
        JButton importButton = new JButton("Import");
        importButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        menu.add(importButton, BorderLayout.WEST);
        
        frame.setTitle("Sudoku");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new TopUI();
    }
}