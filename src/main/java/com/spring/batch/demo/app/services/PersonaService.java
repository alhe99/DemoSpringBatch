package com.spring.batch.demo.app.services;

import com.spring.batch.demo.app.model.Persona;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonaService {

    public List<Persona> findAllPersonas(){
        List<Persona> personas = new ArrayList<Persona>();
        for(int i =0;i<9000;i++){
            Persona persona = new Persona();
            persona.setPrimerNombre("NOMBRE PERSONA: " + i);
            persona.setSegundoNombre("SEGUNDO NOMBRE PERSONA: " + i);
            persona.setTelefono("789972"+i);

            personas.add(persona);
        }
        return personas;
    }
}
