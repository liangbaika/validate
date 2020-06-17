package com.github.liangbaika.validate.core;

/**
 * 自定义验证器需要实现此接口,并被spring容器托管
 *
 * @author liangbaikai
 * @version 0.1.0
 * @date 2020/5/27 13:42
 */
@FunctionalInterface
public interface ParamValidator {
    Boolean validate(Object value);
}
