package sudokupdc.Types;

public class ColumnNode extends Node {
    public int len;
    public ColumnNode right;
    public ColumnNode left;

    public ColumnNode() {
        super();
        this.len = 0;
        this.right = null;
        this.left = null;
    }

    public ColumnNode(int value) {
        super(value);
        this.len = 0;
        this.right = null;
        this.left = null;
    }
}