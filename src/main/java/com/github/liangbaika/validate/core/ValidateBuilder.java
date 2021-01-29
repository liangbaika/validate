package com.github.liangbaika.validate.core;

import com.github.liangbaika.validate.enums.Check;
import com.github.liangbaika.validate.exception.ParamsCheckException;
import com.github.liangbaika.validate.exception.ParamsInValidException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 另外一种代码的方式检查参数
 * since: 1.1.0 版本
 *
 * @author liangbaikai
 * @version 1.1.0
 * @date 2021/1/28 13:42
 */
public class ValidateBuilder {

    /**
     * 请使用build方法构造
     */
    private ValidateBuilder() {
    }

    /**
     * 验证相关信息的容器保存类
     */
    private List<ValidateChain> chains = new ArrayList<ValidateChain>();

    /**
     * 实体类
     */
    public static class ValidateChain {

        private Check check;
        private Object value;
        private String express;
        private Boolean result;
        private String msg;

        public Check getCheck() {
            return check;
        }

        public void setCheck(Check check) {
            this.check = check;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public String getExpress() {
            return express;
        }

        public void setExpress(String express) {
            this.express = express;
        }

        public Boolean getResult() {
            return result;
        }

        public void setResult(Boolean result) {
            this.result = result;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    /**
     * @param check   验证的枚举方法 自定义请使用Custom枚举
     * @param value   验证的值
     * @param express 表达式 非必填 和枚举方法相关
     * @param msg     提示信息 没有默认取枚举方法的提示信息
     * @return
     */
    public ValidateBuilder vali(Check check, Object value, String express, String msg) {
        ValidateChain validateChain = new ValidateChain();
        validateChain.setCheck(check);
        validateChain.setValue(value);
        validateChain.setExpress(express);
        validateChain.setMsg(msg == null || Objects.equals(msg, "") ? check.msg : msg);
        chains.add(validateChain);
        return this;
    }

    public ValidateBuilder vali(Check check, Object value, String msg) {
        return vali(check, value, null, msg);
    }

    public ValidateBuilder vali(Check check, Object value) {
        return vali(check, value, null, null);
    }

    public ValidateBuilder wvali(Check check, Object value, String express) {
        return vali(check, value, express, null);
    }

    /**
     * 真正检查的方法
     *
     * @return
     */
    public ValidateBuilder doCheck() {
        for (ValidateChain conn : chains) {
            Boolean result = conn.getCheck().vali(conn.value, conn.express);
            conn.setResult(result);
        }
        return this;
    }

    /**
     * 此次验证是否通过
     *
     * @return
     */
    public Boolean isPassed() {
        if (chains == null || chains.isEmpty()) {
            throw new ParamsCheckException("chains can not be empty, please call  vali() and doCheck() first");
        }
        List<ValidateChain> faileds = chains.stream().filter(e -> Boolean.FALSE == e.getResult()).collect(Collectors.toList());
        return faileds.size() <= 0;
    }

    /**
     * 不通过就抛出异常 ParamsInValidException
     *
     * @return
     */
    public ValidateBuilder ifNotPasedThrowException() {
        Boolean passed = isPassed();
        if (!passed) {
            throw new ParamsInValidException(getFailedMsgs());
        }
        return this;
    }

    /**
     * 获取失败的消息
     *
     * @return
     */
    public String getFailedMsgs() {
        if (chains == null || chains.isEmpty()) {
            throw new ParamsCheckException("chains can not be empty, please call  vali() and doCheck() first");
        }
        return chains.stream().filter(e -> Boolean.FALSE == e.getResult())
                .map(e -> e.getValue() + " " + e.getMsg() + " " + (e.getExpress() == null ? "" : e.getExpress()))
                .collect(Collectors.joining(","));
    }

    /**
     * 获取失败的条数
     *
     * @return
     */
    public int getFailedCounts() {
        if (chains == null || chains.isEmpty()) {
            return 0;
        }
        return chains.stream()
                .filter(e -> Boolean.FALSE == e.getResult())
                .collect(Collectors.toList())
                .size();
    }

    /**
     * 获取成功的条数
     *
     * @return
     */
    public int getSuccedCounts() {
        if (chains == null || chains.isEmpty()) {
            return 0;
        }
        return chains.size() - chains.stream()
                .filter(e -> Boolean.FALSE == e.getResult())
                .collect(Collectors.toList())
                .size();
    }

    public List<ValidateChain> getChains() {
        return chains;
    }

    public static ValidateBuilder build() {
        return new ValidateBuilder();
    }

    public ValidateBuilder clear() {
        chains.clear();
        return this;
    }


}
