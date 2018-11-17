package com.mango.launcher3.retrofit;

import retrofit2.Response;

/**
 * @author tic
 * created on 18-11-15
 */
public class Http {
    public static final int CODE_OK = 0;

    public interface MIME {
        String TEXT_PLAIN = "text/plain";
        String APPLICATION_JSON = "application/json";
    }

    public static final int CODE_ERROR_UNKNOWN = -1;
    public static final int CODE_ERROR_SERVER = -10086;
    public static final int CODE_ERROR_TIMEOUT = -10087;

    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int NOT_IMPLEMENTED = 501;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;
    private static final int HTTP_VERSION_NOT_SUPPORTED = 505;
    private static final int VARIANT_ALSO_NEGOTIATES = 506;
    private static final int INSUFFICIENT_STORAGE = 507;
    private static final int BANDWIDTH_LIMIT_EXCEEDED = 509;
    private static final int NOT_EXTENDED = 510;
    private static final int UNPARSEABLE_RESPONSE_HEADERS = 600;
    private static final int BAD_REQUEST = 400;
    private static final int UNAUTHORIZED = 401;
    private static final int PAYMENT_REQUIRED = 402;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int METHOD_NOT_ALLOWED = 405;
    private static final int NOT_ACCEPTABLE = 406;
    private static final int PROXY_AUTHENTICATION_REQUIRED = 407;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int CONFLICT = 409;
    private static final int GONE = 410;
    private static final int LENGTH_REQUIRED = 411;
    private static final int PRECONDITION_FAILED = 412;
    private static final int REQUEST_ENTITY_TOO_LARGE = 413;
    private static final int REQUEST_URI_TOO_LONG = 414;
    private static final int UNSUPPORTED_MEDIA_TYPE = 415;
    private static final int REQUESTED_RANGE_NOT_SATISFIABLE = 416;
    private static final int EXPECTATION_FAILED = 417;
    private static final int TOO_MANY_CONNECTIONS = 421;
    private static final int UNPROCESSABLE_ENTITY = 422;
    private static final int LOCKED = 423;
    private static final int FAILED_DEPENDENCY = 424;
    private static final int UNORDERED_COLLECTION = 425;
    private static final int UPGRADE_REQUIRED = 426;
    private static final int RETRY_WITH = 449;
    private static final int UNAVAILABLE_FOR_LEGAL_REASONS = 451;

    public static int isServerError(Response response) {
        int code = response.code();
        switch (code) {
            case BAD_GATEWAY:
            case SERVICE_UNAVAILABLE:
            case BAD_REQUEST:
            case FORBIDDEN:
            case NOT_FOUND:
            case TOO_MANY_CONNECTIONS:
                return CODE_ERROR_SERVER;
            default:
                return code;
        }
    }

    public static boolean isSuccess(Response response) {
        return response != null && response.isSuccessful();
    }

}
