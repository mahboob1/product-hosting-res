package com.intuit.billingcomm.billing.qbeshostingresapp.service;

import com.intuit.billingcomm.billing.qbescommon.service.QbesHostingRequestService;
import com.intuit.billingcomm.billing.qbeshostingresapp.model.IrpHostingResponse;

import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebsDataManagerImpl implements WebsDataManager {
	
	private final Logger LOGGER = LoggerFactory.getLogger(WebsDataManagerImpl.class);
	
	private static final String IN_PROGRESS = "ACK";
	private static final String ERROR = "ERROR";
	private static final String COMPLETED = "COMPLETE_SUCCESS";
	private static final String ABORTED = "COMPLETE_ABORTED";
	
	@Autowired
	private QbesHostingRequestService qbesHostingRequestService;

    @Override
    public void processHostingResponse(IrpHostingResponse irpHostingResponse) throws Exception {
        // Use WEBS to update hosting fulfillment status, should handle cases where a response has already been processed
    	switch(irpHostingResponse.getTransactionStatus()) {
    		case IN_PROGRESS: qbesHostingRequestService.markInProgress(new BigInteger(irpHostingResponse.getTransactionId()));
    			break;
    		case ERROR: qbesHostingRequestService.markError(new BigInteger(irpHostingResponse.getTransactionId()), irpHostingResponse.getErrorText());
    			break;
    		case COMPLETED: qbesHostingRequestService.markCompleted(new BigInteger(irpHostingResponse.getTransactionId()));
    			break;
    		case ABORTED: qbesHostingRequestService.markAborted(new BigInteger(irpHostingResponse.getTransactionId()), irpHostingResponse.getErrorText());
    			break;
    		default:
    			LOGGER.error("Transaction status: {} for transaction id: {}", irpHostingResponse.getTransactionStatus(), irpHostingResponse.getTransactionId());
    			break;
    	}
    }
}
