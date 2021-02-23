package com.function;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lujq on 1/29/2021.
 */
public class DroolsUtil {
    public static final String BIG_OBJ_KEYS = "bigObjKeys";
    public static final String OBJ_KEYS = "objKeys";
    public static final String VALUES_FOR_KEYS = "valuesForKeys";
    public static final String SUB_SEPARATOR = "!@@!";
    public static final String PATTERN_SEPARATOR = "-@@-";
    public static final String RULENAME_LIST_KEY = "drlRuleNameList";
    /**
     * 用于集合属性比较的动作方法中数值类型值的比较
     */
    public static List<String> ValueTypeList = new ArrayList<>();

    static {
        ValueTypeList.add("java.lang.Integer");
        ValueTypeList.add("java.math.BigDecimal");
        ValueTypeList.add("java.lang.Long");
        ValueTypeList.add("java.lang.Short");
        ValueTypeList.add("java.lang.Float");
        ValueTypeList.add("java.lang.Double");
        ValueTypeList.add("java.lang.Byte");
        ValueTypeList.add("java.math.BigInteger");
    }
}
