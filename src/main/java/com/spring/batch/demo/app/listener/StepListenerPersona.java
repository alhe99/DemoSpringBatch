package com.spring.batch.demo.app.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepListener;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.stereotype.Component;

@Component
public class StepListenerPersona extends StepExecutionListenerSupport {

    private static  final Logger LOGGER = LoggerFactory.getLogger(StepListener.class);
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        LOGGER.info("CANTIDAD DE PERSONAS LEIDAS: " + stepExecution.getReadCount());
        LOGGER.info("CANTIDAD DE PERSONAS ESCRITAS: " + stepExecution.getWriteCount());
        return null;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        super.beforeStep(stepExecution);
    }
}
