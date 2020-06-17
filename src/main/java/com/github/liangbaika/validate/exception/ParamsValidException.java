package com.github.liangbaika.validate.exception;

/**
 * 参数校验异常 抛出此异常 自行捕获处理
 *
 * @author liangbaikai
 * @version 0.1.0
 * @date 2020/5/15 18:14
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
