package com.Runtime;

import org.junit.Test;
import org.kie.api.definition.rule.Rule;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lujq on 5/13/2021.
 */
public class RuntimeTest {
    public RuleProvider localRuleProvider() {
        return new LocalFileRuleProvider("rules/runtime/zwzt.drl");
    }

    @Test
    public void DroolsRuntimeTest() throws Exception {

        GisRuleEngineRuntimeManager runtimeManager = new GisRuleEngineRuntimeManager(localRuleProvider());
        // 初始化drools
        KieContainer kieContainer = runtimeManager.getDefaultRuntime().getKieContainer();
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
        System.out.println(firedRuleNameList);

    }

}
