public interface IPriorityQueue {
    // Returns true if the queue is empty, false otherwise
    boolean isEmpty();

    // Adds an element to the queue
    void add(Node node);

    // Removes and returns the element with the highest priority
    Node poll();

    void decreaseKey(int vertex, double newDist);
}
