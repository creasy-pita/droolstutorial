package com.function;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
import org.apache.commons.lang3.StringUtils;

import java.beans.Expression;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static com.function.BooleanUtil.Compare;

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
     * 集合属性和标量值比较的处理方法 ，集合属性时带实体层次的
     * todo sjlxsStr 为java.util.List,java.lang.Object,java.lang.Integer的处理方式
     * todo  throw RuntimeException时后续错误的处理
     * @param gObj  以 boys.scoreReports.score > 90 为例
     * @param keysStr  集合属性数据在gObj中的key路径  比如例子中的【boys.scoreReports.score 】
     * @param sjlxsStr 集合属性数据各层次的数据类型，比如例子中的【java.util.List,java.util.List,java.lang.Integer】
     * @param compareOperator 模式的比较符 比如例子中的 >
     * @param rightOperand  右操作数 比如例子中的 90
     * @param ruleName  规则名称 when中不能通过drools api访问，所以只能解析并通过参数参入
     * @return collectionHandle
     */
    public java.lang.Boolean collectionFieldCompare(com.alibaba.fastjson.JSONObject gObj,String keysStr, String sjlxsStr,String compareOperator,String rightOperand,String ruleName){
        String[] keyList = keysStr.split("\\.");
        String[] sjlxList = sjlxsStr.split(",");
        if(keyList.length<2)return false;
        //按从gObj取集合属性前面部分，比如 boys.scoreReports
        String objKeyPath = keysStr.substring(0, keysStr.lastIndexOf("."));
        Map<String ,Boolean> resultMap = new HashMap<>();
        resultMap.put("result",true);
        //按集合属性数据的key路径从gObj解析获取属性数据
        Object currentObj = gObj;
        currentObj = getAttrObj(currentObj, keyList, sjlxList);
        if(currentObj == null)return false;
        String keyName = keyList[keyList.length - 1];
        String sjlx = sjlxList[sjlxList.length - 1];

        JSONArray valuesForKeys = new JSONArray();
        List<String> bigObjKeys = getObjKeys(gObj, DroolsUtil.OBJ_KEYS, objKeyPath);
        List<String> objKeys = getObjKeys(gObj, DroolsUtil.OBJ_KEYS,null);
        if (currentObj instanceof JSONObject) {

        }
        else if (currentObj instanceof JSONArray) {
            ((JSONArray) currentObj).parallelStream().filter( f -> {
                        boolean re = Compare(((JSONObject) f).getString(keyName), rightOperand, sjlx, compareOperator);
                        if(!re){
                            if (bigObjKeys != null) {
                                JSONObject valuesForKey = new JSONObject();
                                valuesForKeys.add(valuesForKey);
                                for (String objKey : bigObjKeys) {
                                    valuesForKey.put(objKey, ((JSONObject) f).get(objKey));
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
                    objKeyPathObj.put(objKey,gObj.get(objKey));
                }
            }
            if(bigObjKeys!=null)
                gObj.put( DroolsUtil.VALUES_FOR_KEYS + "-" + ruleName, objKeyPathObj);
        }
        return result;
    }

    /**
     * todo 复合集合属性和标量值比较的处理方法 待开发
     * @param gObj  以 boys.scoreReports.age > 18 || boys.scoreReports.cardType ==2 为例
     * @param keysStr  比如例子中的【boys.scoreReports】
     * @param sjlxsStr 比如例子中的【java.util.List,java.util.List】
     * @param postfixTraversal    后缀遍历表达式字符串
     * @param ruleName  规则名称 when中不能通过drools api访问，所以只能解析并通过参数参入
     * @return
     */
    public java.lang.Boolean oneCollectionMultiFieldCompare(com.alibaba.fastjson.JSONObject gObj,String keysStr, String sjlxsStr,String postfixTraversal,String ruleName){
        String[] keyList = keysStr.split("\\.");
        String[] sjlxList = sjlxsStr.split(",");
        if(keyList.length<2)return false;
        Map<String ,Boolean> resultMap = new HashMap<>();
        resultMap.put("result",true);
        //按key层次和各层次的数据类型解析获取最底层的实体
        Object currentObj = gObj;
        currentObj = getAttrObj(currentObj, keyList, sjlxList);
        if(currentObj == null)return false;
        Node node = ExpressionTree.constructTree(postfixTraversal);

        JSONArray valuesForKeys = new JSONArray();
        List<String> bigObjKeys = getObjKeys(gObj, DroolsUtil.OBJ_KEYS, keysStr);
        List<String> objKeys = getObjKeys(gObj, DroolsUtil.OBJ_KEYS,null);
        if (currentObj instanceof JSONObject) {

        }
        else if (currentObj instanceof JSONArray) {
            ((JSONArray) currentObj).stream().filter( f -> {
                boolean re = ExpressionTree.caculate(node,(JSONObject) f);
                    if(!re){
                        if (bigObjKeys != null) {
                            JSONObject valuesForKey = new JSONObject();
                            valuesForKeys.add(valuesForKey);
                            for (String objKey : bigObjKeys) {
                                valuesForKey.put(objKey, ((JSONObject) f).get(objKey));
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
            objKeyPathObj.put(keysStr, valuesForKeys);
            //放入外层的主键信息
            if (objKeys != null) {
                for (String objKey : objKeys) {
                    objKeyPathObj.put(objKey,gObj.get(objKey));
                }
            }
            if(bigObjKeys!=null)
                gObj.put( DroolsUtil.VALUES_FOR_KEYS + "-" + ruleName, objKeyPathObj);
        }
        System.out.println("---------------------------------------------------------");
        System.out.println(gObj);
        System.out.println("---------------------------------------------------------");
        return result;
    }

    /**
     *
     * @param gObj
     * @param keysStr 多层实体名称
     * @param sjlxsStr 多层实体类型
     * @param postfixTraversal 表单式树的后缀遍历字符
     * @param ruleName
     */
    public boolean CompareCollection(com.alibaba.fastjson.JSONObject gObj,String keysStr, String sjlxsStr,String postfixTraversal,String ruleName){
//        if (!CheckRuleToExecute(gObj, ruleName)) {
//            return false;
//        }
        String objKeyPath = keysStr;
        ArrayList<JSONObject> jsonObjects = new ArrayList<>();
        jsonObjects.add(gObj);
        Node boolExpNode = ExpressionTree.constructTree(postfixTraversal);
        JSONObject objKeyPathObj = new JSONObject();
        JSONArray valuesForKeys = new JSONArray();
        List<String> boundKeyNamePath = Arrays.asList(keysStr.split("\\."));
        List<String> boundObjSjlxPath = Arrays.asList(sjlxsStr.split(","));
        JSONArray objKeysArr = gObj.containsKey(DroolsUtil.OBJ_KEYS)?gObj.getJSONArray(DroolsUtil.OBJ_KEYS):null;
        boolean result = ObjCompareInRelationLine(jsonObjects,boolExpNode,boundKeyNamePath,boundObjSjlxPath,valuesForKeys, objKeysArr);
        if (!result) {
            if(objKeysArr!=null)
                gObj.put( DroolsUtil.VALUES_FOR_KEYS + "-" + ruleName, objKeyPathObj);
            objKeyPathObj.put(objKeyPath, valuesForKeys);
        }
        return result;
    }

    /**
     *
     * @param relationObjInline 各层数据关联后组成一个实体集合列表， 用于比较判断前，各层数据组织到一起
     * @param boolExpNode  比较判断的表达式是复合表达式树
     *                     <p>比如boys.scoreReports实体下score字段和boys实体下name字段的复合表达式：（ boys.scoreReports.score>18 || boys.name=="xxx"）
     * @param objNameListInPath  多层实体名称路径组成的列表  比如 本例中的boys.scoreReports
     * @param objSjlxListInPath  多层实体类型路径组成的列表  比如 本例中java.util.List,java.util.List
     * @param valuesForKeys  比较判断不通过是记录主键信息  各层次的在主键信息降维记录在一层  每条数据比较判断不通过时会记录主键信息
     * @param objKeysArr  各层次的主键  比如
    <p>                       "objKeys":[
    <p>                          "name",
    <p>                          "age"
    <p>                          {"boys":["name","age"]},
    <p>                          {"boys.scoreReports": ["subject","average","score"]}
    <p>                        ]
     * @return 数据对象关联后对每一条底层数据做比较判断：
     *         <p>传入数据对象（JSONObject类型是带层次数据），实体路径(跨多层)，各层实体关联到一起后进行比较判断，比较判断的表达式是复合表达式树
     */
    public boolean ObjCompareInRelationLine(List<JSONObject>  relationObjInline,Node boolExpNode,
                                            List<String> objNameListInPath, List<String> objSjlxListInPath
            , JSONArray valuesForKeys,JSONArray objKeysArr){
        boolean re = true;
        //已经到最下层，做比较判断
        if(relationObjInline.size() == objNameListInPath.size()+1){
    /*
    boolExpNode relationObjInline
    比如 node中有 f.a.b.bx,Integer,>,18   则会转化成 relationObjInline.get(2).getInteger("bx") > 18的操作
    比如 node中有 f.a.ax,String,==,"2"   则会转化成 relationObjInline.get(1).getString("ax").compareTo("2")== 的操作
    */
            re = ExpressionTree.caculate(boolExpNode, relationObjInline);
            //校验不通过时记录主键信息
            if (!re) {
                //valuesForKey输出格式为{"name":"val","age":18,"boys.name":"val","boys.age":"val","boys.scoreReports.subject":"val","boys.scoreReports.subject":"val"}
                JSONObject valuesForKey = new JSONObject();
                valuesForKeys.add(valuesForKey);
                for (Object objKeys : objKeysArr) {
                    //第一层的valuesForKey  是String类型
                    if (objKeys instanceof String) {
                        valuesForKey.put(objKeys.toString(), relationObjInline.get(0).get(objKeys.toString()));
                    }
                    //第n(n>1)层的valuesForKey是map类型
                    else{
                        JSONObject jsonObject = (JSONObject) objKeys;
                        Map.Entry<String, Object> first = jsonObject.entrySet().iterator().next();
                        JSONArray objKeyNameList = (JSONArray) first.getValue();
                        //根据objkey确定它的所在层level的数值
                        int level = first.getKey().split("\\.").length;
                        //level需要在列表relationObjInline的大小范围内
                        if (relationObjInline.size() > level) {
                            for (Object objKeyName : objKeyNameList) {
                                valuesForKey.put(first.getKey() + "." + objKeyName, relationObjInline.get(level).get(objKeyName));
                            }
                        }
                    }
                }

            }
            return re;
        }
        //继续下一层处理
        else{
            int level = relationObjInline.size() -1;
            JSONObject obj = relationObjInline.get(level);
            //当前实体路径长度下的实体名称
            if ("java.util.List".equals(objSjlxListInPath.get(level))) {
                JSONArray jarr = obj.getJSONArray(objNameListInPath.get(level));
                for(Object inObj : jarr){
                    //加入  todo 注意检查cast时是否会是JSONObject以外的类型
                    relationObjInline.add((JSONObject) inObj);
                    //re放后边  ObjCompareInRelationLine()&&re
                    re = ObjCompareInRelationLine(relationObjInline,boolExpNode, objNameListInPath,objSjlxListInPath,valuesForKeys, objKeysArr) && re;
                    //再弹出
                    relationObjInline.remove(relationObjInline.size()-1);
                }
            }
            //"java.lang.Object"
            else{
                JSONObject inObj = obj.getJSONObject(objNameListInPath.get(relationObjInline.size()));
                relationObjInline.add((JSONObject) inObj);
                re = ObjCompareInRelationLine(relationObjInline,boolExpNode, objNameListInPath,objSjlxListInPath,valuesForKeys, objKeysArr);
                relationObjInline.remove(relationObjInline.size()-1);
            }
            return re;
        }

    }

    /**
     * 确定规则是否需要执行
     * @param gObj
     * @param ruleName
     * @return
     */
    public java.lang.Boolean CheckRuleToExecute(com.alibaba.fastjson.JSONObject gObj,String ruleName){
        JSONArray ruleNameArr = gObj.getJSONArray(DroolsUtil.RULENAME_LIST_KEY);
        if (ruleNameArr != null) {
            return ruleNameArr.contains(ruleName);
        }
        return false;
    }

    private Object getAttrObj(Object currentObj, String[] keyList,String[] sjlxList){
        for (int keyIndex = 0; keyIndex < keyList.length; keyIndex++) {
            if (currentObj == null) {
                throw new RuntimeException("校验的实际数据格式与配置的数据格式不一致");
            }
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
        return  currentObj;
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
