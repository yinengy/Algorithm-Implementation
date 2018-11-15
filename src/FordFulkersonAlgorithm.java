/*
 * Code by：@yinengy
 * Time： 11/11/2018
 *
 * Correspond to the algorithm state on page 342-344, 353-354
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
     * @param G a directed graph
     * @param s source
     * @param t sink
     * @param c nonnegative integer capacity of each nodes
     * @return max flow
     */
    public static int[][] MaxFlow(AdjacencyList G, int s, int t, int[][] c) {
        int n = G.getNumV();

        // backup c
        int[][] capacity = new int[c.length][c[0].length];
        for (int i = 0; i < c.length; i++) {
            capacity[i] = c[i].clone();
        }


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

            f = augment(f, P, isBackward, capacity); // O(m)

            /* Update the residual graph Gf to be Gf' */
            ListIterator i = P.listIterator();
            int u = (int) i.next();
            int v;
            while (i.hasNext()) { // O(m) iteration
                v = (int) i.next();
                //capacity of augment edge is the old flow
                int[][] lastf = capacity;

                if (f[u][v] != lastf[v][u]) { // only update changed edge
                    if (f[u][v] > 0 && !isBackward[v][u]) {
                        // when not exist, add the augment edge
                        isBackward[v][u] = true;
                        Gf.addEdge(v, u);
                    } else if (f[u][v] == 0 && isBackward[v][u]) {
                        // else, delete it
                        isBackward[v][u] = false;
                        Gf.deleteEdge(v, u);
                    }

                    if (capacity[u][v] == f[u][v]) {
                        // when the edge is full, delete it
                        Gf.deleteEdge(u, v);
                    } else if (lastf[u][v] == capacity[u][v]) {
                        // when the full edge has space again, add it back
                        Gf.addEdge(u, v);
                    }

                    capacity[v][u] = f[u][v]; //update the capacity of augment edge
                }
                u = v;
            }
            P = findPath(Gf, s, t); // find a new path, O(m)
        } /* Endwhile */

        /* Return f */
        return f; //totally O(Cm)

    }

    /**
     * improve the time complexity by choose a path over a parameter delta,
     * time: O(m^2 log(C))
     */
    public static int[][] ScalingMaxFlow(AdjacencyList G, int s, int t, int[][] c) {
        // backup c
        int[][] capacity = new int[c.length][c[0].length];
        for (int i = 0; i < c.length; i++) {
            capacity[i] = c[i].clone();
        }

        int n = G.getNumV();
        boolean[][] isBackward = new boolean[n + 1][n + 1];

        /* Initially f(e) = 0 for all e in G */
        int[][] f = new int[n + 1][n + 1];

        /* Initially set delta to be the largest power of 2 that is no larger
            than the maximum capacity out of s: delta ≤ max_{e out of s} c_e */
        int delta = 0;
        for (int v : G.getEdges(s)) {
            delta = Math.max(capacity[s][v], delta);
        }
        delta = 1 << ((int) Math.floor(Math.log(delta) / Math.log(2)));

        /* While delta ≥ 1 */
        while (delta >= 1) { // O(log(C)) times

            // make a new graph has only the edges that not less than delta
            AdjacencyList GfDelta = new AdjacencyList(n);
            for (int w = 1; w < capacity.length; w++) {
                for (int v = 1; v < capacity.length; v++) {
                    if (capacity[w][v] - f[w][v] >= delta) {
                        GfDelta.addEdge(w, v);
                    }
                }
            } // O(n^2）

            /* Let P be a simple s-t path in Gf(delta) */
            LinkedList<Integer> P = findPath(GfDelta, s, t); // O(m)

            /* While there is an s-t path in the graph Gf(delta) */
            while (P != null && P.size() != 0) { // at most 2m times
                /* f" = augment(f, P) */
                f = augment(f, P, isBackward, capacity); // O(m)

                /* Update the residual graph Gf to be Gf' */
                ListIterator i = P.listIterator();
                int u = (int) i.next();
                int v;
                while (i.hasNext()) {
                    v = (int) i.next();

                    int[][] lastf = capacity;

                    if (f[u][v] != lastf[v][u]) {
                        if (f[u][v] > 0 && !isBackward[v][u]) {
                            isBackward[v][u] = true;
                            if (f[u][v] >= delta) { // only add to the graph when it is over delta
                                GfDelta.addEdge(v, u);
                            }
                        } else if (f[u][v] == 0 && isBackward[v][u]) {
                            isBackward[v][u] = false;
                            GfDelta.deleteEdge(v, u);
                        }

                        if (capacity[u][v] == f[u][v]) {
                            GfDelta.deleteEdge(u, v);
                        } else if (lastf[u][v] == capacity[u][v]) {
                            GfDelta.addEdge(u, v);
                        }

                        if (capacity[u][v] - f[u][v] < delta) { // delete the edge when it is less thn delta
                            GfDelta.deleteEdge(u, v);
                        }

                        capacity[v][u] = f[u][v];
                    }
                    u = v;
                }
                P = findPath(GfDelta, s, t); // find a new path, O(m)
            } /* Endwhile */
            /* delta = delta/2 */
            delta = delta / 2;

        }/* Endwhile */
        /* Return f */
        return f; // all in all, O(m^2 log(C))
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
                f[u][v] += b;
            } else { /* Else ((u, v) is a backward edge, and let e = (v, u)) */
                /* decrease f(e) in G by b */
                f[v][u] -= b;
            } /* Endif */

            u = v;
        } /* Endfor */

        /* Return(f) */
        return f;
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

        System.out.println("Shortest Path Version:");
        int[][] flow = MaxFlow(graph, 1, 4, c);
        System.out.println("Flow:");
        for (int u = 1; u < flow.length; u++) {
            for (int s = 1; s < flow[0].length; s++) {
                if (flow[u][s] != 0)
                    System.out.println(u + " -> " + s + " ( " + flow[u][s] + " )");
            }
        }

        System.out.println("Capacity Scaling Version:");
        int[][] flow2 = ScalingMaxFlow(graph, 1, 4, c);
        System.out.println("Flow:");
        for (int u = 1; u < flow2.length; u++) {
            for (int s = 1; s < flow2[0].length; s++) {
                if (flow2[u][s] != 0)
                    System.out.println(u + " -> " + s + " ( " + flow2[u][s] + " )");
            }
        }
    }
}
