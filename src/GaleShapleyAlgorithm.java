/*
 * Aurthur：@yinengy
 * Time： 9/6/2018
 *
 * Correspond to the algorithm state in page 6,
 * all grey comments are from the book.
 */

import java.util.Collections;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class GaleShapleyAlgorithm {

    /**
     * @param M     the one who selects individual (M in book)
     * @param W     the individual who will be selected (W in book)
     * @param mPref the preference list of all the M
     * @param wPref the preference list of all the W
     * @return a stable perfect matching
     */
    public static String[][] StableMatching(String[] M, String[] W, String[][] mPref, String[][] wPref) {

        /* Initially all m ∈M and w ∈W are free */
        // queue that store all the free M
        Queue<String> freeM = new ArrayBlockingQueue<>(M.length);
        Collections.addAll(freeM, M);

        // HashMap that M be key and W be value to store a pair, so can search W by M in O(1)
        HashMap<String, String> pairMW = new HashMap<>();
        // HashMap that W be key and M be value to store a pair, so can search M by W in O(1)
        HashMap<String, String> pairWM = new HashMap<>();

        // HashMap contains the Ws that has not been proposed by M
        HashMap<String, Queue<String>> ProposedList = buildProposedList(M, mPref);
        // HashMap which can search the preference of each W by O(1)
        HashMap<String, String[]> PreferenceList = buildPreferenceList(W, wPref);

        /* While there is a man m who is free and hasn’t proposed to every woman */
        while (!freeM.isEmpty() && !ProposedList.get(freeM.peek()).isEmpty()) {
            /* Choose such a man m */
            String m = freeM.peek();

            /* Let w be the highest-ranked woman in m’s preference list
                to whom m has not yet proposed */
            String w = ProposedList.get(m).remove();

            /* If w is free then */
            if (!pairWM.containsKey(w)) {
                /* (m, w) become engaged */
                pairMW.put(m, w);
                pairWM.put(w, m);
                freeM.remove();
                /* Else w is currently engaged to m' */
            } else {
                String mPrime = pairWM.get(w);
                String[] preference = PreferenceList.get(w);


                int mPrimeIndex = -1;
                int mIndex = -1;
                //log the preference order of certain m in w's list
                for (int i = 0; i < preference.length; i++) {
                    if (preference[i].equals(mPrime)) {
                        mPrimeIndex = i;
                    }
                    if (preference[i].equals(m)) {
                        mIndex = i;
                    }
                    if (mPrimeIndex > -1 && mIndex > -1) {
                        break;
                    }
                }

                /* If w prefers m' to m then */
                if (mPrimeIndex < mIndex) {
                    /* m remains free */
                    // do nothing
                    /* Else w prefers m to m' */
                } else {
                    /* (m, w) become engaged */
                    /* m' becomes free */
                    pairMW.remove(mPrime);
                    pairWM.remove(w);
                    pairMW.put(m, w);
                    pairWM.put(w, m);
                    freeM.remove();
                    freeM.add(mPrime);
                }
                /* Endif */ // O(1)
            }
            /* Endif */ // O(n) in the worst for travers the the preference list, O(1) in best
        }
        /* Endwhile */ // O(n)


        // convert HashMap to 2D list to show the pair, the row is each pair, the first column is m and second is w;
        String[][] S = new String[M.length][2];
        for (int i = 0; i < M.length; i++) {
            S[i][0] = M[i];
            String temp = pairMW.get(M[i]);
            S[i][1] = temp;
        }

        /* Return the set S of engaged pairs */
        return S;
    }


    /**
     * change the 2-D list preferList to a HashMap will lead to O(1) to
     * check if certain W has been proposed by certain M
     * The queue is in the order of the preference of M, if empty, all the Ws have been proposed
     *
     * @param M        the one who selects individual (M in book)
     * @param prefList the 2-D list of preference in the same order of M
     * @return a HashMap can to searched to check if certain W has been proposed by certain M
     */
    private static HashMap<String, Queue<String>> buildProposedList(String[] M, String[][] prefList) {
        HashMap<String, Queue<String>> map = new HashMap<>();

        for (int i = 0; i < prefList.length; i++) {
            Queue<String> pref = new ArrayBlockingQueue<>(prefList[0].length);
            Collections.addAll(pref, prefList[i]);
            map.put(M[i], pref);
        }
        return map;
    }

    /**
     * change the 2-D list preferList to a HashMap will lead to O(1) to
     * search the preference of every W
     *
     * @param W        the individual who will be selected (W in book)
     * @param prefList the 2-D list of preference in the same order of W
     * @return a HashMap that can search the preference of every W
     */
    private static HashMap<String, String[]> buildPreferenceList(String[] W, String[][] prefList) {
        HashMap<String, String[]> map = new HashMap<>();

        for (int i = 0; i < prefList.length; i++) {
            String[] pref = prefList[i];
            map.put(W[i], pref);
        }
        return map;
    }

    /**
     * Simple test case
     */
    public static void main(String[] args) {
        String[] M = {"Atlanta", "Boston", "Chicago", "Dallas", "Eugene"};
        String[] W = {"Val", "Wayne", "Xavier", "Yolanda", "Zeus"};
        //in the same order of m
        String[][] mPref = {{"Wayne", "Val", "Yolanda", "Zeus", "Xavier"},
                {"Yolanda", "Wayne", "Val", "Xavier", "Zeus"},
                {"Wayne", "Zeus", "Xavier", "Yolanda", "Val"},
                {"Val", "Yolanda", "Xavier", "Wayne", "Zeus"},
                {"Wayne", "Yolanda", "Val", "Zeus,", "Xavier"}};

        String[][] wPref = {{"Eugene", "Atlanta", "Boston", "Dallas", "Chicago"},
                {"Chicago", "Boston", "Dallas", "Atlanta", "Eugene"},
                {"Boston", "Chicago", "Dallas", "Eugene", "Atlanta"},
                {"Atlanta", "Eugene", "Dallas", "Chicago", "Boston"},
                {"Dallas", "Boston", "Eugene", "Chicago", "Atlanta"}};

        String[][] S = StableMatching(M, W, mPref, wPref);
        for (String[] value : S) {
            for (String var : value) {
                System.out.print(var + " ");
            }
            System.out.println();
        }
    }
}
