package com.github.kit.validate.core;

/**
 * @author lq
 * @version 1.0
 * @date 2020/5/27 13:42
 */
@FunctionalInterface
public interface ParamValidator {
    Boolean validate(Object value);
}
