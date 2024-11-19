package com.dipartimento.demowebapplications.persistence.dao;

import com.dipartimento.demowebapplications.model.Piatto;
import com.dipartimento.demowebapplications.model.Ristorante;

import java.util.List;

public interface PiattoDao {


    public List<Piatto> findAll();

    public Piatto findById(String nome);

    public void create(Piatto piatto);

    public void delete(Piatto piatto);  //implementata funzione delete, in PiattoDaoJDBC

    List<Piatto> findAllByRistoranteName(String name);

    public void update(Piatto piatto);


}
