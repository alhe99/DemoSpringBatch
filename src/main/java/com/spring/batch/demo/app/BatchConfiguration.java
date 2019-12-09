package com.spring.batch.demo.app;

import javax.sql.DataSource;

import com.spring.batch.demo.app.reader.PersonReader;
import com.spring.batch.demo.app.services.PersonaService;
import com.spring.batch.demo.app.writter.PersonaWritter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.spring.batch.demo.app.listener.JobListener;
import com.spring.batch.demo.app.model.Persona;
import com.spring.batch.demo.app.processor.PersonaItemProcessor;


@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	private PersonaService personaService;

	/*@Bean
	public FlatFileItemReader<Persona> reader(){
		return new FlatFileItemReaderBuilder<Persona>()
				.name("personaItemReader")
				.resource(new ClassPathResource("sample-data.csv"))
				.delimited()
				.names(new String[] {"primerNombre","segundoNombre","telefono"})
				.fieldSetMapper(new BeanWrapperFieldSetMapper<Persona>() {{
					setTargetType(Persona.class); 
				}})
				.build();
	}*/

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
	
	/*@Bean
	public JdbcBatchItemWriter<Persona> writer(DataSource dataSource){
		return new JdbcBatchItemWriterBuilder<Persona>()
				.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Persona>())
				.sql("INSERT INTO personas (primer_nombre,segundo_nombre,telefono) VALUES (:primerNombre,:segundoNombre,:telefono)")
				.dataSource(dataSource)
				.build();
	}*/
	
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
				.<Persona,Persona>chunk(10)
				.reader(personaItemReader())
				.processor(processor())
				.writer(personaWritter())
				.build();
	}
}
