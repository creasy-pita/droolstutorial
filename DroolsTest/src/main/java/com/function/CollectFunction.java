package com.function;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by lujq on 1/22/2021.
 */
public class CollectFunction {
    /**
     * 是否为正整数
     */
    public Boolean isPositive(Object object){
        if(object==null){
            return false;
        }
        Long value ;
        try {
            value = Long.valueOf(object.toString());
        }catch (Exception e){
            return false;
        }
        if(value>0){return true;}
        else{return false;}
    }

    public java.lang.Boolean arr(com.alibaba.fastjson.JSONObject gObj,List<String> namePathList, List<String> sjlxList,String compareOperator,String rightOperand){
        //boys.scoreReports.score>99 boys.scoreReports.score<2
        boolean result = true;
        gObj.put("lamadasetvalue",true);
        //第一层是  java.util.List 所以 使用getjsonarray
        JSONArray boys = gObj.getJSONArray("boys");
        //第二层是  java.util.List 所以 使用getjsonarray


        List<JSONArray> scores = boys.stream().map(f -> ((JSONObject) f).getJSONArray("scoreReports")).collect(Collectors.toList());

        JSONArray scoreReports = boys.stream().map(f -> ((JSONObject) f).getJSONArray("scoreReports"))
                .reduce(//组合成一个JSONArray
                        (jsonArr1, jsonArr2) -> {
                            jsonArr1.addAll(jsonArr2);
                            return jsonArr1;
                        }
                )
                .get();
        scoreReports
                .stream()
                .filter(f -> {
                    String a="1";
                    boolean re = false;
                    Integer score = ((JSONObject) f).getInteger("score");
//                    re = score >99 || score <2;
                    //这里取组合复合表达式
                    re = score < Integer.parseInt(rightOperand);
                    if(!re){
                        gObj.put("lamadasetvalue",false);
                        System.out.println(String.format("当前值%d不符合内容",score ));
                    }
                    return  re;
                }).count();
        return gObj.getBoolean("lamadasetvalue");
    }


    /**
     *
     * @param gObj  以 boys.scoreReports.score > 90 and boys.scoreReports.score < 50为例
     * @param keysStr  比如例子中的【boys.scoreReports.score 】
     * @param sjlxsStr 比如例子中的【java.util.List,java.util.List,java.lang.Integer】
     * @param compareOperatorStr 模式的比较符 比如例子中的 [>,<]
     * @param rightOperandStr  右操作数 比如例子中的 [90,50]
     * @param boolOpStr  模式间的布尔操作符 比如例子中的 [and]
     * @param ruleName  规则名称 when中不能通过drools api访问，所以只能解析并通过参数参入
     * @return
     */
    public java.lang.Boolean collectionHandle(com.alibaba.fastjson.JSONObject gObj,String keysStr, String sjlxsStr,String compareOperatorStr,String rightOperandStr,String boolOpStr,String ruleName){
        String[] keyList = keysStr.split("\\.");
        String[] sjlxList = sjlxsStr.split(",");
        if(keyList.length<2)return false;
        //主键路径 取前面部分
        String objKeyPath = keysStr.substring(0, keysStr.lastIndexOf("."));
        Map<String ,Boolean> resultMap = new HashMap<>();
        resultMap.put("result",true);
        //object.attribute   不属于本范围  因为没有midstxxzdbsm
        //按key层次和各层次的数据类型解析获取最底层的实体
        Object currentObj = gObj;
        for (int keyIndex = 0; keyIndex < keyList.length; keyIndex++) {
            String sjlx = sjlxList[keyIndex];
            //java.lang.Object
            if ("java.lang.Object".equals(sjlx)) {
                String key = keyList[keyIndex];
                if(currentObj instanceof JSONObject){
                    //JSONObject中取JSONObject
                    currentObj = ((JSONObject) currentObj).getJSONObject(key);
                }else {
                    //JSONArray 中取JSONObject   List<jsonobject>
                    List<JSONObject> collect = ((JSONArray) currentObj).stream().map(f -> ((JSONObject) f).getJSONObject(key))
                            .collect(Collectors.toList());
                    JSONArray  newJArr = new JSONArray();
                    newJArr.addAll(collect);
                    currentObj = newJArr;
                }
            }
            //java.util.List
            else if("java.util.List".equals(sjlx))  {
                String key = keyList[keyIndex];
                if(currentObj instanceof JSONObject){
                    //JSONObject中取JSONArray
                    currentObj = ((JSONObject) currentObj).getJSONArray(key);
                }else {
                    //JSONArray中取JSONArray
                    currentObj = ((JSONArray) currentObj).stream().map(f -> ((JSONObject) f).getJSONArray(key))
                            .reduce((jsonArr1,jsonArr2) ->{
                                //jsonArr1.addAll(jsonArr2); //此做法会引起 $obj jsonobject 解析出现$ref的问题
                                JSONArray newJa = new JSONArray();
                                newJa.addAll(jsonArr1);
                                newJa.addAll(jsonArr2);
                                return  newJa;
                            }).orElse(null);
                }
            }
            else {
                break;
            }
        }
        if(currentObj == null)return false;
        String keyName = keyList[keyList.length - 1];
        String sjlx = sjlxList[sjlxList.length - 1];
        String[] compareOperatorList = compareOperatorStr.split(",");
        String[] rightOperandList = rightOperandStr.split(",");
        String[] boolOpList = boolOpStr.split(",");
        JSONArray valuesForKeys = new JSONArray();
        List<String> bigObjKeys = getObjKeys(gObj, com.function.DroolsUtil.OBJ_KEYS, objKeyPath);
        List<String> objKeys = getObjKeys(gObj, com.function.DroolsUtil.OBJ_KEYS,null);
        if (currentObj instanceof JSONObject) {

        }
        else if (currentObj instanceof JSONArray) {
            ((JSONArray) currentObj).stream().filter( f -> {
                        //todo 处理如下的复合表达式 ,先处理第一种不带先后顺序的
                        //  boolexpression1 ||boolexpression2 || boolexpression3
                        //  boolexpression1 || (boolexpression2 && boolexpression3)
                        boolean re = true;
                        for (int i = 0; i < compareOperatorList.length; i++) {
                            String compareOperator = compareOperatorList[i];
                            String rightOperand = rightOperandList[i];
                            if(i == 0){
                                re = re&&Compare(((JSONObject) f).getString(keyName), rightOperand, sjlx, compareOperator);
                            }else
                            {
                                String boolOp = boolOpList[i - 1];
                                if("and".equals(boolOp.toLowerCase())){
                                    re = re&&Compare(((JSONObject) f).getString(keyName), rightOperand, sjlx, compareOperator);
                                }else {
                                    re = re||Compare(((JSONObject) f).getString(keyName), rightOperand, sjlx, compareOperator);
                                }
                            }
                        }
                        if(!re){
                            if (bigObjKeys != null) {
                                JSONObject valuesForKey = new JSONObject();
                                valuesForKeys.add(valuesForKey);
                                for (String objKey : bigObjKeys) {
                                    valuesForKey.put(objKey.toString(), ((JSONObject) f).getString(objKey.toString()));
                                }
                            }
                            resultMap.put("result", false);
                        }
                        return  re;
                    }
            ).count();
        }
        Boolean result = resultMap.get("result");
        if (!result) {
            JSONObject objKeyPathObj = new JSONObject();
            //todo 验证其他arrv2 调用会不会覆盖gObj中当前的 objKeyPath的内容
            //放入内层主键信息
            objKeyPathObj.put(objKeyPath, valuesForKeys);
            //放入外层的主键信息
            if (objKeys != null) {
                for (String objKey : objKeys) {
                    objKeyPathObj.put(objKey,gObj.getString(objKey));
                }
            }
            gObj.put( com.function.DroolsUtil.VALUES_FOR_KEYS + "-" + ruleName, objKeyPathObj);
        }
        return result;
    }

    /**
     * 内层或外层主键
     * 比如  objKeys:["name","age",{"f.a":["name","age"]}]
     * 内层是{"f.a":["name","age"]} 部分
     * 外层是{"f.a":["name","age"]} 部分
     * @param gObj
     * @param bigObjKeysName
     * @return
     */
    public List<String> getObjKeys(com.alibaba.fastjson.JSONObject gObj,String bigObjKeysName,String objKeyPath){
        JSONArray array = gObj.containsKey(bigObjKeysName)?gObj.getJSONArray(bigObjKeysName):null;
        if (array != null) {
            if (objKeyPath == null) {
                return array.stream().filter(item -> item instanceof String).map(Object::toString).collect(Collectors.toList());
            }else {
                List<Object> collect = array.stream().filter(item -> item instanceof JSONObject && ((JSONObject) item).containsKey(objKeyPath)).collect(Collectors.toList());
                if (collect.size()>0 ) {
                    JSONArray jsonArray = ((JSONObject) collect.get(0)).getJSONArray(objKeyPath);
                    if (jsonArray !=null && jsonArray.size()>0) {
                        return jsonArray.stream().map(Object::toString).collect(Collectors.toList());
                    }
                }
            }
        }
        return null;
    }

    public boolean Compare(String leftValue,String rightValue, String sjlx, String compareOperator){
        //todo 考虑所有操作符 "==",">","<",">=","<=","!=",不为空,为空,为真,为假,为空（含空串),不为空（含空串）,包含
        if("java.lang.String".equals(sjlx)){
//            Arrays.asList("==",">","<",">=","<=","!=").contains(compareOperator)
            if ("==".equals(compareOperator)) {
                return leftValue.compareTo(rightValue) == 0;
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
                throw new RuntimeException("比较操作不合法");
            }
        }
        else if(Arrays.asList("java.lang.Integer","java.math.BigDecimal","java.lang.Long"
                ,"java.lang.Float","java.lang.Double"
                ,"java.lang.Short","java.lang.Byte"
                ,"java.math.BigInteger").contains(sjlx)){
            int re = 0;
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
            }else {
                throw new RuntimeException("数值类型的数据比较操作不合法");
            }
        }
        else if("java.util.Date".equals(sjlx)){
            throw new RuntimeException("日期比较未实现");
        }
        return  false;
    }

    /**
     *
     * @param gObj
     * @param namePath 在gObj中数据所在路径 先固定为 f.boys.age  ljq11.ljq111.age
     * @param sjlx  数据类型 先固定为 java.lang.Integer
     * @param compareOperator 比较符 先固定为 > 或者  <
     * @param rightOperand 右操作数 先固定传值正整数
     * @return
     */
    public java.lang.Boolean collectionHandleold(com.alibaba.fastjson.JSONObject gObj,String namePath, String sjlx,String compareOperator,String rightOperand){
        if (gObj== null) {
            return false;
        }
        String ruleName = "规则名称1";

        //获取f.boys
        String[] pathArr = namePath.split("\\.");
        if(pathArr.length < 2)return false;

        com.alibaba.fastjson.JSONArray arr = gObj.getJSONObject(pathArr[0]).getJSONArray(pathArr[1]);
        String fieldName = pathArr[2];

        //objkeys获取f.boys的部分

        //判断f.boys.age>2
        boolean re = true;

        com.alibaba.fastjson.JSONArray valueOfObjKeyArr = new com.alibaba.fastjson.JSONArray();
        com.alibaba.fastjson.JSONArray valueOffield = new com.alibaba.fastjson.JSONArray();

        for(Object obj : arr){
            com.alibaba.fastjson.JSONObject boy = (com.alibaba.fastjson.JSONObject)obj;
            // todo 需要编写通用的比较功能类，适配不同的情况，这里是大于
            if(compareOperator == ">")
            {
                //如果校验不成功，保存信息的复合主键信息 到$obj
                if(!(boy.getIntValue(fieldName) > java.lang.Integer.parseInt(rightOperand))){
                    re = false;
                    //@todo values of objkeys的组装
                    valueOffield.add(boy.getIntValue(fieldName));
                }
            }
            else if(compareOperator =="<"){
                //如果校验不成功，保存信息的复合主键信息 到$obj
                if(!(boy.getIntValue(fieldName) < java.lang.Integer.parseInt(rightOperand))){
                    re = false;
                    //@todo values of objkeys的组装
                    valueOffield.add(boy.getIntValue(fieldName));
                }
            }
        }
        System.out.println("------------11--------");
        System.out.println("-------------11-------");
        // todo 行不同，需要在 then clause中才能更新global variable
        gObj.put(ruleName+"@@"+namePath,valueOffield);
        return re;
    }

}
