package com.Runtime;

import com.pojo.Person;
import com.pojo.School;
import org.junit.Test;
import org.kie.api.definition.rule.Rule;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by lujq on 5/13/2021.
 */
public class RuntimeTest {
//    public RuleProvider localRuleProvider() {
//        return new LocalFileRuleProvider("rules/runtime/zwzt.drl");
//    }

    public Map<String,RuleProvider> localRuleProvider() {
        Map<String, RuleProvider> rpMap = new HashMap<>();
        rpMap.put("zwzt", new LocalFileRuleProvider("rules/runtime/zwzt.drl"));
        rpMap.put("zbpt", new LocalFileRuleProvider("rules/runtime/zbpt.drl"));
        return rpMap;
    }

    @Test
    public void DroolsRuntimeTest() throws Exception {
        GisRuleEngineRuntimeManager runtimeManager = new GisRuleEngineRuntimeManager();
        runtimeManager.putRuntime("zwzt", new LocalFileRuleProvider("rules/runtime/zwzt.drl"));
        String projectCode = "zwzt";
        // 初始化drools
        KieContainer kieContainer = runtimeManager.getRuntime(projectCode).getKieContainer();
        KieSession kieSession = kieContainer.newKieSession();
        // 设置Filter
        GisAgendaFilter filter = new GisAgendaFilter(Collections.singletonList("申请人姓名不为空"));
        // eventListener
        GisAgendaEventListener listener = new GisAgendaEventListener();
        kieSession.addEventListener(listener);
        //传递需要执行的规则名称列表
        //kieSession.insert(jsonObject);
//        kieSession.setGlobal("$obj", jsonObject);

        kieSession.fireAllRules(filter);
        kieSession.dispose();
        List<String> firedRuleNameList = listener.getFiredRuleNameList();
        System.out.println("---------------------------------------------------------");
        System.out.println("---------------------------------------------------------");
        System.out.println(firedRuleNameList);
        System.out.println("---------------------------------------------------------");
        System.out.println("---------------------------------------------------------");
        runtimeManager.getRuntime(projectCode).clearRule();

        runtimeManager.putRuntime("zbpt", new LocalFileRuleProvider("rules/runtime/zbpt.drl"));
        projectCode = "zbpt";
        // 初始化drools
        kieContainer = runtimeManager.getRuntime(projectCode).getKieContainer();
        kieSession = kieContainer.newKieSession();
        // 设置Filter
        filter = new GisAgendaFilter(Collections.singletonList("申请人姓名不为空"));
        // eventListener
        listener = new GisAgendaEventListener();
        kieSession.addEventListener(listener);
        //传递需要执行的规则名称列表
        //kieSession.insert(jsonObject);
//        kieSession.setGlobal("$obj", jsonObject);
        kieSession.fireAllRules(filter);
        kieSession.dispose();
        System.out.println("---------------------------------------------------------");
        System.out.println("---------------------------------------------------------");
        System.out.println(listener.getFiredRuleNameList());
        System.out.println("---------------------------------------------------------");
        System.out.println("---------------------------------------------------------");

        projectCode = "zwzt";
        // 初始化drools
        kieContainer = runtimeManager.getRuntime(projectCode).getKieContainer();
        kieSession = kieContainer.newKieSession();
        // 设置Filter
        filter = new GisAgendaFilter(Collections.singletonList("申请人姓名不为空"));
        // eventListener
        listener = new GisAgendaEventListener();
        kieSession.addEventListener(listener);
        //传递需要执行的规则名称列表
        //kieSession.insert(jsonObject);
//        kieSession.setGlobal("$obj", jsonObject);
        kieSession.fireAllRules(filter);
        kieSession.dispose();
        System.out.println("---------------------------------------------------------");
        System.out.println("---------------------------------------------------------");
        System.out.println(listener.getFiredRuleNameList());
        System.out.println("---------------------------------------------------------");
        System.out.println("---------------------------------------------------------");

        projectCode = "zbpt";
        // 初始化drools
        kieContainer = runtimeManager.getRuntime(projectCode).getKieContainer();
        kieSession = kieContainer.newKieSession();
        // 设置Filter
        filter = new GisAgendaFilter(Collections.singletonList("申请人姓名不为空"));
        // eventListener
        listener = new GisAgendaEventListener();
        kieSession.addEventListener(listener);
        //传递需要执行的规则名称列表
        //kieSession.insert(jsonObject);
//        kieSession.setGlobal("$obj", jsonObject);
        kieSession.fireAllRules(filter);
        kieSession.dispose();
        System.out.println("---------------------------------------------------------");
        System.out.println("---------------------------------------------------------");
        System.out.println(listener.getFiredRuleNameList());
        System.out.println("---------------------------------------------------------");
        System.out.println("---------------------------------------------------------");

    }


    @Test
    public void DroolsReteAlgorithmTest() throws Exception {
        GisRuleEngineRuntimeManager runtimeManager = new GisRuleEngineRuntimeManager();
        runtimeManager.putRuntime("zwzt", new LocalFileRuleProvider("rules/runtime/zwzt.drl"));
        String projectCode = "zwzt";
        // 初始化drools
        KieContainer kieContainer = runtimeManager.getRuntime(projectCode).getKieContainer();
        KieSession kieSession = kieContainer.newKieSession();
        // eventListener
//        GisAgendaEventListener listener = new GisAgendaEventListener();
//        kieSession.addEventListener(listener);
        Person person=new Person();
        person.setName("张小三");
        kieSession.insert(person);
        Person person2=new Person();
        person2.setName("张小二");
        kieSession.insert(person2);

        School school=new School();
        school.setClassName("一班");
        kieSession.insert(school);

        kieSession.fireAllRules();
        kieSession.dispose();
//        List<String> firedRuleNameList = listener.getFiredRuleNameList();
//        System.out.println("---------------------------------------------------------");
//        System.out.println("---------------------------------------------------------");
//        System.out.println(firedRuleNameList);
//        System.out.println("---------------------------------------------------------");
//        System.out.println("---------------------------------------------------------");

//        第二次会话调用
//        kieSession = kieContainer.newKieSession();
//        // eventListener
//        kieSession.addEventListener(listener);
//        person.setName("张小er");
//        kieSession.insert(person);
//        kieSession.insert(school);
//
//        kieSession.fireAllRules();
//        kieSession.dispose();
//        firedRuleNameList = listener.getFiredRuleNameList();
//        System.out.println("---------------------------------------------------------");
//        System.out.println("---------------------------------------------------------");
//        System.out.println(firedRuleNameList);
//        System.out.println("---------------------------------------------------------");
//        System.out.println("---------------------------------------------------------");


    }
}
