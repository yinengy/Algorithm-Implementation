/*
 * Aurthur：@yinengy
 * Time： 10/8/2018
 *
 * No pseudocode in the book,
 * but I mainly follow the content on page 150
 */

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class PrimsAlgorithm {

    /**
     * node with cost(to s) as key, which is comparable and changeable,
     * which is the same as I implemented in Dijkstra's Algorithm
     */
    private static class Node implements Comparable<Node> {
        // it should be static in order to be referenced by static method
        int id;
        int key;

        private static final int MAX_DISTANCE = 999; // a number to indicate infinity cost

        /**
         * initial cost is infinity(MAX_DISTANCE)
         *
         * @param id node id
         */
        Node(int id) {
            this.id = id;
            this.key = MAX_DISTANCE;
        }

        public int getKey() {
            return key;
        }

        public int getId() {
            return id;
        }

        @Override
        public int compareTo(Node n) {
            return key - n.key;
        }

        public void changeKey(int newKey) {
            this.key = newKey;
        }
    }

    /**
     * main algorithm
     *
     * input is similar to Dijkstra's Algorithm
     */
    public static AdjacencyList MST(AdjacencyList G, int[][] l, int s) {
        // initial a priority queue
        PriorityQueue<Node> pq = new PriorityQueue<>();

        Node[] d = new Node[G.getNumV() + 1];
        for (int i = 1; i <= G.getNumV(); i++) {

            d[i] = new Node(i); // initially infinity

            if (i == s) {
                d[s].changeKey(0);
            }

            pq.add(d[i]); // add to priority queue
        }

        // a set S for explored nodes
        boolean[] S = new boolean[G.getNumV() + 1];

        // a array to store the parent of each node in the tree;
        int[] parent = new int[G.getNumV() + 1];

        while (!pq.isEmpty()) {
            int u = pq.poll().getId();
            S[u] = true; // true for explored.

            LinkedList<Integer> edges = G.getEdges(u);
            for (int v : edges) {
                if (!S[v] & l[u][v] < d[v].getKey()) { // this is different to Dijkstra
                    // update key in PQ
                    pq.remove(d[v]); // O(n)
                    Node new_v = new Node(v);
                    new_v.changeKey(l[u][v]);
                    pq.add(new_v);
                    // update parent
                    parent[v] = u;

                }
            }
        }

        // create tree from parent[], O(n)
        AdjacencyList Tree = new AdjacencyList(G.getNumV());
        for (int i = 1; i < parent.length; i++) {
            if (i != s) {
                Tree.addEdge(parent[i], i);
                Tree.addEdge(i, parent[i]);
            }
        }

        return Tree;

    }

    public static void main(String[] args) throws FileNotFoundException {
        int[][] l = {{0, 1, 2, 3, 4, 5, 6, 7, 8},
                {1, 0, 5, 0, 0, 9, 0, 0, 8},
                {2, 5, 0, 12, 15, 0, 0, 0, 4},
                {3, 0, 12, 0, 3, 0, 1, 11, 7},
                {4, 0, 15, 3, 0, 0, 0, 9, 0},
                {5, 9, 0, 0, 0, 0, 4, 20, 5},
                {6, 0, 0, 1, 0, 4, 0, 13, 6},
                {7, 0, 0, 11, 9, 20, 13, 0, 0},
                {8, 8, 4, 7, 0, 5, 6, 0, 0}
        };


        AdjacencyList graph = new AdjacencyList(8);
        graph.addFromCSV("test\\MST.csv");
        System.out.println(graph);
        AdjacencyList tree = MST(graph, l, 1);
        System.out.println(tree);
    }

}
