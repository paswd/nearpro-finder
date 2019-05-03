package ru.paswd.nearprofinder;

public class NPF {
    public static final int FRAGMENT_PROPERTY_LIST = 1;
    public static final int FRAGMENT_FILTER = 2;
    public static final int FRAGMENT_SETTINGS = 3;
    public static final int FRAGMENT_POINTS_LIST = 4;

    public static final int PRICE_NUMERALS_IN_SECTION = 3;
    public static final char PRICE_NUMERALS_SEPARATOR = ' ';

    public static final int MENU_ITEM_PROPERTY_LIST = 0;
    public static final int MENU_ITEM_FILTER = 1;
    public static final int MENU_ITEM_SETTINGS = 2;

    public static final String DB_NAME = "npfDb";
    public static final String DB_TABLE_PREFIX = "npf_";
    public static final String DB_TABLE_POINTS_LIST = DB_TABLE_PREFIX + "tablePointsList";

    public static final double EARTH_RADIUS_METERS = 6372795.;
}
