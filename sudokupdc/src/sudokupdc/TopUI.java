package sudokupdc;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class TopUI implements PropertyChangeListener {
    SelectBoardUI select;
    JPanel root = new JPanel();
    JFrame frame;
    private static TopUI instance;
    
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("test");
        this.select = (SelectBoardUI) evt.getNewValue();
        JScrollPane scrollBar = new JScrollPane(root);
        scrollBar.getViewport().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                select.adjustGridColumns(scrollBar.getViewport().getWidth());
            }
        });
        frame.add(scrollBar);
    }
    
    private TopUI() {
        frame = new JFrame();
        Dimension size = new Dimension(520, 560);
        frame.setMinimumSize(size);
        frame.add(root);
        
        frame.setTitle("Sudoku");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
    }
    
    public static TopUI getInstance() {
        if (instance == null) {
            instance = new TopUI();
        }
        return instance;
    }
    
    public void setRoot(JPanel panel) {
        frame.remove(root);
        root = panel;
        frame.add(root);
        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }
}