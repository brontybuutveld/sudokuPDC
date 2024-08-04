package com.mycompany.sudoku;


public class List {
    private HeadNode head;

    public List() {
        this.head = new HeadNode();
    }
    public List(HeadNode head) {
        this.head = head;
    }

    public HeadNode getHead() {
        return head;
    }

    public void setHead(HeadNode head) {
        this.head = head;
    }

    public void makeBoard() {

    }
}
