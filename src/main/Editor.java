package main;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

public class Editor {
	
	public int numChars; /** KEEP THIS PUBLIC : use this to store the number of characters in your Editor */
	public int curPos; /** KEEP THIS PUBLIC : use this to store the current cursor index in [0, numChars] */
	
	public Node cur; /** KEEP THIS PUBLIC : use this to reference the node that is after the visual cursor or null if curPos = numChars */
	public Node head; /** KEEP THIS PUBLIC : use this to reference the first node in the Editor's doubly linked list */
	public Node tail; /** KEEP THIS PUBLIC : use this to reference the last node in the Editor's doubly linked list */
	
	public Editor() {
		numChars = 0;
		curPos = 0;
		cur = null;
		head = null;
		tail = null;
	}
	
	public Editor(String filepath) throws FileNotFoundException {
		File file = new File(filepath);
		Scanner scanner = new Scanner(file);

		// Initialize Editor
		numChars = 0;
		curPos = 0;
		cur = null;
		head = null;
		tail = null;

		// Read characters from file and create nodes
		while (scanner.hasNext()) {
			char c = scanner.next().charAt(0);
			Node n = new Node(c);
			if (numChars == 0) {
				head = n;
            } else {
				tail.next = n;
				n.prev = tail;
				curPos++;
            }
            tail = n;
            cur = n;
            numChars++;
		}
		curPos = numChars;
		cur = null;
		scanner.close();
	}
	
	public int getCursorPosition() {
		return curPos;
	}
	
	public int size() {
		return numChars;
	}
	
	public void moveRight() {
		if (curPos < numChars){
			curPos++;
			if (curPos == numChars) {
				cur = null;
			} else {
				cur = cur.next;
			}
		}
	}
	
	public void moveLeft() {
		if(curPos > 0){
			curPos--;
			if (curPos == 0) {
				cur = head;
			} else if(curPos + 1== numChars) {
				cur = tail;
			}else {
				cur = cur.prev;
			}
		}
	}
	
	public void moveToHead() {
		curPos = 0;
		cur = head;
	}
	
	public void moveToTail() {
		curPos = numChars;
		cur = null;
	}
	
	public void insert(char c) {
		Node n = new Node(c);
		if (numChars == 0) {
			head = n;
			tail = n;
			cur = n;
		} else if (curPos == 0) {
			n.next = head;
			head.prev = n;
			head = n;
			cur = n;
		} else if (curPos == numChars) {
			tail.next = n;
			n.prev = tail;
			tail = n;
		} else {
			n.prev = cur.prev;
			n.next = cur;
			cur.prev.next = n;
			cur.prev = n;
		}
		numChars++;
		curPos++;
	}
	
	public void delete() {
		if (curPos == 0) {
			Node temp = head.next;
			head = head.next;
			head.prev = null;
			cur = temp;
		} else if (curPos == numChars - 1) {
			tail = tail.prev;
			tail.next = null;
			cur = null;
		} else {
			Node temp = cur.next;
			cur.prev.next = cur.next;
			cur.next.prev = cur.prev;
			cur = temp;
		}
		numChars--;
	}
	
	public void backspace() {
		if (curPos == 1) {
			head = head.next;
			if(head != null) head.prev = null;
		} else if (curPos == numChars) {
			tail = tail.prev;
			tail.next = null;
		} else {
			cur.prev.prev.next = cur;
			cur.prev = cur.prev.prev;
		}
		numChars--;
		curPos--;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Node n = head;
		while (n != null) {
			sb.append(n.data);
			n = n.next;
		}
		return sb.toString();
	}
	
	public void clear() {
		numChars = 0;
		curPos = 0;
		cur = null;
		head = null;
		tail = null;
	}
	
	public void save(String savepath) throws FileNotFoundException {
		File file = new File(savepath);
		PrintStream output = new PrintStream(file);
		output.print(this);
		output.close();
	}
}
