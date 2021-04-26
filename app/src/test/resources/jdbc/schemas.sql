drop all objects;

create schema WEBS;
create SEQUENCE WEBS.SEQ_SUBS;
create SEQUENCE WEBS.SEQ_SUBS_ADDON;
create SEQUENCE WEBS.SEQ_ENTLPROD;
create SEQUENCE WEBS.SEQ_ENTITLED_PRODUCT_FEATURE;
CREATE SEQUENCE WEBS.SEQ_SUBSCRIPTION_VOUCHER;
CREATE SEQUENCE WEBS.SEQ_VPSTAGING;
CREATE SEQUENCE WEBS.SEQ_VPSUBSSTAGING;
CREATE SEQUENCE WEBS.ENT_PROD_FEAT_PROC_ID_SEQ;
CREATE SEQUENCE WEBS.QBES_HOST_REQ_STAGING_ID_SEQ;
CREATE TABLE WEBS.SUBSCRIPTION
(
   SUBSCRIPTION_ID NUMBER(10, 0) NOT NULL
, CUSTOMER_ACCOUNT_ID NUMBER(19, 0) NOT NULL
, OFFER_ID NUMBER(10, 0)
, OPT_IN_OFFER_ID NUMBER(10, 0) NULL
, CREATE_DATE DATE
, UPDATE_DATE DATE
, ENTITLESTATE_CHANGE_REASON_ID NUMBER(10,0)     NULL
, PROCESSING_STATE_ID NUMBER(10,0)     NULL
, CLIENT_ACCOUNT_ID             NUMBER(19,0)     NULL
, PRICE_PROTECTION_END_DATE DATE
, GROUP_ID VARCHAR2(256 CHAR) NULL
, CONSTRAINT PK_SUBSCRIPTION PRIMARY KEY
  (
    SUBSCRIPTION_ID
  )
);

CREATE TABLE WEBS.SUBSCRIPTION_ADDON
(
    SUBSCRIPTION_ADDON_ID NUMBER(10, 0) NOT NULL
, SUBSCRIPTION_ID NUMBER(10, 0) NOT NULL
, OFFER_ID NUMBER(10, 0) NOT NULL
, OPT_IN_OFFER_ID NUMBER(10, 0) NULL
, CREATE_DATE DATE
, UPDATE_DATE DATE
, ENTITLESTATE_CHANGE_REASON_ID NUMBER(10,0)     NULL
, PRICE_PROTECTION_END_DATE DATE
, CONSTRAINT PK_SUBSCRIPTION_ADDON PRIMARY KEY
  (
    SUBSCRIPTION_ADDON_ID
  )
);

CREATE TABLE WEBS.ENTITLED_PRODUCT
(
   ENTITLED_PRODUCT_ID NUMBER(10, 0) NOT NULL
, SUBSCRIPTION_ID NUMBER(10, 0) NOT NULL
, PRODUCT_ID NUMBER(10, 0) NOT NULL
, ENTITLED_PRODUCT_STATE VARCHAR2(10 CHAR) NOT NULL
, SERVICE_STATE_ID NUMBER(10, 0) NOT NULL
, GRANT_ID NUMBER(19, 0)
, CREATE_DATE DATE
, UPDATE_DATE DATE
, SUBSCRIPTION_ADDON_ID NUMBER(10, 0)
, PRODUCT_CODE           VARCHAR2(256 CHAR)     NULL
, CONSTRAINT PK_ENTITLED_PRODUCT PRIMARY KEY
  (
    ENTITLED_PRODUCT_ID
  )
);

CREATE TABLE WEBS.ENTITLED_PRODUCT_FEATURE
(
    ENTITLED_PRODUCT_FEATURE_ID   NUMBER(10)        NOT NULL,
    ENTITLED_PRODUCT_ID           NUMBER(10)        NOT NULL,
    FEATURE_OFFER_ID                    NUMBER(10)        NOT NULL,
    FEATURE_CODE                  VARCHAR2(20 CHAR) NOT NULL,
    ENTITLED_FEATURE_STATE        VARCHAR2(20 CHAR) NOT NULL,
    SERVICE_STATE_ID              NUMBER(10)        NOT NULL,
    ENTITLESTATE_CHANGE_REASON_ID NUMBER(10)                ,
    CREATE_DATE                   DATE                      ,
    UPDATE_DATE                   DATE                      ,
    PRICE_PROTECTION_END_DATE     DATE                      ,
    CONSTRAINT PK_ENTITLED_PRODUCT_FEATURE PRIMARY KEY
    (
      ENTITLED_PRODUCT_FEATURE_ID
    )
);

CREATE TABLE WEBS.ENTITLED_PRODUCT_ATTR
(
	ENTITLED_PRODUCT_ID NUMBER(10, 0) NOT NULL
, ATTR_CODE VARCHAR2(256 CHAR) NOT NULL
, ATTR_TYPE VARCHAR2(50 CHAR) NOT NULL
, FEATURE_VALUE VARCHAR2(50 CHAR)
, LIMIT_UNIT_VALUE NUMBER(10, 2)
, CREATE_DATE DATE NULL
, UPDATE_DATE DATE
, ATTR_NAME VARCHAR2(50 CHAR)
, CONSTRAINT PK_ENTITLED_PRODUCT_ATTR PRIMARY KEY
  (
    ENTITLED_PRODUCT_ID
  , ATTR_CODE
  )
);

CREATE TABLE WEBS.EP_SERVICE_STATE
(
	SERVICE_STATE_ID NUMBER(10, 0) NOT NULL
, SERVICE_STATE_NAME VARCHAR2(100 CHAR) NOT NULL
, SERVICE_STATE_CODE VARCHAR2(100 CHAR) NOT NULL
, TRIAL VARCHAR2(1 CHAR)
, RENEWAL VARCHAR2(1 CHAR)
, OPT_IN VARCHAR2(1 CHAR)
, COLLECTION VARCHAR2(1 CHAR)
, CREATE_DATE DATE
, UPDATE_DATE DATE
, CONSTRAINT PK_EP_SERVICE_STATE PRIMARY KEY
  (
    SERVICE_STATE_ID
  )
);



-- Create tables
CREATE TABLE WEBS.SUBSCRIPTION_BULK_REQ (
	CORRELATION_ID 		VARCHAR2(50 CHAR) NOT NULL,
	TRANSACTION_ID 		VARCHAR2(50 CHAR) NOT NULL,
	ACCOUNT_ID 			NUMBER(19),
  SOURCE_ACCOUNT_ID      NUMBER(19),
	REQUEST_STATUS 		VARCHAR2(20 CHAR) NOT NULL,
	REQUEST_TYPE 		VARCHAR2(20 CHAR) NOT NULL,
	PUBLISHED_FALLOUT 	VARCHAR2(1),
	PUBLISHED_COMPLETION_EVENT VARCHAR2(1),
	CREATE_DATE    		DATE NOT NULL,
  UPDATE_DATE    		DATE,
  BATCH_RECORDS_COUNT    		NUMBER(6),
  SOURCE    		VARCHAR2(50 CHAR) NULL,
   CONSTRAINT pk_subs_bulk_request PRIMARY KEY
  (
    CORRELATION_ID
  )
);


CREATE TABLE WEBS.SUBSCRIPTION_BULK_REQ_PAYLOAD (
	CORRELATION_ID 		VARCHAR2(50 CHAR) NOT NULL,
	REQUEST_PAYLOAD     CLOB  NOT NULL  ,
	CREATE_DATE         DATE  NOT NULL  ,
	UPDATE_DATE         DATE  NULL ,
	CONSTRAINT pk_subs_bulk_req_payload PRIMARY KEY
  (
    CORRELATION_ID
  )
);


CREATE TABLE WEBS.SUBSCRIPTION_BULK_REQ_UNIT (
	CORRELATION_ID 		VARCHAR2(50 CHAR) NOT NULL,
	SUBSCRIPTION_ID		NUMBER(10) NOT NULL,
	REQUEST_STATUS 		VARCHAR2(20 CHAR) NOT NULL,
	PUBLISHED_FALLOUT 	VARCHAR2(1),
	PUBLISHED_FINAL_EVENT VARCHAR2(1),
	ERRORS_DOC        VARCHAR2(256),
	CREATE_DATE         DATE  NOT NULL  ,
	UPDATE_DATE         DATE  NULL ,
	CUSTOMER_ACCOUNT_ID NUMBER(19),
	TO_OFFER_ID	NUMBER(10),
  SWITCH_OFFER_REASON         VARCHAR2(256 CHAR) NULL,
  RESET_DISCOUNT         VARCHAR2(1 CHAR) NULL,
	CONSTRAINT pk_subs_bulk_req_unit PRIMARY KEY
  (
    CORRELATION_ID,
    SUBSCRIPTION_ID
  )
);

CREATE TABLE WEBS.SUBS_BULK_REQ_UNIT_FALLOUT (
		CORRELATION_ID      VARCHAR2(50 CHAR) NOT NULL,
		CUSTOMER_ACCOUNT_ID VARCHAR2(20 CHAR),
		SUBSCRIPTION_ID	 VARCHAR2(20 CHAR),
		TO_OFFER_ID		 VARCHAR2(10 CHAR),
		SWITCH_OFFER_REASON VARCHAR2(256 CHAR),
		CHANGE_DATE_LOGIC	 VARCHAR2(20 CHAR),
		CHANGE_DATE		 VARCHAR2(50 CHAR),
		REQUEST_STATUS      VARCHAR2(20 CHAR) NOT NULL,
		ERROR_CATEGORY		 VARCHAR2(20 CHAR) NOT NULL,
		ERRORS_DOC          VARCHAR2(512 CHAR),
    RESET_DISCOUNT         VARCHAR2(1 CHAR) NULL,
		CREATE_DATE		 DATE NOT NULL,
		UPDATE_DATE		 DATE,
	CONSTRAINT pk_subs_bulk_req_unit_fout PRIMARY KEY
  (
    CORRELATION_ID,
    CUSTOMER_ACCOUNT_ID
  )
);

CREATE TABLE WEBS.SUBSCRIPTION_VOUCHER (
  SUBSCRIPTION_VOUCHER_ID NUMBER(10, 0) NOT NULL,
  SUBSCRIPTION_ID NUMBER(10, 0) NOT NULL,
  VOUCHER_CODE VARCHAR2(50 CHAR) NOT NULL,
  OFFER_ID NUMBER(10, 0) NOT NULL,
  CREATE_DATE DATE,
  UPDATE_DATE DATE,
  CONSTRAINT PK_SUBSCRIPTION_VOUCHER PRIMARY KEY (SUBSCRIPTION_VOUCHER_ID),
  CONSTRAINT FK_SUBSCRIPTION_VOUCHER FOREIGN KEY (SUBSCRIPTION_ID) REFERENCES WEBS.SUBSCRIPTION (SUBSCRIPTION_ID)
);

CREATE TABLE WEBS.ENTITLED_PRODUCT_LICENSE (
			ENTITLED_PRODUCT_ID NUMBER(10) NOT NULL,
			LICENSE VARCHAR2(25) NOT NULL,
			EOC VARCHAR2(10) NOT NULL,
			CREATE_DATE DATE,
			UPDATE_DATE DATE,
			FULFILLMENT_DATE DATE,
		    STATUS VARCHAR2(10 CHAR),
		    SUBSCRIPTION_ID NUMBER(10) NOT NULL,
			CONSTRAINT ENTITLED_PRODUCT_CODE_PK PRIMARY KEY
	            (ENTITLED_PRODUCT_ID,
	            LICENSE,
	            EOC),
            CONSTRAINT ENTPRODUCT_FK_ENTPRODUCTLIC FOREIGN KEY
	            (ENTITLED_PRODUCT_ID)
	            REFERENCES WEBS.ENTITLED_PRODUCT
	            (ENTITLED_PRODUCT_ID)
);

CREATE TABLE WEBS.VP_DOMAIN(
      VP_DOMAIN_ID NUMBER(2,0) NOT NULL,
      VP_DOMAIN VARCHAR2(25 CHAR) NOT NULL,
      CREATE_DATE DATE NOT NULL,
      UPDATE_DATE DATE NULL,
      CONSTRAINT VP_DOMAIN_PK PRIMARY KEY (VP_DOMAIN_ID)
);

CREATE TABLE WEBS.VP_STAGING(
      VP_STAGING_ID NUMBER(10, 0) NOT NULL,
      VP_DOMAIN_ID NUMBER(2,0) NOT NULL,
      VP_YEAR NUMBER(4,0) NOT NULL,
      ENTITLEMENT_COMPLETED VARCHAR2(1 CHAR),
      REVREC_COMPLETED VARCHAR2(1 CHAR),
      FULFILLMENT_COMPLETED VARCHAR2(1 CHAR),
      EMAIL_COMPLETED VARCHAR2(1 CHAR),
      CLEANUP_COMPLETED VARCHAR2(1 CHAR),
      CREATE_DATE DATE NOT NULL,
      UPDATE_DATE DATE NULL,
      CONSTRAINT VP_STAGING_PK PRIMARY KEY (VP_STAGING_ID)
);

CREATE TABLE WEBS.VP_SUBS_STAGING_STATE(
      VP_SUBS_STAGING_STATE_ID NUMBER(2,0) NOT NULL,
      VP_SUBS_STAGING_STATE VARCHAR2(25 CHAR) NOT NULL,
      CREATE_DATE DATE NOT NULL,
      UPDATE_DATE DATE NULL,
      CONSTRAINT VP_SUBS_STAGING_STATE_PK PRIMARY KEY (VP_SUBS_STAGING_STATE_ID)
);

CREATE TABLE WEBS.VP_SUBS_STAGING(
      VP_SUBS_STAGING_ID NUMBER(10, 0) NOT NULL,
      VP_STAGING_ID NUMBER(10,0) NOT NULl,
      SUBSCRIPTION_ID NUMBER(10,0) NOT NULL,
      LICENSE VARCHAR2(25 CHAR)  NULL,
      VP_EOC VARCHAR2(10 CHAR)  NULL,
      VP_SUBS_STAGING_STATE_ID  NUMBER(2,0) NOT NULL,
      CREATE_DATE DATE NOT NULL,
      UPDATE_DATE  DATE  NULL,
      CONSTRAINT VP_SUBS_STAGING_PK PRIMARY KEY (VP_SUBS_STAGING_ID,VP_STAGING_ID),
      CONSTRAINT VPSUBSSTAGING_FK_SUBID FOREIGN KEY (SUBSCRIPTION_ID) REFERENCES WEBS.SUBSCRIPTION (SUBSCRIPTION_ID),
      CONSTRAINT VPSUBSSTAGING_FK_VPSUBSTGSTID FOREIGN KEY (VP_SUBS_STAGING_STATE_ID) REFERENCES WEBS.VP_SUBS_STAGING_STATE (VP_SUBS_STAGING_STATE_ID)
);


CREATE TABLE WEBS.VP_TRANSACTION(
      VP_TRANSACTION_ID VARCHAR2(40) NOT NULL,
      VP_STAGING_ID NUMBER(10,0) NOT NULL,
      CREATE_DATE DATE NOT NULL,
      UPDATE_DATE DATE NULL,
      CONSTRAINT VP_TRANSACTION_PK PRIMARY KEY (VP_TRANSACTION_ID),
      CONSTRAINT VPSTAGINGFK_VP_STAGING_ID FOREIGN KEY (VP_STAGING_ID) REFERENCES WEBS.VP_STAGING (VP_STAGING_ID)
);

CREATE TABLE WEBS.ENT_PROD_FEAT_PROCESSING(
      ENT_PROD_FEAT_PROCESSING_ID NUMBER(10, 0) NOT NULL,
      ENTITLED_STATE VARCHAR2(20) NOT NULL,
      PROCESSING_STATE VARCHAR2(20) NOT NULL,
      START_DATE DATE,
      END_DATE DATE,
      CREATE_DATE DATE NOT NULL,
      UPDATE_DATE DATE,
      ENTITLED_PRODUCT_FEATURE_ID NUMBER(10),
      CONSTRAINT ENT_PROD_FEAT_PROCESSING_PK PRIMARY KEY (ENT_PROD_FEAT_PROCESSING_ID),
      CONSTRAINT FEATPROCFK_ENT_PROD_FEAT_ID FOREIGN KEY (ENTITLED_PRODUCT_FEATURE_ID) REFERENCES WEBS.ENTITLED_PRODUCT_FEATURE (ENTITLED_PRODUCT_FEATURE_ID)
);

CREATE TABLE WEBS.QBES_HOSTING_REQUEST_STAGING(
      QBES_HOST_REQ_STG_ID NUMBER(10, 0) NOT NULL,
      ACTIVITY_TYPE VARCHAR2(20) NOT NULL,
      EFFECTIVE_DATE DATE NOT NULL,
      CAPTURE_TIME DATE NOT NULL,
      CUSTOMER_ID NUMBER(19) NOT NULL,
      SUBSCRIPTION_ID NUMBER(10, 0) NOT NULL,
      LICENSE VARCHAR2(25) NOT NULL,
      EOC VARCHAR2(10) NOT NULL,
      STATUS VARCHAR2(20) NOT NULL,
      ERROR_MESSAGE VARCHAR2(256),
      ABORT_REASON VARCHAR2(256),
      CREATE_DATE DATE NOT NULL,
      UPDATE_DATE DATE,
      ENT_PROD_FEAT_PROCESSING_ID NUMBER(10),
      CONSTRAINT QBES_HOST_REQ_STAGING_PK PRIMARY KEY (QBES_HOST_REQ_STG_ID),
      CONSTRAINT HOSTREQSTGFK_PRODFEATPROC_ID FOREIGN KEY (ENT_PROD_FEAT_PROCESSING_ID) REFERENCES WEBS.ENT_PROD_FEAT_PROCESSING (ENT_PROD_FEAT_PROCESSING_ID)
);

create schema WEBSMIG;

CREATE TABLE WEBSMIG.CONVERT_ACCOUNT_V2
(
  CONVERSION_ID NUMBER(19, 0) NOT NULL
, REALM_ID NUMBER(19, 0) NOT NULL
, SOURCE_SYSTEM VARCHAR2(20 BYTE) NOT NULL
, SOURCE_ACCOUNT_PRODUCT_TYPE VARCHAR2(50 BYTE) NOT NULL
, CONVERT_ACCOUNT_TYPE_ID NUMBER(10, 0) NOT NULL
, CUSTOMER_SEGMENT VARCHAR2(100 BYTE) NOT NULL
, EXTENDED_CUSTOMER_SEGMENT VARCHAR2(100 BYTE) NOT NULL
, CONVERT_TYPE_ID NUMBER(10, 0) NOT NULL
, CONVERSION_CONTEXT_ID NUMBER(10, 0) NOT NULL
, ACCOUNT_CONTEXT VARCHAR2(25 BYTE) NOT NULL
, INTUIT_TID VARCHAR2(50 BYTE) NOT NULL
, OBILL_CONV_TRY_COUNT NUMBER(5, 0) NOT NULL
, OBILL_CONV_STATUS_ID NUMBER(10, 0) NOT NULL
, OBILL_CONV_STATUS_DATE DATE NOT NULL
, OBILL_CONV_NTFY_STATUS_ID NUMBER(10, 0)
, OBILL_BILLSYS_STATUS_ID NUMBER(10, 0)
, OFFER_MAPPING_RULE_ID NUMBER(10, 0) NOT NULL
, OBILL_PRICE_AFTER_DISCOUNT NUMBER(10, 2)
, OBILL_DISCOUNT NUMBER(10, 2)
, OBILL_TOTAL_PRICE NUMBER(10, 2)
, PARENT_REALM_ID NUMBER(19, 0)
, LGCY_BDOM NUMBER(2, 0)
, LGCY_PRICE_AFTER_DISCOUNT NUMBER(10, 2)
, LGCY_CANCEL_TRY_COUNT NUMBER(5, 0)
, LGCY_CANCEL_STATUS_ID NUMBER(10, 0)
, LGCY_CANCEL_STATUS_DATE DATE
, LGCY_CANCEL_ERROR_MSG VARCHAR2(500 BYTE)
, LGCY_CURRENCY_ISO_CODE VARCHAR2(3 BYTE)
, CONV_COMPANY_ADDRESS_TYPE VARCHAR2(25 BYTE)
, CONV_PAYMENT_INFO_TYPE VARCHAR2(25 BYTE)
, INTUIT_WEBS_TBT VARCHAR2(200 BYTE)
, CLIENTS_COUNT NUMBER(19, 0) NOT NULL
, WEBS_REQUEST_ID VARCHAR2(50 BYTE)
, WEBS_CORRELATION_ID VARCHAR2(50 BYTE)
, CONV_POST_SUBS_CONTEXT VARCHAR2(500 BYTE)
, COMPANY_NAME VARCHAR2(150 BYTE)
, OBILL_TEMP_GRANT_IDS VARCHAR2(4000 BYTE)
, IS_DELETE_OBILL_TEMP_GRANTS VARCHAR2(1 BYTE)
, GRANT_CLEANUP_STATUS_ID NUMBER(10, 0)
, GRANT_CLEANUP_TRY_COUNT NUMBER(5, 0)
, GRANT_CLEANUP_STATUS_DATE DATE
, CREATE_DATE DATE NOT NULL
, UPDATE_DATE DATE
, TAX_EXEMPT_NTFY_STATUS_ID NUMBER(10, 0)
, IS_CLEANUP_OBILL_TEMP_GRANTS VARCHAR2(1 BYTE)
, IS_CLIENT VARCHAR2(1 BYTE)
, CLIENT_CONV_TRIG_TRY_COUNT NUMBER(5, 0)
, CLIENT_CONV_TRIG_TRY_DATE DATE
, CLIENT_CONV_TRIG_ERROR_MSG VARCHAR2(500 BYTE)
, LGCY_OFFER_ID VARCHAR2(128 BYTE)
, WEBS_TRANSACTION_DATE_TIME VARCHAR2(50 CHAR)
, INTUIT_CHAOS_IDS VARCHAR2(256 CHAR)
, INTUIT_CHAOS_POINTS VARCHAR2(1024 CHAR)
, RPS_NTFY_STATUS_ID NUMBER(10, 0)
, DISCOUNT_END_DATE DATE
, HAS_REMAINING_DISCOUNT VARCHAR2(1 BYTE)
, IS_BUNDLE VARCHAR2(1 BYTE)
, EXTERNAL_OFFER_ID VARCHAR2(250 BYTE)
, IS_PENDING_CLIENT VARCHAR2(1 CHAR)
, PERIOD_END_DATE DATE
, LGCY_DISCOUNT_DURATION NUMBER(2, 0)
, IS_ANNUAL_OFF_BDOY VARCHAR2(1 CHAR)
, LEGACY_ACCOUNT_ID VARCHAR2(25 BYTE)
, ENTITLEMENT_STATE VARCHAR2(10 CHAR)
, CONVERT_OFFER_TYPE VARCHAR2(50 BYTE)
, LGCY_COUNTRY VARCHAR2(3 CHAR),
    CONSTRAINT PK_CONVERT_ACCOUNT_V2 PRIMARY KEY (CONVERSION_ID)
);

CREATE TABLE WEBSMIG.CONVERT_ACCOUNT_LGCY_V2
(
  CONVERSION_ID NUMBER(19, 0) NOT NULL
, LGCY_SUBSCRIPTION_ID VARCHAR2(50 BYTE)
, LGCY_BILL_CYCLE_START_DATE DATE
, LGCY_NEXT_CHARGE_DATE DATE
, LGCY_BASE_PLAN_NAME VARCHAR2(50 BYTE)
, LGCY_BASE_PLAN_ID VARCHAR2(50 BYTE)
, LGCY_BASE_PLAN_DURATION VARCHAR2(50 BYTE)
, LGCY_REFUND_CALC_STATUS_ID NUMBER(10, 0)
, LGCY_REFUND_TOTAL_AMOUNT NUMBER(10, 2)
, LGCY_CANCEL_BILLSYS_STATUS_ID NUMBER(10, 0)
, LGCY_CANCEL_PLTFRM_STATUS_ID NUMBER(10, 0)
, LGCY_BILLHIST_CONV_TRY_COUNT NUMBER(5, 0)
, LGCY_BILLHIST_CONV_STATUS_ID NUMBER(10, 0)
, LGCY_BILLHIST_CONV_STATUS_DATE DATE
, LGCY_BILLHIST_CONV_ERROR_MSG VARCHAR2(500 BYTE)
, LGCY_REFUND_IS_NCD_CHANGED VARCHAR2(1 BYTE)
, LGCY_REFUND_IS_PLAN_CHANGED VARCHAR2(1 BYTE)
, LGCY_REFUND_HAS_OTB VARCHAR2(1 BYTE)
, CREATE_DATE DATE NOT NULL
, UPDATE_DATE DATE
);

CREATE TABLE WEBSMIG.CONVERT_ACCOUNT_REQUEST_V2
(
  CONVERSION_ID NUMBER(19, 0) NOT NULL
, REQUEST_PAYLOAD CLOB NOT NULL
, PROCESS_SUBS_REQUEST CLOB NOT NULL
, CREATE_DATE DATE NOT NULL
, UPDATE_DATE DATE
, REQUEST_KEY_ATTRIBUTES VARCHAR2(4000 BYTE)
, CONVERSION_ATTRIBUTE_GROUPS VARCHAR2(4000 CHAR)
);

CREATE TABLE WEBSMIG.CONVERT_ACCOUNT_RESPONSE_V2
(
  CONVERSION_ID NUMBER(19, 0) NOT NULL
, ERROR_CODE VARCHAR2(30 BYTE)
, ERROR_MSG VARCHAR2(500 BYTE)
, RESPONSE_PAYLOAD VARCHAR2(4000 BYTE)
, CREATE_DATE DATE NOT NULL
, UPDATE_DATE DATE
);

CREATE TABLE WEBSMIG.CONVERT_SUBSCRIPTION_V2
(
  CONVERSION_ID NUMBER(19, 0) NOT NULL
, SUBSCRIPTION_ID NUMBER(10, 0) NOT NULL
, OFFER_ID NUMBER(10, 0) NOT NULL
, CREATE_DATE DATE NOT NULL
, UPDATE_DATE DATE
, OFFER_TYPE VARCHAR2(20 BYTE) DEFAULT 'BASE' NOT NULL
);
