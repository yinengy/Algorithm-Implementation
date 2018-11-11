/*
 * Code by：@yinengy
 * Time： 11/4/2018
 *
 * Correspond to the algorithm state on page 269,
 * all /* comments are from the book.
 */


public class Knapsack {
    public static int[] weight; // the weight of each item
    public static int[] value; // the value of each item
    public static int[][] M;  // Opt(n,w)
    /**
     * change the Subset-Sum algorithm on page 269 to a Knapsack Problem version
     * @param n the number of items
     * @param W the max weight (note it is in fact a input of size log(W), thus leads to pseudo-polynomial time)
     * @return the max value of items within the weight limit
     */
    public static int SubsetSum(int n, int W) {
        /* Array M[0 . . . n,0 . . . W] */
        /* Initialize M[0, w]= 0 for each w = 0, 1, . . . , W */
        M = new int[n + 1][W + 1];

        /* For i = 1, 2, . . . , n */
        for (int i = 1; i <= n; i++) { // O(n)
            /* For w = 0, . . . , W */
            for (int w = 0; w <= W; w++) { // O(W)
                /* Use the recurrence (6.8) to compute M[i, w] */
                if (w < weight[i-1]) {
                    M[i][w] = M[i-1][w];
                } else {
                    M[i][w] = Math.max(M[i-1][w],value[i-1] + M[i-1][w-weight[i-1]]);
                }
            } /* Endfor */
        } /* Endfor */

        /* Return M[n, W] */
        return M[n][W]; // totally O(nW)
    }

    /**
     * input is same as SubsetSum, but this method will print out the chosen items
     */
    public static void FindSolution(int n, int W ) {
        if (n == 0) {

        } else if (M[n][W] > M[n-1][W]) {
            System.out.printf("item %d, weight %d, value %d\n",n,weight[n-1],value[n-1]);
            FindSolution(n-1, W-weight[n-1]);
        } else {
            FindSolution(n-1, W);
        }
    }

    /**
     * test
     */
    public static void main(String[] args) {
        weight = new int[] {1,2,5,6,7};
        value = new int[] {1,6,18,22,28};
        SubsetSum(5,11);
        FindSolution(5,11);
    }
}
