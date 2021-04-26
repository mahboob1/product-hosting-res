package com.intuit.billingcomm.billing.qbeshostingresapp.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.intuit.billingcomm.billing.qbescommon.service.QbesHostingRequestService;
import com.intuit.billingcomm.billing.qbeshostingresapp.TestHelpers;
import com.intuit.billingcomm.billing.qbeshostingresapp.model.IrpHostingResponse;

@RunWith(MockitoJUnitRunner.class)
public class WebsDataManagerImplTest {
	
	private static final String IN_PROGRESS = "ACK";
	private static final String ERROR = "ERROR";
	private static final String COMPLETED = "COMPLETE_SUCCESS";
	private static final String ABORTED = "COMPLETE_ABORTED";
	private static final String INITIATED = "INITIATED";
		
	@InjectMocks
	WebsDataManagerImpl websDataManager;
	
	@Mock
	private QbesHostingRequestService qbesHostingRequestService;
	
	private IrpHostingResponse irpHostingResponse;
	
	@Before
	public void init() {
		irpHostingResponse = TestHelpers.generateIrpHostingResponse();
	}
		
	@Test
	public void hostingCancellationProcessorInProgress() throws Exception {
		ArgumentCaptor<BigInteger> requestEntityIdArgCaptor = ArgumentCaptor.forClass(BigInteger.class);
		irpHostingResponse.setTransactionStatus(IN_PROGRESS);
		websDataManager.processHostingResponse(irpHostingResponse);
		verify(qbesHostingRequestService, times(1)).markInProgress(requestEntityIdArgCaptor.capture());
		BigInteger argValue = requestEntityIdArgCaptor.getValue();
		assertEquals(new BigInteger(irpHostingResponse.getTransactionId()), argValue);
	}
	
	@Test
	public void hostingCancellationProcessorError() throws Exception {
		ArgumentCaptor<BigInteger> requestEntityIdArgCaptor = ArgumentCaptor.forClass(BigInteger.class);
		ArgumentCaptor<String> errorTextArgCaptor = ArgumentCaptor.forClass(String.class);
		irpHostingResponse.setTransactionStatus(ERROR);
		irpHostingResponse.setErrorText("Response error 01");
		websDataManager.processHostingResponse(irpHostingResponse);
		verify(qbesHostingRequestService, times(1)).markError(requestEntityIdArgCaptor.capture(), errorTextArgCaptor.capture());
		BigInteger argValue = requestEntityIdArgCaptor.getValue();
		assertEquals(new BigInteger(irpHostingResponse.getTransactionId()), argValue);
	}
	
	@Test
	public void hostingCancellationProcessorCompleted() throws Exception {
		ArgumentCaptor<BigInteger> requestEntityIdArgCaptor = ArgumentCaptor.forClass(BigInteger.class);
		irpHostingResponse.setTransactionStatus(COMPLETED);
		websDataManager.processHostingResponse(irpHostingResponse);
		verify(qbesHostingRequestService, times(1)).markCompleted(requestEntityIdArgCaptor.capture());
		BigInteger argValue = requestEntityIdArgCaptor.getValue();
		assertEquals(new BigInteger(irpHostingResponse.getTransactionId()), argValue);
	}
	
	@Test
	public void hostingCancellationProcessorAborted() throws Exception {
		ArgumentCaptor<BigInteger> requestEntityIdArgCaptor = ArgumentCaptor.forClass(BigInteger.class);
		ArgumentCaptor<String> abortTextArgCaptor = ArgumentCaptor.forClass(String.class);
		irpHostingResponse.setTransactionStatus(ABORTED);
		irpHostingResponse.setErrorText("Abort reason 01");
		websDataManager.processHostingResponse(irpHostingResponse);
		verify(qbesHostingRequestService, times(1)).markAborted(requestEntityIdArgCaptor.capture(), abortTextArgCaptor.capture());
		BigInteger argValue = requestEntityIdArgCaptor.getValue();
		assertEquals(new BigInteger(irpHostingResponse.getTransactionId()), argValue);
	}
	
	@Test
	public void hostingCancellationProcessorDefault() throws Exception {
		ArgumentCaptor<BigInteger> requestEntityIdArgCaptor = ArgumentCaptor.forClass(BigInteger.class);
		irpHostingResponse.setTransactionStatus(INITIATED);
		websDataManager.processHostingResponse(irpHostingResponse);
		verify(qbesHostingRequestService, times(0)).markSubmitted(requestEntityIdArgCaptor.capture());
	}

}
