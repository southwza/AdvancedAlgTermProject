package edu.utexas.ece382v.shortest_path.data_structure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import edu.utexas.ece382v.shortest_path.entities.Node;

public class TestDijkstraHeap {

    @Test
    public void testAddElements() {
        List<Integer> list = Arrays.asList( 12,10,15,14,11,2,6,2,18 );
        enqueueAndDequeue(list);

        list = Arrays.asList( 8,12,8,6,7,2,1,4,6,12,23,4,4,9 );
        enqueueAndDequeue(list);
    }

    private void enqueueAndDequeue(List<Integer> list) {
        DijkstraHeap<Integer> heap = new DijkstraHeap<Integer>();
        List<Integer> results = new ArrayList<Integer>();
        for (Integer i : list) {
            heap.offer(i);
        }
        while (heap.size() > 0) {
            results.add(heap.poll());
        }

        Collections.sort(list);
        System.out.println("list:    " + list);
        System.out.println("results: " + results);
        assertTrue(list.equals(results));
    }

    @Test
    public void testInsertAndAdjust() {
        Node nodeA = new Node("a");
        Node nodeB = new Node("b");
        Node nodeC = new Node("c");
        Node nodeD = new Node("d");
        Node nodeE = new Node("e");
        Node nodeF = new Node("f");
        Node nodeG = new Node("g");

        nodeA.setWeight(10D);
        nodeB.setWeight(20D);
        nodeC.setWeight(30D);
        nodeD.setWeight(40D);
        nodeE.setWeight(50D);
        nodeF.setWeight(60D);
        nodeG.setWeight(70D);

        DijkstraHeap<Node> heap = new DijkstraHeap<>();
        heap.offer(nodeD);
        heap.offer(nodeB);
        heap.offer(nodeC);
        heap.offer(nodeA);
        heap.offer(nodeG);
        heap.offer(nodeE);
        heap.offer(nodeF);

        //Let's make sure that we get the smallest element
        Node pollTest = heap.poll();
        assertEquals(nodeA, pollTest);

        //Let's modify the weight of G to be the smallest, call insertOrAdjust, and make sure G is
        //the first off the queue
        nodeG.setWeight(11D);
        heap.insertOrAdjust(nodeG);
        pollTest = heap.poll();
        assertEquals(nodeG, pollTest);

        //Sanity check to make sure the remaining elements are in order
        assertEquals(nodeB, heap.poll());
        assertEquals(nodeC, heap.poll());
        assertEquals(nodeD, heap.poll());
        assertEquals(nodeE, heap.poll());
        assertEquals(nodeF, heap.poll());

        //And now the queue should be empty
        assertThrows(IndexOutOfBoundsException.class, () -> heap.poll());

    }
}
