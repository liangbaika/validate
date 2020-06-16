package com.github.liangbaika.validate.annations;

import com.github.liangbaika.validate.core.AbcValidator;
import com.github.liangbaika.validate.enums.Check;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

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
