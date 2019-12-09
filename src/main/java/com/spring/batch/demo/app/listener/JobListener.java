package com.spring.batch.demo.app.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.spring.batch.demo.app.model.Persona;

import java.util.concurrent.TimeUnit;

@Component
public class JobListener extends JobExecutionListenerSupport {
	
	private static final Logger LOG = LoggerFactory.getLogger(JobListener.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public void beforeJob(JobExecution jobExecution) {
		LOG.info("INICIA EL JOB EN :  " + jobExecution.getStartTime());
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
			long diff = jobExecution.getEndTime().getTime() - jobExecution.getStartTime().getTime();
			LOG.info("FINALIZO EL JOB EN :  " + TimeUnit.SECONDS.convert(diff, TimeUnit.MILLISECONDS));
			
			/*jdbcTemplate.query("SELECT primer_nombre,segundo_nombre,telefono FROM personas",
					(rs,row) -> new Persona(rs.getString(1), rs.getString(2), rs.getString(3)))
			.forEach(persona -> LOG.info("REGISTRO < "+persona+" > "));*/
		}
	}

}
