package com.intuit.billingcomm.billing.qbeshostingresapp.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.intuit.billingcomm.billing.qbescommon.model.CustomerContactInfo;
import com.intuit.billingcomm.billing.qbescommon.service.CustomerContactService;
import com.intuit.billingcomm.billing.qbescommon.service.QbesHostingRequestService;
import com.intuit.billingcomm.billing.qbeshostingresapp.TestHelpers;
import com.intuit.billingcomm.billing.qbespfts.model.HostingRequestFile;
import com.intuit.billingcomm.billing.qbespfts.service.QbesPftsService;
import com.intuit.platform.webs.subscription.internal.model.entity.QbesHostingRequestStagingEntity;

@RunWith(MockitoJUnitRunner.class)
public class HostingCancellationProcessorImplTest {
	
	@InjectMocks
	HostingCancellationProcessorImpl hostingCancellationProcessor;
	
	@Mock
	private QbesPftsService qbesPftsService;
	
	@Mock
	private QbesHostingRequestService qbesHostingRequestService;
	
	@Mock
	private CustomerContactService customerContactService;
	
	@Mock
	private HostingRequestFile hostingRequest;
	
	List<QbesHostingRequestStagingEntity> qbesHostingRequestStagingEntitys;
	
	@Mock
	Future<CustomerContactInfo> customerContactInfoFuture;
	
	CustomerContactInfo customerContactInfo;
	
	@Before
	public void init() {
		qbesHostingRequestStagingEntitys = TestHelpers.generateQbesHostingRequestStagingEntitys();
		customerContactInfo = TestHelpers.generateCustomerContactInfo();
	}
	
	@Test
	public void hostingCancellationProcessorSuccess() throws Exception {
		ArgumentCaptor<HostingRequestFile> hostingRequestFileArgCaptor = ArgumentCaptor.forClass(HostingRequestFile.class);
		when(qbesHostingRequestService.findEligibleUnsubmittedCancellations()).thenReturn(qbesHostingRequestStagingEntitys);
		when(customerContactService.fetchCustomerContactInfoAsync(any())).thenReturn(customerContactInfoFuture);
		when(customerContactInfoFuture.get()).thenReturn(customerContactInfo);
		hostingCancellationProcessor.processEligibleUnsubmittedCancellations();
		verify(customerContactService, times(qbesHostingRequestStagingEntitys.size())).fetchCustomerContactInfoAsync(any());
		verify(qbesPftsService, times(1)).submitIrpHostingRequest(hostingRequestFileArgCaptor.capture());
		HostingRequestFile hostingRequestFile = hostingRequestFileArgCaptor.getValue();
		assertEquals(qbesHostingRequestStagingEntitys.size(), hostingRequestFile.getRows().size());
		assertEquals(customerContactInfo.getCompanyName(), hostingRequestFile.getRows().get(0).getCustomer().getCompanyName());
		assertNotEquals(customerContactInfo.getFirstName(), hostingRequestFile.getRows().get(0).getCustomer().getFirstName());
		assertEquals("FirstName1", hostingRequestFile.getRows().get(0).getCustomer().getFirstName());
		assertNotEquals(customerContactInfo.getLastName(), hostingRequestFile.getRows().get(0).getCustomer().getLastName());
		assertEquals("LastName1", hostingRequestFile.getRows().get(0).getCustomer().getLastName());
	}
	
	@Test
	public void hostingCancellationProcessorNoCancellation() throws Exception {
		when(qbesHostingRequestService.findEligibleUnsubmittedCancellations()).thenReturn(new ArrayList<QbesHostingRequestStagingEntity>());
		hostingCancellationProcessor.processEligibleUnsubmittedCancellations();
		verify(customerContactService, times(0)).fetchCustomerContactInfoAsync(any());
	}
	
}
