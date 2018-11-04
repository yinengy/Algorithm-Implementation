/*
 * Aurthur：@yinengy
 * Time： 11/4/2018
 *
 * Correspond to the algorithm state on page 282,
 * all /* comments are from the book.
 */

public class SequenceAlignment {
    public static int[][] alpha; // α(x,y) for the penalty of a mismatch
    public static int delta; // δ for the penalty of a gap

    public static int[][] A; // Opt(m, n)


    /**
     * compute the error for optimal alignment
     */
    public static int Alignment(String X, String Y) {
        // some preparation
        X = X.toLowerCase();
        Y = Y.toLowerCase();
        int m = X.length();
        int n = Y.length();

        /* Array A[0 . . . m,0 . . . n] */
        A = new int[m + 1][n + 1];

        /* Initialize A[i, 0]= iδ for each i */
        for (int i = 0; i <= m; i++) {
            A[i][0] = i * delta;
        }
        /* Initialize A[0, j]= jδ for each j */
        for (int j = 0; j <= n; j++) {
            A[0][j] = j * delta;
        }

        /* For j = 1, . . . , n */
        for (int j = 1; j <= n; j++) { // O(n)
            /* For i = 1, . . . , m */
            for (int i = 1; i <= m; i++) { // O(m)
                /* Use the recurrence (6.16) to compute A[i, j] */
                int a_ij = alpha[X.charAt(i - 1) - 'a'][Y.charAt(j - 1) - 'a'];
                A[i][j] = Math.min(Math.min(a_ij + A[i - 1][j - 1], delta + A[i - 1][j]), delta + A[i][j - 1]);
            } /* Endfor */
        } /* Endfor */

        /* Return A[m, n] */
        return A[m][n]; // totally O(mn)
    }

    /**
     * print out the result of alignment by the table we get from Alignment(X,Y)
     * input m and n is the current position f X and Y
     */
    public static void FindSolution(int m, int n, String X, String Y) {
        // base case
        if (n == 0 && m == 0) {
            return;
        } else if (n == 0) {
            System.out.println(X.charAt(m - 1) + " " + "|");
            FindSolution(m - 1, n, X, Y);
            return;
        } else if (m == 0) {
            System.out.println("|" + " " + Y.charAt(n - 1));
            FindSolution(m, n-1, X, Y);
            return;
        }

        // use recurrence (6.16) to find solution
        int a_ij = alpha[X.charAt(m - 1) - 'a'][Y.charAt(n - 1) - 'a'];
        if (A[m][n] == a_ij + A[m - 1][n - 1]) {
            System.out.println(X.charAt(m - 1) + " " + Y.charAt(n - 1));
            FindSolution(m - 1, n - 1, X, Y);
        } else if (A[m][n] == delta + A[m - 1][n]) {
            System.out.println(X.charAt(m - 1) + " " + "|");
            FindSolution(m - 1, n, X, Y);
        } else if (A[m][n] == delta + A[m][n - 1]) {
            System.out.println("|" + " " + Y.charAt(n - 1));
            FindSolution(m, n-1, X, Y);
        }
    }

    public static void main(String[] args) {
        alpha = new int[26][26];
        for (int i = 0; i < 26; i++) {
            for (int j = 0; j < 26; j++) {
                if (i != j) {
                    alpha[i][j] = 1;
                }
            }
        }

        delta = 4;

        String X = "ocurrance";
        String Y = "occurrence";

        Alignment(X, Y);
        System.out.println("Inverse order:");
        FindSolution(X.length(), Y.length(), X.toLowerCase(),Y.toLowerCase());
    }

}
