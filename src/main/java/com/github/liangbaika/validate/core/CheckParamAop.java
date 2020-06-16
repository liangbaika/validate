package com.github.liangbaika.validate.core;


import com.alibaba.fastjson.JSONObject;
import com.github.liangbaika.validate.annations.ValidateParam;
import com.github.liangbaika.validate.annations.ValidateParams;
import com.github.liangbaika.validate.exception.ParamsValidException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 相比 hibernate-validate相比 此方法更加灵活多样。
 *
 * @author lq
 * @version 1.0
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
            throw new ParamsValidException(sampleResult.getMsg());
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
            throw new ParamsValidException(sampleResult.getMsg());
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
            ValidateParam[] annos = annotation.value();
            for (ValidateParam anno : annos) {
                String argName = anno.argName();
                //参数值
                Object value = this.getParamValue(arguments, paramName, argName);
                Boolean tmpValid = anno.value().fun.apply(value, anno.express());
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
        } else {
            // 单个参数校验
            // AOP监听带注解的方法，所以不用判断注解是否为空
            ValidateParam anno = method.getAnnotation(ValidateParam.class);

            String argName = anno.argName();
            //参数值
            Object value = this.getParamValue(arguments, paramName, argName);
            // 执行判断 // 调用枚举类的 CheckUtil类方法
            isValid = anno.value().fun.apply(value, anno.express());
            msg = anno.msg();
            if (null == msg || "".equals(msg)) {
                msg = argName + ": " + anno.value().msg + " " + anno.express();
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
        String[] strings = methodSignature.getParameterNames();
        return strings;
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
            argName = argName.split("\\.")[1];
            JSONObject jo = (JSONObject) JSONObject.toJSON(value);
            // 从实体对象中取值
            value = jo.get(argName);
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
                // log.error("" + e);
            }
        }
        return method;
    }


    static class SampleResult {
        private String msg;
        private Boolean pass;

        public SampleResult() {
        }

        public SampleResult(String msg, Boolean pass) {
            this.msg = msg;
            this.pass = pass;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public Boolean getPass() {
            return pass;
        }

        public void setPass(Boolean pass) {
            this.pass = pass;
        }
    }

}
