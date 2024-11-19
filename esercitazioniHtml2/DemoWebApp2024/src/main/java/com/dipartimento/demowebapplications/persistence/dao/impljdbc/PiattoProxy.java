package com.dipartimento.demowebapplications.persistence.dao.impljdbc;

import com.dipartimento.demowebapplications.model.Piatto;
import com.dipartimento.demowebapplications.model.Ristorante;
import com.dipartimento.demowebapplications.persistence.DBManager;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class PiattoProxy extends Piatto {
    public List<Ristorante> getRistoranti() {
        if(this.getRistoranti() == null){
            this.setRistoranti(DBManager.getInstance().getRistoranteDao().findAllByPiattoName(this.getNome()));
        }
        return this.getRistoranti();
    }
}
