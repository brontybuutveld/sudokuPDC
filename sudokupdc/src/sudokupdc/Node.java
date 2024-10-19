/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sudokupdc;

/**
 *
 * @author brontybuutveld
 */
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
