/*
 * Code by：@yinengy
 * Time： 11/3/2018
 *
 * Correspond to the algorithm state in page 256-258,
 * all /* comments are from the book.
 */

import java.util.Arrays;
import java.util.Comparator;

public class WeightedIntervalScheduling {
    static int[] M; // where the optimal value stored
    static int[][] request; // the intervals in form {id, starting time, finishing time, weight}
    static int[] p; // the last elements that is not overlapped by j


    /**
     * call methods to form the final algorithm
     */
    public static void AlgorithmCall() {

        // sort request by finishing time, O(nlogn)
        Arrays.sort(request, Comparator.comparingInt(array -> array[2]));

        M = new int[request.length];
        p = new int[request.length];

        ComputeOpt(request.length - 1);   // it will change M, O(n)
        System.out.print("schedule in reverse order: ");
        FindSolution(request.length - 1); // get output, O(n)
    }

    /**
     * compute value of M[j]
     *
     * @param j the current sub-problem's index (begin at 0 rather than 1)
     */
    public static int ComputeOpt(int j) {
        /* If j = 0 then */
        if (j == -1) { // in fact, we have element in M[0]
            /* Return 0 */
            return 0;

            /* Else if M[j] is not empty then */
        } else if (M[j] != 0) {
            /* Return M[j] */
            return M[j];

            /* Else */
        } else {
            // compute "p" which is the last elements that is not overlapped by j
            p[j] = -1; // if not found, it will remain -1
            for (int i = j-1; i >= 0; i--) {
                if (request[i][1] <= request[j][2]) {
                    p[j] = i;
                    break;
                }
            }

            /* Define M[j] = max(v_j+M-Compute-Opt(p(j)), M-Compute-Opt(j − 1)) */
            M[j] = Math.max(request[j][3] + ComputeOpt(p[j]), ComputeOpt(j - 1));

            /* Return M[j] */
            return M[j];
        } /* Endif */
    }

    /**
     * main part of the algorithm
     *
     * @param j the current sub-problem's index (begin at 0 rather than 1)
     */
    public static void FindSolution(int j) {
        /* If j = 0 then */
        if (j == -1) {
            /* Output nothing */
        } else if (j == 0) { // to avoid M out of boundary
            System.out.print(request[0][0] + " ");
            /* Else */
        } else {
            /* If v_j +M[p(j)]≥M[j − 1] then */
            if (request[j][3] + M[p[j]] >= M[j - 1]) {
                /* Output j together with the result of Find-Solution(p(j)) */
                System.out.print(request[j][0] + " ");
                FindSolution(p[j]);
                /* Else */
            } else {
                /* Output the result of Find-Solution(j − 1) */
                FindSolution(j - 1);
            } /* Endif */
        } /* Endif */
    }

    /**
     * test
     */
    public static void main(String[] args) {
        // {id, starting time, finishing time}
        request =new int[][] {{1, 0, 5, 5},
                {2, 1, 3, 7},
                {3, 3, 4, 8},
                {4, 3, 7, 4},
                {5, 4, 6, 10},
                {6, 5, 8, 3},
                {7, 6, 9, 1},
                {8, 8, 10, 6}};

        AlgorithmCall();
    }
}
