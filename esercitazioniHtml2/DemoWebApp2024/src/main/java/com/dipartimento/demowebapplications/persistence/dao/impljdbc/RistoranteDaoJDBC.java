package com.dipartimento.demowebapplications.persistence.dao.impljdbc;

import com.dipartimento.demowebapplications.model.Piatto;
import com.dipartimento.demowebapplications.model.Ristorante;
import com.dipartimento.demowebapplications.persistence.DBManager;
import com.dipartimento.demowebapplications.persistence.dao.PiattoDao;
import com.dipartimento.demowebapplications.persistence.dao.RistoranteDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RistoranteDaoJDBC implements RistoranteDao {
    Connection connection = null;


    public RistoranteDaoJDBC(Connection conn){

        this.connection = conn;
    }



    @Override
    public List<Ristorante> findAll() {
        List<Ristorante> ristoranti = new ArrayList<Ristorante>();
        String query = "select * from ristorante";

        System.out.println("going to execute:"+query);

        Statement st = null;
        try {
            st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()){
                Ristorante rist = new RistoranteProxy();
                rist.setNome(rs.getString("nome"));
                rist.setDescrizione(rs.getString("descrizione"));
                rist.setUbicazione(rs.getString("ubicazione"));
                ristoranti.add(rist);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ristoranti;
    }

    @Override
    public Ristorante findById(String nome) {

        String query = "SELECT nome, descrizione, ubicazione FROM ristorante WHERE nome = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nome);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String descrizione = resultSet.getString("descrizione");
                String ubicazione = resultSet.getString("ubicazione");
                RistoranteProxy rist = new RistoranteProxy();
                rist.setNome(nome);
                rist.setDescrizione(descrizione);
                rist.setUbicazione(ubicazione);
                return rist;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void create(Ristorante ristorante) {
        String query = "INSERT INTO ristorante (nome, descrizione, ubicazione) VALUES (?, ?, ?) " +
                "ON CONFLICT (nome) DO UPDATE SET " +
                "   descrizione = EXCLUDED.descrizione , "+
                "   ubicazione = EXCLUDED.ubicazione ";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, ristorante.getNome());
            statement.setString(2, ristorante.getDescrizione());
            statement.setString(3, ristorante.getUbicazione());
            statement.executeUpdate();

            List<Piatto> piatti = ristorante.getPiatti();
            if(piatti==null || piatti.isEmpty()){
                return;
            }

            // reset all relation present in the join table
            restRelationsPResentInTheJoinTable(connection , ristorante.getNome());

            PiattoDao pd = DBManager.getInstance().getPiattoDao();

            for (Piatto tempP : piatti) {
                pd.create(tempP);
                insertJoinRistorantePiatto(connection , ristorante.getNome() , tempP.getNome());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void restRelationsPResentInTheJoinTable(Connection connection, String nomeRistorante) throws Exception {

        String query="Delete FROM ristorante_piatto WHERE ristorante_nome= ? ";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, nomeRistorante);


        preparedStatement.execute();

    }

    private void insertJoinRistorantePiatto(Connection connection , String nomeRistorante, String nomePiatto) throws SQLException {

        String query="INSERT INTO ristorante_piatto (ristorante_nome,piatto_nome) VALUES (? , ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, nomeRistorante);
        preparedStatement.setString(2, nomePiatto);

        preparedStatement.execute();

    }


    @Override
    public void delete(Ristorante ristorante) {
        String deleteJoinQuery = "DELETE FROM ristorante_piatto WHERE ristorante_nome = ?";
        String deleteRistoranteQuery = "DELETE FROM ristorante WHERE nome = ?";

        try (PreparedStatement deleteJoinStmt = connection.prepareStatement(deleteJoinQuery);
             PreparedStatement deleteRistoranteStmt = connection.prepareStatement(deleteRistoranteQuery)) {

            // Rimuove tutte le associazioni di quel ristorante nella tabella di join
            deleteJoinStmt.setString(1, ristorante.getNome());
            deleteJoinStmt.executeUpdate();

            // Elimina il ristorante dalla tabella principale
            deleteRistoranteStmt.setString(1, ristorante.getNome());
            deleteRistoranteStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Ristorante ristorante) {
        String updateRistoranteQuery = "UPDATE ristorante SET descrizione = ?, ubicazione = ? WHERE nome = ?";

        try (PreparedStatement updateRistoranteStmt = connection.prepareStatement(updateRistoranteQuery)) {

            // Aggiorna i dettagli del ristorante
            updateRistoranteStmt.setString(1, ristorante.getDescrizione());
            updateRistoranteStmt.setString(2, ristorante.getUbicazione());
            updateRistoranteStmt.setString(3, ristorante.getNome());
            updateRistoranteStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {
        RistoranteDao ristoDao = DBManager.getInstance().getRistoranteDao();
        List<Ristorante> ristoranti = ristoDao.findAll();
        for (Ristorante ristorante : ristoranti) {
            System.out.println(ristorante.getNome());
            System.out.println(ristorante.getDescrizione());
            System.out.println(ristorante.getUbicazione());
        }
    }

    @Override
    public List<Ristorante> findAllByPiattoName(String piattoNome){
        List<Ristorante> ristoranti = new ArrayList<>();
        String query = "SELECT r.nome, r.descrizione, r.ubicazione, r.piatti FROM ristorante r" +
                "JOIN ristorante_piatto rp ON r.piattoNome =rp.ristorante_nome" +
                "WHERE rp.piatto_nome = ?";

        System.out.println("going to execute:"+query);

        try (PreparedStatement statement =connection.prepareStatement(query)){
            statement.setString(1, piattoNome);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String nome = resultSet.getString("nome");
                String descrizione = resultSet.getString("descrizione");
                String ubicazione = resultSet.getString("ubicazione");
                List<Piatto> piatti= (List<Piatto>) resultSet.getArray("piatti");
                ristoranti.add(new Ristorante(nome, descrizione, ubicazione, piatti));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return ristoranti;
    }
}


