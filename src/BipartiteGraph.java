/*
 * Code by：@yinengy
 * Time： 11/14/2018
 *
 * Correspond to the algorithm discussed at page 94-97
 */

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class BipartiteGraph extends AdjacencyList {
    private boolean[] color; // represent two color by boolean

    public BipartiteGraph(int numV) {
        super(numV);
        color = new boolean[numV + 1];
    }

    /**
     * always label node 1 be first, and run BFS from 1 to label all other the nodes
     * O(m+n)
     */
    public void labelColor() {
        Boolean[] Discovered = new Boolean[this.getNumV() + 1];
        for (int n = 0; n < Discovered.length; n++) {
            Discovered[n] = false;
        }
        Discovered[1] = true;
        color[1] = true; // color start point to red

        Queue<Integer>[] L = new Queue[this.getNumV() + 1];
        L[0] = new LinkedBlockingQueue<>();
        L[0].add(1);

        int i = 0;

        while (!L[i].isEmpty()) {
            L[i + 1] = new LinkedBlockingQueue<>();

            while (!L[i].isEmpty()) {
                int u = L[i].remove();


                LinkedList<Integer> edges = this.getEdges(u);

                for (int v : edges) {
                    if (!Discovered[v]) {
                        Discovered[v] = true;
                        L[i + 1].add(v);
                        color[v] = ((i + 1) % 2 == 0); // odd layer be blue(false), even be red(true)
                    }
                }
            }
            i++;
        }
    }

    /**
     * check all edges to find if two adjacent node are in the same color
     * O(m + n)
     */
    public boolean testBipartiteness() {
        for (int v = 1; v <= this.getNumV(); v++) {
            for (int u: this.getEdges(v)) {
                if (color[u] == color[v]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * return a 2-d array, each row represent one group with the same color.
     * it should first must pass the bipartiteness test
     */
    public  LinkedList<Integer>[] getGroups() {
        LinkedList<Integer>[] bipartite = new LinkedList[2];
        bipartite[0] = new LinkedList<>();
        bipartite[1] = new LinkedList<>();
        for (int i = 1; i < color.length; i++) {
            if (color[i]) {
                bipartite[0].add(i);
            } else {
                bipartite[1].add(i);
            }
        }

        return bipartite;
    }

    public boolean getColor(int v) {
        return color[v];
    }
}
