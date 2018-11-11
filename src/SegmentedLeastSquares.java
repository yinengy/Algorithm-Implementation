/*
 * Code by：@yinengy
 * Time： 11/3/2018
 *
 * Correspond to the algorithm state on page 265-266,
 * all /* comments are from the book.
 */


public class SegmentedLeastSquares {
    public static final double C = 2; // the penalty for one addition line

    // the coordinate of points form: {x, y} (in the ascending of x)
    public static double[][] points;

    // the value of linear regression's value (a and b) is store here for future use
    public static double[][][] regression;

    public static double[][] M; // form: (OPT(j), index of i that make lead min)

    public static double[][] e; // LeastSquaresError e(i,j)

    /**
     * call methods to form the final algorithm
     */
    public static void AlgorithmCall() {
        regression = new double[points.length + 1][points.length + 1][2];
        e = new double[points.length + 1][points.length + 1]; // O(n^2) space

        M = computeOpt(points.length); // O(n^3)
        FindSegements(points.length); // O(n)
    }

    public static double[][] computeOpt(int n) {
        /* Array M[0 . . . n] */
        double[][] M = new double[n + 1][2];
        regression = new double[n + 1][n + 1][2];
        /* Set M[0]= 0 */
        M[0][0] = 0;

        /* For all pairs i ≤ j */
        for (int i = 1; i <= n; i++) {
            for (int j = i; j <= n; j++) {
                /* Compute the least squares error ei, j for the segment pi,. . . , p */
                e[i][j] = LeastSquaresError(i, j); // O(n) per time.
            }
        } /* Endfor */
        /* For j = 1, 2, . . . , n */
        for (int j = 1; j <= n; j++) {
            /* Use the recurrence (6.7) to compute M[j] */
            double min = Double.MAX_VALUE;
            int index = -1;
            for (int i = 1; i <= j; i++) {
                if ((e[i][j] + C + M[i - 1][0]) < min) {
                    min = e[i][j] + C + M[i - 1][0];
                    index = i;
                }
            }

            M[j][0] = min;
            M[j][1] = index;
        } /* Endfor */

        /* Return M[n] */
        return M;
    }

    /**
     * main part of the algorithm
     *
     * @param j the current sub-problem's index (begin at 1 rather than 0)
     */
    public static void FindSegements(int j) {
        /* If j = 0 then */
        if (j == 0) {
            /* Output nothing */
        } else { /* Else */
            /* Find an i that minimizes ei, j + C +M[i − 1] */
            int i = (int) M[j][1];
            /* Output the segment {pi,. . . , pj} and the result of Find-Segments(i − 1)*/
            System.out.printf("y = %.3fx + %.3f for points from %d to %d\n",
                    regression[i][j][0], regression[i][j][0], i, j);
            FindSegements(i-1);
        } /* Endif */
    }


    /**
     * compute the LSE for given interval, by the formula on page 262
     *
     * @param i the start index
     * @param j the end index
     */
    public static double LeastSquaresError(int i, int j) {
        double SumXY = 0, SumX = 0, SumY = 0, SumXSquare = 0;

        for (int n = i; n <= j; n++) {
            double x = points[n-1][0];
            double y = points[n-1][1];
            SumX += x;
            SumY += y;
            SumXY += x * y;
            SumXSquare += x * x;
        }

        int n = j - i + 1;

        double a = (n * SumXY - SumX * SumY) / (n * SumXSquare - SumX * SumX);
        double b = (SumY - a * SumX) / n;
        regression[i][j][0] = a;
        regression[i][j][1] = b;

        double E = 0;

        for (int k = i; k <= j; k++) {
            E += Math.pow((points[k-1][1] - a * points[k-1][0] - b), 2);
        }

        return E;
    }

    /**
     * test
     */
    public static void main(String[] args) {
        points = new double[][] {{1,1},
                {2,2},
                {3,3},
                {4,2},
                {5,1},
                {6,0},
                {7,10},
                {8,20},
                {9,30},
                {10,0},
                {11,-30},
                {12,-60}};

        AlgorithmCall();
    }
}
