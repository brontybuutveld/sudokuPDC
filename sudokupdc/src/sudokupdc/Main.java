package sudokupdc;

import sudokupdc.UI.MenuUI;
import sudokupdc.UI.TopUI;

public class Main {
    public static void main(String[] args) {
        TopUI top = TopUI.getInstance();
        top.setRoot(new MenuUI());
    }
}