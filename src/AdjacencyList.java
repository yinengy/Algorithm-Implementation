/*

 * Aurthur：@yinengy

 * Time： 9/22/2018

 *

 * An implement of Adjacency List that can represent graph.

 * This will be used in the implement of BFS and DFS

 * Notice: All the method won't handle bad inputs

 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

class AdjacencyList {

    private LinkedList[] list; // list of pointer to a linked list that will store incident edges of every V

    /**
     * initial an empty AdjacencyList
     * V means vertices(node), and E will be edge
     *
     * @param numV the number of vertices in the graph
     */
    public AdjacencyList(int numV) {
        list = new LinkedList[numV];
        for (int i = 0; i < numV; i++) {
            list[i] = new LinkedList<Integer>();
        }
    }

    /**
     * add edge to the list
     *
     * @param begin the beginning vertex of the edge
     * @param end   the ending vertex of the edge
     */
    public void addEdge(int begin, int end) {
        list[begin - 1].add(end); // there is no V that numbered 0 in the graph
    }

    /**
     * add edges from a CSV file, which is in the form of AdjacencyMatrix
     *
     * @param filename the filename of csv
     * @throws FileNotFoundException
     */
    public void addFromCSV(String filename) throws FileNotFoundException {
        File file = new File(filename);
        Scanner scan = new Scanner(file);
        int currentV = 1;

        scan.nextLine(); // skip the first line which is the number of all V
        while (scan.hasNextLine()) {
            String nl = scan.nextLine();
            nl = nl.replaceAll("[^\\w]", "");
            for (int i = 1; i < nl.length(); i++) { // skip the first column which is number of V
                if (nl.charAt(i) == '1') {
                    addEdge(currentV, i);
                }
            }
            currentV++;
        }
    }

    public String toString() {
        StringBuilder s = new StringBuilder();

        for (int i = 0; i < list.length; i++) {
            s.append((i + 1) + ": ");
            for (Object o : list[i]) {
                s.append(" -> " + o);
            }
            s.append("\n");
        }

        return s.toString();
    }

    public static void main(String[] args) throws FileNotFoundException {
        AdjacencyList graph = new AdjacencyList(8);
        graph.addFromCSV("test\\graph.csv");
        System.out.println(graph);
    }
}
