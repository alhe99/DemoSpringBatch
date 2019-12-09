package com.spring.batch.demo.app.reader;

import com.spring.batch.demo.app.model.Persona;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

import java.util.List;

public class PersonReader implements ItemReader<Persona> {

    private int nextIndex;

    private List<Persona> personas;

    private static final Logger logger = LoggerFactory.getLogger(PersonReader.class);

    public PersonReader(List<Persona> personas) {
        this.nextIndex = 0;
        this.personas = personas;
    }

    @Override
    public Persona read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        Persona persona = null;
        logger.info("INICIAL EL READER");
        if (this.nextIndex < personas.size()){
            persona = personas.get(this.nextIndex);
            this.nextIndex++;
        }
        return persona;
    }
}
