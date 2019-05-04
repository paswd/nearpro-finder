package ru.paswd.nearprofinder;

public class NPF {
    public static final class Fragment {
        public static final int PROPERTY_LIST = 1;
        public static final int FILTER = 2;
        public static final int SETTINGS = 3;
        public static final int POINTS_LIST = 4;
    }

    public static final class Price {
        public static final int NUMERALS_IN_SECTION = 3;
        public static final char NUMERALS_SEPARATOR = ' ';
    }

    public static final class MenuItem {
        public static final int PROPERTY_LIST = 0;
        public static final int FILTER = 1;
        public static final int SETTINGS = 2;
    }

    public static final class DB {
        public static final String NAME = "npfDb";
        public static final String TABLE_PREFIX = "npf_";
        public static final String TABLE_POINTS_LIST = TABLE_PREFIX + "tablePointsList";
    }

    public static final class Geo {
        public static final double RADIUS_METERS = 6372795.;
        public static final double ABS_LAT_MAX = 90.;
        public static final double ABS_LNG_MAX = 180.;

        public static final class Optimization {
            public static final double DEGREE_STEP_BASIC = 1.;
            public static final double DEGREE_STEP_EPS = .00001;

        }
    }

    public static final class MethodResult {
        public static final int CORRECT = 0;
        public static final int EMPTY = 1;
        public static final int EXSISTS = 2;
        public static final int NOT_EXSISTS = 3;
        public static final int INCORRECT_DATA = 4;
    }
}
