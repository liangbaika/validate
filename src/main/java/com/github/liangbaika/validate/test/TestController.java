package com.github.liangbaika.validate.test;

import com.github.liangbaika.validate.annations.ValidateParam;
import com.github.liangbaika.validate.annations.ValidateParams;
import com.github.liangbaika.validate.enums.Check;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author lq
 * @version 1.0
 * @date 2020/6/16 11:56
 */
@RestController
@RequestMapping("/test")
public class TestController {


    /**
     * 单字段简单验证
     *
     * @param name
     * @return
     */
    @GetMapping("test0")
    @ValidateParam(value = Check.Length, argName = "name", express = "2,6")
    public Object test0(String name) {
        return Boolean.TRUE;
    }

    /**
     * 多字段验证
     *
     * @param name
     * @param age
     * @param idcard
     * @return
     */
    @ValidateParams(
            value = {
                    @ValidateParam(value = Check.NotEmpty, argName = "name"),
                    @ValidateParam(value = Check.Number, argName = "age"),
                    @ValidateParam(value = Check.isIDCard, argName = "idcard"),
            }
    )
    @GetMapping("test1")
    public Object test1(String name, Integer age, Integer idcard) {
        return Boolean.TRUE;
    }

    /**
     * 实体验证 javax-validation验证方式
     *
     * @param oneData
     * @return
     */
    @PostMapping("test2")
    public Object test2(@RequestBody @Valid OneData oneData) {
        return oneData;
    }


    /**
     * 对象多级验证，混合验证
     *
     * @param oneData
     * @return
     */
    @PostMapping("test3")
    @ValidateParams(
            value = {
                    @ValidateParam(value = Check.NotEmpty, argName = "oneData.name"),
                    @ValidateParam(value = Check.Number, argName = "oneData.age"),
            }
    )
    public Object test3(@RequestBody @Valid OneData oneData) {
        return oneData;
    }


    /**
     * 自定义验证
     *
     * @param oneData
     * @return
     */
    @PostMapping("test4")
    @ValidateParam(value = Check.Custom, argName = "oneData.name", express = "nameValidater")
    public Object test4(@RequestBody OneData oneData) {
        return oneData;
    }

}
