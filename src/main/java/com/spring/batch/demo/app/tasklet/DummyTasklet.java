package com.spring.batch.demo.app.tasklet;

import com.spring.batch.demo.app.processor.PersonaItemProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class DummyTasklet implements Tasklet {

    private static final Logger LOG = LoggerFactory.getLogger(DummyTasklet.class);

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
            throws Exception {
        LOG.info("Dummy Tasklet called.");
        return RepeatStatus.FINISHED;
    }
}