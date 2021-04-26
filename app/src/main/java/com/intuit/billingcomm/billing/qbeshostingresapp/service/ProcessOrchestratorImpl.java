package com.intuit.billingcomm.billing.qbeshostingresapp.service;

import com.intuit.billingcomm.billing.qbeshostingresapp.model.IrpHostingResponse;

import lombok.Getter;
import lombok.Setter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Getter
@Setter
@ConfigurationProperties(prefix = "orchestrator")
public class ProcessOrchestratorImpl implements ProcessOrchestrator {

    private final Logger LOGGER = LoggerFactory.getLogger(ProcessOrchestratorImpl.class);

    @Autowired
    private HostingResponsePuller hostingResponsePuller;

    @Autowired
    private WebsDataManager websDataManager;
    
    @Autowired
    private HostingCancellationProcessor hostingCancellationProcessor;
    
    private String jobName;

    private static final String RESPONSE_JOB_NAME = "PULL_RESPONSES";
    private static final String CANCELLATION_JOB_NAME = "PROCESS_CANCELLATION";

    @Override
    public void orchestrate() {
    	if (jobName.equals(RESPONSE_JOB_NAME)) { 
    		orchestrateResponseProcessing();
    	} else if (jobName.equals(CANCELLATION_JOB_NAME)) {
    		orchestrateCancellationProcessing();
    	}
    }
    
    private void orchestrateResponseProcessing() {
    	try {
    		List<IrpHostingResponse> irpHostingResponses = hostingResponsePuller.pullResponses();
    		
    	 
    		LOGGER.info("Processing {} transaction responses: {}", irpHostingResponses.size(),
    				irpHostingResponses.stream().map(IrpHostingResponse::getTransactionId));
    		irpHostingResponses.forEach(irpHostingResponse -> {
    			try {
    				websDataManager.processHostingResponse(irpHostingResponse);
    				irpHostingResponse.setProcessed(true);
    			} catch (Exception e) {
    				LOGGER.error("Exception occurred while processing response for transaction ID: " +
                        irpHostingResponse.getTransactionId(), e);
    			}
    		});
    	 
    		LOGGER.info("{} out of {} transaction responses processed successfully.",
    				irpHostingResponses.stream().filter(res->res.isProcessed()).count(), irpHostingResponses.size());
        	hostingResponsePuller.acknowledgeProcessedResponses(irpHostingResponses);
    	} catch (Exception e) {
     		 LOGGER.error("Exception in hostingResponsePuller pullResponses method", e);
    	}
    }
    
    private void orchestrateCancellationProcessing() {
    	try {
    		hostingCancellationProcessor.processEligibleUnsubmittedCancellations();
    	} catch (Exception e) {
    		 LOGGER.error("Exception in hostingCancellationProcessor processEligibleUnsubmittedCancellations method", e);
   	}
    }
}
