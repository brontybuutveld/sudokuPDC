package sudokupdc;

public class Node {
    public int value;
    public Node down;
    //public Node left;
    //public Node right;
    public ColumnNode top;
    Node up;

    public Node() {
        this.down = null;
        this.top = null;
        //this.left = null;
        //this.right = null;
        this.value = 0;
    }

    public Node(int value) {
        this.down = null;
        this.top = null;
        this.value = value;
    }
}
