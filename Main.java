import java.math.BigInteger;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        // Test Case 1
        Map<Integer, String[]> testCase1 = new LinkedHashMap<>();
        testCase1.put(1, new String[]{"10", "4"});
        testCase1.put(2, new String[]{"2", "111"});
        testCase1.put(3, new String[]{"10", "12"});
        testCase1.put(6, new String[]{"4", "213"});
        int k1 = 3;

        // Test Case 2
        Map<Integer, String[]> testCase2 = new LinkedHashMap<>();
        testCase2.put(1, new String[]{"6", "13444211440455345511"});
        testCase2.put(2, new String[]{"15", "aed7015a346d63"});
        testCase2.put(3, new String[]{"15", "6aeeb69631c227c"});
        testCase2.put(4, new String[]{"16", "e1b5e05623d881f"});
        testCase2.put(5, new String[]{"8", "316034514573652620673"});
        testCase2.put(6, new String[]{"3", "2122212201122002221120200210011020220200"});
        testCase2.put(7, new String[]{"3", "20120221122211000100210021102001201112121"});
        testCase2.put(8, new String[]{"6", "20220554335330240002224253"});
        testCase2.put(9, new String[]{"12", "45153788322a1255483"});
        testCase2.put(10, new String[]{"7", "1101613130313526312514143"});
        int k2 = 7;

        System.out.println("Test Case 1 Secret: " + findSecretSimple(testCase1, k1));
        System.out.println("Test Case 2 Secret: " + findSecretSimple(testCase2, k2));
    }

    public static BigInteger findSecretSimple(Map<Integer, String[]> testCase, int k) {
        List<Point> points = new ArrayList<>();

        for (Map.Entry<Integer, String[]> entry : testCase.entrySet()) {
            int x = entry.getKey();
            int base = Integer.parseInt(entry.getValue()[0]);
            String value = entry.getValue()[1];

            BigInteger y = decodeFromBase(value, base);
            points.add(new Point(BigInteger.valueOf(x), y));
        }

        // Use first k points for interpolation
        List<Point> selectedPoints = points.subList(0, Math.min(k, points.size()));

        return lagrangeInterpolation(selectedPoints, BigInteger.ZERO);
    }

    public static BigInteger decodeFromBase(String value, int base) {
        BigInteger result = BigInteger.ZERO;
        BigInteger baseVal = BigInteger.valueOf(base);

        for (int i = 0; i < value.length(); i++) {
            char digit = value.charAt(i);
            int digitValue;

            if (Character.isDigit(digit)) {
                digitValue = digit - '0';
            } else if (Character.isLetter(digit)) {
                digitValue = Character.toLowerCase(digit) - 'a' + 10;
            } else {
                throw new IllegalArgumentException("Invalid character: " + digit);
            }

            if (digitValue >= base) {
                throw new IllegalArgumentException("Digit '" + digit + "' invalid for base " + base);
            }

            result = result.multiply(baseVal).add(BigInteger.valueOf(digitValue));
        }

        return result;
    }

    public static BigInteger lagrangeInterpolation(List<Point> points, BigInteger x) {
        BigInteger result = BigInteger.ZERO;

        for (int i = 0; i < points.size(); i++) {
            BigInteger xi = points.get(i).x;
            BigInteger yi = points.get(i).y;

            BigInteger num = BigInteger.ONE;
            BigInteger den = BigInteger.ONE;

            for (int j = 0; j < points.size(); j++) {
                if (i == j) continue;

                BigInteger xj = points.get(j).x;
                num = num.multiply(x.subtract(xj));
                den = den.multiply(xi.subtract(xj));
            }

            BigInteger term = yi.multiply(num).divide(den);
            result = result.add(term);
        }

        return result;
    }

    static class Point {
        BigInteger x;
        BigInteger y;

        Point(BigInteger x, BigInteger y) {
            this.x = x;
            this.y = y;
        }
    }
}
