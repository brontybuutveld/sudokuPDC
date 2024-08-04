package com.mycompany.sudoku;
import java.util.Stack;

public class Main {
    public static Stack<Node> deleted = new Stack<>();

    public static void cover(HeadNode i) {
        Node p = i.down;
        while (p != i) {
            hide(p);
            p = p.down;
        }
        HeadNode l = i.left;
        HeadNode r = i.right;
        l.right = r;
        r.left = l;
    }
    public static void hide(Node p) {
        Node q = matrix2[p.value +1];
        while (q != p) {
            HeadNode x = q.top;
            Node u = q.up;
            Node d = q.down;
            if (x.value == 0) {
                q = u;
            } else {
                u.down = d;
                d.up = u;
                x.len--;
                q = matrix2[q.value +1];
            }
        }
    }
    public static void uncover(HeadNode i) {
        HeadNode l = i.left;
        HeadNode r = i.right;
        l.right = i;
        r.left = i;
        Node p = i.up;
        while (p != i) {
            unhide(p);
            p = p.up;
        }
    }
    public static void unhide(Node p) {
        Node q = matrix2[p.value -1];
        while (q != p) {
            HeadNode x = q.top;
            Node u = q.up;
            Node d = q.down;
            if (x.value == 0) {
                q = d;
            } else {
                u.down = q;
                d.up = q;
                x.len++;
                q = matrix2[q.value -1];
            }
        }
    }
    static int columns = 9*9*4, rows = 9*9*9, carry, carry2;
    static boolean[][] sparseMatrix = new boolean[rows][columns];
    static Node[][] matrix = new Node[rows][columns+2];
    static HeadNode[] matrixHead = new HeadNode[columns+1];
    static Node[] matrix2 = new Node[4000];
    public static void makeMatrix() {
        for (int i = 0; i < 81; i++) {
            for (int j = 0; j < 9; j++) {
                int x = i * 9 + j;
                int y = i;
                sparseMatrix[x][y] = true;
            }
        }
        carry = -9;
        for (int i = 0; i < 81; i++) {
            if (i%9==0)
                carry += 9;
            for (int j = 0; j < 9; j++) {
                int x = i * 9 + j;
                int y = j + carry + 81;
                sparseMatrix[x][y] = true;
            }
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 81; j++) {
                int x = i * 81 + j;
                int y = j + 2*81;
                sparseMatrix[x][y] = true;
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
                    int y = k + 3*81 + carry2 + carry;
                    sparseMatrix[x][y] = true;
                }
            }
        }
    }
    public static void heads(HeadNode head, int end) {
        HeadNode headPtr = head;
        for (int i = 1; i < end; i++) {
            HeadNode newNode = new HeadNode(i);
            head.right = newNode;
            newNode.left = head;
            head = newNode;
            matrixHead[i - 1] = newNode;
        }
        headPtr.left = head;
        head.right = headPtr;
    }
    public static void base(HeadNode head, Node[][] arr) {

        int nodeCount = columns + 1;
        Node up = null;
        for (int i = 0; i < rows; i++) {
            matrix[i][0] = new Node(nodeCount++);
            matrix2[nodeCount - 1] = matrix[i][0];
            if (i > 0) {
                matrix[i - 1][columns + 1] = matrix[i][0];
                matrix[i][0].up = up;

            }

            Node newNode = null;

            for (int j = 0; j < columns; j++) {
                if (sparseMatrix[i][j]) {
                    newNode = new Node(nodeCount++);
                    newNode.top = matrixHead[j];
                    matrixHead[j].len++;
                    if (matrixHead[j].up == null) {
                        matrixHead[j].up = newNode;
                        matrixHead[j].down = newNode;
                        newNode.down = matrixHead[j];
                        newNode.up = matrixHead[j];

                    } else {
                        newNode.up = matrixHead[j].up;
                        newNode.down = matrixHead[j];
                        matrixHead[j].up = newNode;
                    }
                    if (j < 81)
                        up = newNode;
                    matrix[i][j + 1] = newNode;
                    matrix2[nodeCount - 1] = newNode;
                }
            }
            matrix[i][0].down = newNode;
            matrix[i][0].top = head;
            if (i == rows - 1) {
                matrix[i][columns + 1] = new Node(nodeCount++);
                matrix[i][columns + 1].up = up;
                matrix[i][columns + 1].top = head;
            }
        }
    }
    public static void main(String[] args) {



        /*for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (sparseMatrix[i][j]) {
                    Node newNode = new Node(nodeCount++);
                    if (i != 0) {
                        int x = i;
                        while (!sparseMatrix[--x][j] && x != 0);
                        if (x != 0) {
                            newNode.left =
                        }
                    }
                }

            }
        }*/
        makeMatrix();
        List list = new List();
        heads(list.getHead(), 81*4+1);
        base(list.getHead(), matrix);
        //rec(list.getHead(), list.getHead(), 1, 7);
        /*delete(list.getHead().right);
        delete(list.getHead().right);
        delete(list.getHead().right);
        undo(deleted.pop());
        undo(deleted.pop());*/
        cover(matrixHead[1]);
        cover(matrixHead[2]);
        cover(matrixHead[0]);
        cover(matrixHead[5]);
        uncover(matrixHead[1]);
        uncover(matrixHead[2]);
        uncover(matrixHead[0]);
        uncover(matrixHead[5]);
        for (int i = 0; i < rows; i++) {
            for (int j = 0*81; j < 4*81; j++) {
                if (sparseMatrix[i][j]) {
                    System.out.print("#");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println("");
        }
        HeadNode head = list.getHead();
        for (int i = 0; i < columns; i++) {

            System.out.println(head.value);
            head = head.right;
        }
        System.out.println("test");
    }
}
