package com.mycompany.sudoku;

public class HeadNode extends Node {
    public int len;
    public HeadNode right;
    public HeadNode left;
    public HeadNode() {
        super();
    }
    public HeadNode(int value) {
        super(value);
        this.value = value;
    }
}
