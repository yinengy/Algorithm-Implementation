/*
 * Aurthur：@yinengy
 * Time： 10/5/2018
 *
 * Correspond to the algorithm state in page 138,
 * all /* comments are from the book.
 */

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class DijkstrasAlgorithm {

    /**
     * node with distance(to s) as key, which is comparable and changeable
     */
    private static class Node implements Comparable<Node> {
        // it should be static in order to be referenced by static method
        int id;
        int key;

        private static final int MAX_DISTANCE = 999; // a number to indicate infinity distance

        /**
         * initial distance is infinity(MAX_DISTANCE)
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
     * @param G a graph in AdjacencyList form
     * @param l a 2-D list contain the length of each edges
     * @param s the node to start (it should be able to reach all node)
     * @return shortest path tree of s
     */
    public static AdjacencyList shortestPathTree(AdjacencyList G, int[][] l, int s) {
        /* Let S be the set of explored nodes */
        // S in this algorithm is replaced by a PriorityQueue to improve time complexity
        PriorityQueue<Node> pq = new PriorityQueue<>();

        /* For each u ∈ S, we store a distance d(u) */
        Node[] d = new Node[G.getNumV() + 1];
        for (int i = 1; i <= G.getNumV(); i++) {

            d[i] = new Node(i); // initially infinity
        }

        /* Initially S ={s} and d(s) = 0 */
        d[s].changeKey(0);

        // update PriorityQueue, O(nlogn)
        for (int i = 1; i < d.length; i++) {
            // Here I stored the reference of Node objects in the PQ,
            // so when I change key in d, the value in the PQ will also be changed
            // thus it takes only O(1) to update distance
            pq.add(d[i]);
        }

        // a array to store the parent of each node in the tree;
        int[] parent = new int[G.getNumV() + 1];
        /* While S != V */
        while (!pq.isEmpty()) { // n times loop
            /* Select a node v !∈ S with at least one edge from S for which
               d'(v) = min_{e=(u, v):u∈S}d(u) + l_e is as small as possible
             */

            int u = pq.remove().getId(); // O(logn) to delete in PQ
            LinkedList<Integer> edges = G.getEdges(u);
            for (int v : edges) { //O(degree n) per loop, totally O(m)
                if (d[u].getKey() + l[u][v] < d[v].getKey()) {
                    /* Add v to S and define d(v) = d"(v) */
                    d[v].changeKey(d[u].getKey() + l[u][v]); //O(1)
                    // update parent
                    parent[v] = u;
                }
            }
        } /* EndWhile */

        // create tree from parent[], O(n)
        AdjacencyList pathTree = new AdjacencyList(G.getNumV());
        for (int i = 1; i < parent.length; i++) {
            if (i != s) {
                pathTree.addEdge(parent[i], i);
            }
        }

        return pathTree; // all in all, O(nlogn + m)
    }

    public static void main(String[] args) throws FileNotFoundException {
        int[][] l = {{0, 1, 2, 3, 4, 5, 6, 7, 8},
                {1, 0, 5, 0, 0, 9, 0, 0, 8},
                {2, 0, 0, 12, 15, 0, 0, 0, 4},
                {3, 0, 0, 0, 3, 0, 0, 11, 0},
                {4, 0, 0, 0, 0, 0, 0, 9, 0},
                {5, 0, 0, 0, 0, 0, 4, 20, 5},
                {6, 0, 0, 1, 0, 0, 0, 13, 0},
                {7, 0, 0, 0, 0, 0, 0, 0, 0},
                {8, 0, 0, 7, 0, 0, 6, 0, 0}
        };


        AdjacencyList graph = new AdjacencyList(8);
        graph.addFromCSV("test\\DirectedGraph.csv");
        System.out.println(graph);
        AdjacencyList tree = shortestPathTree(graph, l, 1);
        System.out.println(tree);
    }
}
