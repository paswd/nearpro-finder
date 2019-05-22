package ru.paswd.nearprofinder.config;

public class NPF {
    public static final class Global {
        public static final int TIMER_DELAY = 2000;
    }
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

    public static final class Session {
        public static final class Messages {
            public static final String SESSION_INVALID = "Время сессии истекло";
        }
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
            public static final String GET_USER = HOST + API_PATH +
                    "get_user.php";

            public static final class Respond {
                public static final int OK = 1;
                public static final int ERROR = 0;

                public static final class Errors {
                    public static final class NoNetworkConnection {
                        public static final int CODE = -1;
                        public static final String MESSAGE = "Интернет-соединение отсутствует";
                    }
                    public static final class AccessDenied {
                        public static final int CODE = 1;
                        public static final String MESSAGE = "Доступ запрещён";
                    }
                    public static final class SqlError {
                        public static final int CODE = 2;
                        public static final String MESSAGE = "Ошибка базы данных";
                    }

                    public static final class FieldsEmpty {
                        public static final int CODE = 3;
                        public static final String MESSAGE = "Некоторые поля не заполнены";
                    }

                    public static final class Reg {
                        public static final class UserExists {
                            public static final int CODE = 4;
                            public static final String MESSAGE = "Пользователь с указанным логином уже зарегистрирован";
                        }
                    }

                    public static final class Auth {
                        public static final class InvalidInput {
                            public static final int CODE = 5;
                            public static final String MESSAGE = "Неправильный логин или пароль";
                        }
                        public static final class InvalidSession {
                            public static final int CODE = 6;
                            public static final String MESSAGE = "Сессия завершена";
                        }
                    }

                    public static final class Edit {
                        public static final class InvalidOldPassword {
                            public static final int CODE = 7;
                            public static final String MESSAGE = "Старый пароль указан неверно";
                        }
                    }
                }
            }
        }
    }
}
