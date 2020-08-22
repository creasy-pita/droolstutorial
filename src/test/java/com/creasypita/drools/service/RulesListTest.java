package com.creasypita.drools.service;

import com.creasypita.drools.core.DroolsEngineRuntime;
import org.junit.Assert;
import org.junit.Test;
import org.kie.api.definition.rule.Rule;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by creasypita on 8/19/2020.
 *
 * @ProjectName: droolstutorial
 */
public class RulesListTest {
    @Test
    public void WhenLoadDefaultRules_ThenSizeGreatThan1() throws Exception {
        DroolsEngineRuntime droolsEngineRuntime = new DroolsEngineRuntime();
        // 初始化drools
        KieContainer kieContainer = droolsEngineRuntime.getKieContainer();
        // 检查运行时
        Collection<Rule> rules = kieContainer.getKieBase().getKiePackage("com.creasypita.drools.rules").getRules();
        List<String> runtimeRules = rules.stream().map(Rule::getName).collect(Collectors.toList());
        Assert.assertNotEquals(0,runtimeRules.size());
    }

    @Test
    public void WhenDeployMoreRules_ThenOldIsRemainThere() throws Exception {

        DroolsEngineRuntime droolsEngineRuntime = new DroolsEngineRuntime();
        // 初始化drools
        KieContainer kieContainer = droolsEngineRuntime.getKieContainer();
        Collection<Rule> rules = kieContainer.getKieBase().getKiePackage("com.creasypita.drools.rules").getRules();
        List<String> runtimeRules = rules.stream().map(Rule::getName).collect(Collectors.toList());
        // 加载另一批
        droolsEngineRuntime.deploy(ResourceFactory.newClassPathResource("com/creasypita/drools/rules/rules.drl"));
        //注意重新部署后生成新的 kieContainer
        kieContainer = droolsEngineRuntime.getKieContainer();
        rules = kieContainer.getKieBase().getKiePackage("com.creasypita.drools.rules").getRules();
        runtimeRules = rules.stream().map(Rule::getName).collect(Collectors.toList());
        Assert.assertNotEquals(0,runtimeRules.size());
    }

}
