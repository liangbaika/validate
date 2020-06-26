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
     * 参数校验调用方法
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
     * 参数名称用.表示层级，最多支持2级如： entity.userName
     *
     * @return
     */
    String argName();

    /**
     * 暂时没啥用 已过时
     * 参数类型取值举例：
     *
     * @return
     */
    @Deprecated
    String argType() default "";


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