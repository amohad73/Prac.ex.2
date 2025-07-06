package fibonacciHeaps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tester {
	/*public static void main(String[] arg) {
			FibonacciHeap h = new FibonacciHeap(2);
			h.insert(3, "df");
			FibonacciHeap.HeapNode x = h.insert(12, "s");
			h.insert(2, "s");
			h.insert(312, "s");
			h.insert(112, "s");
			h.insert(1, "s");
			FibonacciHeap.HeapNode y = h.insert(9, "s");
			h.deleteMin();
			System.out.println(h.decreaseKey(x, 8));
			FibonacciHeap.HeapNode node = h.min;
			for (int i = 0; i < h.size; i++) {
				System.out.println(node.key + " " + node.info + " " + node.child + " " + node.parent);
				node = node.next;
			}
			h.deleteMin();
			System.out.println(h.decreaseKey(y, 2));
			System.out.println("done!");
			System.out.println("done!");
	}*/
	public static void main(String[] args) {
        final int N = 464646;
        final int C = 2;
        List<Integer> lst = new ArrayList<>();
        var fibonacciHeap = new FibonacciHeap(C);
        for(int i = 0; i < N; i++){
            lst.add(i);
        }
        Collections.shuffle(lst);

        FibonacciHeap.HeapNode[] arr = new FibonacciHeap.HeapNode[464646];
        int max = 464645;
        
        long startTime = System.nanoTime();

        for(int i = 0; i < N; i++) {
            arr[lst.get(i)] = fibonacciHeap.insert(lst.get(i), "");
        }

        //test 1
        fibonacciHeap.deleteMin();
        while(fibonacciHeap.size()>46) {
            fibonacciHeap.delete(arr[max]);
            max -= 1;
        }

        //test 2
        max=464645;
        fibonacciHeap.deleteMin();
        for(int i = 0; i < N-46; i++) {
            fibonacciHeap.decreaseKey(arr[max],arr[max].key);
            max-=1;
        }
        fibonacciHeap.deleteMin();



        long endTime = System.nanoTime();
        double durationInMilliseconds = (double) (endTime-startTime) / 1000000;
        System.out.println("Execution time: " + durationInMilliseconds + " ms");
        System.out.println("Heap size: " + fibonacciHeap.size());
        System.out.println("Number of links: " + fibonacciHeap.totalLinks());
        System.out.println("Number of cuts: " + fibonacciHeap.totalCuts());
        System.out.println("Number of trees: " + fibonacciHeap.numTrees());
    }
}
