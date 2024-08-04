package com.mycompany.sudoku;

public class Node {
    public int value;
    public HeadNode top;
    public Node up;
    public Node down;

    public Node() {
        this.value = 0;
    }
    public Node(int value) {
        this.value = value;
    }
    public void setUp(Node up) {
        this.up = up;
    }
    public void setDown(Node down) {
        this.down = down;
    }
    public Node getUp() {
        return up;
    }
    public Node getDown() {
        return down;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
