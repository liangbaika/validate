package com.github.liangbaika.validate.annations;

import com.github.liangbaika.validate.core.AbcValidator;
import com.github.liangbaika.validate.enums.Check;
import com.github.liangbaika.validate.exception.ParamsInValidException;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 注解 一般用于bean字段 方法上做参数校验 实现了JSR303规范。
 *
 * @author liangbaikai
 * @version 0.1.0
 * @date 2020/5/15 18:15
 * @see ParamsInValidException
 */
@Target({METHOD, FIELD, CONSTRUCTOR, PARAMETER, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {AbcValidator.class})
@Repeatable(AbcValidate.List.class)
public @interface AbcValidate {

    /**
     * 是否必传
     */
    boolean required() default false;

    /**
     * 实际上会取验证函数的msg 自定义此值的话优先级会覆盖的
     */
    String message() default "参数验证错误";

    /**
     * 分组
     */
    Class<?>[] groups() default {};

    /**
     * 极少用到 携带数据
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * 验证函数
     *
     * @return
     */
    Check fun() default Check.NotNull;

    /**
     * 多个值逗号隔开 此值和fun的里的验证方法息息相关
     */
    String express() default "";

    /**
     * Defines several {@link AbcValidate} annotations on the same element.
     *
     * @see AbcValidate
     */
    @Target({METHOD, FIELD, CONSTRUCTOR, PARAMETER, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        AbcValidate[] value();
    }


}
