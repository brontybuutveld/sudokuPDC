package sudokupdc;

import sudokupdc.Types.ColumnNode;
import sudokupdc.Types.Node;

public class MakeData {
    private ColumnNode head;
    private int numColumns, numRows;
    Node[] matrix = new Node[4*9*9*9];

    public MakeData(ColumnNode head) {
        this.head = head;
    }

    public ColumnNode[] makeColumns(int len) {
        this.numColumns = len;
        ColumnNode headPtr = head;
        ColumnNode[] columns = new ColumnNode[numColumns + 1];

        for (int i = 0; 0 < len; i++) {
            head.right = new ColumnNode(i + 1);
            head.right.left = head;
            columns[i] = head;
            head = head.right;
            len--;
        }

        headPtr.left = head;
        head.right = headPtr;
        columns[numColumns] = head;

        return columns;
    }

    public Node[] makeMatrix(ColumnNode[] columns) {
        int carry, carry2;

        for (int i = 0; i < 81; i++) {
            for (int j = 0; j < 9; j++) {
                int x = i * 9 + j;
                int y = i;

                this.matrix[x * 4] = linkNodes(x * 4, y, columns);
            }
        }

        carry = -9;
        for (int i = 0; i < 81; i++) {
            if (i%9==0)
                carry += 9;
            for (int j = 0; j < 9; j++) {
                int x = i * 9 + j;
                int y = j + carry
                        + 81; // range shift
                this.matrix[x * 4 + 1] = linkNodes(x * 4 + 1, y, columns);
                //this.matrix[x * 4].right = this.matrix[x * 4 + 1];
                //this.matrix[x * 4 + 1].left = this.matrix[x * 4];
            }
        }

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 81; j++) {
                int x = i * 81 + j;
                int y = j
                        + 2*81; // range shift
                this.matrix[x * 4 + 2] = linkNodes(x * 4 + 2, y, columns);
                //this.matrix[x * 4 + 1].right = this.matrix[x * 4 + 2];
                //this.matrix[x * 4 + 2].left = this.matrix[x * 4 + 1];
            }
        }

        carry = -27;
        for (int i = 0; i < 9; i++) {
            if (i % 3 == 0)
                carry += 27;
            carry2 = -9;
            for (int j = 0; j < 9; j++) {
                if (j % 3 == 0)
                    carry2 += 9;
                for (int k = 0; k < 9; k++) {
                    int x = k + 9 * j + 81 * i;
                    int y = k + carry2 + carry
                            + 3*81; //range shift
                    this.matrix[x * 4 + 3] = linkNodes(x * 4 + 3, y, columns);
                    //this.matrix[x * 4 + 2].right = this.matrix[x * 4 + 3];
                    //this.matrix[x * 4 + 3].left = this.matrix[x * 4 + 2];
                    //this.matrix[x * 4 + 3].right = this.matrix[x * 4];
                    //this.matrix[x * 4].left = this.matrix[x * 4 + 3];
                }
            }
        }
        return matrix;
    }

    private Node linkNodes(int x, int y1, ColumnNode[] columns) {
        Node newNode = new Node();
        newNode.value = x;
        newNode.top = columns[y1 + 1];
        newNode.top.len++;
        newNode.down = newNode.top;
        if(columns[y1 + 1].down == null)
            newNode.up = newNode.top;
        else
            newNode.up = newNode.top.up;
        newNode.down.up = newNode;
        newNode.up.down = newNode;

        //rows.get(x).add(newNode);
        return newNode;
    }

}
