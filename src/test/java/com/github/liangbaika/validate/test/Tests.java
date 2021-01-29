package com.github.liangbaika.validate.test;

import com.github.liangbaika.validate.core.ValidateBuilder;

import static com.github.liangbaika.validate.enums.Check.*;

/**
 * 测试
 *
 * @author lq
 * @version 1.0
 * @date 2021/1/29 21:44
 */
public class Tests {
    public static void main(String[] args) {

        ValidateBuilder validateBuilder = ValidateBuilder.build();
        int failedCounts = validateBuilder
                .vali(ne, "3", "3","不能等于")
                .vali(Chinese, "测试中文")
                .vali(isBirthdaystr, "1992-12-09")
                .vali(isUrl, "https://baidu.com")
                .doCheck()
                .getFailedCounts();
        System.out.println(failedCounts);
        System.out.println(validateBuilder.getSuccedCounts());
        System.out.println(validateBuilder.getFailedMsgs());
        System.out.println(validateBuilder.isPassed());


        //重复使用 validateBuilder  先调用clear方法
        int failedCounts2 = validateBuilder
                .clear()
                .vali(isGeneral, "ssa2_")
                .vali(Chinese, "测试中文")
                .vali(isBirthdaystr, "1992/12/09")
                .vali(isUrl, "http://baidu.com")
                .doCheck()
                .ifNotPassedThrowException()
                .getFailedCounts();
        System.out.println(failedCounts2);
        System.out.println(validateBuilder.getSuccedCounts());

    }
}
