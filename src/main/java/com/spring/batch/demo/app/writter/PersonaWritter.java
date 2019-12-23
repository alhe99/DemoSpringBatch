package com.spring.batch.demo.app.writter;

import com.spring.batch.demo.app.model.Persona;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class PersonaWritter implements ItemWriter<Persona> {

    private static  final Logger LOGGER = LoggerFactory.getLogger(PersonaWritter.class);

    @Override
    public void write(List<? extends Persona> items) throws Exception {
        /*items.forEach(persona ->{
            LOGGER.info("SAVE PERSONA: " + persona.getPrimerNombre());
        });*/
    }
}
