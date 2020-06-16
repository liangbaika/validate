package com.github.kit.validate.exception;

/**
 * 参数校验异常
 */
public class ParamsValidException extends RuntimeException {

    public ParamsValidException() {
    }

    public ParamsValidException(String message) {
        super(message);
    }

    public ParamsValidException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParamsValidException(Throwable cause) {
        super(cause);
    }

    public ParamsValidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
