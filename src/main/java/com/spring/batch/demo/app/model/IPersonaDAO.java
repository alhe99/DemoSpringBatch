package com.spring.batch.demo.app.model;

import org.springframework.data.repository.CrudRepository;

public interface IPersonaDAO extends CrudRepository<Persona,Integer> {
}
