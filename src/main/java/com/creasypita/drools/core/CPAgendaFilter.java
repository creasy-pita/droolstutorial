package com.creasypita.drools.core;

import org.kie.api.runtime.rule.AgendaFilter;
import org.kie.api.runtime.rule.Match;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by creasypita on 8/20/2020.
 *自定义规则过滤器
 * @ProjectName: droolstutorial
 */
public class CPAgendaFilter implements AgendaFilter {

    private static final Logger logger = LoggerFactory.getLogger(CPAgendaFilter.class);
    private List<String> acceptRuleNameList;

    public CPAgendaFilter(List<String> acceptRuleNameList) {
        this.acceptRuleNameList = acceptRuleNameList;
    }
    @Override
    public boolean accept(Match match) {
        String ruleName = match.getRule().getName();
        logger.info("匹配规则是否accept："+ruleName);
        Boolean accept =acceptRuleNameList.contains(ruleName);
        logger.info(accept.toString());
        return accept;
    }
}
