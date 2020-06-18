package com.github.liangbaika.validate.core;

import com.github.liangbaika.validate.annations.AbcValidate;
import com.github.liangbaika.validate.enums.Check;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 自定义验证注解支持类 jsr303
 *
 * @author liangbaikai
 * @version 0.1.0
 * @date 2020/5/15 18:14
 */
public class AbcValidator implements ConstraintValidator<AbcValidate, Object> {

    private boolean required = false;
    private Check func;
    private String express;

    public AbcValidator() {
    }

    /**
     * jsr303 初始化
     *
     * @param constraintAnnotation
     */
    @Override
    public void initialize(AbcValidate constraintAnnotation) {
        required = constraintAnnotation.required();
        func = constraintAnnotation.fun();
        express = constraintAnnotation.express();
    }

    /**
     * jsr303 验证
     *
     * @param value
     * @param context
     * @return
     */
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (required) {
            return func.fun.apply(value, express);
        } else {
            return true;
        }
    }

}