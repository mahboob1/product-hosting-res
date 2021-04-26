package com.intuit.billingcomm.billing.qbeshostingresapp;

import com.intuit.billingcomm.billing.qbeshostingresapp.service.ProcessOrchestrator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {"orchestrator.jobName=TEST"})
public class QbesHostingResApplicationTest {

    @Autowired
    private ProcessOrchestrator processOrchestrator;

    @Test
    public void contextLoads() {
        assertNotNull(processOrchestrator);
    }
}
