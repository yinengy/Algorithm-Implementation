/*
 * Code by：@yinengy
 * Time： 11/11/2018
 *
 * Correspond to the algorithm state on page 342-344,
 * all /* comments are from the book.
 */

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.ListIterator;

public class FordFulkersonAlgorithm {

    /**
     * main algorithm, space O(n^2), time seems to be O(mC).
     * But it has been proved that when choose BFS rather than DFS,
     * the max iteration will be O(mn) rather than O(C),
     * thus overall time complexity should be O(n*m^2)
     *
     * @param G        a directed graph
     * @param s        source
     * @param t        sink
     * @param capacity nonnegative integer capacity of each nodes
     * @return max flow
     */
    public static int[][] MaxFlow(AdjacencyList G, int s, int t, int[][] capacity) {
        int n = G.getNumV();

        // used to check whether an edge is forward(false) or backward(true)
        boolean[][] isBackward = new boolean[n + 1][n + 1];

        /* Initially f(e) = 0 for all e in G */
        int[][] f = new int[n + 1][n + 1];

        AdjacencyList Gf = G.clone(); // all backward edges is 0 at the begin

        /* While there is an s-t path in the residual graph Gf */
        /* Let P be a simple s-t path in Gf */
        LinkedList<Integer> P = findPath(Gf, s, t);
        while (P != null && P.size() != 0) { // at most O(mn) rather than O(C)
            /* f' = augment(f, P) */
            /* Update f to be f' */
            int[][] fprime = augment(f, P, isBackward, capacity); // O(m)

            /* Update the residual graph Gf to be Gf' */
            ListIterator i = P.listIterator();
            int u = (int) i.next();
            int v;
            while (i.hasNext()) { // O(m) iteration
                v = (int) i.next();
                if (fprime[u][v] != f[u][v]) {
                    // when the edge is increase from 0, we should add a dotted edge to the graph
                    if (fprime[u][v] > f[u][v] && f[u][v] == 0) {
                        Gf.addEdge(v, u);
                        isBackward[v][u] = true;
                        capacity[v][u] = capacity[u][v] - fprime[u][v]; // update c for dotted edge
                    }

                    // when increase a forward edge to max, the edge itself will not exist anymore
                    if (fprime[u][v] > f[u][v] && fprime[u][v] == capacity[u][v]) {
                        Gf.deleteEdge(u, v);
                    } else if (fprime[u][v] < f[u][v] && fprime[u][v] == 0) {
                        // when it decrease to 0, its dotted edge will not exist anymore
                        Gf.deleteEdge(v, u);
                        isBackward[v][u] = false;
                    }

                    f[u][v] = fprime[u][v];
                }
                u = v;
            }
            P = findPath(Gf, s, t); // find a new path, O(m)
        } /* Endwhile */

        /* Return f */
        return f; //totally O(Cm)

    }

    /**
     * get a new f in the augmented graph, O(m)
     *
     * @param f          flow
     * @param P          path from s to t in Gf
     * @param isBackward a record of whether a edge is forward or backward
     * @param capacity   c of each edges
     */
    public static int[][] augment(int[][] f, LinkedList<Integer> P, boolean[][] isBackward, int[][] capacity) {
        // deep copy to protect the original value
        int[][] fprime = new int[f.length][f[0].length];
        for (int x = 0; x < f.length; x++) {
            fprime[x] = f[x].clone();
        }

        /* Let b = bottleneck(P, f) */
        int b = Integer.MAX_VALUE;
        ListIterator i = P.listIterator();
        int previous = (int) i.next();
        int next;
        while (i.hasNext()) {
            next = (int) i.next();
            b = Math.min(b, capacity[previous][next] - f[previous][next]); // b = min(c - f)

            previous = next;
        }

        /* For each edge (u, v) ∈ P */
        i = P.listIterator();
        int u = (int) i.next();
        int v;
        while (i.hasNext()) {
            v = (int) i.next();
            /* If e = (u, v) is a forward edge then */
            if (!isBackward[u][v]) {
                /* increase f(e) in G by b */
                fprime[u][v] += b;
            } else { /* Else ((u, v) is a backward edge, and let e = (v, u)) */
                /* decrease f(e) in G by b */
                fprime[v][u] -= b;
            } /* Endif */

            u = v;
        } /* Endfor */

        /* Return(f) */
        return fprime;
    }

    /**
     * find a path from s->t in graph, if not exist, return null
     * Because in the book, it is assumed that every node has at least one incident edges
     * so m > n/2, thus O(m+n) = O(m).
     */
    public static LinkedList<Integer> findPath(AdjacencyList G, int s, int t) {
        LinkedList<Integer> stPath = new LinkedList<>();

        AdjacencyList pathTree = BreadthFirstSearch.BFS(G, s); // O(m+n)

        pathTree = pathTree.reverse();

        int v = t;

        while (v != s) {
            stPath.addFirst(v);

            LinkedList<Integer> edges = pathTree.getEdges(v);

            if (edges == null || edges.size() == 0) {
                return null;
            } else {
                v = edges.getFirst(); //in fact, we can choose it randomly
            }
        }
        stPath.addFirst(v);

        return stPath;
    }

    /**
     * test
     */
    public static void main(String[] args) throws FileNotFoundException {
        int[][] c = {{0, 1, 2, 3, 4},
                {1, 0, 20, 10, 0},
                {2, 0, 0, 30, 10},
                {3, 0, 0, 0, 20},
                {4, 0, 0, 0, 0}};


        AdjacencyList graph = new AdjacencyList(4);
        graph.addFromCSV("test\\FlowGraph.csv");
        System.out.println(graph);

        int[][] flow = MaxFlow(graph, 1, 4, c);
        System.out.println("Flow:");
        for (int u = 1; u < flow.length; u++) {
            for (int s = 1; s < flow[0].length; s++) {
                if (flow[u][s] != 0)
                    System.out.println(u + " -> " + s + " ( " + flow[u][s] + " )");
            }
        }
    }
}
