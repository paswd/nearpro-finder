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
        public static final double EARTH_RADIUS_METERS = 6372795.;
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

    public static final class Server {
        public static final String HOST = "http://nearpro.paswd.ru";
        public static final String ACCESS_TOKEN = "AC2C50DE84A36DDC91EA425CE3EBFACEA481F29D";
        public static final String API_PATH = "/api/1.0/";

        public static final class API {
            public static final String AUTH = HOST + API_PATH +
                    "auth.php";
            public static final String CHANGE_PASSWORD = HOST + API_PATH +
                    "change_password.php";
            public static final String CHECK_SESSION = HOST + API_PATH +
                    "check_session.php";
            public static final String EDIT_USER = HOST + API_PATH +
                    "edit_user.php";
            public static final String GET_PROPERTY = HOST + API_PATH +
                    "get_property.php";
            public static final String GET_PROPERTY_TYPES = HOST + API_PATH +
                    "get_property_types.php";
            public static final String GET_REGIONS = HOST + API_PATH +
                    "get_regions.php";
            public static final String LOGOUT = HOST + API_PATH +
                    "logout.php";
            public static final String REGISTER = HOST + API_PATH +
                    "register.php";
            public static final String REGISTER_FOLLOWING = HOST + API_PATH +
                    "register_following.php";

            public static final class Respond {
                public static final int OK = 1;
                public static final int ERROR = 0;

                public static final class Errors {
                    public static final int NO_NETWORK_CONNECTION = -1;
                    public static final int ACCESS_DENIED = 1;
                    public static final int MYSQL_ERROR = 2;

                    public static final class Reg {
                        public static final int FIELDS_EMPTY = 3;
                        public static final int USER_EXSISTS = 4;
                    }

                    public static final class Auth {
                        public static final int INVALID_INPUT = 5;
                        public static final int INVALID_SESSION = 6;
                    }

                    public static final class Edit {
                        public static final int INVALID_OLD_PASSWORD = 7;
                    }
                }
            }
        }
    }
}
