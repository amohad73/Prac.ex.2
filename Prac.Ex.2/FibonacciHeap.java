
/**
 * FibonacciHeap
 *
 * An implementation of Fibonacci heap over positive integers.
 *
 */
public class FibonacciHeap
{
	public int c;
	public HeapNode min;
	public int links;
	public int cuts;
	public int size;
	public int nTrees;
	
	/**
	 *
	 * Constructor to initialize an empty heap.
	 * pre: c >= 2.
	 *
	 */
	public FibonacciHeap(int c)
	{
		this.c = c;
		this.links = 0;
		this.cuts = 0;
		this.size = 0;
		this.nTrees = 0;
	}

	/**
	 * 
	 * pre: key > 0
	 *
	 * Insert (key,info) into the heap and return the newly generated HeapNode.
	 *
	 */
	public HeapNode insert(int key, String info) 
	{    
		this.size += 1;
		this.nTrees += 1;
		if (this.size == 1) {
			this.min = new HeapNode(key, info, null, null, null, null,  0);
			this.min.next = this.min;
			this.min.prev = this.min;
			return this.min;
		}
		HeapNode newNode = new HeapNode(key, info, null, this.min.next, this.min, null,  0);
		this.min.next.prev = newNode;
		this.min.next = newNode;
		if (key < this.min.key) {
			this.min = newNode;
		}
		return newNode; // should be replaced by student code
	}

	/**
	 * 
	 * Return the minimal HeapNode, null if empty.
	 *
	 */
	public HeapNode findMin()
	{
		return this.min; // should be replaced by student code
	}

	/**
	 * 
	 * Delete the minimal item.
	 * Return the number of links.
	 *
	 */
	public int deleteMin()
	{
		HeapNode node = this.min.child;
		if (!(this.nTrees == 1)) {
			if (this.min.rank == 0) {
				this.min.prev.next = this.min.next;
				this.min.next.prev = this.min.prev;
			}
			else {
				this.min.prev.next = node;
				this.min.next.prev = node.prev;
				node.prev.next = this.min.next;
				node.prev = this.min.prev;
		
			}
		}
		else {
			if (this.min.rank == 0) {
				this.min = null;
				return 0;
			}
		}
		for (int i = 0; i < this.min.rank; i++) {
			node.parent = null;
			node = node.next;
		}
		
		this.size -= 1;
		this.nTrees += this.min.rank - 1;
		
		HeapNode curr = node;
		if (this.min.rank == 0) {
			curr = this.min.next;
		}
		HeapNode m = curr;
		for (int i = 0; i < this.nTrees; i++) {
			if (m.key > curr.key) {
				m = curr;
			}
			curr = curr.next;
		}
		this.min = m;
		return CSLinking(); // should be replaced by student code
	}

	public int CSLinking() {
		java.util.ArrayList<Object> lst = new java.util.ArrayList<>();
		int max = this.min.rank;
		HeapNode node = this.min;
		for (int i = 0; i < this.nTrees; i++) {
			if (max < node.rank) {
				max = node.rank;
			}
			node = node.next;
		}
		for (int i = 0; i <= max; i++) {
			lst.add(null);
		}
		HeapNode tmp;
		HeapNode up;
		HeapNode down;
		HeapNode next;
		HeapNode curr = this.min;
		int cnt = 0;
		for (int i = 0; i < this.nTrees; i++) {
			tmp = curr;
			next = curr.next;
			curr.next = curr;
			curr.prev = curr;
			while (!(lst.get(tmp.rank) == null)) {
				if (((HeapNode)lst.get(tmp.rank)).key < tmp.key) {
					up = (HeapNode)lst.get(tmp.rank);
					down = tmp;
				}
				else {
					up = tmp;
					down = (HeapNode)lst.get(tmp.rank);
				}
				if (!(tmp.rank == 0)) {
					down.next = up.child;
					down.prev = up.child.prev;
					up.child.prev.next = down;
					up.child.prev = down;
				}
				up.child = down;
				down.parent = up;
				up.rank += 1;
				if (up.rank > max) {
					lst.add(null);
					max += 1;
				}
				cnt += 1;
				lst.set(down.rank, null);
				tmp = up;
				if (lst.size() == tmp.rank) {
					lst.add(null);
				}
			}
			lst.set(tmp.rank, tmp);
			curr = next;
		}
		this.nTrees -= cnt;
		HeapNode n = this.min;
		HeapNode p = this.min;
		this.min.prev = this.min;
		this.min.next = this.min;
		//System.out.println(lst);
		for (int i = 0; i < lst.size(); i++) {
			if ((lst.get(i) == null) || (((HeapNode)lst.get(i)).key == this.min.key)) {
				continue;
			}
			n.prev = (HeapNode)lst.get(i);
			((HeapNode)lst.get(i)).prev = p;
			p.next  = (HeapNode)lst.get(i);
			((HeapNode)lst.get(i)).next = n;
		}
		return cnt;
	}
	
	/**
	 * 
	 * pre: 0<diff<x.key
	 * 
	 * Decrease the key of x by diff and fix the heap.
	 * Return the number of cuts.
	 * 
	 */
	public int decreaseKey(HeapNode x, int diff) 
	{    
		x.key -= diff;
		int m = x.mark;
		int n = this.cascadingCuts(x);
		x.mark = m;
		if (x.key < this.min.key) {
			this.min = x;
		}
		
		return n; // should be replaced by student code
	}

	public int cascadingCuts(HeapNode x) {
		if (x.parent == null) {
			return 0;
		}
		x.parent.rank -= 1;
		this.nTrees += 1;
		x.mark = 0;
		x.parent.mark += 1;
		if (x.next == x) {
			x.parent.child = null;
		}
		else{
			if (x.parent.child == x) {
				x.parent.child = x.next;
			}
			x.next.prev = x.prev;
			x.prev.next = x.next;
		}
		HeapNode tmp = x.parent;
		x.parent = null;
		x.next = this.min.next;
		x.prev = this.min;
		x.next.prev = x;
		this.min.next = x;
		if (tmp.mark == c) {
			return this.cascadingCuts(tmp)+1;
		}
		return 1;
	}
	
	/**
	 * 
	 * Delete the x from the heap.
	 * Return the number of links.
	 *
	 */
	public int delete(HeapNode x) 
	{    
		this.decreaseKey(x, x.key-this.min.key+1);
		return this.deleteMin(); // should be replaced by student code
	}


	/**
	 * 
	 * Return the total number of links.
	 * 
	 */
	public int totalLinks()
	{
		return this.links; // should be replaced by student code
	}


	/**
	 * 
	 * Return the total number of cuts.
	 * 
	 */
	public int totalCuts()
	{
		return this.cuts; // should be replaced by student code
	}


	/**
	 * 
	 * Meld the heap with heap2
	 *
	 */
	public void meld(FibonacciHeap heap2)
	{
		heap2.min.prev.next = this.min.next;
		this.min.next.prev = heap2.min.prev;
		heap2.min.prev = this.min;
		this.min.next = heap2.min;
		if (this.min.key > heap2.min.key) {
					this.min = heap2.min;
		}
		
		this.size += heap2.size;
		this.nTrees += heap2.nTrees;
		
		return; // should be replaced by student code   		
	}

	/**
	 * 
	 * Return the number of elements in the heap
	 *   
	 */
	public int size()
	{
		return this.size; // should be replaced by student code
	}


	/**
	 * 
	 * Return the number of trees in the heap.
	 * 
	 */
	public int numTrees()
	{
		return this.nTrees; // should be replaced by student code
	}

	
	
	/**
	 * Class implementing a node in a Fibonacci Heap.
	 *  
	 */
	public static class HeapNode{
		public int key;
		public String info;
		public HeapNode child;
		public HeapNode next;
		public HeapNode prev;
		public HeapNode parent;
		public int rank;
		public int mark;
		
		public HeapNode(int key, String info, HeapNode child, HeapNode next, HeapNode prev, HeapNode parent, int rank) {
			this.key = key;
			this.info = info;
			this.child = child;
			this.next = next;
			this.prev = prev;
			this.parent = parent;
			this.rank = rank;
			this.mark = 0;
		}
	}
}
