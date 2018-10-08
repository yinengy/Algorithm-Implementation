/*
 * Aurthur：@yinengy
 * Time： 10/8/2018
 *
 * No pseudocode in the book,
 * but I mainly follow the content on Section 4.6
 */

public class UnionFind {

    /**
     * a node with a pointer points to the set it belongs to
     */
    private class Node {
        private int name;
        private int size; // number of descendants

        /**
         * @param name the name of set
         */
        private Node(int name) {
            this.name = name;
            this.size = 0;
        }
    }

    private Node[] set; // contains the name of the set currently containing each element, index 0 is invalid

    /**
     * initiate the Union-Find. Each nodes in the input S will be in separate set.
     * O(n)
     *
     * @param S a array contain all nodes (index begin from 1 rather than 0)
     */
    public UnionFind(int[] S) {
        set = new Node[S.length + 1];

        for (int s : S) {
            set[s] = new Node(s); // each in its own set
        }
    }


    /**
     * make union of two sets, O(1)
     */
    public void union(int a, int b) {
        if (set[a].size < set[b].size) {
            set[a].name = b;
        } else {
            set[b].name = a;
        }
    }


    /**
     * find the set that v belongs to by recursive (no path compression), O(logn)
     */
    public int recursivefind(int v) {
        if (set[v].name == v) {
            return v;
        }

        return recursivefind(set[v].name);
    }

    /**
     * find the set while doing path compression,
     * O(logn) for first time, and O(nα(n)) for n subsequent calls.
     * α(n) is inverse Ackermann function
     */
    public int find(int v) {
        int trav = v;

        while (set[trav].name != trav) {
            trav = set[trav].name; // find the root
        }

        set[v].name = trav; // compress the path

        return trav;
    }

}
