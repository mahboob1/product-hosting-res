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
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@TestPropertySource(properties = {"orchestrator.jobName=PROCESS_CANCELLATION"})
public class HostingCancellationProcessorTest extends BaseApplicationBehaviorTest {

    @Autowired
    private ProcessOrchestrator processOrchestrator;

    @Autowired
    private QbesHostingRequestStagingRepository qbesHostingRequestStagingRepository;

    @Test
    public void processEligibleCancellations() throws Exception {
        processOrchestrator.orchestrate();
        List<QbesHostingRequestStagingEntity> submittedCancellations = qbesHostingRequestStagingRepository
                .findByStatusInAndActivityTypeInAndCreateDateBefore(
                        Collections.singletonList(QbesHostingRequestStagingStatusEnum.SUBMITTED.name()),
                        Arrays.asList(HostingRequestType.CANCEL.name(), HostingRequestType.DELINQUENT.name()),
                        new Date()
                );
        Assert.assertEquals(2, submittedCancellations.size());
        Assert.assertTrue(submittedCancellations.stream()
                .map(QbesHostingRequestStagingEntity::getQbesHostingRequestStagingId).collect(Collectors.toList())
                .containsAll(Arrays.asList(BigInteger.valueOf(111), BigInteger.valueOf(333))));
        Assert.assertTrue(isReqFileInSftp());
    }
}
