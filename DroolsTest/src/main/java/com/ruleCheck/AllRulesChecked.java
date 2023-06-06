package com.ruleCheck;

import org.drools.core.base.RuleNameStartsWithAgendaFilter;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

/**
 * Created by creasypita on 8/22/2020.
 *
 * @ProjectName: drools
 */
public class AllRulesChecked {

    @Test
    public void testPackage() {
        KieServices kss = KieServices.Factory.get();
        KieContainer kc = kss.getKieClasspathContainer();
        KieSession ks = kc.newKieSession("rulecheck");
//        ks.getAgenda().getAgendaGroup("ag1").setFocus();//让AgendaGroup分组为ag1的获取焦点
        int count = ks.fireAllRules(new RuleNameStartsWithAgendaFilter("test001"));
//        int count = ks.fireAllRules();
        System.out.println("总执行了" + count + "条规则");
        ks = kc.newKieSession("rulecheck");
//        ks.getAgenda().getAgendaGroup("ag2").setFocus();//让AgendaGroup分组为ag2的获取焦点
        count = ks.fireAllRules(new RuleNameStartsWithAgendaFilter("test002"));
//        int count = ks.fireAllRules();
        System.out.println("总执行了" + count + "条规则");

        try {
            Thread.sleep(3600000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ks.dispose();
    }
}
