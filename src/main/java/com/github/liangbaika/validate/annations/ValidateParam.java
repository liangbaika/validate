package com.github.liangbaika.validate.annations;

/**
 * 验证注解 一般放在 Controoler里的路由方法上使用
 *
 * @author liangbaikai
 * @version 0.1.0
 * @date 2020/5/15 18:15
 */


import com.github.liangbaika.validate.enums.Check;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateParam {

    /**
     * 参数校验调用方法
     *
     * @return
     */
    Check value() default Check.NotNull;

    /**
     * 多个值逗号隔开
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
     * 暂时没啥用
     * 参数类型取值举例：
     * java.lang.String
     */
    String argType() default "";


    /**
     * 自定义错误提示信息 特殊的才需要填 一般不用填
     *
     * @return
     */
    String msg() default "";


}