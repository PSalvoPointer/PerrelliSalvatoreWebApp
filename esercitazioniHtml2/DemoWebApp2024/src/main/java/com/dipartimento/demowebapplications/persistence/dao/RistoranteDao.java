package com.dipartimento.demowebapplications.persistence.dao;

import com.dipartimento.demowebapplications.model.Ristorante;

import java.util.List;

public interface RistoranteDao {
    public List<Ristorante> findAll();
    public Ristorante findById(String nome);
    public void create(Ristorante ristorante);


    public void delete(Ristorante ristorante);
    public void update(Ristorante ristorante);

    List<Ristorante> findAllByPiattoName(String nome);
}
