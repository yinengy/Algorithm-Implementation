/*
 * Code by：@yinengy
 * Time： 9/24/2018
 *
 * Correspond to the algorithm state in page 102,
 * but in iteration way rather than recursion
 */

import java.io.FileNotFoundException;
import java.util.LinkedList;

public class FindTopologicalOrdering {
    /**
     * main algorithm
     *
     * @param graph the graph in Adjacency List form
     * @return an int[] in topological ordering
     */
    public static int[] findOrder(AdjacencyList graph) {
        // a list of the numbers of incoming edges in all Nodes
        int[] inEdges = new int[graph.getNumV() + 1];
        for (int i = 0; i < inEdges.length; i++) {
            inEdges[i] = 0; //initial
        }

        for (int i = 1; i <= graph.getNumV(); i++) {
            LinkedList<Integer> edges = graph.getEdges(i);
            for (int v : edges) {
                inEdges[v]++;
            }
        }

        // a list store current ordering
        int[] order = new int[graph.getNumV()];
        int counter = 0; // count current index of the order list

        while (counter < order.length) {
            // the node that has no incoming edge and will be put on next
            int toAdd = findzero(inEdges);

            if (toAdd == -1) {
                System.out.println("It is not a DAG!");
                break;
            } else {
                order[counter] = toAdd;

                // delete node that we added
                LinkedList<Integer> edges = graph.getEdges(toAdd);
                for (int v: edges){
                    inEdges[v]--;
                }
                inEdges[toAdd] = -1;

                counter++;
            }
        }

        return order;
    }

    /**
     * return the index of the first 0 in input array (begins from the second element)
     */
    public static int findzero(int[] edges) {
        for (int i = 1; i < edges.length; i++) {
            if (edges[i] == 0) {
                return i;
            }
        }

        return -1; //not found
    }

    public static void main(String[] args) throws FileNotFoundException {
        AdjacencyList graph = new AdjacencyList(7);
        graph.addFromCSV("test\\DAG.csv");
        System.out.println(graph);
        int[] order = findOrder(graph);
        for (int var : order) {
            System.out.println(var + " ");
        }
    }
}
