package ru.paswd.nearprofinder.model;

import ru.paswd.nearprofinder.config.NPF;

public class Utils {
    public static String setPriceConvenientFormat(long price) {
        StringBuilder result = new StringBuilder();
        int counter = 0;
        if (price == 0) {
            result.insert(0, 0);
            return result.toString();
        }
        while (price > 0) {
            if (counter >= NPF.Price.NUMERALS_IN_SECTION) {
                counter = 0;
                result.insert(0, NPF.Price.NUMERALS_SEPARATOR);
            }
            Long currNum = price % 10;
            price /= 10;

            result.insert(0, currNum);
            counter++;
        }
        return result.toString();
    }
    public static long getCurrentUnixTimestamp() {
        return System.currentTimeMillis() / 1000L;
    }
}
