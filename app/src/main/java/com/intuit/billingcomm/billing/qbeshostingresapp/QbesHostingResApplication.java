package com.intuit.billingcomm.billing.qbeshostingresapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.intuit.billingcomm.billing.qbeshostingresapp.service.ProcessOrchestrator;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({
        "com.intuit.billingcomm.billing.qbeshostingresapp",
        "com.intuit.billingcomm.billing.qbescommon",
        "com.intuit.billingcomm.billing.qbespfts",
        "com.intuit.platform.webs.conversion.model.config"
})
public class QbesHostingResApplication implements ApplicationRunner {

    private final Logger LOGGER = LoggerFactory.getLogger(QbesHostingResApplication.class);

    @Autowired
    private ProcessOrchestrator processOrchestrator;

    public static void main(String[] args) {
        SpringApplication.run(QbesHostingResApplication.class, args).close();
    }

    @Override
    public void run(ApplicationArguments args) {
        LOGGER.info("Starting QBES Hosting Response Processor");
        processOrchestrator.orchestrate();
        LOGGER.info("QBES Hosting Res Java job finished.");
    }
}
