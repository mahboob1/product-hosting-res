package com.intuit.billingcomm.billing.qbeshostingresapp.service;

import com.intuit.billingcomm.billing.qbeshostingresapp.model.IrpHostingResponse;
import com.intuit.billingcomm.billing.qbespfts.model.HostingResponseFile;
import com.intuit.billingcomm.billing.qbespfts.model.HostingResponseFile.Row;
import com.intuit.billingcomm.billing.qbespfts.service.QbesPftsService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


@Service
public class HostingResponsePullerImpl implements HostingResponsePuller {
	 
	private final Logger LOGGER = LoggerFactory.getLogger(HostingResponsePullerImpl.class);
	
	@Autowired
	private QbesPftsService qbesPftsService;

    @Override
    public List<IrpHostingResponse> pullResponses() throws Exception {
    	List<IrpHostingResponse> irpHostingResponses = new ArrayList<>();
    	try {
    		List<HostingResponseFile> hostingResponseFiles = qbesPftsService.pullIrpHostingResponses();
    		LOGGER.info("Pulled {} responses", hostingResponseFiles.size());
    		hostingResponseFiles.forEach(hostingResponseFile -> {
    			List<Row> rows = hostingResponseFile.getRows();
    			rows.forEach(row -> {
    				IrpHostingResponse irpHostingResponse = new IrpHostingResponse();
    				irpHostingResponse.setFileName(hostingResponseFile.getFileName());
    				irpHostingResponse.setTransactionId(row.getTransactionId());
    				irpHostingResponse.setTransactionStatus(row.getResponse());
    				irpHostingResponse.setErrorText(row.getErrorText());
    				irpHostingResponses.add(irpHostingResponse);
    			});
    		});
    	} catch (Exception e) {
    		LOGGER.error("Exception in qbesPftsService pullIrpHostingResponses method", e);
    		throw e;
    	}  
    	return irpHostingResponses;
    }
    
    // Delete files that correspond to processed IRP transactions
    @Override
    public void acknowledgeProcessedResponses(List<IrpHostingResponse> irpHostingResponses) throws Exception {
    	Map<String, Integer> fileResCntMap = new HashMap<>();
    	Map<String, Integer> fileProcessedResCountMap = new HashMap<>();
    	for(IrpHostingResponse irpHostingResponse : irpHostingResponses){
    		if(irpHostingResponse.isProcessed()) {
    			fileProcessedResCountMap.merge(irpHostingResponse.getFileName(), 1, Integer::sum);
    		}
    		fileResCntMap.merge(irpHostingResponse.getFileName(), 1, Integer::sum);
    	}
    	for(Entry<String, Integer> ent : fileResCntMap.entrySet()) {
    		// If all transactions in a file are processed
    		if(fileProcessedResCountMap.containsKey(ent.getKey()) && fileProcessedResCountMap.get(ent.getKey()) == ent.getValue()) {
    			try {
    				this.qbesPftsService.acknowledgeResponseFile(ent.getKey());
    			} catch (Exception e) {
    				LOGGER.error("Exception in qbesPftsService acknowledgeResponseFile method", e);
    				throw e;
    			}  
    		} 
    	}
        
    }
         
}
