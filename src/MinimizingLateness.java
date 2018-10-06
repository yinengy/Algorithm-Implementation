/*
 * Aurthur：@yinengy
 * Time： 10/5/2018
 *
 * Correspond to the algorithm state in page 127 and 128,
 * all /* comments are from the book.
 */

import java.util.Arrays;
import java.util.Comparator;

public class MinimizingLateness {

    public static int[] Minimizing(int[][] jobs){
        /* Order the jobs in order of their deadlines */
        Arrays.sort(jobs, Comparator.comparingInt(array -> array[2])); // O(nlogn)

        /* Assume for simplicity of notation that d1 ≤ ... ≤ dn */
        /* Initially, f = s */ // f is the finishing time of last job
        int f = 0;

        // counting total processing time
        int totalTime = 0;
        for (int[] n: jobs) {
            totalTime+= n[1];
        }
        // make a array to represent schedule
        int[] schedule = new int[totalTime];


        /* Consider the jobs i = 1, . . . , n in this order */
        for (int i = 0; i < jobs.length; i++) { // O(n)
            /* Assign job i to the time interval from s(i) = f to f(i) = f + ti */
            for (int j = f; j < f + jobs[i][1]; j++) {
                schedule[j] = jobs[i][0];
            }
            /* Let f = f + ti */
            f += jobs[i][1];
        } /* End */
        /* Return the set of scheduled intervals [s(i), f(i)] for i = 1, . . . , n */
        return schedule; // all in all, O(nlogn)

    }

    /**
     * test
     */
    public static void main(String[] args) {
        // {id, processing time, due}
        int[][] jobs = {{1, 3, 6},
                {2, 2, 8},
                {3, 1, 9},
                {4, 4, 9},
                {5, 3, 14},
                {6, 2, 15}};

        int[] schedule = Minimizing(jobs);
        for (int num : schedule) {
            System.out.print(num + " ");
        }

    }
}
