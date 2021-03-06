/*
 * Code by：@yinengy
 * Time： 10/8/2018
 *
 * No pseudocode in the book,
 * but I mainly follow the content on page 157
 */

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Comparator;

public class KruskalsAlgorithm {

    /**
     * main algorithm
     *
     * @param G    a graph in AdjacencyList form
     * @param cost a 2-D list in format {u, v, cost}
     * @param s    the node to start (it should be able to reach all node)
     * @return minimum spanning tree
     */
    public static AdjacencyList MST(AdjacencyList G, int[][] cost, int s) {
        //sort the edges by cost in ascending order, O(mlogn)
        Arrays.sort(cost, Comparator.comparingInt(array -> array[2]));

        // use union-find data structure to improve time complexity
        UnionFind set = new UnionFind(G.getNumV());

        // a array to store the parent of each node in the tree;
        int[] parent = new int[G.getNumV() + 1];

        // create tree from parent[], O(n)
        AdjacencyList Tree = new AdjacencyList(G.getNumV());

        for (int[] edge : cost) {
            // union the two set if they are not in the same connected component
            if (set.find(edge[0]) != set.find(edge[1])) {
                set.union(set.find(edge[0]), set.find(edge[1]));

                parent[edge[0]] = edge[1]; // no circle was created, so add them to tree
                Tree.addEdge(edge[0], edge[1]);
                Tree.addEdge(edge[1], edge[0]);
            }
        }

        return Tree;
    }

    /**
     * test
     */
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
        AdjacencyList tree = MST(graph, testHelper(l, 16), 1);
        System.out.println(tree);
    }

    /**
     * a helper function to convert origin adjacency matrix to a list of edges
     */
    public static int[][] testHelper(int[][] l, int edgesNum) {
        int[][] edges = new int[edgesNum][3];
        int counter = 0;

        for (int i = 1; i < l.length; i++) {
            for (int j = i; j < l[0].length; j++) {
                if (l[i][j] != 0) {
                    edges[counter][0] = i;
                    edges[counter][1] = j;
                    edges[counter][2] = l[i][j];
                    counter++;
                }
            }
        }

        return edges;
    }
}
