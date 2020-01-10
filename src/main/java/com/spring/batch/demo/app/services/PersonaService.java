package com.spring.batch.demo.app.services;

import com.spring.batch.demo.app.model.Persona;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonaService {

    private static final Logger logger = LoggerFactory.getLogger(PersonaService.class);

    public List<Persona> findAllPersonas(){
        List<Persona> personas = new ArrayList<Persona>();
        for(int i =1;i<100;i++){
            Persona persona = new Persona();
            persona.setPrimerNombre("NOMBRE PERSONA: " + i);
            persona.setSegundoNombre("SEGUNDO NOMBRE PERSONA: " + i);
            persona.setTelefono("789972"+i);
            persona.setCorrelativo(i);


            personas.add(persona);
        }
        return personas;
    }

    public Persona procesarPersona(Persona persona) throws InterruptedException {

        logger.info("PROCESANDO PERSONA : " + persona.getPrimerNombre());
        //persona.setPrimerNombre(persona.getPrimerNombre().toLowerCase());
        persona.setSegundoNombre(persona.getSegundoNombre().toLowerCase());
        Thread.sleep(1000);

        return persona;
    }
}
