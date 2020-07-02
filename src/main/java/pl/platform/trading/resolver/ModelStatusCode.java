package pl.platform.trading.resolver;

public class ModelStatusCode {

    // default success code
    public static final int STATUS_SUCCESS = 10;

    // error codes

    // take position error codes
    public static final int ERROR_NOT_ENOUGH_MONEY = 101;

    // place order error codes
    public static final int ERROR_DB_SAVE_EXCEPTION = 201;

    // close position error codes
    public static final int ERROR_DB_DELETE_EXCEPTION = 301;
    public static final int ERROR_BELONGS_TO_ANOTHER_USER = 302;
    public static final int ERROR_CANNOT_FIND_POSITION = 303;

    // cancel order error codes
    public static final int ERROR_CANNOT_FIND_ORDER = 401;

    // registration error codes
    public static final int ERROR_EMAIL_ALREADY_EXISTS = 501;
    public static final int ERROR_INVALID_USER_DATA = 502;
    public static final int ERROR_INVALID_FIELD_VALUE = 503;
    public static final int ERROR_CONSTRAINT_VIOLATION = 504;

    // login error codes
    public static final int ERROR_BAD_CREDENTIALS = 601;
    public static final int ERROR_UNKNOWN_LOGIN_ERROR = 602;

}
