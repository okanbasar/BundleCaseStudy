package app.bundle.casestudy.caseone;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CaseOneMain {

    private static final int GROUP_SIZE = 3;
    private static final int GROUP_SUM_LIMIT_MIN = 90;

    public static void main(String[] args) {
        // Given example
        String[][] inputArrays = {
                {"0", "s1", null, "35", "90", "60"},
                {"ttt", null, null, "15"},
                {"75", "95", "0", "0", null, "ssss", "0", "-15"},
                {"25", "fgdfg", "", "105", "dsfdsf", "-5"}
        };

        // The requested filtering applied
        List<Integer> result = filter(inputArrays);
        System.out.println(result);
    }

    // traverses all inputArrays sequentially,
    // finds all numeric values and groups each of GROUP_SIZE to check their sum
    // if sum is greater than or equal to GROUP_SUM_LIMIT_MIN,
    // then adds values sequentially to resultList and returns
    private static List<Integer> filter(String[][] inputArrays) {
        List<Integer> resultList = new ArrayList<>();
        Integer[] groupValues = new Integer[GROUP_SIZE];
        int groupSum = 0;
        int groupCount = 0;
        for (String[] array : inputArrays) {
            if (array != null) {
                for (String str : array) {
                    NumericResult numericResult = isNumeric(str);
                    if (numericResult.isNumeric()) {
                        int numValue = numericResult.getValue();
                        groupValues[groupCount++] = numValue;
                        groupSum += numValue;
                        if (groupCount == GROUP_SIZE) {
                            if (groupSum >= GROUP_SUM_LIMIT_MIN) {
                                // when a group of three with valid integers formed, add them to the resultList with given order.
                                Collections.addAll(resultList, groupValues);
                            }
                            groupSum = 0;
                            groupCount = 0;
                        }
                    }
                }
            }
        }

        return resultList;
    }

    // parse given string as integer,
    // if it succeeds then keep the result and set isNumeric flag TRUE,
    // otherwise set isNumeric flag FALSE.
    private static NumericResult isNumeric(String str) {
        if (str == null) {
            return new NumericResult(false, 0);
        }
        try {
            int parsedValue = Integer.parseInt(str);
            return new NumericResult(true, parsedValue);
        } catch (NumberFormatException nfe) {
            return new NumericResult(false, 0);
        }
    }

    private static class NumericResult {
        private final boolean isNumeric;
        private final int value;

        public NumericResult(boolean isNumeric, int value) {
            this.isNumeric = isNumeric;
            this.value = value;
        }

        public boolean isNumeric() {
            return isNumeric;
        }

        public int getValue() {
            return value;
        }
    }

}