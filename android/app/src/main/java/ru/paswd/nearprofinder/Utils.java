package ru.paswd.nearprofinder;

public class Utils {
    public static String setPriceConvenientFormat(long price) {
        StringBuilder result = new StringBuilder();
        int counter = 0;
        if (price == 0) {
            result.insert(0, 0);
            return result.toString();
        }
        while (price > 0) {
            if (counter >= NPF.PRICE_NUMERALS_IN_SECTION) {
                counter = 0;
                result.insert(0, NPF.PRICE_NUMERALS_SEPARATOR);
            }
            Long currNum = price % 10;
            price /= 10;

            result.insert(0, currNum);
            counter++;
        }
        return result.toString();
    }
}
