package com.intuit.billingcomm.billing.qbeshostingresapp.service;

import java.math.BigInteger;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.intuit.billingcomm.billing.qbescommon.model.CustomerContactInfo;
import com.intuit.billingcomm.billing.qbescommon.service.CustomerContactService;
import com.intuit.billingcomm.billing.qbescommon.service.QbesHostingRequestService;
import com.intuit.billingcomm.billing.qbespfts.model.HostingRequestFile;
import com.intuit.billingcomm.billing.qbespfts.model.HostingRequestFile.Customer;
import com.intuit.billingcomm.billing.qbespfts.model.HostingRequestFile.Product;
import com.intuit.billingcomm.billing.qbespfts.model.HostingRequestFile.Row;
import com.intuit.billingcomm.billing.qbespfts.model.HostingRequestFile.Subscription;
import com.intuit.billingcomm.billing.qbespfts.model.HostingRequestFile.Transaction;
import com.intuit.billingcomm.billing.qbespfts.service.QbesPftsService;
import com.intuit.platform.webs.subscription.internal.model.entity.QbesHostingRequestStagingEntity;
import org.springframework.stereotype.Service;

@Service
public class HostingCancellationProcessorImpl implements HostingCancellationProcessor {
	private static final String REQ_FILE_DATE_FORMAT = "yyyy-MM-dd";
	private static final String TIMEZONE_ID = "America/Edmonton";
	
	private final Logger LOGGER = LoggerFactory.getLogger(HostingCancellationProcessorImpl.class);
	
	@Autowired
	private QbesHostingRequestService qbesHostingRequestService;
	
	@Autowired
	private CustomerContactService customerContactService;
	
	@Autowired
	private QbesPftsService qbesPftsService;
	
	public void processEligibleUnsubmittedCancellations() throws Exception {
		HostingRequestFile hostingRequest = new HostingRequestFile();
		List<QbesHostingRequestStagingEntity> qbesHostingRequestStagingEntitys = qbesHostingRequestService
				.findEligibleUnsubmittedCancellations();
		List<Row> rows = new ArrayList<>();
		List<BigInteger> reqIds = new ArrayList<>();
		for(QbesHostingRequestStagingEntity qbesHostingRequestStagingEntity : qbesHostingRequestStagingEntitys) {
			try {
				rows.add(createHostingRequestRow(qbesHostingRequestStagingEntity));
				reqIds.add(qbesHostingRequestStagingEntity.getQbesHostingRequestStagingId());
			} catch(Exception e) {
				LOGGER.error("Exception in createHostingRequestRow getting CustomerContactInfo for Customer Id: " + qbesHostingRequestStagingEntity.getCustomerId(), e);
			}
		}
		if(rows.size() > 0) {
			hostingRequest.setRows(rows);
			try {
				qbesPftsService.submitIrpHostingRequest(hostingRequest);
				reqIds.forEach(id -> qbesHostingRequestService.markSubmitted(id));
			} catch (Exception e) {
				LOGGER.error("Exception in qbesPftsService submitIrpHostingRequest method", e);
				throw e;
			}
		} else {
			LOGGER.info("Eithe no eligible unsubmitted cancellations or check previous CustomerContactInfo exception if any.");
		}
	}
	
	private Row createHostingRequestRow(QbesHostingRequestStagingEntity qbesHostingRequestStagingEntity) throws Exception {
		Row row = new Row();
		Transaction transaction = new Transaction();
		Subscription subscription = new Subscription();
		Product product = new Product();
		Customer customer = new Customer();
		
		Future<CustomerContactInfo> customerContactInfoFuture = customerContactService.fetchCustomerContactInfoAsync(qbesHostingRequestStagingEntity.getCustomerId());
		// create transaction info
		transaction.setId(qbesHostingRequestStagingEntity.getQbesHostingRequestStagingId().toString());
		transaction.setActivityType(qbesHostingRequestStagingEntity.getActivityType());
		ZonedDateTime now = ZonedDateTime.now(ZoneId.of(TIMEZONE_ID));
		String formattedDateStr = now.format(DateTimeFormatter.ofPattern(REQ_FILE_DATE_FORMAT));
		transaction.setEffectiveDate(formattedDateStr);
		transaction.setCaptureTime(formattedDateStr);
		row.setTransaction(transaction);
		// create subscription info
		subscription.setSubscriptionId(qbesHostingRequestStagingEntity.getSubscriptionId().toString());
		subscription.setNumberOfUsers(getNumberOfUsers(qbesHostingRequestStagingEntity));
		row.setSubscription(subscription);
		// create product info
		product.setLicense(qbesHostingRequestStagingEntity.getLicense());
		product.setEoc(qbesHostingRequestStagingEntity.getEoc());
		row.setProduct(product);
		// set customer info
		customer.setId(qbesHostingRequestStagingEntity.getCustomerId().toString());
		CustomerContactInfo customerContactInfo = customerContactInfoFuture.get();
		setCustomerContactInfo(customer, customerContactInfo);			
		row.setCustomer(customer);
		return row;
	}
		
	private void setCustomerContactInfo(Customer customer, CustomerContactInfo customerContactInfo) {
		customer.setCompanyName(customerContactInfo.getCompanyName());
		customer.setFirstName(StringUtils.capitalize(customerContactInfo.getFirstName()));
		customer.setLastName(StringUtils.capitalize(customerContactInfo.getLastName()));
		customer.setCity(customerContactInfo.getCity());
		customer.setProvince(customerContactInfo.getProvince());
		customer.setPostalCode(customerContactInfo.getPostalCode());
		customer.setPhone(customerContactInfo.getPhone());
		customer.setEmail(customerContactInfo.getEmail());
		customer.setOldId(customerContactInfo.getLegacyId());
	}

	private int getNumberOfUsers(QbesHostingRequestStagingEntity req) {
		String featureCode = req.getEntitledProductFeatureProcessing().getEntitledProductFeature().getFeatureCode();
		int start = featureCode.indexOf("_") + 1;
		int end = featureCode.lastIndexOf("_");
		String numberOfUsers = featureCode.substring(start, end);
		return Integer.parseInt(numberOfUsers);
	}
}
