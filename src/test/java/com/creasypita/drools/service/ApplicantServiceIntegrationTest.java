package com.creasypita.drools.service;

import com.creasypita.drools.model.Applicant;
import com.creasypita.drools.model.SuggestedRole;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Created by creasypita on 8/19/2020.
 *
 * @ProjectName: droolstutorial
 */
public class ApplicantServiceIntegrationTest {
    private ApplicantService applicantService;

    @Before
    public void setup() {
        applicantService = new ApplicantService();
    }

    @Test
    public void whenCriteriaMatching_ThenSuggestManagerRole() throws IOException {
        Applicant applicant = new Applicant("Davis", 21, 1600000.0, 1);
        SuggestedRole suggestedRole = new SuggestedRole();
        applicantService.suggestARoleForApplicant(applicant, suggestedRole);
//        assertEquals("Manager", suggestedRole.getRole());
        assertEquals("assist Developer", suggestedRole.getRole());
    }


}
