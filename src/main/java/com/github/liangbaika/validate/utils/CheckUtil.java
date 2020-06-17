package com.github.liangbaika.validate.utils;


import com.github.liangbaika.validate.core.ParamValidator;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.liangbaika.validate.utils.CheckUtil.RegexPattern.*;


/**
 * 验证方法
 *
 * @author liangbaiakai
 * @version 0.1.0
 * @date 2020/5/15 18:17
 */
public class CheckUtil {

    /**
     * 判断value == null
     *
     * @param value   字段值
     * @param express 这里不需要，只是为了参数统一
     * @return true or false
     */
    public static Boolean isNull(Object value, String express) {
        if (null != value) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }


    /**
     * 此方法比较特殊 用于自定义验证逻辑
     *
     * @param value    参数值
     * @param beanName 实现了Validator接口的具体验证器的bean名称，所以需要能在spring容器里找到,之所以不采用反射原因
     *                 运行时类加载,反射等带来的性能开销,而spring在启动阶段就进行扫描并管理了。
     * @return
     */
    public static Boolean customValidate(Object value, String beanName) {
        ParamValidator bean = SpringContextHolder.getBean(beanName);
        if (bean == null) {
            throw new IllegalArgumentException("非法的bean名称，无法在spring容器里找到此bean");
        }
        Function<Object, Boolean> func = bean::validate;
        return func.apply(value);
    }


    /**
     * 是否手机号
     *
     * @param value   参数值
     * @param express 空
     * @return
     */
    public static Boolean isMobilePhone(Object value, String express) {
        if (null == value) {
            return Boolean.FALSE;
        }
        return MOBILE.matcher(String.valueOf(value)).matches();
    }

    /**
     * 是否数字类型 包括小数
     *
     * @param value
     * @param express
     * @return
     */
    public static Boolean isNumber(Object value, String express) {
        if (null == value) {
            return Boolean.FALSE;
        }
        if (value instanceof Number) {
            return Boolean.TRUE;
        }
        return NUMBER_CODE.matcher(String.valueOf(value)).matches();
    }

    /**
     * 判断value != null
     *
     * @param value   字段值
     * @param express 这里不需要，只是为了参数统一
     * @return true or false
     */
    public static Boolean isNotNull(Object value, String express) {
        if (null == value) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }


    /**
     * 判断value ==null || length size <= 0
     * 支持字符串判断
     * 支持集合判断
     */
    public static Boolean isEmpty(Object value, String express) {
        return !isNotEmpty(value, express);
    }


    /**
     * 判断value !=null && length、size > 0
     * 支持字符串判断
     * 支持集合判断
     */
    public static Boolean isNotEmpty(Object value, String express) {
        if (isNull(value, express)) {
            return Boolean.FALSE;
        }
        if (value instanceof String && "".equals(((String) value).trim())) {
            return Boolean.FALSE;
        }
        if (value instanceof Collection && (value == null || ((Collection) value).size() == 0)) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }


    /**
     * 判断参数是否是 true
     * 支持Boolean类型
     * 支持String类型
     */
    public static Boolean isTrue(Object value, String express) {
        if (isNull(value, express)) {
            return Boolean.FALSE;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof String) {
            try {
                return Boolean.parseBoolean((String) value);
            } catch (Exception e) {
                return Boolean.FALSE;
            }
        }
        return Boolean.FALSE;
    }

    /**
     * 判断参数是否是 false
     * 支持Boolean类型
     * 支持String类型
     */
    public static Boolean isFalse(Object value, String express) {
        return !isTrue(value, express);
    }


    /**
     * 判断参数是否是一个日期
     * 支持Date类型
     * 支持LocalDate类型
     * 支持String类型，yyyy-MM-dd、yyyyMMdd、yyyy/MM/dd格式； 默认仅支持yyyy-MM-dd
     */
    public static Boolean isDate(Object value, String express) {
        if (isNull(value, express)) {
            return Boolean.FALSE;
        }
        if (value instanceof String) {      // 通常json格式参数，都是以字符串类型传递，优先判断
            // 验证参数，不能处理掉所有异常的符号
            // String v = ((String) value).trim().replaceAll("[-/\\s]", "");
            String v = ((String) value); //.replaceAll("[-/]", "");
            try {
                LocalDate.parse(v, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                return Boolean.TRUE;
            } catch (Exception e) {
                return Boolean.FALSE;
            }
        }
        if (value instanceof Date) {
            return Boolean.TRUE;
        }
        if (value instanceof LocalDate) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }


    /**
     * 判断参数是否是一个日期
     * 支持Date类型
     * 支持LocalDateTime类型
     * 支持String类型，yyyy-MM-dd HH:mm:ss、yyyyMMddHHmmss、yyyy/MM/dd HH:mm:ss格式； 默认仅支持yyyy-MM-dd HH:mm:ss
     */
    public static Boolean isDateTime(Object value, String express) {
        if (isNull(value, express)) {
            return Boolean.FALSE;
        }
        if (value instanceof String) {   // 通常json格式参数，都是以字符串类型传递，优先判断
            String v = ((String) value); //.replaceAll("[-/]", "");  // 验证参数，不能处理掉所有异常的符号
            try {
                LocalDateTime.parse(v, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                return Boolean.TRUE;
            } catch (Exception e) {
                /*try {
                    LocalDateTime.parse(v, DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
                    return Boolean.TRUE;
                } catch (Exception e1) {
                    return Boolean.FALSE;
                }*/
                return Boolean.FALSE;
            }
        }
        if (value instanceof Date) {
            return Boolean.TRUE;
        }
        if (value instanceof LocalDateTime) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * 判断参数是否是一个过去时间
     * 支持Date类型
     * 支持LocalDate类型
     * 支持LocalDateTime类型
     * 支持String类型，yyyy-MM-dd、yyyyMMdd、yyyy/MM/dd格式； 默认仅支持yyyy-MM-dd
     * 支持String类型，yyyy-MM-dd HH:mm:ss、yyyyMMddHHmmss、yyyy/MM/dd HH:mm:ss格式； 默认仅支持yyyy-MM-dd HH:mm:ss
     *
     * @param value
     * @param express
     * @return
     */
    public static Boolean isPast(Object value, String express) {
        if (isNull(value, express)) {
            return Boolean.FALSE;
        }
        if (value instanceof String) {   // 通常json格式参数，都是以字符串类型传递，优先判断
            String v = ((String) value); //.replaceAll("[-/]", "");  // 验证参数，不能处理掉所有异常的符号
            if (v.length() <= 10) { // 日期
                try {
                    LocalDate ld = LocalDate.parse(v, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    return LocalDate.now().isAfter(ld);
                } catch (Exception e) {
                    return Boolean.FALSE;
                }
            }
            try {
                LocalDateTime ldt = LocalDateTime.parse(v, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                return LocalDateTime.now().isAfter(ldt);
            } catch (Exception e) {
                /*try {
                    LocalDateTime ldt = LocalDateTime.parse(v, DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
                    return LocalDateTime.now().isAfter(ldt);
                } catch (Exception e1) {
                    return Boolean.FALSE;
                }*/
                return Boolean.FALSE;
            }
        }
        if (value instanceof Date) {
            return new Date().after((Date) value);
        }
        if (value instanceof LocalDate) {
            return LocalDate.now().isAfter((LocalDate) value);
        }
        if (value instanceof LocalDateTime) {
            return LocalDateTime.now().isAfter((LocalDateTime) value);
        }
        return Boolean.FALSE;
    }

    /**
     * 判断参数是否是一个将来时间
     * 支持Date类型
     * 支持LocalDate类型
     * 支持LocalDateTime类型
     * 支持String类型，yyyy-MM-dd、yyyyMMdd、yyyy/MM/dd格式; 默认仅支持yyyy-MM-dd
     * 支持String类型，yyyy-MM-dd HH:mm:ss、yyyyMMddHHmmss、yyyy/MM/dd HH:mm:ss格式； 默认仅支持yyyy-MM-dd HH:mm:ss
     *
     * @param value
     * @param express
     * @return
     */
    public static Boolean isFuture(Object value, String express) {
        if (isNull(value, express)) {
            return Boolean.FALSE;
        }
        if (value instanceof String) {   // 通常json格式参数，都是以字符串类型传递，优先判断
            String v = ((String) value); // .replaceAll("[-/]", "");  // 验证参数，不能处理掉所有异常的符号
            if (v.length() <= 10) { // 日期
                try {
                    LocalDate ld = LocalDate.parse(v, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    return LocalDate.now().isBefore(ld);
                } catch (Exception e) {
                    return Boolean.FALSE;
                }
            }
            try {
                LocalDateTime ldt = LocalDateTime.parse(v, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                return LocalDateTime.now().isBefore(ldt);
            } catch (Exception e) {
                /*try {
                    LocalDateTime ldt = LocalDateTime.parse(v, DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
                    return LocalDateTime.now().isBefore(ldt);
                } catch (Exception e1) {
                    return Boolean.FALSE;
                }*/
                return Boolean.FALSE;
            }
        }
        if (value instanceof Date) {
            return new Date().before((Date) value);
        }
        if (value instanceof LocalDate) {
            return LocalDate.now().isBefore((LocalDate) value);
        }
        if (value instanceof LocalDateTime) {
            return LocalDateTime.now().isBefore((LocalDateTime) value);
        }
        return Boolean.FALSE;
    }


    /**
     * 判断是否是今天的日期
     * 支持Date类型
     * 支持LocalDate类型
     * 支持String类型，默认仅支持yyyy-MM-dd
     *
     * @param value
     * @param express
     * @return
     */
    public static Boolean isToday(Object value, String express) {
        if (isNull(value, express)) {
            return Boolean.FALSE;
        }
        if (value instanceof String) {   // 通常json格式参数，都是以字符串类型传递，优先判断
            String v = ((String) value); // .replaceAll("[-/]", "");  // 验证参数，不能处理掉所有异常的符号
            try {
                LocalDate ld = LocalDate.parse(v, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                return LocalDate.now().equals(ld);
            } catch (Exception e) {
                return Boolean.FALSE;
            }
        }
        if (value instanceof Date) {
            return new Date().equals((Date) value);
        }
        if (value instanceof LocalDate) {
            return LocalDate.now().equals((LocalDate) value);
        }
        return Boolean.FALSE;
    }


    /**
     * 判断是否是邮箱
     * 使用正则表达式判断
     *
     * @param value
     * @param express
     * @return
     */
    public static Boolean isEmail(Object value, String express) {
        if (isNull(value, express)) {
            return Boolean.FALSE;
        }
        if (value instanceof String) {
            String regEx = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher((String) value);
            if (m.matches()) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    /**
     * 判断参数的取值范围，逗号隔开，无空格；闭区间
     * 支持Integer、Long、Short、Float、Double、BigDecimal
     *
     * @param value
     * @param rangeStr
     * @return
     */
    public static Boolean inRange(Object value, String rangeStr) {
        if (isNull(value, rangeStr)) {
            return Boolean.FALSE;
        }
        if (null == rangeStr || "".equals(rangeStr)) {
            return Boolean.FALSE;
        }
        if (value instanceof Integer) {
            Integer begin = Integer.valueOf(rangeStr.split(",")[0]);
            Integer end = Integer.valueOf(rangeStr.split(",")[1]);
            Integer v = ((Integer) value);
            return begin <= v && v <= end;
        }
        if (value instanceof Long) {
            Long begin = Long.valueOf(rangeStr.split(",")[0]);
            Long end = Long.valueOf(rangeStr.split(",")[1]);
            Long v = ((Long) value);
            return begin <= v && v <= end;
        }
        if (value instanceof Short) {
            Short begin = Short.valueOf(rangeStr.split(",")[0]);
            Short end = Short.valueOf(rangeStr.split(",")[1]);
            Short v = ((Short) value);
            return begin <= v && v <= end;
        }
        if (value instanceof Float) {
            Float begin = Float.valueOf(rangeStr.split(",")[0]);
            Float end = Float.valueOf(rangeStr.split(",")[1]);
            Float v = ((Float) value);
            return begin <= v && v <= end;
        }
        if (value instanceof Double) {
            Double begin = Double.valueOf(rangeStr.split(",")[0]);
            Double end = Double.valueOf(rangeStr.split(",")[1]);
            Double v = ((Double) value);
            return begin <= v && v <= end;
        }
        if (value instanceof BigDecimal) {
            BigDecimal begin = new BigDecimal(rangeStr.split(",")[0]);
            BigDecimal end = new BigDecimal(rangeStr.split(",")[1]);
            BigDecimal v = ((BigDecimal) value);
            return begin.compareTo(v) <= 0 && v.compareTo(end) <= 0;
        }

        return Boolean.FALSE;
    }


    /**
     * 等价于  !Range
     *
     * @param value
     * @param rangeStr
     * @return
     */
    public static Boolean outRange(Object value, String rangeStr) {
        return !inRange(value, rangeStr);
    }


    /**
     * 判断参数的取值范围，逗号隔开，无空格；闭区间
     * 判断String的length范围, rangeStr取值举例："6,18"
     *
     * @param value
     * @param rangeStr
     * @return
     */
    public static Boolean inLength(Object value, String rangeStr) {
        if (isNull(value, rangeStr)) {
            return Boolean.FALSE;
        }
        if (null == rangeStr || "".equals(rangeStr)) {
            return Boolean.FALSE;
        }
        if (value instanceof String) {
            Integer begin = null;
            Integer end = null;
            if (!rangeStr.contains(",")) {
                begin = 0;
            } else {
                begin = Integer.valueOf(rangeStr.split(",")[0]);
            }
            if (begin == 0) {
                end = Integer.valueOf(rangeStr);
            } else {
                end = Integer.valueOf(rangeStr.split(",")[1]);
            }
            Integer v = ((String) value).length();
            return begin <= v && v <= end;
        }
        return Boolean.FALSE;
    }


    /**
     * 判断参数是否在枚举的数据中, 枚举的表达式用 英文逗号隔开，无空格，如： "男,女,太监"
     * 校验过程，不在对表达式进行校验，所以请确保表达式的格式正确
     * 支持String
     * 支持Integer Short Long
     *
     * @param value
     * @param enumStr
     * @return
     */
    public static Boolean inEnum(Object value, String enumStr) {
        if (isNull(value, null)) {
            return Boolean.FALSE;
        }
        if (null == enumStr || "".equals(enumStr)) {
            return Boolean.FALSE;
        }
        String[] array = enumStr.split(",");
        Set<String> set = new HashSet<>(Arrays.asList(array));
        return set.contains(value.toString());
    }


    /**
     * 是否大于指定值
     * 支持Integer、Long、Short、Float、Double、BigDecimal
     * 支持String，判断length值
     * 支持Collection，判断size的值
     *
     * @param value
     * @param express
     * @return
     */
    public static Boolean isGreaterThan(Object value, String express) {
        if (value == null) {
            return Boolean.FALSE;
        }
        if (value instanceof Integer) {
            return ((Integer) value) > Integer.valueOf(express);
        }
        if (value instanceof Long) {
            return ((Long) value) > Long.valueOf(express);
        }
        if (value instanceof Short) {
            return ((Short) value) > Short.valueOf(express);
        }
        if (value instanceof Float) {
            return ((Float) value) > Float.valueOf(express);
        }
        if (value instanceof Double) {
            return ((Double) value) > Double.valueOf(express);
        }
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).compareTo(new BigDecimal(express)) > 0;
        }
        if (value instanceof String) {
            return ((String) value).length() > Integer.valueOf(express);
        }
        if (value instanceof Collection) {
            return ((Collection) value).size() > Integer.valueOf(express);
        }
        return Boolean.FALSE;
    }


    /**
     * 是否大于等于
     * 支持Integer、Long、Short、Float、Double、BigDecimal
     * 支持String，判断length值
     * 支持Collection，判断size的值
     *
     * @param value
     * @param express
     * @return
     */
    public static Boolean isGreaterThanEqual(Object value, String express) {
        if (value == null) {
            return Boolean.FALSE;
        }
        if (value instanceof Integer) {
            return ((Integer) value) >= Integer.valueOf(express);
        }
        if (value instanceof Long) {
            return ((Long) value) >= Long.valueOf(express);
        }
        if (value instanceof Short) {
            return ((Short) value) >= Short.valueOf(express);
        }
        if (value instanceof Float) {
            return ((Float) value) >= Float.valueOf(express);
        }
        if (value instanceof Double) {
            return ((Double) value) >= Double.valueOf(express);
        }
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).compareTo(new BigDecimal(express)) >= 0;
        }
        if (value instanceof String) {
            return ((String) value).length() >= Integer.valueOf(express);
        }
        if (value instanceof Collection) {
            return ((Collection) value).size() >= Integer.valueOf(express);
        }
        return Boolean.FALSE;

    }

    /**
     * 是否少于
     * 支持Integer、Long、Short、Float、Double、BigDecimal
     * 支持String，判断length值
     * 支持Collection，判断size的值
     *
     * @param value
     * @param express
     * @return
     */
    public static Boolean isLessThan(Object value, String express) {
        if (value == null) {
            return Boolean.FALSE;
        }
        if (value instanceof Integer) {
            return ((Integer) value) < Integer.valueOf(express);
        }
        if (value instanceof Long) {
            return ((Long) value) < Long.valueOf(express);
        }
        if (value instanceof Short) {
            return ((Short) value) < Short.valueOf(express);
        }
        if (value instanceof Float) {
            return ((Float) value) < Float.valueOf(express);
        }
        if (value instanceof Double) {
            return ((Double) value) < Double.valueOf(express);
        }
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).compareTo(new BigDecimal(express)) < 0;
        }
        if (value instanceof String) {
            return ((String) value).length() < Integer.valueOf(express);
        }
        if (value instanceof Collection) {
            return ((Collection) value).size() < Integer.valueOf(express);
        }
        return Boolean.FALSE;
    }

    /**
     * 是否少于等于
     * 支持Integer、Long、Short、Float、Double、BigDecimal
     * 支持String，判断length值
     * 支持Collection，判断size的值
     *
     * @param value
     * @param express
     * @return
     */
    public static Boolean isLessThanEqual(Object value, String express) {
        if (value == null) {
            return Boolean.FALSE;
        }
        if (value instanceof Integer) {
            return ((Integer) value) <= Integer.valueOf(express);
        }
        if (value instanceof Long) {
            return ((Long) value) <= Long.valueOf(express);
        }
        if (value instanceof Short) {
            return ((Short) value) <= Short.valueOf(express);
        }
        if (value instanceof Float) {
            return ((Float) value) <= Float.valueOf(express);
        }
        if (value instanceof Double) {
            return ((Double) value) <= Double.valueOf(express);
        }
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).compareTo(new BigDecimal(express)) <= 0;
        }
        if (value instanceof String) {
            return ((String) value).length() <= Integer.valueOf(express);
        }
        if (value instanceof Collection) {
            return ((Collection) value).size() <= Integer.valueOf(express);
        }
        return Boolean.FALSE;
    }

    /**
     * 判断是否 notEqual指定的值
     * 支持String、Integer、Long、Short、Float、Double、BigDecimal
     * 支持Collection，判断size的值
     *
     * @param value
     * @param express
     * @return
     */
    public static Boolean isNotEqual(Object value, String express) {
        return !isEqual(value, express);
    }


    /**
     * 判断是否Equal指定的值
     * 支持String、Integer、Long、Short、Float、Double、BigDecimal
     * 支持Collection，判断size的值
     *
     * @param value
     * @param express
     * @return
     */
    public static Boolean isEqual(Object value, String express) {
        if (value == null) {
            return Boolean.FALSE;
        }
        if (value instanceof String) {
            return ((String) value).equals(express);
        }
        if (value instanceof Integer) {
            return ((Integer) value).equals(Integer.valueOf(express));
        }
        if (value instanceof Long) {
            return ((Long) value).equals(Long.valueOf(express));
        }
        if (value instanceof Short) {
            return ((Short) value).equals(Short.valueOf(express));
        }
        if (value instanceof Float) {
            return ((Float) value).equals(Float.valueOf(express));
        }
        if (value instanceof Double) {
            return ((Double) value).equals(Double.valueOf(express));
        }
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).compareTo(new BigDecimal(express)) == 0;
        }
        if (value instanceof Collection) {
            return ((Collection) value).size() == Integer.valueOf(express);
        }
        return Boolean.FALSE;
    }


    /**
     * 判断String是否满足正则表达式
     *
     * @param value
     * @param regEx 正则表达式
     * @return
     */
    public static Boolean isPattern(Object value, String regEx) {
        if (isNull(value, null)) {
            return Boolean.FALSE;
        }
        if (value instanceof String) {
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher((String) value);
            if (m.matches()) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    /**
     * 是否是身份证号
     *
     * @param value
     * @param regEx
     * @return
     */
    public static Boolean isIDCard(Object value, String regEx) {
        if (value == null) {
            return Boolean.FALSE;
        }
        return CITIZEN_ID.matcher(String.valueOf(value)).matches();
    }

//
//    /**
//     * 是否是有效的统一社会信用代码
//     *
//     * @param value
//     * @param regEx
//     * @return
//     */
//    public static Boolean isCreditCode(Object value, String regEx) {
//        if (value == null) {
//            return Boolean.FALSE;
//        }
//        return Validator.isCreditCode(String.valueOf(value));
//    }

    /**
     * 是否是中国邮编
     *
     * @param value
     * @param regEx
     * @return
     */
    public static Boolean isChinesePostCode(Object value, String regEx) {
        if (value == null) {
            return Boolean.FALSE;
        }
        return ZIP_CODE.matcher(String.valueOf(value)).matches();
    }

    /**
     * 是否是Ipv4
     *
     * @param value
     * @param regEx
     * @return
     */
    public static Boolean isIpv4(Object value, String regEx) {
        if (value == null) {
            return Boolean.FALSE;
        }
        return IPV4.matcher(String.valueOf(value)).matches();
    }

    /**
     * 是否是Ipv6
     *
     * @param value
     * @param regEx
     * @return
     */
    public static Boolean isIpv6(Object value, String regEx) {
        if (value == null) {
            return Boolean.FALSE;
        }
        return IPV6.matcher(String.valueOf(value)).matches();
    }

    /**
     * 是否是汉字
     *
     * @param value
     * @param regEx
     * @return
     */
    public static Boolean isChinese(Object value, String regEx) {
        if (value == null) {
            return Boolean.FALSE;
        }
        return CHINESES.matcher(String.valueOf(value)).matches();
    }

    /**
     * 验证是否为英文字母 、数字和下划线
     *
     * @param value
     * @param regEx
     * @return
     */
    public static Boolean isGeneral(Object value, String regEx) {
        if (value == null) {
            return Boolean.FALSE;
        }
        return GENERAL.matcher(String.valueOf(value)).matches();
    }

    /**
     * 验证是否为MAC地址
     *
     * @param value
     * @param regEx
     * @return
     */
    public static Boolean isMac(Object value, String regEx) {
        if (value == null) {
            return Boolean.FALSE;
        }
        return MAC_ADDRESS.matcher(String.valueOf(value)).matches();
    }

    /**
     * 验证是否为中国车牌号
     *
     * @param value
     * @param regEx
     * @return
     */
    public static Boolean isPlateNumber(Object value, String regEx) {
        if (value == null) {
            return Boolean.FALSE;
        }
        return PLATE_NUMBER.matcher(String.valueOf(value)).matches();
    }

    /**
     * 验证是否为URL
     *
     * @param value
     * @param regEx
     * @return
     */
    public static Boolean isUrl(Object value, String regEx) {
        if (value == null) {
            return Boolean.FALSE;
        }
        try {
            new java.net.URL(String.valueOf(value));
        } catch (MalformedURLException e) {
            return false;
        }
        return true;
    }

    /**
     * 验证是否为UUID
     * 包括带横线标准格式和不带横线的简单模式
     *
     * @param value
     * @param regEx
     * @return
     */
    public static Boolean isUUID(Object value, String regEx) {
        if (value == null) {
            return Boolean.FALSE;
        }
        return RegexPattern.UUID.matcher(String.valueOf(value)).matches() || UUID_SIMPLE.matcher(String.valueOf(value)).matches();
    }

    /**
     * 验证是否为生日<br>
     * 只支持以下几种格式：
     * <ul>
     * <li>yyyyMMdd</li>
     * <li>yyyy-MM-dd</li>
     * <li>yyyy/MM/dd</li>
     * <li>yyyy.MM.dd</li>
     * <li>yyyy年MM月dd日</li>
     * </ul>
     *
     * @param value 值
     * @return 是否为生日
     */
    public static Boolean isBirthday(Object value, String regEx) {
        if (value == null) {
            return Boolean.FALSE;
        }
        final Matcher matcher = BIRTHDAY.matcher(String.valueOf(value));
        if (matcher.find()) {
            int year = Integer.parseInt(matcher.group(1));
            int month = Integer.parseInt(matcher.group(3));
            int day = Integer.parseInt(matcher.group(5));
            // 验证年
            int thisYear = new Date().getYear();
            if (year < 1900 || year > thisYear) {
                return false;
            }

            // 验证月
            if (month < 1 || month > 12) {
                return false;
            }

            // 验证日
            if (day < 1 || day > 31) {
                return false;
            }
            // 检查几个特殊月的最大天数
            if (day == 31 && (month == 4 || month == 6 || month == 9 || month == 11)) {
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * 时间毫秒值
     *
     * @param value
     * @param regEx
     * @return
     */
    public static Boolean isTimeMillSeconds(Object value, String regEx) {
        Boolean number = isNumber(value, regEx);
        if (number) {
            if (String.valueOf(value).length() == 13) {
                return true;
            }
        }
        return false;

    }

    public static class RegexPattern {
        /**
         * 英文字母 、数字和下划线
         */
        public final static Pattern GENERAL = Pattern.compile("^\\w+$");
        /**
         * 数字
         */
        public final static Pattern NUMBERS = Pattern.compile("\\d+");
        /**
         * 字母
         */
        public final static Pattern WORD = Pattern.compile("[a-zA-Z]+");
        /**
         * 单个中文汉字
         */
        public final static Pattern CHINESE = Pattern.compile("[\u4E00-\u9FFF]");
        /**
         * 中文汉字
         */
        public final static Pattern CHINESES = Pattern.compile("[\u4E00-\u9FFF]+");
        /**
         * 分组
         */
        public final static Pattern GROUP_VAR = Pattern.compile("\\$(\\d+)");
        /**
         * IP v4
         */
        public final static Pattern IPV4 = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
        /**
         * IP v6
         */
        public final static Pattern IPV6 = Pattern.compile("(([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]+|::(ffff(:0{1,4})?:)?((25[0-5]|(2[0-4]|1?[0-9])?[0-9])\\.){3}(25[0-5]|(2[0-4]|1?[0-9])?[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1?[0-9])?[0-9])\\.){3}(25[0-5]|(2[0-4]|1?[0-9])?[0-9]))");
        /**
         * 货币
         */
        public final static Pattern MONEY = Pattern.compile("^(\\d+(?:\\.\\d+)?)$");
        /**
         * 邮件，符合RFC 5322规范，正则来自：http://emailregex.com/
         */
        // public final static Pattern EMAIL = Pattern.compile("(\\w|.)+@\\w+(\\.\\w+){1,2}");
        public final static Pattern EMAIL = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])", Pattern.CASE_INSENSITIVE);
        /**
         * 移动电话
         */
        public final static Pattern MOBILE = Pattern.compile("(?:0|86|\\+86)?1[3456789]\\d{9}");
        /**
         * 18位身份证号码
         */
        public final static Pattern CITIZEN_ID = Pattern.compile("[1-9]\\d{5}[1-2]\\d{3}((0\\d)|(1[0-2]))(([012]\\d)|3[0-1])\\d{3}(\\d|X|x)");
        /**
         * 邮编
         */
        public final static Pattern ZIP_CODE = Pattern.compile("[1-9]\\d{5}(?!\\d)");
        /**
         * 生日
         */
        public final static Pattern BIRTHDAY = Pattern.compile("^(\\d{2,4})([/\\-.年]?)(\\d{1,2})([/\\-.月]?)(\\d{1,2})日?$");
        /**
         * URL
         */
        public final static Pattern URL = Pattern.compile("[a-zA-z]+://[^\\s]*");
        /**
         * Http URL
         */
        public final static Pattern URL_HTTP = Pattern.compile("(https://|http://)?([\\w-]+\\.)+[\\w-]+(:\\d+)*(/[\\w- ./?%&=]*)?");
        /**
         * 中文字、英文字母、数字和下划线
         */
        public final static Pattern GENERAL_WITH_CHINESE = Pattern.compile("^[\u4E00-\u9FFF\\w]+$");
        /**
         * UUID
         */
        public final static Pattern UUID = Pattern.compile("^[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}$");
        /**
         * 不带横线的UUID
         */
        public final static Pattern UUID_SIMPLE = Pattern.compile("^[0-9a-z]{32}$");
        /**
         * MAC地址正则
         */
        public static final Pattern MAC_ADDRESS = Pattern.compile("((?:[A-F0-9]{1,2}[:-]){5}[A-F0-9]{1,2})|(?:0x)(\\d{12})(?:.+ETHER)", Pattern.CASE_INSENSITIVE);
        /**
         * 16进制字符串
         */
        public static final Pattern HEX = Pattern.compile("^[a-f0-9]+$", Pattern.CASE_INSENSITIVE);
        /**
         * 时间正则
         */
        public static final Pattern TIME = Pattern.compile("\\d{1,2}:\\d{1,2}(:\\d{1,2})?");
        /**
         * 中国车牌号码（兼容新能源车牌）
         */
        public final static Pattern PLATE_NUMBER = Pattern.compile(
                //https://gitee.com/loolly/hutool/issues/I1B77H?from=project-issue
                "^(([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z](([0-9]{5}[ABCDEFGHJK])|([ABCDEFGHJK]([A-HJ-NP-Z0-9])[0-9]{4})))|" +
                        //https://gitee.com/loolly/hutool/issues/I1BJHE?from=project-issue
                        "([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领]\\d{3}\\d{1,3}[领])|" +
                        "([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z][A-HJ-NP-Z0-9]{4}[A-HJ-NP-Z0-9挂学警港澳使领]))$");


        /**
         * 社会统一信用代码
         * <pre>
         * 第一部分：登记管理部门代码1位 (数字或大写英文字母)
         * 第二部分：机构类别代码1位 (数字或大写英文字母)
         * 第三部分：登记管理机关行政区划码6位 (数字)
         * 第四部分：主体标识码（组织机构代码）9位 (数字或大写英文字母)
         * 第五部分：校验码1位 (数字或大写英文字母)
         * </pre>
         */
        public static final Pattern CREDIT_CODE = Pattern.compile("^[0-9A-HJ-NPQRTUWXY]{2}\\d{6}[0-9A-HJ-NPQRTUWXY]{10}$");

        /**
         * 数字
         */
        public static final Pattern NUMBER_CODE = Pattern.compile("\\d+(\\.\\d+)?");

    }
}
