package com.creasypita.drools.service;

import com.creasypita.drools.config.DroolsBeanFactory;
import com.creasypita.drools.core.CPAgendaFilter;
import com.creasypita.drools.model.Applicant;
import com.creasypita.drools.model.SuggestedRole;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.AgendaFilter;
import org.kie.api.runtime.rule.Match;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ApplicantService {
    private static final Logger logger = LoggerFactory.getLogger(ApplicantService.class);

    KieSession kieSession = new DroolsBeanFactory().getKieSession();

    public SuggestedRole suggestARoleForApplicant(Applicant applicant, SuggestedRole suggestedRole) throws IOException {
        kieSession.insert(applicant);
        kieSession.setGlobal("suggestedRole", suggestedRole);
//        kieSession.setGlobal("logger", logger);
        //加入 自定义CPAgendaFilter
        List<String> accepteRulenameList = new ArrayList<>();
        accepteRulenameList.add("Suggest Manager Role");
        accepteRulenameList.add("Suggest assist Developer Role");
        AgendaFilter filter = new CPAgendaFilter(accepteRulenameList);
        kieSession.fireAllRules(filter);
        System.out.println(suggestedRole.getRole());
        return suggestedRole;

    }
}
