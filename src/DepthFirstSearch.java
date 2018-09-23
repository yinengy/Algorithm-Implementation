/*

 * Aurthur：@yinengy

 * Time： 9/22/2018

 *

 * Correspond to the algorithm state in page 93,

 * all /* comments are from the book.

 */

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Stack;

public class DepthFirstSearch {
    /**
     * main algorithm
     *
     * @param graph the graph in Adjacency List form
     * @param s     the root node
     * @return a tree generated DFS
     */
    public static AdjacencyList DFS(AdjacencyList graph, int s) {
        // initial the list of explored V
        Boolean[] Explored = new Boolean[graph.getNumV() + 1];
        for (int n = 0; n < Explored.length; n++) {
            Explored[n] = false;
        }

        // initial the tree
        AdjacencyList tree = new AdjacencyList(graph.getNumV());

        /* Initialize S to be a stack with one element s */
        Stack<Integer> S = new Stack<>();
        S.push(s);

        // store the parent of node to build a tree
        int[] parent = new int[graph.getNumV() + 1];

        /* While S is not empty */
        while (!S.isEmpty()) {
            /* Take a node u from S */
            int u = S.pop();

            /* If Explored[u] = false then */
            if (!Explored[u]) {
                /* Set Explored[u] = true */
                Explored[u] = true; // will record n tims, which is O(n) in total
                // add edge to tree
                if (u != s) {
                    tree.addEdge(parent[u], u);
                }

                /* For each edge (u, v) incident to u */
                LinkedList<Integer> edges = graph.getEdges(u);
                for (int v : edges) {
                    /* Add v to the stack S */
                    S.push(v); // will push 2m times, which is O(m) in total

                    // record the parent of v
                    parent[v] = u;
                } /* Endfor */
            }/* Endif */
        } /* Endwhile */

        //All in all, O(m+n)
        return tree;
    }

    /**
     * simple test case use the csv file in /test/
     */
    public static void main(String[] args) throws FileNotFoundException {
        AdjacencyList graph = new AdjacencyList(8);
        graph.addFromCSV("test\\graph.csv");
        System.out.println(graph);
        AdjacencyList tree = DFS(graph, 1);
        System.out.println(tree);
    }
}
