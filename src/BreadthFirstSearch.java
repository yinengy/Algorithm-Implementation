/*

 * Aurthur：@yinengy

 * Time： 9/22/2018

 *

 * Correspond to the algorithm state in page 90 and 91,

 * all /* comments are from the book.

 */

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class BreadthFirstSearch {
    /**
     * main algorithm
     *
     * @param graph the graph in Adjacency List form
     * @param s     the root node
     * @return a tree generated BFS
     */
    public static AdjacencyList BFS(AdjacencyList graph, int s) {
        /* Set Discovered[s] = true and Discovered[v] = false for all other v */
        Boolean[] Discovered = new Boolean[graph.getNumV() + 1]; //initial false
        for (int n = 0; n < Discovered.length; n++){
            Discovered[n] = false;
        }
        Discovered[s] = true;

        /* Initialize L[0] to consist of the single element s */
        Queue<Integer>[] L = new Queue[graph.getNumV()]; // store all pointers to those layers
        L[0] = new LinkedBlockingQueue<>();
        L[0].add(s);

        /* Set the layer counter i = 0 */
        int i = 0;

        /* Set the current BFS tree T = ∅ */
        AdjacencyList tree = new AdjacencyList(graph.getNumV()); // use AdjacencyList to repent a new tree

        /* While L[i] is not empty */
        while (!L[i].isEmpty()) { // O(n) to set up L
            /* Initialize an empty list L[i + 1] */
            L[i + 1] = new LinkedBlockingQueue<>();

            /* For each node u ∈ L[i] */
            while (!L[i].isEmpty()) { // O(m) for consider all edges in all loops
                /* Consider each edge (u, v) incident to u */
                int u = L[i].remove();
                LinkedList<Integer> edges = graph.getEdges(u);

                for (int v : edges) { // O(degree(n))
                    /* If Discovered[v] = false then */
                    if (!Discovered[v]) {
                        /* Set Discovered[v] = true */
                        Discovered[v] = true;

                        /* Add edge (u, v) to the tree T */
                        tree.addEdge(u, v);
                        //tree.addEdge(v, u); //uncomment this line will get undirected tree

                        /* Add v to the list L[i + 1] */
                        L[i + 1].add(v);
                    } /* Endif */
                }
            }/* Endfor */
            /* Increment the layer counter i by one */
            i++;
        }/* Endwhile */

        // all in all, O(n+m)
        return tree;
    }

    /**
     * simple test case use the csv file in /test/
     */
    public static void main(String[] args) throws FileNotFoundException {
        AdjacencyList graph = new AdjacencyList(8);
        graph.addFromCSV("test\\graph.csv");
        System.out.println(graph);
        AdjacencyList tree = BFS(graph,1);
        System.out.println(tree);
    }
}
