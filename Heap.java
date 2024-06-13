import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class Heap implements IPriorityQueue {
    private List<Node> heap;
    private Map<Integer, Integer> position; // Para rastrear la posición de cada nodo en el heap

    public Heap() {
        heap = new ArrayList<>();
        position = new HashMap<>();
    }

    public void add(Node node) {
        heap.add(node);
        int idx = heap.size() - 1;
        position.put(node.vertex, idx);
        heapifyUp(idx);
    }

    public Node poll() {
        if (heap.isEmpty()) {
            throw new NoSuchElementException();
        }
        Node root = heap.get(0);
        Node lastNode = heap.remove(heap.size() - 1);
        if (!heap.isEmpty()) {
            heap.set(0, lastNode);
            position.put(lastNode.vertex, 0);
            heapifyDown(0);
        }
        position.remove(root.vertex);
        return root;
    }

    public void decreaseKey(Node nodi, double newDist) {
        Integer idx = position.get(nodi.vertex);
        if (idx == null) {
            throw new NoSuchElementException();
        }
        Node node = heap.get(idx);
        node.distance = newDist;
        heapifyUp(idx);
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    private void heapifyUp(int idx) {
        while (idx > 0) {
            int parentIdx = (idx - 1) / 2;
            if (heap.get(parentIdx).compareTo(heap.get(idx)) > 0) {
                swap(parentIdx, idx);
                idx = parentIdx;
            } else {
                break;
            }
        }
    }

    private void heapifyDown(int idx) {
        int size = heap.size();
        while (idx < size / 2) {
            int leftChildIdx = 2 * idx + 1;
            int rightChildIdx = 2 * idx + 2;
            int smallestIdx = idx;

            if (leftChildIdx < size && heap.get(leftChildIdx).compareTo(heap.get(smallestIdx)) < 0) {
                smallestIdx = leftChildIdx;
            }
            if (rightChildIdx < size && heap.get(rightChildIdx).compareTo(heap.get(smallestIdx)) < 0) {
                smallestIdx = rightChildIdx;
            }
            if (smallestIdx != idx) {
                swap(smallestIdx, idx);
                idx = smallestIdx;
            } else {
                break;
            }
        }
    }

    private void swap(int i, int j) {
        Node temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
        position.put(heap.get(i).vertex, i);
        position.put(heap.get(j).vertex, j);
    }
}

