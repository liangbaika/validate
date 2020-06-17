package com.github.liangbaika.validate.config;

import com.github.liangbaika.validate.core.CheckParamAop;
import com.github.liangbaika.validate.utils.SpringContextHolder;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * springboot stater 自动装配
 *
 * @author liangbaikai
 * @version 0.1.0
 * @date 2020/5/17 14:23
 */
@Configuration
@Import({CheckParamAop.class, SpringContextHolder.class})
public class SpringValidateAutoConfig {
}
