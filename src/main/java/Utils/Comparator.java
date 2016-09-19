package Utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by rabbit on 6/11/2016.
 */
public class Comparator {

    public static boolean similarity(String sen_a, String sen_b) {

        sen_a = sen_a.toLowerCase();
        sen_b = sen_b.toLowerCase();

        String[] wordsOfA = sen_a.split(" ");
        String[] wordsOfB = sen_b.split(" ");

        Set<String> shorter = null;
        Set<String> longer = null;

        if (wordsOfA.length < wordsOfB.length) {
            shorter = new HashSet<String>(Arrays.asList(wordsOfA));
            longer = new HashSet<String>(Arrays.asList(wordsOfB));
        } else {
            shorter = new HashSet<String>(Arrays.asList(wordsOfB));
            longer = new HashSet<String>(Arrays.asList(wordsOfA));
        }

        //如果两者单词数相差25%，则说明不是同一句子
        double thresholdDistance = 0.25;

        double distance = (longer.size() - shorter.size() + 0.0) / shorter.size();
        if (distance > thresholdDistance) return false;

        double thresholdSimilarity = 0.75;

        int sameWordCount = 0;
        for (String word : shorter) {
            if (longer.contains(word)) {
                sameWordCount++;
            }
        }

        if (((sameWordCount + 0.0) / shorter.size()) < thresholdSimilarity) return false;

        return true;
    }
}
