package com.spring.batch.demo.app.writter;

import com.spring.batch.demo.app.model.IPersonaDAO;
import com.spring.batch.demo.app.model.Persona;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PersonaWritter implements ItemWriter<Persona> {

    private static  final Logger LOGGER = LoggerFactory.getLogger(PersonaWritter.class);

    @Autowired
    private IPersonaDAO personaDAO;

    @Override
    public void write(List<? extends Persona> items) throws Exception {
       personaDAO.saveAll(items);

        LOGGER.info("CANTIDAD DE PERSONAS EN WRITTER: " + items.size());
    }
}
