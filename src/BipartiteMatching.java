/*
 * Code by：@yinengy
 * Time： 11/15/2018
 *
 * Correspond to the algorithm discussed at page 367-373
 */

import java.io.FileNotFoundException;
import java.util.LinkedList;

public class BipartiteMatching {
    public static void matching(BipartiteGraph G) {
        G.labelColor();
        if (!G.testBipartiteness()) {
            System.out.println("It is not a bipartite graph");
            return;
        }

        LinkedList<Integer>[] groups = G.getGroups(); // get nodes X and Y

        // build a directed graph G' to apply Max-Flow
        AdjacencyList G2 = new AdjacencyList(G.getNumV() + 2);
        int[][] capacity = new int[G.getNumV() + 3][G.getNumV() + 3];

        int s = G.getNumV() + 1;
        int t = G.getNumV() + 2;

        for (int v : groups[0]) {
            // add edges from source
            G2.addEdge(s, v);
            capacity[s][v] = 1;
            for (int u : G.getEdges(v)) {
                G2.addEdge(v, u); // only add edges from X to Y
                capacity[v][u] = 1;
            }
        }

        for (int u : groups[1]) {
            G2.addEdge(u, t);
            capacity[u][t] = 1;
        }


        // find Max Flow by Ford-Fulkerson Algorithm
        int[][] maxflow = FordFulkersonAlgorithm.MaxFlow(G2, s, t, capacity);

        // print matching result
        int PathNum = 0;
        for (int n : maxflow[s]) {
            PathNum += n;
        }
        System.out.println("The max number of matching is: " + PathNum);

        System.out.println("Matching is:");

        for (int u = 1; u < maxflow.length - 2; u++) {
            for (int v = 1; v < maxflow.length - 2; v++) {
                if (maxflow[u][v] == 1) {
                    System.out.println(u + " - " + v);
                }
            }
        }
        String flag;
        if (PathNum == groups[0].size() && groups[0].size() == groups[1].size()) {
            flag = "true";
        } else {
            flag = "false";
        }
        System.out.println("Is perfect matching?: " + flag);
    }

    public static void main(String[] args) throws FileNotFoundException {
        BipartiteGraph graph = new BipartiteGraph(10);
        graph.addFromCSV("test\\BipartiteGraph.csv");
        matching(graph);
        BipartiteGraph graph2 = new BipartiteGraph(10);
        graph2.addFromCSV("test\\PerfectBipartiteGraph.csv");
        matching(graph2);
    }
}
