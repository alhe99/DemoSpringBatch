package com.spring.batch.demo.app.processor;

import com.spring.batch.demo.app.services.PersonaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.spring.batch.demo.app.model.Persona;
import org.springframework.beans.factory.annotation.Autowired;

public class PersonaItemProcessor implements ItemProcessor<Persona,Persona> {
	
	private static final Logger LOG = LoggerFactory.getLogger(PersonaItemProcessor.class);

	@Autowired
	private PersonaService personaService;

	public PersonaItemProcessor(){

	}

	@Override
	public Persona process(Persona item) throws Exception {
		String primerNombre = item.getPrimerNombre().toUpperCase();
		String segundoNombre = item.getSegundoNombre().toUpperCase();
		int corre = item.getCorrelativo();

		Persona persona = new Persona(primerNombre,segundoNombre,item.getTelefono());
		LOG.info("PERSONA A PROCESAR: " + persona.getPrimerNombre());
		persona.setCorrelativo(corre);

		return personaService.procesarPersona(persona);
	}

}
