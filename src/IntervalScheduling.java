/*
 * Aurthur：@yinengy
 * Time： 10/5/2018
 *
 * Correspond to the algorithm state in page 118,
 * all /* comments are from the book.
 */

public class IntervalScheduling {

    public static int[] Scheduling(int[][] interval) {

    }

    /**
     * test
     */
    public static void main(String[] args) {
        // first column is id, second is finishing time
        int[][] interval = {{1, 4},
                {2, 1},
                {3, 6},
                {4, 3},
                {5, 10},
                {6, 2},
                {7, 4}};

        int[] schedule = Scheduling(interval);

    }
}
