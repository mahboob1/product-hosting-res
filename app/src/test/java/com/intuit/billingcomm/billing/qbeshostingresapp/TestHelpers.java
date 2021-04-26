package com.intuit.billingcomm.billing.qbeshostingresapp;

import com.intuit.billingcomm.billing.qbescommon.model.CustomerContactInfo;
import com.intuit.billingcomm.billing.qbeshostingresapp.model.IrpHostingResponse;
import com.intuit.billingcomm.billing.qbespfts.model.HostingResponseFile;
import com.intuit.platform.webs.subscription.internal.model.entity.EntitledProductFeatureEntity;
import com.intuit.platform.webs.subscription.internal.model.entity.EntitledProductFeatureProcessingEntity;
import com.intuit.platform.webs.subscription.internal.model.entity.QbesHostingRequestStagingEntity;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestHelpers {

    public static List<IrpHostingResponse> generateHostingResponses() {
        return new ArrayList<IrpHostingResponse>() {{
            add(new IrpHostingResponse() {{
                setTransactionId("test1");
                setTransactionStatus("ACK");
                setProcessed(true);
            }});
            add(new IrpHostingResponse() {{
                setTransactionId("test2");
                setTransactionStatus("ACK");
                setProcessed(true);
            }});
        }};
    }
    
    public static List<IrpHostingResponse> generateHostingResponsesPartial() {
        return new ArrayList<IrpHostingResponse>() {{
            add(new IrpHostingResponse() {{
                setTransactionId("test1");
                setTransactionStatus("ACK");
                setProcessed(true);
            }});
            add(new IrpHostingResponse() {{
                setTransactionId("test2");
                setTransactionStatus("ACK");
            }});
        }};
    }
    
    public static List<HostingResponseFile> generateHostingResponseFilesSingle() {
        return new ArrayList<HostingResponseFile>() {{
            add(new HostingResponseFile() {{
            	setFileName("file1");
            	setRows(new ArrayList<Row>() {{
            		add(new Row() {{
            			setTransactionId("IRPtest1");
            			setResponse("ACK");
            		}});
            		add(new Row() {{
            			setTransactionId("IRPtest2");
            			setResponse("ERROR");
            		}});
            		add(new Row() {{
            			setTransactionId("IRPtest3");
            			setResponse("COMPLETE_SUCCESS");
            		}});
            		add(new Row() {{
            			setTransactionId("IRPtest3");
            			setResponse("COMPLETE_ABORTED");
            		}});
                }});
            }});
        }};
    }
    
    public static List<HostingResponseFile> generateHostingResponseMiltiple() {
        return new ArrayList<HostingResponseFile>() {{
            add(new HostingResponseFile() {{
            	setFileName("file1");
            	setRows(new ArrayList<Row>() {{
            		add(new Row() {{
            			setTransactionId("IRPtest1");
            			setResponse("ACK");
            		}});
            		add(new Row() {{
            			setTransactionId("IRPtest2");
            			setResponse("ERROR");
            		}});
            		add(new Row() {{
            			setTransactionId("IRPtest3");
            			setResponse("COMPLETE_SUCCESS");
            		}});
            		add(new Row() {{
            			setTransactionId("IRPtest4");
            			setResponse("COMPLETE_ABORTED");
            		}});
                }});
            }});
            
            add(new HostingResponseFile() {{
            	setFileName("file2");
            	setRows(new ArrayList<Row>() {{
            		add(new Row() {{
            			setTransactionId("IRP123");
            			setResponse("ACK");
            		}});
            		add(new Row() {{
            			setTransactionId("IRP234");
            			setResponse("ERROR");
            		}});
            		add(new Row() {{
            			setTransactionId("IRP456");
            			setResponse("COMPLETE_SUCCESS");
            		}});
            		add(new Row() {{
            			setTransactionId("IRPtest5");
            			setResponse("COMPLETE_ABORTED");
            		}});
                }});
            }});
        }};
    }
    
    public static List<IrpHostingResponse> generateIrpHostingResponseSuccess() {
    	List<IrpHostingResponse> irpHostingResponses = new ArrayList<>();
    	IrpHostingResponse irpHostingResponse = new IrpHostingResponse();
    	irpHostingResponse.setFileName("file1");
    	irpHostingResponse.setTransactionId("IRPTest1");
    	irpHostingResponse.setTransactionStatus("ACK");
    	irpHostingResponse.setProcessed(true);
    	irpHostingResponses.add(irpHostingResponse);
    	irpHostingResponse = new IrpHostingResponse();
    	irpHostingResponse.setFileName("file1");
    	irpHostingResponse.setTransactionId("IRPTest2");
    	irpHostingResponse.setTransactionStatus("ACK");
    	irpHostingResponse.setProcessed(true);
    	irpHostingResponses.add(irpHostingResponse);
    	return irpHostingResponses;
    }
    
    public static List<IrpHostingResponse> generateIrpHostingResponseFail() {
    	List<IrpHostingResponse> irpHostingResponses = new ArrayList<>();
    	IrpHostingResponse irpHostingResponse = new IrpHostingResponse();
    	irpHostingResponse.setFileName("file1");
    	irpHostingResponse.setTransactionId("IRPTest1");
    	irpHostingResponse.setTransactionStatus("ACK");
    	irpHostingResponse.setProcessed(true);
    	irpHostingResponses.add(irpHostingResponse);
    	irpHostingResponse = new IrpHostingResponse();
    	irpHostingResponse.setFileName("file1");
    	irpHostingResponse.setTransactionId("IRPTest2");
    	irpHostingResponse.setTransactionStatus("ERROR");
    	irpHostingResponses.add(irpHostingResponse);
    	irpHostingResponse = new IrpHostingResponse();
    	irpHostingResponse.setFileName("file1");
    	irpHostingResponse.setTransactionId("IRPTest3");
    	irpHostingResponse.setTransactionStatus("COMPLETE_SUCCESS");
    	irpHostingResponses.add(irpHostingResponse);
    	irpHostingResponse = new IrpHostingResponse();
    	irpHostingResponse.setFileName("file1");
    	irpHostingResponse.setTransactionId("IRPTest3");
    	irpHostingResponse.setTransactionStatus("COMPLETE_ABORTED");
    	irpHostingResponses.add(irpHostingResponse);
    	
    	return irpHostingResponses;
    }
    
    public static List<QbesHostingRequestStagingEntity> generateQbesHostingRequestStagingEntitys() {
    	return new ArrayList<QbesHostingRequestStagingEntity>() {{
    		add(new QbesHostingRequestStagingEntity() {{
    			setQbesHostingRequestStagingId(BigInteger.valueOf(100));
    			setActivityType("Activity1");
    			setEffectiveDate(new Date());
    			setCaptureTime(new Date());
    			setCustomerId(BigInteger.ONE);
    			setSubscriptionId(BigInteger.TEN);
    			setLicense("LicenseId1");
    			setEoc("Eoc1");
    			setEntitledProductFeatureProcessing(generateHostingFeatureProcessing());
    		}});
    		
    		add(new QbesHostingRequestStagingEntity() {{
				setQbesHostingRequestStagingId(BigInteger.valueOf(200));
    			setActivityType("Activity2");
    			setEffectiveDate(new Date());
    			setCaptureTime(new Date());
    			setCustomerId(BigInteger.ONE);
    			setSubscriptionId(BigInteger.TEN);
    			setLicense("LicenseId1");
    			setEoc("Eoc1");
				setEntitledProductFeatureProcessing(generateHostingFeatureProcessing());
    		}});
    	}};
    }
    
    public static IrpHostingResponse generateIrpHostingResponse() {
    	IrpHostingResponse irpHostingResponse = new IrpHostingResponse();
    	irpHostingResponse.setFileName("ResponseFile1");
    	irpHostingResponse.setTransactionId("1234567");
    	return irpHostingResponse;
    }
    
    public static CustomerContactInfo generateCustomerContactInfo() {
    	return new CustomerContactInfo() {{
    		setCompanyName("Company1");
    		setFirstName("firstName1");
    		setLastName("lastName1");
    		setCity("City1");
    		setProvince("Province1");
    		setPostalCode("PostalCode1");
    		setPhone("getPhone1");
    		setEmail("Email1");
    	}};
    }

    public static EntitledProductFeatureProcessingEntity generateHostingFeatureProcessing() {
    	return new EntitledProductFeatureProcessingEntity() {{
    		setEntitledProductFeature(generateHostingFeature());
		}};
	}

    public static EntitledProductFeatureEntity generateHostingFeature() {
    	return new EntitledProductFeatureEntity() {{
    		setFeatureCode("HOST_5_USER");
		}};
	}
}
