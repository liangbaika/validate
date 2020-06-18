package com.github.liangbaika.validate.annations;

import com.github.liangbaika.validate.core.AbcValidator;
import com.github.liangbaika.validate.enums.Check;
import com.github.liangbaika.validate.exception.ParamsInValidException;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

/**
 * 注解 一般用于bean字段 方法上做参数校验 实现了JSR303规范。使用此注解的字段如果非法那么会抛出
 *
 * @author liangbaikai
 * @version 0.1.0
 * @date 2020/5/15 18:15
 * @see ParamsInValidException
 */
@Target({METHOD, FIELD, CONSTRUCTOR, PARAMETER, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {AbcValidator.class})
@Inherited
public @interface AbcValidate {

    boolean required() default false;

    String message() default "参数验证错误";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Check fun() default Check.NotNull;

    String express() default "";


}
