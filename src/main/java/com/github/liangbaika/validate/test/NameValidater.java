package com.github.liangbaika.validate.test;

import com.github.liangbaika.validate.core.ParamValidator;
import org.springframework.stereotype.Component;

/**
 * 自定义验证器 复杂逻辑校验
 *
 * @author lq
 * @version 1.0
 * @date 2020/6/16 12:01
 */
@Component
public class NameValidater implements ParamValidator {

    @Override
    public Boolean validate(Object value) {
        String name = (String) value;
        if (name.startsWith("张") && name.length() == 3) {
            return true;
        }
        return false;
    }
}
