package com.example.util;

import java.util.Collections;

/**
 * SpringMVC Response Result 工具类
 */
public class ResultUtils {
    /**
     * @return ResponseResult<Object>
     */
    public static ResponseResult success() {
        return success(Collections.EMPTY_MAP);
    }

    /**
     * @param data
     * @param <T>
     * @return ResponseResult<T>
     */
    public static <T> ResponseResult<T> success(final T data) {
        return build(200, "success", data);
    }

    /**
     * @param msg
     * @return ResponseResult<Object>
     */
    public static ResponseResult failure(final String msg) {
        return failure(-1, msg);
    }

    /**
     * @param errorCode
     * @return ResponseResult<Object>
     */
    public static ResponseResult failure(final ErrorCode errorCode) {
        return failure(errorCode, null);
    }

    /**
     * @param errorCode
     * @param data
     * @param <T>
     * @return ResponseResult<T>
     */
    public static <T> ResponseResult<T> failure(final ErrorCode errorCode, final T data) {
        return build(errorCode.getCode(), errorCode.getMessage(), data);
    }


    /**
     * @param code
     * @param msg
     * @return ResponseResult<Object>
     */
    public static ResponseResult failure(final int code, final String msg) {
        return build(code, msg, null);
    }


    /**
     * @param code
     * @param msg
     * @param data
     * @param <T>
     * @return ResponseResult<T>
     */
    public static <T> ResponseResult<T> build(final int code, final String msg, final T data) {
        return new ResponseResult<>(code, msg, data);
    }
}
