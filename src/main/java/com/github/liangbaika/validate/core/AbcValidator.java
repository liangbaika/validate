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

    private static final String DEFAULT_MSG = "参数验证错误";


    private boolean required = false;
    private Check func;
    private String express;
    private String msg;


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
        msg = constraintAnnotation.message();
        if (DEFAULT_MSG.equals(msg)) {
            msg = func.msg + express;
        }
    }


    /**
     * jsr303 验证
     * 这里面尽量不要抛出异常
     *
     * @param value
     * @param context
     * @return
     */
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (required) {
            String tmpMsg = "";
            Boolean res = false;
            try {
                res = func.fun.apply(value, express);
            } catch (Exception e) {
                // handle exception
                String errorMessage = "";
                if (e.getCause() != null && e.getCause().getMessage() != null) {
                    errorMessage = e.getCause().getMessage();
                } else {
                    errorMessage = e.getMessage();
                }
                tmpMsg = msg + "; raw exception occured, info: " + errorMessage;
            }
            if (!res) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(tmpMsg)
                        .addConstraintViolation();
            }
            return res;
        } else {
            return true;
        }
    }

}