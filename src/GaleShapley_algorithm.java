/*
 * Correspond to the algorithm state in page 6
 */

import java.util.Arrays;

public class GaleShapley_algorithm {

    /**
     * @param M     the one who select individual (M in book)
     * @param W     the individual who will be selected (W in book)
     * @param mPref the preference list of all the M
     * @param wPref the preference list of all the W
     * @return a stable perfect matching
     */
    public static Object[][] StableMatching(Object[] M, Object[] W, Object[][] mPref, Object[][] wPref) {

        /* Initially all m ∈M and w ∈W are free */
        // list that store all the free M and W, when not free, it will be changed to null
        Object[] freeM = Arrays.copyOf(M, M.length);
        Object[] freeW = Arrays.copyOf(W, W.length);

        /* While there is a man m who is free and hasn’t proposed to every woman */
        while ()
    }
}
