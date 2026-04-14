//gerer par alune badara sarr
package dao;

import model.Compte;
import database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompteDAO {

    public void addCompte(Compte compte) throws SQLException {
        String sql = "INSERT INTO compte (numero_compte, solde, type_compte, id_client) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, compte.getNumeroCompte());
            stmt.setDouble(2, compte.getSolde());
            stmt.setString(3, compte.getTypeCompte());
            stmt.setInt(4, compte.getIdClient());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                compte.setId(rs.getInt(1));
            }
        }
    }

    public List<Compte> getAllComptes() throws SQLException {
        List<Compte> comptes = new ArrayList<>();
        String sql = "SELECT * FROM compte";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Compte c = new Compte();
                c.setId(rs.getInt("id"));
                c.setNumeroCompte(rs.getString("numero_compte"));
                c.setSolde(rs.getDouble("solde"));
                c.setTypeCompte(rs.getString("type_compte"));
                c.setIdClient(rs.getInt("id_client"));
                comptes.add(c);
            }
        }
        return comptes;
    }

    public List<Compte> getComptesByClientId(int idClient) throws SQLException {
        List<Compte> comptes = new ArrayList<>();
        String sql = "SELECT * FROM compte WHERE id_client = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idClient);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Compte c = new Compte();
                c.setId(rs.getInt("id"));
                c.setNumeroCompte(rs.getString("numero_compte"));
                c.setSolde(rs.getDouble("solde"));
                c.setTypeCompte(rs.getString("type_compte"));
                c.setIdClient(rs.getInt("id_client"));
                comptes.add(c);
            }
        }
        return comptes;
    }

    public void updateCompte(Compte compte) throws SQLException {
        String sql = "UPDATE compte SET numero_compte=?, solde=?, type_compte=?, id_client=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, compte.getNumeroCompte());
            stmt.setDouble(2, compte.getSolde());
            stmt.setString(3, compte.getTypeCompte());
            stmt.setInt(4, compte.getIdClient());
            stmt.setInt(5, compte.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteCompte(int id) throws SQLException {
        String sql = "DELETE FROM compte WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public Compte getCompteById(int id) throws SQLException {
        String sql = "SELECT * FROM compte WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Compte c = new Compte();
                c.setId(rs.getInt("id"));
                c.setNumeroCompte(rs.getString("numero_compte"));
                c.setSolde(rs.getDouble("solde"));
                c.setTypeCompte(rs.getString("type_compte"));
                c.setIdClient(rs.getInt("id_client"));
                return c;
            }
            return null;
        }
    }
}