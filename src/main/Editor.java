package main;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * This class represents a simple text editor. The editor is implemented as a doubly linked list of characters.
 * Known Bugs: None
 *
 * @author Zhenxu Chen
 * @email  zhenxuchen@brandeis.edu
 * @date Feb 19, 2024
 * <p>
 * COSI 21A PA1
 */

public class Editor {
	
	public int numChars; /** KEEP THIS PUBLIC : use this to store the number of characters in your Editor */
	public int curPos; /** KEEP THIS PUBLIC : use this to store the current cursor index in [0, numChars] */
	
	public Node cur; /** KEEP THIS PUBLIC : use this to reference the node that is after the visual cursor or null if curPos = numChars */
	public Node head; /** KEEP THIS PUBLIC : use this to reference the first node in the Editor's doubly linked list */
	public Node tail; /** KEEP THIS PUBLIC : use this to reference the last node in the Editor's doubly linked list */

	/**
	 * This constructor initializes an empty Editor.
	 */
	public Editor() {
		numChars = 0;
		curPos = 0;
		cur = null;
		head = null;
		tail = null;
	}

	/**
	 * This constructor initializes an Editor with the characters from the file at the given filepath.
	 *
	 * @param filepath the path to the file to read from
	 * @throws FileNotFoundException if the file at filepath does not exist
	 */
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
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			for (char c : line.toCharArray()) {
				Node n = new Node(c);
				if (numChars == 0) {
					head = n;
				} else {
					tail.next = n;
					n.prev = tail;
				}
				tail = n;
				numChars++;
			}
			// 添加换行符到链表
			Node newLineNode = new Node('\n');
			if (numChars == 0) {
				head = newLineNode;
			} else {
				tail.next = newLineNode;
				newLineNode.prev = tail;
			}
			tail = newLineNode;
			numChars++;
		}
		curPos = numChars;
		cur = null;
		scanner.close();
	}

	/**
	 * This method returns the current cursor position.
	 *
	 * @return the current cursor position
	 * @runtime O(1)
	 */
	public int getCursorPosition() {
		return curPos;
	}

	/**
	 * This method returns the number of characters in the Editor.
	 *
	 * @return the number of characters in the Editor
	 * @runtime O(1)
	 */
	public int size() {
		return numChars;
	}

	/**
	 * This method moves the cursor to the right by one character.
	 * @runtime O(1)
	 */
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

	/**
	 * This method moves the cursor to the left by one character.
	 * @runtime O(1)
	 */
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

	/**
	 * This method moves the cursor to the head of the list.
	 * @runtime O(1)
	 */
	public void moveToHead() {
		curPos = 0;
		cur = head;
	}

	/**
	 * This method moves the cursor to the tail of the list.
	 * @runtime O(1)
	 */
	public void moveToTail() {
		curPos = numChars;
		cur = null;
	}

	/**
	 * This method inserts the given character at the current cursor position.
	 *
	 * @param c the character to insert
	 * @runtime O(1)
	 */
	public void insert(char c) {
		Node n = new Node(c);
		if (numChars == 0) {
			head = n;
			tail = n;
			cur = null;
		} else if (curPos == 0) {
			n.next = head;
			head.prev = n;
			cur = head;
			head = n;
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

	/**
	 * This method deletes the character after the current cursor position.
	 * @runtime O(1)
	 */
	public void delete() {
		if (curPos == 0 && numChars > 0) {
			Node temp = head.next;
			head = head.next;
			if(head != null) head.prev = null;
			cur = temp;
		} else if (numChars == 0 || curPos == numChars) {
			return;
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

	/**
	 * This method deletes the character before the current cursor position.
	 * @runtime O(1)
	 */
	public void backspace() {
		if (curPos == 1) {
			head = head.next;
			if(head != null){
				head.prev = null;
			}else{
				tail = null;
			}
		} else if (curPos == 0) {
			return;
		} else if (curPos == numChars) {
			tail = tail.prev;
			if(tail != null) tail.next = null;
		} else {
			cur.prev.prev.next = cur;
			cur.prev = cur.prev.prev;
		}
		numChars--;
		curPos--;
	}

	/**
	 * This method returns a string representation of the characters in the Editor.
	 *
	 * @return a string representation of the characters in the Editor
	 * @runtime O(n)
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Node n = head;
		while (n != null) {
			sb.append(n.data);
			n = n.next;
		}
		return sb.toString();
	}

	/**
	 * This method clears the Editor.
	 * @runtime O(1)
	 */
	public void clear() {
		numChars = 0;
		curPos = 0;
		cur = null;
		head = null;
		tail = null;
	}

	/**
	 * This method saves the contents of the Editor to a file at the given savepath.
	 *
	 * @param savepath the path to the file to save to
	 * @throws FileNotFoundException if the file at savepath cannot be created
	 * @runtime O(n)
	 */
	public void save(String savepath) throws FileNotFoundException {
		File file = new File(savepath);
		PrintStream output = new PrintStream(file);
		output.print(this);
		output.close();
	}
}
