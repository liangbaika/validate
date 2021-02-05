package com.github.liangbaika.validate.test;

import com.github.liangbaika.validate.core.ValidateBuilder;
import org.junit.Test;

import static com.github.liangbaika.validate.enums.Check.*;

/**
 * 测试
 *
 * @author lq
 * @version 1.0
 * @date 2021/1/29 21:44
 */
public class Tests {

    @Test
    public void testValidateBuilder() {
        ValidateBuilder validateBuilder = ValidateBuilder.build();
        int failedCounts = validateBuilder
                .vali(ne, "3", "3", "不能等于")
                .vali(Chinese, "测试中文")
                .vali(isBirthdaystr, "1992-12-09")
                .vali(isUrl, "https://baidu.com")
                .doCheck()
                .getFailedCounts();
        assert failedCounts == 1;
        assert validateBuilder.getSuccedCounts() == 3;

    }

}
