/*
 * Aurthur：@yinengy
 * Time： 10/5/2018
 *
 * Correspond to the algorithm state in page 124,
 * all /* comments are from the book.
 */

import java.util.Arrays;
import java.util.Comparator;

public class IntervalPartitioning {
    /**
     * main algorithm
     *
     * @param interval  2D list in format {id, starting time, finishing time}
     * @param timelimit the max time interval is [0, timelimit]
     * @return a optimal schedule
     */
    public static int[][] Partitioning(int[][] interval, int timelimit) {
        // an array to log the label of all interval, initial value is 0
        int[] label = new int[interval.length];

        // number of current total label
        int totalLable = 1;

        /* Sort the intervals by their start times, breaking ties arbitrarily */
        Arrays.sort(interval, Comparator.comparingInt(array -> array[1]));

        /* Let I1, I2,. . . , In denote the intervals in this order */
        /* For j = 1, 2, 3, . . . , n */
        out:
        for (int j = 0; j < interval.length; j++) {
            // record which label should Ij be added to
            boolean[] consideration = new boolean[totalLable];
            Arrays.fill(consideration, true);

            /* For each interval Ii that precedes Ij in sorted order and overlaps it */
            for (int i = 0; i < j; i++) {
                if (interval[i][1] <= interval[j][2] & interval[i][2] >= interval[j][1]) {
                    /* Exclude the label of Ii from consideration for Ij */
                    consideration[label[i]] = false;
                }
            } /* Endfor */

            /* If there is any label from {1, 2, . . . , d} that has not been excluded then */
            for (int i = 0; i < consideration.length; i++) {
                if (consideration[i]) {
                    /* Assign a nonexcluded label to Ij */
                    label[j] = i;
                    continue out;
                }
            }
            /* Else */
            /* Leave Ij unlabeled */
            // But I will make a new label instead of leave if unlabeled
            label[j] = totalLable;
            totalLable++;
            /* Endif */
        } /* Endfor */

        // make schedule
        int[][] schedule = new int[totalLable][timelimit + 1];
        for (int i = 0; i < label.length; i++) {
            for (int j = interval[i][1]; j <= interval[i][2]; j++) {
                schedule[label[i]][j] = interval[i][0]; // assign id
            }
        }

        return schedule;

    }

    /**
     * test
     */
    public static void main(String[] args) {
        // {id, starting time, finishing time}
        int[][] interval = {{1, 0, 2},
                {2, 0, 6},
                {3, 0, 2},
                {4, 4, 6},
                {5, 4, 9},
                {6, 8, 10},
                {7, 8, 10},
                {8, 10, 14},
                {9, 12, 14},
                {10, 12, 14}};

        int[][] schedule = Partitioning(interval, 14);
        for (int[] o : schedule) {
            for (int num : o) {
                System.out.print(num + " ");
            }
            System.out.println();
        }
    }
}
