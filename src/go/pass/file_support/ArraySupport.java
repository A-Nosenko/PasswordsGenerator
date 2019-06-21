package go.pass.file_support;

import go.pass.SaltUtils;

import java.math.BigInteger;

import static go.pass.Constants.ARRAY_SEPARATOR;
import static go.pass.Constants.STAFF;

/**
 * @author Anatolii Nosenko
 * @version 1.0 6/20/2019.
 */
public class ArraySupport {

    private static ArraySupport arraySupport;

    private final SaltUtils saltUtils = SaltUtils.getSaltUtils();

    private final char[][] tetrasChars_4x16 = new char[4][16];
    private final int[] staffMasksInt_16 = new int[16];



    private BigInteger capacity;
    private BigInteger[] steps = new BigInteger[12];

    private char[][][] saltMatrix = new char[13][][];

    private ArraySupport() {
        for (int j = 0; j < tetrasChars_4x16.length; j++) {
            switch (j) {
                case 0:
                    for (int i = 0; i < tetrasChars_4x16[j].length; i++) {
                        tetrasChars_4x16[j][i] = STAFF[STAFF.length - 1 - i];
                    }
                    break;
                case 1:
                    tetrasChars_4x16[j] = STAFF;
                    break;
                case 2:
                    for (int i = 0; i < tetrasChars_4x16[j].length; i++) {
                        if (i % 2 == 0) {
                            tetrasChars_4x16[j][i] = STAFF[i / 2];
                        } else {
                            tetrasChars_4x16[j][i] = STAFF[8 + i / 2];
                        }
                    }
                    break;
                case 3:
                    for (int i = 0; i < tetrasChars_4x16[j].length; i++) {
                        if (i % 2 != 0) {
                            tetrasChars_4x16[j][i] = STAFF[i / 2];
                        } else {
                            tetrasChars_4x16[j][i] = STAFF[8 + i / 2];
                        }
                    }
                    break;
            }
        }
        //////////////////////////////////////////////////////////////////////

        int[] maxGeneratedCounters_13 = new int[13];
        maxGeneratedCounters_13[12] = 3; // max generated level

        // split tetras to staffChars_16x4
        char[][] staffChars_16x4 = new char[16][4];
        char[][] buffer;
        for (int i = 0; i < tetrasChars_4x16.length; i++) {
            buffer = splitArrayFrom16_To4(tetrasChars_4x16[i]);
            for (char[] chars : buffer) {
                System.arraycopy(buffer, 0, staffChars_16x4, i * 4, chars.length);
            }
        }


        char[] staffMaskChars_64 = new char[64];
        for (int i = 0; i < staffChars_16x4.length; i++) {
            System.arraycopy(staffChars_16x4[i], 0, staffMaskChars_64, i * 4, 4);
        }

        ////////////////////////////////////////////////////////////////
        for (char[] chars : staffChars_16x4) {
            System.out.print(new String(chars) + " ");
        }
        ////////////////////////////////////////////////////////////////


        for (int i = 0; i < staffChars_16x4.length; i++) {
            staffMasksInt_16[i] = Integer.parseInt(new String(staffChars_16x4[i]), 16);
        }


        ////////////////////////////////////////////////////////////////
        System.out.println("\nSTART MASKS INT: ");
        for (int mask : staffMasksInt_16) {
            System.out.print(mask + "_");
        }
        System.out.println("\n=========================================================");
        ////////////////////////////////////////////////////////////////

        int[] counterInt;
        for (int i = staffMasksInt_16.length - 5; i >= 0; i--) {
            maxGeneratedCounters_13[i] = saltUtils.getCount(staffMasksInt_16[i]) - 1;
        }
        ////////////////////////////////////////////////////////////////

        System.out.println("\n=========================================================");
        System.out.println("\nREAL:");
        for (int i : maxGeneratedCounters_13) {
            System.out.print(i + "\t");
        }
        System.out.println("\n=========================================================");

        System.out.println("MASK: \n");
        for (char c : staffMaskChars_64) {
            System.out.print(c);
        }
        System.out.println("\n=========================================================");
        ////////////////////////////////////////////////////////////////

        // capacity and steps initialization
        initCapacity(maxGeneratedCounters_13);

        initMatrix();
    }

    public static ArraySupport getArraySupport() {
        if (arraySupport == null) {
            arraySupport = new ArraySupport();
        }
        return arraySupport;
    }

    public BigInteger getCount(String line) {
        if (line == null || line.isEmpty()) {
            return BigInteger.ZERO;
        }

        int[] array = convertCounterToInt(line);

        if (array == null) {
            return BigInteger.ZERO;
        }

        return getCount(array);
    }

    public int[] convertCounterToInt(String line) {
        int[] array = new int[13];
        if (line == null || line.isEmpty()) {
            return array;
        }
        String[] strings = line.trim().split(ARRAY_SEPARATOR);
        if (strings.length != 13) {
            return null;
        }

        for (int i = 0; i < strings.length; i++) {
            try {
                array[i] = Integer.parseInt(strings[i]);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Can't convert number!");
            }
        }
        return array;
    }

    public String convertCounterToString(int[] array) {
        StringBuilder builder = new StringBuilder();
        for (int i : array) {
            builder.append(i);
            builder.append(ARRAY_SEPARATOR);
        }
        builder.replace(builder.length() - 1, builder.length(), "");
        return builder.toString().trim();
    }

    public BigInteger getCount(int[] array) {
        if (array.length != 13) {
            throw new RuntimeException("Incorrect array! array.length = " + array.length);
        }
        BigInteger result = BigInteger.valueOf(array[11]);

        if (array[12] > 0) {
            result = result.add(steps[0].multiply(BigInteger.valueOf(array[12])));
        }

        for (int i = 0; i < array.length - 2; i++) {
            if (array[i] > 0) {
                result = result.add(steps[i + 1].multiply(BigInteger.valueOf(array[i])));
            }
        }

        return result;
    }

    private void initCapacity(int[] array) {

        if (array.length != 13) {
            throw new RuntimeException("array.length = " + array.length);
        }

        BigInteger result;

        for (int i : array) {
            if (i < 0) {
                throw new RuntimeException("Negative count: " + i);
            }
        }

        result = BigInteger.valueOf(array[11] + 1);
        steps[11] = result;

        for (int i = 10; i >= 0; i--) {
            if (array[i] > 0) {
                result = result.multiply(BigInteger.valueOf(array[i])).add(result);
            }
            steps[i] = result;
        }

        for (int i = 0; i < steps.length; i++) {
            System.out.println("step " + i + " --> " + steps[i]);
        }

        if (array[12] > 0) {
            result = result.multiply(BigInteger.valueOf(array[12])).add(result);
        }

        capacity = result.subtract(BigInteger.ONE);
    }

    private char[][] splitArrayFrom16_To4(char[] tetra) {
        char[][] result = new char[4][4];
        for (int i = 0; i < result.length; i++) {
            System.arraycopy(tetra, i * 4, result[i], 0, 4);
        }
        return result;
    }

    public BigInteger getCapacity() {
        return capacity;
    }

    private void initMatrix() {
        for (int i = 0; i < 12; i++) {
            saltMatrix[i] = saltUtils.getSalts(staffMasksInt_16[i]);
        }
        saltMatrix[12] = tetrasChars_4x16;
    }

    public char[][][] getSaltMatrix() {
        return saltMatrix;
    }
}
