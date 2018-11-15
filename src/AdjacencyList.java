/*
 * Code by：@yinengy
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

class AdjacencyList implements Cloneable {
    private int numV;
    private LinkedList[] list; // list of pointer to a linked list that will store incident edges of every V

    /**
     * initial an empty AdjacencyList
     * V means vertices(node), and E will be edge
     *
     * @param numV the number of vertices in the graph
     */
    public AdjacencyList(int numV) {
        this.numV = numV;
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
            String[] temp = nl.split("[^\\w]");
            for (int i = 1; i <= numV; i++) { // skip the first column which is number of V
                int value = Integer.parseInt(temp[i]);
                if (value > 0 ) {
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

    public int getNumV() {
        return numV;
    }

    /**
     * get edges
     *
     * @param V the vertex that want to check
     * @return a list that contains all incident edge of V
     */
    public LinkedList<Integer> getEdges(int V) {
        return list[V - 1];
    }

    /**
     * get a deep copy of the AdjacencyList
     */
    public AdjacencyList clone() {
        AdjacencyList toClone = new AdjacencyList(this.numV);

        for (int x = 0; x < this.list.length; x++) {
            for (Object o : this.list[x]) {
                toClone.list[x].addLast(o);
            }
        }

        return toClone;
    }

    /**
     * delete edge from s to w, if such edge not exist, return false.
     * O(degree(s))
     *
     * @param s the start node
     * @param w the end node
     * @return success delete the not
     */
    public boolean deleteEdge(int s, int w) {
        if (!list[s - 1].remove((Integer) w)) {
            return false;
        }

        return list[s - 1].remove((Integer) w) || deleteEdge(s, w);
    }

    /**
     * return a tree with reverse edges
     */
    public AdjacencyList reverse() {
        AdjacencyList newTree = new AdjacencyList(this.numV);
        for (int v = 0; v < this.list.length; v++) {
            for (Object w : this.list[v]) {
                newTree.addEdge((int) w, v + 1);
            }
        }
        return newTree;
    }
}
