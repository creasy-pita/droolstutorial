package com.function;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by lujq on 2/2/2021.
 */
public class BooleanUtil {
    public static boolean Compare(String leftValue,String rightValue, String sjlx, String compareOperator){
        //todo 考虑所有操作符 "==",">","<",">=","<=","!=","!=null"不为空,"==null"为空,"==true"为真,"==false"为假,"==empty"为空（含空串),"!=empty"不为空（含空串）,包含
        if("java.lang.String".equals(sjlx)){
            if ("==".equals(compareOperator)) {
                return leftValue.compareTo(rightValue) == 0;
            }else if("!=null".equals(compareOperator)){
                return leftValue != null;
            }else if("!=empty".equals(compareOperator)){
                return StringUtils.isNotEmpty(leftValue);
            }else if(">".equals(compareOperator)){
                return leftValue.compareTo(rightValue) > 0;
            }else if("<".equals(compareOperator)){
                return leftValue.compareTo(rightValue) < 0;
            }else if(">=".equals(compareOperator)){
                return leftValue.compareTo(rightValue) >= 0;
            }else if("<=".equals(compareOperator)){
                return leftValue.compareTo(rightValue) <= 0;
            }else if("!=".equals(compareOperator)){
                return leftValue.compareTo(rightValue) != 0;
            }else if("==null".equals(compareOperator)){
                return leftValue == null;
            }else if("==empty".equals(compareOperator)){
                return StringUtils.isEmpty(leftValue);
            }else if("contains".equals(compareOperator)){
                return leftValue.indexOf(rightValue) > 0;
            }else {
                throw new RuntimeException("比较操作不合法");
            }
        }
        else if("java.lang.Boolean".equals(sjlx)){
            if ("==true".equals(compareOperator)) {
                return "true".equals(leftValue);
            }else if("==false".equals(compareOperator)){
                return "false".equals(leftValue);
            }else {
                throw new RuntimeException(String.format("java.lang.Boolean类型的数据不能使用比较操作【%s】",compareOperator));
            }
        }
        else if(DroolsUtil.ValueTypeList.contains(sjlx)){
            int re = 0;
            if (StringUtils.isEmpty(rightValue)) {
                throw new RuntimeException(String.format("数值类型的比较操作【%s】未实现",compareOperator));
//                return false;
            }
            if("java.lang.Integer".equals(sjlx)){
                re = new Integer(leftValue).compareTo(new Integer(rightValue));
            }
            else if("java.math.BigDecimal".equals(sjlx)){
                re = new BigDecimal(leftValue).compareTo(new BigDecimal(rightValue));
            }
            else if("java.lang.Long".equals(sjlx)){
                re = new Long(leftValue).compareTo(new Long(rightValue));
            }
            else if("java.lang.Float".equals(sjlx)){
                re = new Float(leftValue).compareTo(new Float(rightValue));
            }
            else if("java.lang.Double".equals(sjlx)){
                re = new Double(leftValue).compareTo(new Double(rightValue));
            }
            else if("java.lang.Short".equals(sjlx)){
                re = new Short(leftValue).compareTo(new Short(rightValue));
            }
            else if("java.lang.Byte".equals(sjlx)){
                re = new Byte(leftValue).compareTo(new Byte(rightValue));
            }
            else if("java.math.BigInteger".equals(sjlx)){
                re = new BigInteger(leftValue).compareTo(new BigInteger(rightValue));
            }
            if ("==".equals(compareOperator)) {
                return re == 0;
            }else if(">".equals(compareOperator)){
                return re > 0;
            }else if("<".equals(compareOperator)){
                return re < 0;
            }else if(">=".equals(compareOperator)){
                return re >= 0;
            }else if("<=".equals(compareOperator)){
                return re <= 0;
            }else if("!=".equals(compareOperator)){
                return re != 0;
            }else if("==null".equals(compareOperator)){
                return leftValue == null;
            }else if("!=null".equals(compareOperator)){
                return leftValue != null;
            }else {
                throw new RuntimeException( String.format("数值类型的数据不能使用比较操作【%s】", compareOperator));
            }
        }
        else if("java.util.Date".equals(sjlx)){
            throw new RuntimeException("数据类型java.util.Date的比较操作未实现");
        }
        return false;
    }
}
