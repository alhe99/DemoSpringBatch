package com.spring.batch.demo.app;

import javax.sql.DataSource;

import com.spring.batch.demo.app.listener.StepListenerPersona;
import com.spring.batch.demo.app.reader.PersonReader;
import com.spring.batch.demo.app.services.PersonaService;
import com.spring.batch.demo.app.tasklet.DummyTasklet;
import com.spring.batch.demo.app.writter.PersonaWritter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.spring.batch.demo.app.listener.JobListener;
import com.spring.batch.demo.app.model.Persona;
import com.spring.batch.demo.app.processor.PersonaItemProcessor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.net.MalformedURLException;
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


	/*@Bean(name = "asyncExecutor")
	public TaskExecutor getAsyncExecutor()
	{
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5);
		executor.setMaxPoolSize(5);
		executor.setQueueCapacity(100);
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.setThreadNamePrefix("AsyncExecutor-");
		return executor;
	}*/

	@Bean
	public ItemProcessor<Persona, Future<Persona>> asyncItemProcessor(){
		AsyncItemProcessor<Persona, Persona> asyncItemProcessor = new AsyncItemProcessor<>();
		asyncItemProcessor.setDelegate(processor());
		asyncItemProcessor.setTaskExecutor(taskExecutor());
		return asyncItemProcessor;
	}

	@Bean
	public ItemWriter<Future<Persona>> asyncItemWriter(){
		AsyncItemWriter<Persona> asyncItemWriter = new AsyncItemWriter<>();
		asyncItemWriter.setDelegate(personaWritter());
		return asyncItemWriter;
	}


	@Bean
	public PersonReader personaItemReader(){
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
	public Step slaveStep()
			throws UnexpectedInputException, MalformedURLException, ParseException {
		return stepBuilderFactory.get("slaveStep").<Persona, Persona>chunk(1)
				.reader(personaItemReader())
				.processor(processor())
				.writer(personaWritter())
				.build();
	}

	@Bean
	public Step partitionStep()
			throws UnexpectedInputException, MalformedURLException, ParseException {
		return stepBuilderFactory.get("partitionStep")
				.partitioner("slaveStep", rangePartitioner())
				.step(slaveStep())
				.taskExecutor(taskExecutor())
				.build();
	}

	@Bean
	public Job PartitionJob() {
		return jobBuilderFactory.get("partitionJob").incrementer(new RunIdIncrementer()).listener(new JobListener())
				.start(masterStep()).next(step2()).build();
	}

	@Bean
	public Step step2() {
		return stepBuilderFactory.get("step2").tasklet(dummyTask()).build();
	}

	@Bean
	public DummyTasklet dummyTask() {
		return new DummyTasklet();
	}

	@Bean
	public Step masterStep() {
		return stepBuilderFactory.get("masterStep").partitioner(slave().getName(), rangePartitioner()).listener(new StepListenerPersona())
				.partitionHandler(masterSlaveHandler()).build();
	}

	@Bean
	public PartitionHandler masterSlaveHandler() {
		TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();
		handler.setGridSize(10);
		handler.setTaskExecutor(taskExecutor());
		handler.setStep(slave());
		try {
			handler.afterPropertiesSet();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return handler;
	}
	@Bean(name = "slave")
	public Step slave() {
		return stepBuilderFactory.get("slave").<Persona, Future<Persona>>chunk(1)
				.reader(personaItemReader())
				.processor(asyncItemProcessor()).writer(asyncItemWriter()).build();
	}

	@Bean
	public RangePartitioner rangePartitioner() {
		return new RangePartitioner();
	}

	@Bean
	public SimpleAsyncTaskExecutor taskExecutor() {
		return new SimpleAsyncTaskExecutor();
	}
}
