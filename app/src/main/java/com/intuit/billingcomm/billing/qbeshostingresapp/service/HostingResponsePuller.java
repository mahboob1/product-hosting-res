package com.intuit.billingcomm.billing.qbeshostingresapp.service;

import com.intuit.billingcomm.billing.qbeshostingresapp.model.IrpHostingResponse;

import java.util.List;

public interface HostingResponsePuller {
    List<IrpHostingResponse> pullResponses() throws Exception;
    void acknowledgeProcessedResponses(List<IrpHostingResponse> irpHostingResponses) throws Exception;
}
