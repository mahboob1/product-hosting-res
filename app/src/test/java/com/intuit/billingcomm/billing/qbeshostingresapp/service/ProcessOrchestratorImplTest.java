package com.intuit.billingcomm.billing.qbeshostingresapp.service;

import com.intuit.billingcomm.billing.qbeshostingresapp.TestHelpers;
import com.intuit.billingcomm.billing.qbeshostingresapp.model.IrpHostingResponse;

import org.junit.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ProcessOrchestratorImplTest {
	private static final String RESPONSE_JOB_NAME = "PULL_RESPONSES";
	private static final String CANCELLATION_JOB_NAME = "PROCESS_CANCELLATION";
	@Nested
	@ExtendWith(SpringExtension.class)
	@SpringBootTest
	@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
	@EnableConfigurationProperties
	@ContextConfiguration(classes = ProcessOrchestratorImpl.class)
	@TestPropertySource(properties = {"orchestrator.jobName=" + RESPONSE_JOB_NAME})
	@RunWith(SpringRunner.class)
	@ActiveProfiles("test")
	public static class HostingResponseOrchestratorImplResTest {
	 
		@Autowired
		private ProcessOrchestratorImpl hostingResponseOrchestrator;

		@MockBean
		private HostingResponsePuller hostingResponsePuller;

		@MockBean
		private WebsDataManager websDataManager;
    
		@MockBean
		private HostingCancellationProcessor hostingCancellationprocessor;
    
		@Test
		public void orchestrateResponseProcessing_AllResponsesSuccess() throws Exception {
			List<IrpHostingResponse> responses = TestHelpers.generateHostingResponses();
			int responsesCount = responses.size();
			ArgumentCaptor<List<IrpHostingResponse>> acknwolegmentArgCaptor = ArgumentCaptor.forClass(List.class);
        	when(hostingResponsePuller.pullResponses()).thenReturn(responses);
        	hostingResponseOrchestrator.orchestrate();

        	verify(websDataManager, times(responsesCount)).processHostingResponse(any());
        	verify(hostingResponsePuller, times(1))
                .acknowledgeProcessedResponses(acknwolegmentArgCaptor.capture());
        	assertEquals(responsesCount, acknwolegmentArgCaptor.getValue().size());
        	assertEquals(2, acknwolegmentArgCaptor.getValue().stream().filter(res->res.isProcessed()).count());
		}

		@Test
		public void orchestrateResponseProcessing_PartialProcessingFailure() throws Exception {
			List<IrpHostingResponse> responses = TestHelpers.generateHostingResponsesPartial();
			int responsesCount = responses.size();
			ArgumentCaptor<List<IrpHostingResponse>> ackArgCaptor = ArgumentCaptor.forClass(List.class);
        	when(hostingResponsePuller.pullResponses()).thenReturn(responses);
        	doNothing().doThrow(new RuntimeException()).when(websDataManager).processHostingResponse(any());
        	hostingResponseOrchestrator.orchestrate();

        	verify(websDataManager, times(responsesCount)).processHostingResponse(any());
        	verify(hostingResponsePuller, times(1))
                .acknowledgeProcessedResponses(ackArgCaptor.capture());
        	assertEquals(2, ackArgCaptor.getValue().size());
        	assertEquals(1, ackArgCaptor.getValue().stream().filter(res->res.isProcessed()).count());
 
		}
	}
	@Nested
	@ExtendWith(SpringExtension.class)
	@SpringBootTest
	@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
	@EnableConfigurationProperties
	@ContextConfiguration(classes = ProcessOrchestratorImpl.class)
	@TestPropertySource(properties = {"orchestrator.jobName=" + CANCELLATION_JOB_NAME})
	@RunWith(SpringRunner.class)
	@ActiveProfiles("test")
	public static class HostingResponseOrchestratorImplCancellationTest {
		@Autowired
	    private ProcessOrchestratorImpl hostingResponseOrchestrator;
	    @MockBean
	    private HostingResponsePuller hostingResponsePuller;
	    @MockBean
	    private WebsDataManager websDataManager;
	    @MockBean
	    private HostingCancellationProcessor hostingCancellationprocessor;
		@Test
	    public void orchestrateCancellationProcessingSuccess() {
			hostingResponseOrchestrator.orchestrate();
	    }
		@Test
	    public void orchestrateCancellationProcessingFailure() throws Exception {
			doNothing().doThrow(new RuntimeException()).when(hostingCancellationprocessor).processEligibleUnsubmittedCancellations();
			hostingResponseOrchestrator.orchestrate();
	    }
	}

}
