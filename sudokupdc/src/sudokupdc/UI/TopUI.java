package sudokupdc.UI;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

public class TopUI {
    private SelectBoardUI select;
    private JPanel root = new JPanel();
    private final JFrame frame;
    private static TopUI instance;
    private JScrollPane scrollBar;
    
    private TopUI() {
        frame = new JFrame();
        Dimension size = new Dimension(535, 560);
        frame.setMinimumSize(size);
        frame.add(root);
        scrollBar = null;
        
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
    
    public void setSelect(SelectBoardUI select) {
        this.select = select;
    }
    
    public void setRoot(JPanel panel) {
        frame.remove(root);
        root = panel;
        frame.add(root);
                
        if (select != null)
            addScroll();
        else if (scrollBar != null)
            removeScroll();
        
        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }
    
    private void removeScroll() {
        frame.remove(scrollBar);
    }

    private void addScroll() {
        scrollBar = new JScrollPane(root);
        scrollBar.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);

        frame.add(scrollBar);
        scrollBar.getViewport().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                select.adjustGridColumns(scrollBar.getViewport().getWidth());
            }
        });
    }
}