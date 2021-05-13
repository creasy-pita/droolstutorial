package com.Runtime;

import org.kie.api.runtime.rule.AgendaFilter;
import org.kie.api.runtime.rule.Match;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GisAgendaFilter implements AgendaFilter {
    private Logger logger = LoggerFactory.getLogger(GisAgendaFilter.class);
    /**
     * 选择执行的规则名称
     */
    private List<String> acceptRuleNameList;
    /**
     * 被选择而且运行时确实存在的规则名称
     */
    //private List<String> matchedRuleNameList;

    public GisAgendaFilter(List<String> acceptRuleNameList){
        this.acceptRuleNameList=acceptRuleNameList;
        //matchedRuleNameList = new ArrayList<>();
    }
    @Override
    public boolean accept(Match match) {
        String ruleName = match.getRule().getName();
        logger.info("匹配规则是否accept："+ruleName);
        Boolean accept  =acceptRuleNameList.contains(ruleName);
        logger.info(accept.toString());
        return accept;
    }

    /**
     * 选择执行的规则数与运行时匹配的规则数是否一致,
     * 如果不一致,可能是运行时的规则不完整,也可能是调用者指定的规则本身不存在
     * @return
     */
    /*public boolean checkAssertRuntime(){
        return matchedRuleNameList.size()==acceptRuleNameList.size();
    }*/
}
