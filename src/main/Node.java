package main;

/**
 * This class represents a node in a doubly linked list of characters.
 * Known Bugs: None
 *
 * @author Zhenxu Chen
 * @email  zhenxuchen@brandeis.edu
 * @date Feb 19, 2024
 * <p>
 * COSI 21A PA1
 */
public class Node {
	
	public Node next;
	public Node prev;
	public char data;
	
	public Node(char c) {
		this.next = null;
		this.prev = null;
		this.data = c;
	}
}
