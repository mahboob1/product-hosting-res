package com.intuit.billingcomm.billing.qbeshostingresapp.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IrpHostingResponse {
	private String fileName;
    private String transactionId;
    private String transactionStatus;
    private boolean isProcessed;
    private String errorText;
}
