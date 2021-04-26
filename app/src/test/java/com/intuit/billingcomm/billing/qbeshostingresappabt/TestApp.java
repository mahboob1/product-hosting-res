package com.intuit.billingcomm.billing.qbeshostingresappabt;

import com.intuit.billingcomm.billing.qbescommon.config.DataConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.intuit.billingcomm.billing.qbeshostingresappabt.config",
        "com.intuit.billingcomm.billing.qbeshostingresapp.service",
        "com.intuit.billingcomm.billing.qbescommon",
        "com.intuit.billingcomm.billing.qbespfts"
},
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = DataConfig.class)
)
public class TestApp {}
