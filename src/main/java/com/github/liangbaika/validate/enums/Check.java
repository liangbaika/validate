package com.github.liangbaika.validate.enums;


import com.github.liangbaika.validate.utils.CheckUtil;

import java.util.function.BiFunction;

/**
 * 验证方法 枚举
 *
 * @author liangbaikai
 * @version 0.1.0
 * @date 2020/5/15 18:17
 */
public enum Check {
    /**
     * 特殊 用于自定义验证器逻辑   此时 express = beanName
     */
    Custom("参数验证不通过", CheckUtil::customValidate),

    Null("参数必须为空", CheckUtil::isNull),

    NotNull("参数必须不为空", CheckUtil::isNotNull),

    Empty("参数的必须为空", CheckUtil::isEmpty),

    NotEmpty("参数必须非空", CheckUtil::isNotEmpty),

    True("参数必须为 true", CheckUtil::isTrue),

    False("参数必须为 false", CheckUtil::isFalse),

    Date("参数必须是一个日期 yyyy-MM-dd", CheckUtil::isDate),

    DateTime("参数必须是一个日期时间 yyyy-MM-dd HH:mm:ss", CheckUtil::isDateTime),

    TimeMillSeconds("参数必须是一个时间毫秒值", CheckUtil::isTimeMillSeconds),

    Past("参数必须是一个过去的日期", CheckUtil::isPast),

    Future("参数必须是一个将来的日期", CheckUtil::isFuture),

    Today("参数必须今天的日期", CheckUtil::isToday),

    Enum("参数必须在枚举中", CheckUtil::inEnum),

    Email("参数必须是Email地址", CheckUtil::isEmail),

    MobilePhone("参数必须是手机号", CheckUtil::isMobilePhone),

    Number("参数必须是数字类型", CheckUtil::isNumber),

    Range("参数必须在合适的范围内", CheckUtil::inRange),

    NotIn("参数必须不在指定的范围内", CheckUtil::outRange),

    Length("参数长度必须在指定范围内", CheckUtil::inLength),

    gt("参数必须大于指定值", CheckUtil::isGreaterThan),

    lt("参数必须小于指定值", CheckUtil::isLessThan),

    ge("参数必须大于等于指定值", CheckUtil::isGreaterThanEqual),

    le("参数必须小于等于指定值", CheckUtil::isLessThanEqual),

    ne("参数必须不等于指定值", CheckUtil::isNotEqual),

    Equal("参数必须不等于指定值", CheckUtil::isEqual),

    Pattern("参数必须符合指定的正则表达式", CheckUtil::isPattern),

    Chinese("参数必须是汉字", CheckUtil::isChinese),

    isUrl("参数必须是url", CheckUtil::isUrl),

    isISBN("参数必须是一个书籍ISBN编号", CheckUtil::isISBN),

    isChinesePostCode("参数必须是中国邮编", CheckUtil::isChinesePostCode),

    isPlateNumber("参数必须是中国车牌号", CheckUtil::isPlateNumber),

    isUUID("参数必须是UUID", CheckUtil::isUUID),

    isIpv4("参数必须是ipv4", CheckUtil::isIpv4),

    isIpv6("参数必须是ipv6", CheckUtil::isIpv6),

    isMac("参数必须是mac地址", CheckUtil::isMac),

    isIDCard("参数必须是身份证", CheckUtil::isIDCard),

    isGeneral("参数必须是英文字母,数字和下划线", CheckUtil::isGeneral),

    isBirthdaystr("参数必须是生日字符串格式", CheckUtil::isBirthday);


    /**
     * msg 信息
     */
    public String msg;

    /**
     * BiFunction：接收字段值(Object)和 表达式(String)，返回是否符合规则(Boolean)
     */
    public BiFunction<Object, String, Boolean> fun;

    Check(String msg, BiFunction<Object, String, Boolean> fun) {
        this.msg = msg;
        this.fun = fun;
    }
}