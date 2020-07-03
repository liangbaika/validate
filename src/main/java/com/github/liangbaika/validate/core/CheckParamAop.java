package com.github.liangbaika.validate.core;


import com.github.liangbaika.validate.annations.ValidateParam;
import com.github.liangbaika.validate.annations.ValidateParams;
import com.github.liangbaika.validate.exception.ParamsCheckException;
import com.github.liangbaika.validate.exception.ParamsInValidException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * AOP切面验证
 * 相比 hibernate-validate相比 此方法更加灵活多样。
 * 也是验证的核心逻辑
 *
 * @author liangbaikai
 * @version 0.1.0
 * @date 2020/5/15 18:14
 */
@Aspect
@Component
public class CheckParamAop {

    @Pointcut("@annotation(com.github.liangbaika.validate.annations.ValidateParam)")
    public void checkParam() {
    }

    @Pointcut("@annotation(com.github.liangbaika.validate.annations.ValidateParams)")
    public void checkParams() {
    }

    @Around("checkParam()") // 这里要换成自定义注解的路径
    public Object check1(ProceedingJoinPoint point) throws Throwable {
        Object obj;
        // 参数校验
        SampleResult sampleResult = doCheck(point, false);
        if (!sampleResult.getPass()) {
            throw new ParamsInValidException(sampleResult.getMsg());
        }
        // 通过校验，继续执行原有方法
        obj = point.proceed();
        return obj;
    }


    @Around("checkParams()")
    public Object check2(ProceedingJoinPoint point) throws Throwable {
        Object obj;
        // 参数校验
        SampleResult sampleResult = doCheck(point, true);
        if (!sampleResult.getPass()) {
            throw new ParamsInValidException(sampleResult.getMsg());
        }
        // 通过校验，继续执行原有方法
        obj = point.proceed();
        return obj;
    }


    /**
     * 参数校验
     *
     * @param point 切点
     * @param multi 多参数校验
     * @return 错误信息
     */
    private SampleResult doCheck(JoinPoint point, boolean multi) {
        Method method = this.getMethod(point);
        String[] paramName = this.getParamName(point);
        // 获取接口传递的所有参数
        Object[] arguments = point.getArgs();

        Boolean isValid = true;
        String msg = " ";
        if (multi) {
            // 多个参数校验
            // AOP监听带注解的方法，所以不用判断注解是否为空
            ValidateParams annotation = method.getAnnotation(ValidateParams.class);
            boolean shortPath = annotation.shortPath();
            boolean anded = annotation.anded();
            ValidateParam[] annos = annotation.value();
            int passCounter = 0;
            for (ValidateParam anno : annos) {
                boolean required = anno.required();
                String argName = anno.argName();
                //参数值
                Object value = this.getParamValue(arguments, paramName, argName);
                if (!required && value == null) {
                    continue;
                }
                Boolean tmpValid = anno.value().fun.apply(value, anno.express());
                if (tmpValid) {
                    passCounter++;
                }
                if (isValid) {
                    isValid = tmpValid;
                }
                // 执行判断 // 调用枚举类的 CheckUtil类方法
                if (!tmpValid) {
                    // 只要有一个参数判断不通过，立即返回
                    String tmpMsg = anno.msg();
                    msg += tmpMsg;
                    if (null == tmpMsg || "".equals(tmpMsg)) {
                        msg += (argName + ": " + anno.value().msg + " " + anno.express());
                    }
                    msg += "; ";
                    if (shortPath) {
                        break;
                    }
                }
            }
            if (!anded && annos.length > 1 && passCounter > 0) {
                isValid = true;
            }
        } else {
            // 单个参数校验
            // AOP监听带注解的方法，所以不用判断注解是否为空
            ValidateParam anno = method.getAnnotation(ValidateParam.class);
            boolean required = anno.required();
            String argName = anno.argName();
            //参数值
            Object value = this.getParamValue(arguments, paramName, argName);
            if (required || value != null) {
                // 执行判断 // 调用枚举类的 CheckUtil类方法
                isValid = anno.value().fun.apply(value, anno.express());
                msg = anno.msg();
                if (null == msg || "".equals(msg)) {
                    msg = argName + ": " + anno.value().msg + " " + anno.express();
                }
            }
        }
        return new SampleResult(msg, isValid);

    }


    /**
     * 获取参数名称
     * jdk 1.8 特性
     *
     * @param joinPoint
     * @return
     */
    private String[] getParamName(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        return methodSignature.getParameterNames();
    }


    /**
     * 根据参数名称，获取参数值
     *
     * @param arguments
     * @param paramName
     * @param argName
     * @return
     */
    private Object getParamValue(Object[] arguments, String[] paramName, String argName) {
        Object value = null;
        String name = argName;
        if (argName.contains(".")) {
            name = argName.split("\\.")[0];
        }
        int index = 0;
        for (String string : paramName) {
            if (string.equals(name)) {
                //基本类型取值	// 不做空判断，如果注解配置的参数名称不存在，则取值为null
                value = arguments[index];
                break;
            }
            index++;
        }
        //从对象中取值
        if (argName.contains(".")) {
            //从实体对象中取值 无限级  如 user.tokenObj.value
            String[] argNames = argName.split("\\.");
            try {
                value = getObjValue(1, value, argNames);
            } catch (NoSuchMethodException e) {
                throw new ParamsCheckException("can not  found getter method");
            } catch (InvocationTargetException e) {
                throw new ParamsCheckException(" invoke getter method error");
            } catch (IllegalAccessException e) {
                throw new ParamsCheckException(" when get filed value IllegalAccessException occured");
            }
        }
        return value;
    }


    /**
     * 获取方法
     *
     * @param joinPoint ProceedingJoinPoint
     * @return 方法
     */
    private Method getMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        if (method.getDeclaringClass().isInterface()) {
            try {
                method = joinPoint
                        .getTarget()
                        .getClass()
                        .getDeclaredMethod(joinPoint.getSignature().getName(),
                                method.getParameterTypes());
            } catch (SecurityException | NoSuchMethodException e) {
            }
        }
        return method;
    }

    /**
     * 对应字段必须要有对应的getter方法
     * 反射获取值 无限级
     *
     * @param index    参数索引  应该从1开始
     * @param value    对象
     * @param argNames 字段名数组
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private static Object getObjValue(int index, Object value, String[] argNames) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        assert index > 0;
        assert argNames != null;
        if (index >= argNames.length || value == null) {
            return value;
        }

        String tempName = argNames[index];
        // 注意 调用公共的getter方法;  某些bool值命名不规范时 可能会找不到对应方法,导致失败。
        String filedMethodName = "get" + tempName.substring(0, 1).toUpperCase() + tempName.substring(1, tempName.length());
        Method getterMethod = value.getClass().getMethod(filedMethodName);
        Object tempValue = getterMethod.invoke(value);

        return getObjValue(index + 1, tempValue, argNames);
    }


    private static class SampleResult {
        private String msg;
        private Boolean pass;


        public SampleResult(String msg, Boolean pass) {
            this.msg = msg;
            this.pass = pass;
        }

        public String getMsg() {
            return msg;
        }


        public Boolean getPass() {
            return pass;
        }

    }

}
