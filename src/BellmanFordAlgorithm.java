/*
 * Aurthur：@yinengy
 * Time： 11/10/2018
 *
 * Correspond to the algorithm state on page 294,
 * all /* comments are from the book.
 */


import java.io.FileNotFoundException;

public class BellmanFordAlgorithm {
    public static int[][] M; // store Opt(i,v)

    /**
     * main algorithm
     *
     * @param G a directed graph with no negative cycle in AdjacencyList form
     * @param s the node to start
     * @param t the node to reach
     * @param c cost of edges
     * @return the minimum cost of s to t
     */
    public static int ShortestPath(AdjacencyList G, int s, int t, int[][] c) {
        /* n = number of nodes in G */
        int n = G.getNumV();

        /* Array M[0 . . . n − 1, V] */
        M = new int[n][n + 1];
        /* Define M[0, t]= 0 and M[0, v]=∞ for all other v ∈ V */
        for (int v = 1; v <= n; v++) {
            M[0][v] = 999; // use 999 to denote ∞
        }
        M[0][t] = 0;

        /* For i = 1, . . . , n − 1 */
        for (int i = 1; i < n; i++) {
            /* For v ∈ V in any order */
            for (int v = 1; v <= n; v++) {
                /* Compute M[i, v] using the recurrence (6.23) */
                // compute min w∈V, min(OPT(i − 1, w) + c_vw))
                int temp = 999; // infinite

                for (int w : G.getEdges(v)) {
                    temp = Math.min(M[i - 1][w] + c[v][w], temp);
                }

                M[i][v] = Math.min(M[i - 1][v], temp);
            } /* Endfor */
        } /* Endfor */

        /* Return M[n-1, s] */
        return M[n - 1][s];
    }

    /**
     * backtrack the M to find the path, same input as ShortestPath()
     */
    public static void FindSolution(AdjacencyList G, int s, int t, int[][] c) {
        int n = G.getNumV();
        System.out.print(s );
        for (int i = n - 1; i > 0; i--) {

            if (M[i][s] < M[i - 1][s]) {
                for (int w : G.getEdges(s)) {
                    if (c[s][w] + M[i - 1][w] == M[i][s]) {
                        System.out.print(" -> " + w);
                        s = w;
                        break;
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        int[][] c = {{0, 1, 2, 3, 4, 5, 6},
                {1, 0, -4, 0, 0, 0, -3},
                {2, 0, 0, 0, -1, -2, 0},
                {3, 0, 8, 0, 0, 0, 3},
                {4, 6, 0, 0, 0, 0, 4},
                {5, 0, 0, -3, 0, 0, 2},
                {6, 0, 0, 0, 0, 0, 0}
        };


        AdjacencyList graph = new AdjacencyList(6);
        graph.addFromCSV("test\\DirectedGraphWithNegativeEdge.csv");
        System.out.println(graph);
        int cost = ShortestPath(graph, 4, 6, c);
        System.out.println("from " + 4 + " to " + 6 + " cost is " + cost);
        FindSolution(graph, 4, 6, c);
    }
}
