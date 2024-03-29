# validate-spring-boot-starter  

# latest=1.2.2  


#  中央仓库
```
        maven 
        <dependency>
            <groupId>com.github.liangbaika</groupId>
            <artifactId>validate-spring-boot-starter</artifactId>
            <version>{latest}</version>
        </dependency>
        
        gradle
        compile group: 'com.github.liangbaika', name: 'validate-spring-boot-starter', version: '{latest}'
```

# validate-springboot-starter 简介 （desc）
是一个validate-spring-boot-starter,与springboot框架无缝集成的灵活丰富的验证框架。
完全兼容javax-validation 和 hibernate Validation，并比他们更灵活 简单 强大
versus springboot Framework for seamless integration of verification framework.
 
# 优点（advantages）
1.  内置常用验证 
2.  对 javax validation 和hibernate-validate **完全兼容**
3.  支持验证Bean可**重用**
4.  支持条件分组 支持自定义验证器 （自定义**更简单强大**，可做到和业务**完全解耦**）
5.  多条件操作符
6.  聚合功能 
7.  错误信息提示**更加友好** 更准确
8.  **简单 灵活 无脑**

常用验证：比如手机号验证，正则验证，ip,邮箱，长度，范围，数字，小数，中国车牌号，身份证，长度，
url, 图书ISBN编号,文件后缀,文件大小 等常用验证。

Integrated with a lot of authentication, such as phone number authentication, regular authentication, email,
Numbers, decimals, license plate number, ID card, length, URL,ISBN and so on.Our beans are reusable and have more 
flexibility than Javax Validation.

# 自定义(custom)
只需要实现 **_ParamValidator_** 接口就好了，便可以处理复杂的验证，和业务代码完全解耦(你需要让这个实现接口的Bean**被Spring容器托管**);
All you need to do is implement the ParamValidator interface, which handles complex validation and is completely
decoupled from the business code (you need to have the Bean that implements the interface hosted by the Spring container).

# 注意(attention)
1. 如果非法参数将抛出 **_ParamsInValidException_**，您应该捕获这个特殊的异常并解决它。
如果采用的是jsr303型即javax-validation验证 则需要自行处理异常(**org.springframework.web.bind.MethodArgumentNotValidException**)。
对象多级验证时 支持无限级 如 'user.tokenObj.value' （0.7版本开始支持无限级 之前只支持2级）

2. ValidateParam里的express字段使用，一般情况下 可以为空，
 当value是Custem时，express=validateBeanName, 有的验证方法需要多个值 逗号分隔即可。
 反正注意的是 此express字段和验证的value方法息息相关.

3. 用到了JDK8的新特性,因此JDK版本需要大于等于 **1.8** 


if illegal paramas then will throw  ParamsValidException, and you should catch this special exception  
and resolve it. 
 
# 更新记录
 更新记录,请查看changelog.md文件
 
**_JDK>=1.8_**

# 快速开始（quick start）

## web
```
package com.github.liangbaika.validate.test;

import com.github.liangbaika.validate.annations.ValidateParam;
import com.github.liangbaika.validate.annations.ValidateParams;
import com.github.liangbaika.validate.enums.Check;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author liangbaikai
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
    @ValidateParam(value = Check.Length, argName = "name", express = "2,6",msg="自定义提示信息")
    public Object test0(String name) {
        return Boolean.TRUE;
    }

    /**
     * 多字段验证
     * 建议 如果字段过多（超过三个）使用自定义方式 更简洁
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
     * 0.7 版本开始支持无限级 0.7版本之前的只支持2级
     * 0.8.8开始支持重复注解 
     * @param oneData
     * @return
     */
    @PostMapping("test3")
    @ValidateParam(value = Check.NotEmpty, argName = "oneData.name")
    @ValidateParam(value = Check.Number, argName = "oneData.age")
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
    
     @GetMapping("/test6")
    @ValidateParam(required = false, value = Check.NotNull, argName = "age")
    public RestResponse test6(String name, Integer age) {
        return RestResponse.success();
    }
    
    @GetMapping("/test8")
    @ValidateParams(
            value = {
                    @ValidateParam(value = Check.NotNull, argName = "name"),
                    @ValidateParam(required = false, value = Check.NotNull, argName = "age")
            },
            anded = false
    )
    public RestResponse test8(String name, Integer age) {
        return RestResponse.success();
    }
}


package com.github.liangbaika.validate.test;

import com.github.liangbaika.validate.annations.AbcValidate;
import com.github.liangbaika.validate.enums.Check;

/**
 * @author liangbaikai
 * @version 1.0
 * @date 2020/6/16 11:59
 */

public class OneData {
    
     //自定义的时候 fun=Custom；express= bean名字 
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






package com.github.liangbaika.validate.test;

import com.github.liangbaika.validate.core.ParamValidator;
import org.springframework.stereotype.Component;

/**
 * 自定义验证器 模拟复杂逻辑校验
 * 需要被spring容器管理
 * @author liangbaikai
 * @version 1.0
 * @date 2020/6/16 12:01
 */
@Component
public class NameValidater implements ParamValidator {
    
    // 也可以在这里面抛出 com.github.liangbaika.validate.exception.ParamsInValidException 特定异常
    
    @Override
    public Boolean validate(Object value) {
        String name = (String) value;
        if (name.startsWith("张") && name.length() == 3) {
            return true;
        }
        return false;
    }
    
    
}

```

## 普通代码形式


```
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
                .vali(ne, "测试不等", "测试不等")
                .vali(Chinese, "测试中文")
                .vali(isBirthdaystr, "1992-12-09")
                .vali(isUrl, "https://baidu.com")
                .doCheck()
                .getFailedCounts();
        System.out.println(failedCounts);
        System.out.println(validateBuilder.isPassed());
        System.out.println(validateBuilder.getSuccedCounts());
        System.out.println(validateBuilder.getFailedMsgs());

        // 重复使用 validateBuilder  先调用clear方法
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

```

#  参数异常错误统一处理( resolve error)
 ```
 @ControllerAdvice
 public class GlobalExceptionHandler {
 
     private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
 
      @ExceptionHandler({ParamsInValidException.class, MethodArgumentNotValidException.class, BindException.class})
      @ResponseBody
      public RestResponse handleParamsValidException(Exception e) {
          if (e instanceof ParamsInValidException) {
              return RestResponse.error(new ErrorCode("500001", e.getMessage()));
          } else {
              BindingResult bindingResult = null;
              if (e instanceof BindException) {
                  bindingResult = ((BindException) e).getBindingResult();
              } else {
                  bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
              }
              StringBuilder errMsg = getErrorInfo(bindingResult);
              return RestResponse.error(new ErrorCode("500001", errMsg.toString()));
          }
      }
    
    
      private StringBuilder getErrorInfo(BindingResult bindingResult) {
          List<ObjectError> allErrors = bindingResult.getAllErrors();
          StringBuilder errMsg = new StringBuilder();
          for (ObjectError allError : allErrors) {
              FieldError fieldError = (FieldError) allError;
              String field = fieldError.getField();
              String defaultMessage = fieldError.getDefaultMessage();
              errMsg.append(field + ":" + defaultMessage + ";  ");
          }
          return errMsg;
      }
     
    
 }

```


# 欢迎使用 （Thanks and Suggestions）
此项目采用 Apache License协议, 欢迎大家使用或提出建议或贡献代码 
如果需要PR，请遵守标准提PR的那套标准原则。
You are welcome to use or suggest or contribute code
If PR is required, please follow the set of standard principles mentioned in the standard.
