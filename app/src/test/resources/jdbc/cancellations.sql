-- Eligible CANCEL
insert into WEBS.ENTITLED_PRODUCT_FEATURE (
    ENTITLED_PRODUCT_FEATURE_ID,
    ENTITLED_PRODUCT_ID,
    FEATURE_OFFER_ID,
    FEATURE_CODE,
    ENTITLED_FEATURE_STATE,
    SERVICE_STATE_ID,
    ENTITLESTATE_CHANGE_REASON_ID,
    CREATE_DATE,
    UPDATE_DATE
)
values (
    1,
    100120,
    0,
    'HOST_5_USER',
    'INACTIVE',
    103,
    null,
    '2019-08-05',
    '2019-09-05'
);
insert into WEBS.ENT_PROD_FEAT_PROCESSING (
    ENT_PROD_FEAT_PROCESSING_ID,
    ENTITLED_STATE,
    PROCESSING_STATE,
    START_DATE,
    END_DATE,
    CREATE_DATE,
    UPDATE_DATE,
    ENTITLED_PRODUCT_FEATURE_ID
)
values (
    11,
    'INACTIVE',
    'PENDING',
    null,
    null,
    DATEADD('DAY', -1 , CURRENT_DATE),
    null,
    1
);
insert into WEBS.QBES_HOSTING_REQUEST_STAGING (
    QBES_HOST_REQ_STG_ID,
    ACTIVITY_TYPE,
    EFFECTIVE_DATE,
    CAPTURE_TIME,
    CUSTOMER_ID,
    SUBSCRIPTION_ID,
    LICENSE,
    EOC,
    STATUS,
    ERROR_MESSAGE,
    ABORT_REASON,
    CREATE_DATE,
    UPDATE_DATE,
    ENT_PROD_FEAT_PROCESSING_ID
) values (
    111,
    'CANCEL',
    '2019-10-05',
    '2019-10-05',
    1111,
    11111,
    'Lic1',
    'EOC1',
    'INITIATED',
    null,
    null,
    DATEADD('DAY', -1 , CURRENT_DATE),
    null,
    11
);

-- Ineligible CANCEL
insert into WEBS.ENTITLED_PRODUCT_FEATURE (
    ENTITLED_PRODUCT_FEATURE_ID,
    ENTITLED_PRODUCT_ID,
    FEATURE_OFFER_ID,
    FEATURE_CODE,
    ENTITLED_FEATURE_STATE,
    SERVICE_STATE_ID,
    ENTITLESTATE_CHANGE_REASON_ID,
    CREATE_DATE,
    UPDATE_DATE
)
values (
    2,
    100120,
    0,
    'HOST_5_USER',
    'INACTIVE',
    103,
    null,
    '2019-08-05',
    '2019-09-05'
);
insert into WEBS.ENT_PROD_FEAT_PROCESSING (
    ENT_PROD_FEAT_PROCESSING_ID,
    ENTITLED_STATE,
    PROCESSING_STATE,
    START_DATE,
    END_DATE,
    CREATE_DATE,
    UPDATE_DATE,
    ENTITLED_PRODUCT_FEATURE_ID
)
values (
    22,
    'INACTIVE',
    'PENDING',
    null,
    null,
    CURRENT_DATE,
    null,
    2
);
insert into WEBS.QBES_HOSTING_REQUEST_STAGING (
    QBES_HOST_REQ_STG_ID,
    ACTIVITY_TYPE,
    EFFECTIVE_DATE,
    CAPTURE_TIME,
    CUSTOMER_ID,
    SUBSCRIPTION_ID,
    LICENSE,
    EOC,
    STATUS,
    ERROR_MESSAGE,
    ABORT_REASON,
    CREATE_DATE,
    UPDATE_DATE,
    ENT_PROD_FEAT_PROCESSING_ID
) values (
    222,
    'CANCEL',
    '2019-10-05',
    '2019-10-05',
    2222,
    22222,
    'Lic2',
    'EOC2',
    'INITIATED',
    null,
    null,
    CURRENT_DATE,
    null,
    22
);

-- Eligible DELINQUENT
insert into WEBS.ENTITLED_PRODUCT_FEATURE (
    ENTITLED_PRODUCT_FEATURE_ID,
    ENTITLED_PRODUCT_ID,
    FEATURE_OFFER_ID,
    FEATURE_CODE,
    ENTITLED_FEATURE_STATE,
    SERVICE_STATE_ID,
    ENTITLESTATE_CHANGE_REASON_ID,
    CREATE_DATE,
    UPDATE_DATE
)
values (
    3,
    100120,
    0,
    'HOST_5_USER',
    'INACTIVE',
    103,
    null,
    '2019-08-05',
    '2019-09-05'
);
insert into WEBS.ENT_PROD_FEAT_PROCESSING (
    ENT_PROD_FEAT_PROCESSING_ID,
    ENTITLED_STATE,
    PROCESSING_STATE,
    START_DATE,
    END_DATE,
    CREATE_DATE,
    UPDATE_DATE,
    ENTITLED_PRODUCT_FEATURE_ID
)
values (
    33,
    'INACTIVE',
    'PENDING',
    null,
    null,
    DATEADD('DAY', -1 , CURRENT_DATE),
    null,
    3
);
insert into WEBS.QBES_HOSTING_REQUEST_STAGING (
    QBES_HOST_REQ_STG_ID,
    ACTIVITY_TYPE,
    EFFECTIVE_DATE,
    CAPTURE_TIME,
    CUSTOMER_ID,
    SUBSCRIPTION_ID,
    LICENSE,
    EOC,
    STATUS,
    ERROR_MESSAGE,
    ABORT_REASON,
    CREATE_DATE,
    UPDATE_DATE,
    ENT_PROD_FEAT_PROCESSING_ID
) values (
    333,
    'DELINQUENT',
    '2019-10-05',
    '2019-10-05',
    3333,
    33333,
    'Lic3',
    'EOC3',
    'INITIATED',
    null,
    null,
    DATEADD('DAY', -1 , CURRENT_DATE),
    null,
    33
);

-- Ineligible DELINQUENT
insert into WEBS.ENTITLED_PRODUCT_FEATURE (
    ENTITLED_PRODUCT_FEATURE_ID,
    ENTITLED_PRODUCT_ID,
    FEATURE_OFFER_ID,
    FEATURE_CODE,
    ENTITLED_FEATURE_STATE,
    SERVICE_STATE_ID,
    ENTITLESTATE_CHANGE_REASON_ID,
    CREATE_DATE,
    UPDATE_DATE
)
values (
    4,
    100120,
    0,
    'HOST_5_USER',
    'INACTIVE',
    103,
    null,
    '2019-08-05',
    '2019-09-05'
);
insert into WEBS.ENT_PROD_FEAT_PROCESSING (
    ENT_PROD_FEAT_PROCESSING_ID,
    ENTITLED_STATE,
    PROCESSING_STATE,
    START_DATE,
    END_DATE,
    CREATE_DATE,
    UPDATE_DATE,
    ENTITLED_PRODUCT_FEATURE_ID
)
values (
    44,
    'INACTIVE',
    'PENDING',
    null,
    null,
    CURRENT_DATE,
    null,
    4
);
insert into WEBS.QBES_HOSTING_REQUEST_STAGING (
    QBES_HOST_REQ_STG_ID,
    ACTIVITY_TYPE,
    EFFECTIVE_DATE,
    CAPTURE_TIME,
    CUSTOMER_ID,
    SUBSCRIPTION_ID,
    LICENSE,
    EOC,
    STATUS,
    ERROR_MESSAGE,
    ABORT_REASON,
    CREATE_DATE,
    UPDATE_DATE,
    ENT_PROD_FEAT_PROCESSING_ID
) values (
    444,
    'DELINQUENT',
    '2019-10-05',
    '2019-10-05',
    4444,
    44444,
    'Lic4',
    'EOC4',
    'INITIATED',
    null,
    null,
    CURRENT_DATE,
    null,
    44
);
