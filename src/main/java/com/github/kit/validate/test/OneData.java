package com.github.kit.validate.test;

import com.github.kit.validate.annations.AbcValidate;
import com.github.kit.validate.enums.Check;

/**
 * @author lq
 * @version 1.0
 * @date 2020/6/16 11:59
 */

public class OneData {


    @AbcValidate(required = true, fun = Check.Custom, express = "nameValidater")
    private String name;
    @AbcValidate(required = true)
    private Integer age;
    @AbcValidate(required = true, fun = Check.le, express = "1")
    private Integer sex;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }
}
