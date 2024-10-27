package sudokupdc.UI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeSupport;
import javax.swing.JButton;
import javax.swing.JPanel;

public class MenuUI extends JPanel {
    SelectBoardUI select;
    private PropertyChangeSupport support;
    private TopUI top = TopUI.getInstance();
    
    public MenuUI() {
        JPanel div = new JPanel();
        JPanel menu = new JPanel();
        div.add(menu);
        support = new PropertyChangeSupport(this);
        
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
                select = new SelectBoardUI();
                support.firePropertyChange("set", null, select);
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