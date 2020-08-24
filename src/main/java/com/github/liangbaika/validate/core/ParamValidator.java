package com.github.liangbaika.validate.core;

/**
 * 自定义验证器需要实现此接口,并被spring容器托管
 * change: 0.9.7版本开始改为泛型支持
 *
 * @author liangbaikai
 * @version 0.1.0
 * @date 2020/5/27 13:42
 */
@FunctionalInterface
public interface ParamValidator<T> {
    /**
     * 自定义实现方法  需要用户自己实现 并让此实现类被spring 容器管理
     *
     * @param value 值
     * @return
     */
    Boolean validate(T value);
}
