package sudokupdc;

public class Main {
    public static void main(String[] args) {
        TopUI top = TopUI.getInstance();
        top.setRoot(new MenuUI());
    }
}