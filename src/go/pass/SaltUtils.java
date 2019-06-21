package go.pass;

import static go.pass.Constants.*;

/**
 * @author Anatolii Nosenko
 * @version 1.0 6/19/2019.
 */
public class SaltUtils {

    private static SaltUtils saltUtils;

    private SaltUtils() {
    }

    public int getCount(final int saltBound) {
        int start;
        int finish;
        boolean increase = true;
        if (saltBound > MIDDLE) {
            start = DOWN_BOUND;
            finish = saltBound;
            increase = false;
        } else {
            start = saltBound;
            finish = TOP_BOUND;
        }

        int counter = 0;

        String line;
        char[] chars;
        if (increase) {
            while (start < finish) {
                start++;

                line = Integer.toHexString(start);

                chars = line.toCharArray();
                if (chars.length != 4) {
                    continue;
                }
                if (chars[0] == chars[1] || chars[2] == chars[3]) {
                    continue;
                }
                counter++;
            }
        } else {
            while (start < finish) {
                finish--;

                line = Integer.toHexString(finish);

                chars = line.toCharArray();
                if (chars.length != 4) {
                    continue;
                }
                if (chars[0] == chars[1] || chars[2] == chars[3]) {
                    continue;
                }
                counter++;
            }
        }
        return counter;
    }

    public char[][] getSalts(final int saltBound) {

        char[][] result = new char[getCount(saltBound)][];

        int start;
        int finish;
        boolean increase = true;
        if (saltBound > MIDDLE) {
            start = DOWN_BOUND;
            finish = saltBound;
            increase = false;
        } else {
            start = saltBound;
            finish = TOP_BOUND;
        }

        int counter = 0;

        String line;
        char[] chars;
        if (increase) {
            while (start < finish) {
                start++;

                line = Integer.toHexString(start);

                chars = line.toCharArray();
                if (chars.length != 4) {
                    continue;
                }
                if (chars[0] == chars[1] || chars[2] == chars[3]) {
                    continue;
                }
                result[counter] = chars;
                counter++;
            }
        } else {
            while (start < finish) {
                finish--;

                line = Integer.toHexString(finish);

                chars = line.toCharArray();
                if (chars.length != 4) {
                    continue;
                }
                if (chars[0] == chars[1] || chars[2] == chars[3]) {
                    continue;
                }
                result[counter] = chars;
                counter++;
            }
        }
        System.out.println("Salt count = " + counter);
        return result;
    }

    public static SaltUtils getSaltUtils() {
        if (saltUtils == null) {
            saltUtils = new SaltUtils();
        }
        return saltUtils;
    }
}
