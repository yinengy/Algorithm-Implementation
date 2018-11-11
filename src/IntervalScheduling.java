/*
 * Code by：@yinengy
 * Time： 10/5/2018
 *
 * Correspond to the algorithm state in page 118,
 * all /* comments are from the book.
 */


import java.util.Arrays;
import java.util.Comparator;

public class IntervalScheduling {

    /**
     * main algorithm
     *
     * @param request   all request , 2D list in format of {id, starting time, finishing time}
     * @param timelimit the max time [0, timelimit]
     * @return an int[] to represent schedule
     */
    public static int[] Scheduling(int[][] request, int timelimit) {
        /* Initially let R be the set of all requests, and let A be empty */
        int[] A = new int[timelimit+1]; //initialize A

        // a boolean array to represent all request's condition
        boolean[] R = new boolean[request.length + 1]; //initial false
        int countR = 1;
        // sort request by finishing time, O(nlogn)
        Arrays.sort(request, Comparator.comparingInt(array -> array[2]));

        // a pointer to indicate the next request
        int next = 0;

        /* While R is not yet empty */
        out:
        while (next < request.length) { // O(n) times
            /* Choose a request i ∈ R that has the smallest finishing time */
            int id = request[next][0];
            next++;
            while (R[id]) {
                if (next >= request.length) {
                    break out; // all request are considered
                } else {
                    id = request[next][0];
                    next++;
                }
            }

            /* Add request i to A */
            for (int i = request[next - 1][1]; i <= request[next - 1][2]; i++) {
                A[i] = id;
            }
            R[id] = true;
            countR++;

            /* Delete all requests from R that are not compatible with request i */
            for (int i = next; i < request.length & countR < R.length; i++) { // in all loops totally R.length times
                if (!R[request[i][0]] & request[i][1] <= request[next -1][2]) {
                    R[request[i][0]] = true;
                    countR++;
                }
            }
        } /* EndWhile */ //O(n) + O(n)

        /* Return the set A as the set of accepted requests */
        return A; //all in all O(nlogn)
    }

    /**
     * test
     */
    public static void main(String[] args) {
        // {id, starting time, finishing time}
        int[][] interval = {{1, 0, 5},
                {2, 1, 3},
                {3, 3, 4},
                {4, 3, 7},
                {5, 4, 6},
                {6, 5, 8},
                {7, 6, 9},
                {8, 8, 10}};

        int[] schedule = Scheduling(interval, 10);
        for (int n : schedule) {
            System.out.print(n + " ");
        }
    }
}
