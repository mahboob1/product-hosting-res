package com.intuit.billingcomm.billing.qbeshostingresapp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.intuit.billingcomm.billing.qbeshostingresapp.TestHelpers;
import com.intuit.billingcomm.billing.qbeshostingresapp.model.IrpHostingResponse;
import com.intuit.billingcomm.billing.qbespfts.model.HostingResponseFile;
import com.intuit.billingcomm.billing.qbespfts.service.QbesPftsService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class HostingResponsePullerImplTest {
	
	@InjectMocks
	private HostingResponsePullerImpl hostingResponsePuller;
	
	@Mock
	private QbesPftsService qbesPftsService; 
	
	private List<HostingResponseFile> responseFilesSingle;
	private List<HostingResponseFile> responseFilesMultiple;
	private List<IrpHostingResponse> irpHostingResponsesSuccess;
	private List<IrpHostingResponse> irpHostingResponsesFail;
	
	@Before
	public void init() {
		responseFilesSingle = TestHelpers.generateHostingResponseFilesSingle();
		responseFilesMultiple = TestHelpers.generateHostingResponseMiltiple();
		irpHostingResponsesSuccess = TestHelpers.generateIrpHostingResponseSuccess();
		irpHostingResponsesFail = TestHelpers.generateIrpHostingResponseFail();
	}
	
	@Test
	public void pullResponsesSingle() {
		try {
			when(qbesPftsService.pullIrpHostingResponses()).thenReturn(responseFilesSingle);
			List<IrpHostingResponse> irpHostingResponses = hostingResponsePuller.pullResponses();
			List<IrpHostingResponse> irpHostingResponsesAck = irpHostingResponses.stream()
					.filter(res -> res.isProcessed() == true).collect(Collectors.toList());
			assertEquals(4, irpHostingResponses.size());
			assertEquals(0, irpHostingResponsesAck.size());
		} catch (Exception e) {
			
		}
	}
	
	@Test
	public void pullResponsesMultiple() {
		try {
			when(qbesPftsService.pullIrpHostingResponses()).thenReturn(responseFilesMultiple);
			List<IrpHostingResponse> irpHostingResponse = hostingResponsePuller.pullResponses();
			List<IrpHostingResponse> irpHostingResponseFile1 = irpHostingResponse.stream()
					.filter(res -> res.getFileName().equals("file1")).collect(Collectors.toList());
			assertEquals(4, irpHostingResponseFile1.size());
			assertEquals(8, irpHostingResponse.size());
		} catch (Exception e) {
			
		}
	}
	
	@Test
	public void acknowledgeProcessedResponsesSuccess() {
		try {
			hostingResponsePuller.acknowledgeProcessedResponses(irpHostingResponsesSuccess);
			verify(qbesPftsService, times(1)).acknowledgeResponseFile(any());
		} catch (Exception e) {
			
		}
	}
	
	@Test
	public void acknowledgeProcessedResponsesFailure() {
		try {
			hostingResponsePuller.acknowledgeProcessedResponses(irpHostingResponsesFail);
			verify(qbesPftsService, times(0)).acknowledgeResponseFile(any());
		} catch (Exception e) {
			
		}
	}

}
