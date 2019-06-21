package go.pass;

/**
 * @author Anatolii Nosenko
 * @version 1.0 6/18/2019.
 */
public class Constants {

    public static final char[] STAFF =
            {       '1', '2', 'a',
                    '3', '4', 'b',
                    '5', '6', 'c',
                    '7', '8', 'd',
                    '9', '0', 'e',
                    'f'
            };
    public static final String NEW_LINE = System.lineSeparator();
    public static final String RESULT_FOLDER = "result\\";
    public static final String MEMORY = "memory.properties";

    public static final int TOP_BOUND = 0xfeff;
    public static final int DOWN_BOUND = 0x1211;
    public static final int MIDDLE = (TOP_BOUND - DOWN_BOUND) / 2;

    public static final String GENERATED_COUNTERS_13_PROP = "generatedCounters_13_prop";

    public static final String ARRAY_SEPARATOR = "_";
}
