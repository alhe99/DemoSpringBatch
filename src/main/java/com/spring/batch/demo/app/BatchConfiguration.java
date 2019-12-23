package com.spring.batch.demo.app;

import javax.sql.DataSource;

import com.spring.batch.demo.app.listener.StepListenerPersona;
import com.spring.batch.demo.app.reader.PersonReader;
import com.spring.batch.demo.app.services.PersonaService;
import com.spring.batch.demo.app.writter.PersonaWritter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.spring.batch.demo.app.listener.JobListener;
import com.spring.batch.demo.app.model.Persona;
import com.spring.batch.demo.app.processor.PersonaItemProcessor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;


@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	private PersonaService personaService;

	@Bean(name = "asyncExecutor")
	public TaskExecutor getAsyncExecutor()
	{
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5);
		executor.setMaxPoolSize(5);
		executor.setQueueCapacity(100);
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.setThreadNamePrefix("AsyncExecutor-");
		return executor;
	}

	@Bean
	public ItemProcessor<Persona, Future<Persona>> asyncItemProcessor(){
		AsyncItemProcessor<Persona, Persona> asyncItemProcessor = new AsyncItemProcessor<>();
		asyncItemProcessor.setDelegate(processor());
		asyncItemProcessor.setTaskExecutor(getAsyncExecutor());
		return asyncItemProcessor;
	}

	@Bean
	public ItemWriter<Future<Persona>> asyncItemWriter(){
		AsyncItemWriter<Persona> asyncItemWriter = new AsyncItemWriter<>();
		asyncItemWriter.setDelegate(personaWritter());
		return asyncItemWriter;
	}

	@Bean
	public ItemReader<Persona> personaItemReader(){
		return new PersonReader(personaService.findAllPersonas());
	}
	
	@Bean
	public PersonaItemProcessor processor () {
		return new PersonaItemProcessor();
	}

	@Bean
	public PersonaWritter personaWritter(){
		return new PersonaWritter();
	}

	@Bean
	public Job importPersonaJob(JobListener listener,Step step1) {
		return jobBuilderFactory.get("importPersonaJob")
				.incrementer(new RunIdIncrementer())
				.listener(listener)
				.flow(step1)
				.end()
				.build();
	}
	
	@Bean
	public Step step1(PersonaWritter writer) {
		return stepBuilderFactory.get("step1")
				.<Persona, Future<Persona>>chunk(100)
				.reader(personaItemReader())
				.processor(asyncItemProcessor())
				.writer(asyncItemWriter())
				.listener(new StepListenerPersona())
				.build();
	}
}
