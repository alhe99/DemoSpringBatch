package com.spring.batch.demo.app.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.spring.batch.demo.app.model.Persona;

public class PersonaItemProcessor implements ItemProcessor<Persona,Persona> {
	
	private static final Logger LOG = LoggerFactory.getLogger(PersonaItemProcessor.class);
	
	@Override
	public Persona process(Persona item) throws Exception {
		String primerNombre = item.getPrimerNombre().toUpperCase();
		String segundoNombre = item.getSegundoNombre().toUpperCase();
		
		Persona persona = new Persona(primerNombre,segundoNombre,item.getTelefono());
		
		LOG.info("Convirtiendo ("+item+") a ("+persona+")");
		return persona;
	}

}
