package com.github.liangbaika.validate.annations;

/**
 * 验证注解 一般放在 Controoler里的路由方法上使用
 * 如果字段过多 建议使用自定义方式
 *
 * @author liangbaikai
 * @version 0.1.0
 * @date 2020/5/15 18:15
 */


import com.github.liangbaika.validate.enums.Check;

import java.lang.annotation.*;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(ValidateParam.List.class)
public @interface ValidateParam {


    /**
     * 是否必传;  JSR303里默认false,此处由于历史兼容原因默认true; 需要注意
     *
     * @return
     */
    boolean required() default true;


    /**
     * 函数 参数校验调用方法
     *
     * @return
     */
    Check value() default Check.NotNull;

    /**
     * 多个值逗号隔开 此值和value的里的验证方法息息相关
     *
     * @return
     */
    String express() default "";

    /**
     * 参数名称用 ;    .表示层级，支持无限级如： entityObj.userObj.age
     *
     * @return
     */
    String argName();


    /**
     * 自定义错误提示信息 特殊的才需要填 一般不用填
     *
     * @return
     */
    String msg() default "";


    /**
     * Defines several {@link ValidateParam} annotations on the same element.
     *
     * @see ValidateParam
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        ValidateParam[] value();
    }


}