package go.pass;

import go.pass.file_support.ArraySupport;
import go.pass.file_support.PassWriter;
import go.pass.file_support.PropertyHandler;

import java.math.BigInteger;
import java.util.Properties;

import static go.pass.Constants.*;

/**
 * @author Anatolii Nosenko
 * @version 1.0 6/18/2019.
 */
public class Generator {

    private static Generator generator;

    private PassWriter passWriter = new PassWriter();
    private PropertyHandler propertyHandler = new PropertyHandler();
    private ArraySupport arraySupport = ArraySupport.getArraySupport();

    Properties properties = propertyHandler.read(MEMORY);

    private BigInteger totalPass;

    private BigInteger ready;

    private Generator() {
        totalPass = arraySupport.getCapacity();
        System.out.println("totalPass = " + totalPass);
        init();
    }

    public static Generator getGenerator() {
        if (generator == null) {
            generator = new Generator();
        }
        return generator;
    }

    public BigInteger getTotalPass() {
        return totalPass;
    }

    public BigInteger getReady() {
        return ready;
    }

    public void generate(final int count) throws Exception {

        int[] readyCounter = arraySupport.convertCounterToInt(properties.getProperty(GENERATED_COUNTERS_13_PROP));
        StringBuilder builder = new StringBuilder();

        char[][][] matrix = arraySupport.getSaltMatrix();

        char[][] lastPass = new char[13][];
        for (int i = 0; i < lastPass.length; i++) {
            lastPass[i] = matrix[i][readyCounter[i]];
        }

        char[][] pass = new char[13][];
        boolean increase;
        int flag;
        for (int i = 0; i < count; i++) {
            increase = false;
            flag = 11;
            while (flag >= 0) {
                if (readyCounter[flag] < matrix[flag].length - 1) {
                    readyCounter[flag] = readyCounter[flag] + 1;
                    for (int j = 0; j < pass.length; j++) {
                        pass[j] = matrix[j][readyCounter[j]];
                    }
                    break;
                } else if (flag == 0 && readyCounter[flag] == (matrix[flag].length - 1)){
                    increase = true;
                    break;
                } else if (flag > 0 && readyCounter[flag] == (matrix[flag].length - 1)) {
                    readyCounter[flag] = 0;
                    flag--;
                }
             }

            if (increase) {
                for (int j = 0; j < readyCounter.length - 1; j++) {
                    readyCounter[j] = 0;
                }
                readyCounter[readyCounter.length - 1] = readyCounter[readyCounter.length - 1] + 1;
                for (int j = 0; j < pass.length; j++) {
                    pass[j] = matrix[j][readyCounter[j]];
                }
            }

            for (char[] chars : pass) {
                builder.append(chars);
            }
            builder.append(NEW_LINE);
        }

        passWriter.write(builder.toString(), count);
        save(arraySupport.convertCounterToString(readyCounter));
        init();

    }

    private void save(String readyCounter) {
        properties.setProperty(GENERATED_COUNTERS_13_PROP, readyCounter);
        propertyHandler.write(properties, MEMORY);
    }

    private void init() {
        if (properties.getProperty(GENERATED_COUNTERS_13_PROP) == null
                || properties.getProperty(GENERATED_COUNTERS_13_PROP).isEmpty()) {
            ready = BigInteger.ZERO;
        } else {
            ready = arraySupport.getCount(properties.getProperty(GENERATED_COUNTERS_13_PROP));
        }
    }
}
