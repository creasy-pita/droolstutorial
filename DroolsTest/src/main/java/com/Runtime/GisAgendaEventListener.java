package com.Runtime;

import org.kie.api.event.rule.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class GisAgendaEventListener extends DefaultAgendaEventListener {
    private Logger logger = LoggerFactory.getLogger(GisAgendaEventListener.class);
    private List<String> firedRuleNameList = new ArrayList<>();

    @Override
    public void afterMatchFired(AfterMatchFiredEvent event) {
        String ruleName = event.getMatch().getRule().getName();
        logger.info("listener监听---规则触发:"+ruleName);
        firedRuleNameList.add(ruleName);
    }

    /*@Override
    public void matchCancelled(MatchCancelledEvent event) {
        System.out.println("matchCancelled_"+event.getMatch().getRule().getName());
    }

    @Override
    public void matchCreated(MatchCreatedEvent event) {
        System.out.println("matchCreated_"+event.getMatch().getRule().getName());
    }

    @Override
    public void agendaGroupPopped(AgendaGroupPoppedEvent event) {
        System.out.println("agendaGroupPopped_"+event.getAgendaGroup().getName());
    }

    @Override
    public void agendaGroupPushed(AgendaGroupPushedEvent event) {
        System.out.println("agendaGroupPushed_"+event.getAgendaGroup().getName());
    }

    @Override
    public void beforeMatchFired(BeforeMatchFiredEvent event) {
        System.out.println("beforeMatchFired_"+event.getMatch().getRule().getName());
    }

    @Override
    public void beforeRuleFlowGroupActivated(RuleFlowGroupActivatedEvent event) {
        System.out.println("beforeRuleFlowGroupActivated_"+event.getRuleFlowGroup().getName());
    }

    @Override
    public void afterRuleFlowGroupActivated(RuleFlowGroupActivatedEvent event) {
        System.out.println("afterRuleFlowGroupActivated_"+event.getRuleFlowGroup().getName());
    }

    @Override
    public void beforeRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent event) {
        System.out.println("beforeRuleFlowGroupDeactivated_"+event.getRuleFlowGroup().getName());
    }

    @Override
    public void afterRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent event) {
        System.out.println("afterRuleFlowGroupDeactivated_"+event.getRuleFlowGroup().getName());
    }*/

    public List<String> getFiredRuleNameList() {
        return firedRuleNameList;
    }
}
