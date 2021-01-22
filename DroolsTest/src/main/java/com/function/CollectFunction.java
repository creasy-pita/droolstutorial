package com.function;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;
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

    public java.lang.Boolean arr(com.alibaba.fastjson.JSONObject gObj,String namePath, String sjlx,String compareOperator,String rightOperand){
        //boys.scoreReports.score>99 boys.scoreReports.score<2
        boolean result = true;
        gObj.put("lamadasetvalue",true);
        JSONArray boys = gObj.getJSONArray("boys");
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
     * @param gObj
     * @param namePath 在gObj中数据所在路径 先固定为 f.boys.age  ljq11.ljq111.age
     * @param sjlx  数据类型 先固定为 java.lang.Integer
     * @param compareOperator 比较符 先固定为 > 或者  <
     * @param rightOperand 右操作数 先固定传值正整数
     * @return
     */
    public java.lang.Boolean collectionHandle(com.alibaba.fastjson.JSONObject gObj,String namePath, String sjlx,String compareOperator,String rightOperand){
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
