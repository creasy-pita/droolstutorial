package com.other;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.function.CollectFunction;
import com.function.DroolsUtil;
import com.pojo.Person;
import com.rits.cloning.Cloner;
import org.drools.core.base.RuleNameMatchesAgendaFilter;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.AgendaFilter;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by lujq on 1/12/2021.
 */
public class BigObjRule {

//    @Test
//    public void testBigObjJson() throws URISyntaxException, IOException {
//        java.util.List<Object> a = new ArrayList<>();
//        java.util.List b = new ArrayList();
//
//        KieServices kss = KieServices.Factory.get();
//        KieContainer kc = kss.getKieClasspathContainer();
//        KieSession ks =kc.newKieSession("bigobj");
//        //查看resouce目录
//        System.out.println(this.getClass().getResource("/").getPath());
//        java.net.URL url = this.getClass().getResource("/bigobj.json");
//        java.nio.file.Path resPath = java.nio.file.Paths.get(url.toURI());
//        String jsonStr = new String(java.nio.file.Files.readAllBytes(resPath), "UTF8");
//        JSONObject obj = JSON.parseObject(jsonStr);
//        JSONArray objKeys = obj.getJSONArray("objKeysv2");
//        for (Object objKey : objKeys) {
//            System.out.println(objKey.toString());
//        }
//
//    }

    @Test
    public void testBigObjRule() throws URISyntaxException, IOException {


        java.util.List<Object> a = new ArrayList<>();
        java.util.List b = new ArrayList();

        KieServices kss = KieServices.Factory.get();
        KieContainer kc = kss.getKieClasspathContainer();
        KieSession ks =kc.newKieSession("bigobj");
        //查看resouce目录
        System.out.println(this.getClass().getResource("/").getPath());
        java.net.URL url = this.getClass().getResource("/bigobj.json");
        java.nio.file.Path resPath = java.nio.file.Paths.get(url.toURI());
        String jsonStr = new String(java.nio.file.Files.readAllBytes(resPath), "UTF8");
        JSONObject obj = JSON.parseObject(jsonStr);
        // 设置Filter
        AgendaFilter filter = new RuleNameMatchesAgendaFilter("实体字段复合表达式测试111");
        //模拟大数据量测试
//        if (obj.containsKey("boys")) {
//            JSONArray boys = obj.getJSONArray("boys");
//            JSONObject firstBoy = boys.getJSONObject(0);
//            for (int i = 0; i < 10000; i++) {
//                Cloner cloner = new Cloner();
//                JSONObject cloneBoy = cloner.deepClone(firstBoy);
//                boys.add(cloneBoy);
//            }
//        }

        ks.setGlobal("$obj",obj);
        long startTime = System.currentTimeMillis();
        int count = ks.fireAllRules();
        long estimatedTime = System.currentTimeMillis() - startTime;
//        int count = ks.fireAllRules(filter);
        System.out.println("time duration millseconds %d" + estimatedTime);
        System.out.println("总执行了"+count+"条规则");
        System.out.println(obj);
        ks.dispose();
    }

//    @Test
    public void testObjRule() throws URISyntaxException, IOException {

        KieServices kss = KieServices.Factory.get();
        KieContainer kc = kss.getKieClasspathContainer();
        KieSession ks =kc.newKieSession("bigobj");
        String jsonStr = "{\"a\":1}";
        JSONObject obj = JSON.parseObject(jsonStr);
        // 设置Filter
//        ArrayList<String> ruleNames = new ArrayList<>();
//        ruleNames.add("实体字段复合表达式测试");
        AgendaFilter filter = new RuleNameMatchesAgendaFilter("rule1");
        ks.setGlobal("$obj",obj);
        int count = ks.fireAllRules();
//        int count = ks.fireAllRules(filter);
        System.out.println("总执行了"+count+"条规则");
        System.out.println(obj);
        ks.dispose();
    }

//    @Test
    public void testComp() throws URISyntaxException, IOException {
        System.out.println(this.getClass().getResource("/").getPath());
        java.net.URL url = this.getClass().getResource("/bigobj.json");
        java.nio.file.Path resPath = java.nio.file.Paths.get(url.toURI());
        String jsonStr = new String(java.nio.file.Files.readAllBytes(resPath), "UTF8");
        JSONObject obj = JSON.parseObject(jsonStr);
        String keysStr = "boys.scoreReports";
        String sjlxsStr = "java.util.List,java.util.List";
        String postfixTraversal = "boys.scoreReports.subject!@@!java.lang.String!@@!==!@@!math-@@-boys.scoreReports.score!@@!java.lang.Integer!@@!>!@@!18-@@-&&";
        String ruleName = "规则名称";

        //gObj,String keysStr, String sjlxsStr,String postfixTraversal,String ruleName
        new CollectFunction().CompareCollection(obj,keysStr,sjlxsStr,postfixTraversal,ruleName);
    }

//        @Test
    public void testListContains() throws URISyntaxException, IOException {
            String jsonStr = "{\"a\":1}";
            JSONObject obj = JSON.parseObject(jsonStr);
            String[] forCheckarr = {"ab", "ae"};
            List<String> forCheckList = Arrays.asList(forCheckarr);
            obj.put(DroolsUtil.RULENAME_LIST_KEY, forCheckList);

            String[] arr = {"ab", "ac", "ad", "ae"};
            List<String> nameList = Arrays.asList(arr);
            JSONArray nameArr = obj.getJSONArray(DroolsUtil.RULENAME_LIST_KEY);
            for (String name : nameList) {
                if (!nameArr.contains(name)) {
                    System.out.println(name);
                }
            }
    }

}
