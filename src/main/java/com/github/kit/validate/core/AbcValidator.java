package com.github.kit.validate.core;

import com.github.kit.validate.annations.AbcValidate;
import com.github.kit.validate.enums.Check;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 自定义验证注解支持类 jsr303
 *
 * @author lq
 */
public class AbcValidator implements ConstraintValidator<AbcValidate, Object> {

    private boolean required = false;
    private Check func;
    private String express;

    public AbcValidator() {
    }

    @Override
    public void initialize(AbcValidate constraintAnnotation) {
        required = constraintAnnotation.required();
        func = constraintAnnotation.fun();
        express = constraintAnnotation.express();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (required) {
            return func.fun.apply(value, express);
        } else {
            return true;
        }
    }

}