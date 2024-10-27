package sudokupdc.UI;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
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
                top.setRoot(new BoardUI(-2));
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

            }
        });
        menu.add(importButton, BorderLayout.WEST);
        add(div);
    }
}