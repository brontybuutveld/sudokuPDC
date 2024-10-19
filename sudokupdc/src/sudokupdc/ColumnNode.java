/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sudokupdc;

/**
 *
 * @author brontybuutveld
 */

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