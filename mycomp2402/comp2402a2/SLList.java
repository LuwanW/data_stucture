package comp2402a2;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.NoSuchElementException;

/**
 * An implementation of a FIFO Queue as a singly-linked list.
 * This also includes the stack operations push and pop, which
 * operate on the head of the queue
 * @author luwan
 *
 * @param <T> the class of objects stored in the queue
 */
public class SLList<T> extends AbstractList<T> implements Queue<T> {
	class Node {
		T x;
		Node next;
	}

	/**
	 * Front of the queue
	 */
	Node head;

	/**
	 * Tail of the queue
	 */
	Node tail;

	/**
	 * The number of elements in the queue
	 */
	int n;

	public T get(int i) {
		// TODO: Implement this
		if (i < 0 || i > n - 1) throw new IndexOutOfBoundsException();
		return this.getNode(i).x;
	}

	public T set(int i, T x) {
		// TODO: Implement this
		if (i < 0 || i > n - 1) throw new IndexOutOfBoundsException();
		Node target = this.getNode(i-1);
		Node new_node = new Node();
		new_node.x = x;
		this.addAfter(target,new_node);
		this.deleteNext(new_node);
		return x;
	}

	public void add(int i, T x) {
		// TODO: Implement this
		if (i < 0 || i > n) throw new IndexOutOfBoundsException();		
		if(n==0) {
			this.add(x);
		}else if(i == 0) {
			Node new_node = new Node();
			new_node.x = x;
			new_node.next = this.head;
			this.head = new_node;
			n++;
		}
		else {
			Node target = this.getNode(i-1);
			Node new_node = new Node();
			new_node.x = x;
			this.addAfter(target,new_node);
			n++;
		}
	}

	public T remove(int i) {
		// TODO: Implement this
		if (i < 0 || i > n - 1) throw new IndexOutOfBoundsException();
		T value;
		if (i == 0){
			value = this.pop();
		}else {
			Node target = this.getNode(i-1);
			this.deleteNext(target);
			value = target.x;
			n--;
		}
		return value;
	}

	public void reverse() {
		// TODO: Implement this
		Node current = head;
		Node prev = null, next = null;
		while(current != null) {
			next = current.next;
			current.next = prev;
			prev = current;
			current = next;
		}
		head = prev;
		
	}

	public Iterator<T> iterator() {
		class SLIterator implements Iterator<T> {
			protected Node p;

			public SLIterator() {
				p = head;
			}
			public boolean hasNext() {
				return p != null;
			}
			public T next() {
				T x = p.x;
				p = p.next;
				return x;
			}
			public void remove() {
				throw new UnsupportedOperationException();
			}
		}
		return new SLIterator();
	}

	public int size() {
		return n;
	}

	public boolean add(T x) {
		Node u = new Node();
		u.x = x;
		if (n == 0) {
			head = u;
		} else {
			tail.next = u;
		}
		tail = u;
		n++;
		return true;
	}

	public boolean offer(T x) {
		return add(x);
	}

	public T peek() {
		if (n == 0) return null;
		return head.x;
	}

	public T element() {
		if (n == 0) throw new NoSuchElementException();
		return head.x;
	}

	public T poll() {
		if (n == 0)
			return null;
		T x = head.x;
		head = head.next;
		if (--n == 0)
			tail = null;
		return x;
	}

	/**
	 * Stack push operation - push x onto the head of the list
	 * @param x the element to push onto the stack
	 * @return x
	 */
	public T push(T x) {
		Node u = new Node();
		u.x = x;
		u.next = head;
		head = u;
		if (n == 0)
			tail = u;
		n++;
		return x;
	}

	protected void deleteNext(Node u) {
		if (u.next == tail)
			tail = u;
		u.next = u.next.next;
	}

	protected void addAfter(Node u, Node v) {
		v.next = u.next;
		u.next = v;
		if (u == tail)
			tail = v;
	}

	protected Node getNode(int i) {
		Node u = head;
		for (int j = 0; j < i; j++)
			u = u.next;
		return u;
	}

	/**
	 * Stack pop operation - pop off the head of the list
	 * @return the element popped off
	 */
	public T remove() {
		if (n == 0)	return null;
		T x = head.x;
		head = head.next;
		if (--n == 0) tail = null;
		return x;
	}

	public T pop() {
		if (n == 0)	return null;
		T x = head.x;
		head = head.next;
		if (--n == 0) tail = null;
		return x;
	}



	public static void main(String[] args) {
		List<Integer> bl = new SLList<Integer>();
		List<Integer> list = new ArrayList<Integer>();
		System.out.println(bl);
		for (int i = 0; i < 53; i++) {
			bl.add(i);
			list.add(i);
			if (!bl.equals(list)) {
				System.out.println("xxxxxxxx");
				System.out.println("bl"+bl);
				System.out.println("list"+list);		

			}
		}

		System.out.println("bl"+bl);
		System.out.println("list"+list);
		for (int i = -1; i > -25; i--) {
			bl.add(0, i);
			list.add(0,i);
			if (!bl.equals(list)) {
				System.out.println("xxxxxxxx");
				System.out.println("bl"+bl);
				System.out.println("list"+list);

			}
		}

		
		int [] array = new int [] {-24, -23, 209, 245, -20, -19, -18, 215, 201, 217, 236, -17, -16, 240, -15, -14, 205, -13, 223, -12, -11, 233, -10, -9, -8, 249, 246, -7, -6, 218, -5, 239, 227, 222, 220, 203, -4, -3, -2, -1, 0, 1, 2, 242, 225, 3, 4, 5, 6, 7, 207, 241, 8, 206, 9, 211, 10, 11, 200, 235, 247, 221, 12, 13, 244, 219, 216, 234, 14, 15, 16, 238, 17, 214, 213, 18, 19, 20, 243, 21, 22, 23, 24, 25, 26, 230, 27, 28, 29, 30, 202, 31, 32, 33, 34, 35, 226, 36, 204, 37, 224, 38, 210, 39, 40, 41, 228, 208, 42, 43, 44, 45, 46, 47, 229, 48, 49, 248, 50, 51, 52, 212};

		for (int i = 0; i < 10; i++) {
				bl.add(i, array[i]);
				list.add(i, array[i]);


		}
		System.out.println("bl here"+ bl);
		System.out.println("list here"+list);
		while(bl.size() > 5) {
			bl.remove(3);
			list.remove(3);
			if (!bl.equals(list)) {
				System.out.println("!!!!!!!!!!!!!!!!");
				System.out.println(bl);
				System.out.println(list);		

			}
		}
	}
}
