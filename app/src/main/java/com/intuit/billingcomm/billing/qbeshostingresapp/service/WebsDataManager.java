package com.intuit.billingcomm.billing.qbeshostingresapp.service;

import com.intuit.billingcomm.billing.qbeshostingresapp.model.IrpHostingResponse;

public interface WebsDataManager {
    void processHostingResponse(IrpHostingResponse irpHostingResponse) throws Exception;
}
