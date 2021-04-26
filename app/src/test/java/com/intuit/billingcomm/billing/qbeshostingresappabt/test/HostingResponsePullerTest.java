package com.intuit.billingcomm.billing.qbeshostingresappabt.test;

import com.intuit.billingcomm.billing.qbescommon.enums.QbesHostingRequestStagingStatusEnum;
import com.intuit.billingcomm.billing.qbeshostingresapp.service.ProcessOrchestrator;
import com.intuit.billingcomm.billing.qbespfts.enums.HostingRequestType;
import com.intuit.platform.webs.subscription.internal.model.entity.QbesHostingRequestStagingEntity;
import com.intuit.platform.webs.subscription.internal.model.repository.QbesHostingRequestStagingRepository;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@TestPropertySource(properties = {"orchestrator.jobName=PULL_RESPONSES"})
public class HostingResponsePullerTest extends BaseApplicationBehaviorTest {

    @Autowired
    private ProcessOrchestrator processOrchestrator;

    @Autowired
    private QbesHostingRequestStagingRepository qbesHostingRequestStagingRepository;

    @Test
    public void processResponseFiles() throws Exception {
        processOrchestrator.orchestrate();
        List<QbesHostingRequestStagingEntity> completedReqs = qbesHostingRequestStagingRepository
                .findByStatusInAndActivityTypeInAndCreateDateBefore(
                        Collections.singletonList(QbesHostingRequestStagingStatusEnum.COMPLETED.name()),
                        Collections.singletonList(HostingRequestType.NEW.name()),
                        new Date()
                );
        Assert.assertEquals(1, completedReqs.size());
        Assert.assertEquals(BigInteger.valueOf(555), completedReqs.get(0).getQbesHostingRequestStagingId());
        Assert.assertFalse(isFileInSftp(RESPONSE_FILE_NAME));
    }
}
