package com.dipartimento.demowebapplications.persistence.dao.impljdbc;

import com.dipartimento.demowebapplications.model.Piatto;
import com.dipartimento.demowebapplications.model.Ristorante;
import com.dipartimento.demowebapplications.persistence.DBManager;

import java.util.ArrayList;
import java.util.List;

public class PiattoProxy extends Piatto {
    public List<Ristorante> getRistoranti() {
        if(this.getRistoranti() == null){
            this.setRistoranti(DBManager.getInstance().getRistoranteDao().findAllByPiattoName(this.getNome()));
        }
        return this.getRistoranti();
    }
}
