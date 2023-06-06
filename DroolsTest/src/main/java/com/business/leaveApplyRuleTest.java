package com.business;

import com.Runtime.GisAgendaEventListener;
import com.Runtime.GisAgendaFilter;
import com.alibaba.fastjson.JSONObject;
import org.drools.core.base.RuleNameStartsWithAgendaFilter;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.definition.rule.Rule;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lujq on 6/6/2023.
 * 请假申请规则校验
 */
public class leaveApplyRuleTest {
    @Test
    public void testPackage() {
        KieServices kss = KieServices.Factory.get();
        // 加载容器，会调用init方法默认加载`META-INF/kmodule.xml`配置文件，包含了kidmodule,kiebase,kiessession的信息
        KieContainer kc = kss.getKieClasspathContainer();
        KieSession ks = kc.newKieSession("leaveApply");
        // 按包名获取包下的规则列表
        Collection<Rule> rules = ks.getKieBase().getKiePackage("rules.leaveApply").getRules();
        com.alibaba.fastjson.JSONObject obj = new JSONObject();
        obj.put("reason", "lala");
        ks.setGlobal("$obj", obj);

        List<String> ruleList = rules.stream().map(item -> item.getName()).collect(Collectors.toList());
        // 设置Filter 设置哪些规则需要触发校验
        GisAgendaFilter filter = new GisAgendaFilter(ruleList);
        // eventListener
        GisAgendaEventListener listener = new GisAgendaEventListener();
        ks.addEventListener(listener);

        int count = ks.fireAllRules(filter);
        // 获取触发了的规则名称
        List<String> firedRuleNameList = listener.getFiredRuleNameList();
        for (String rule : firedRuleNameList) {
            System.out.println("触发了规则" + rule );
        }
        ks.dispose();
    }

}
