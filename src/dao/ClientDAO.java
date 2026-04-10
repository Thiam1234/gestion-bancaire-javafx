package dao;

import model.Client;
import database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    public void addClient(Client client) throws SQLException {
        String sql = "INSERT INTO client (nom, prenom, telephone, email) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, client.getNom());
            stmt.setString(2, client.getPrenom());
            stmt.setString(3, client.getTelephone());
            stmt.setString(4, client.getEmail());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                client.setId(rs.getInt(1));
            }
        }
    }

    public List<Client> getAllClients() throws SQLException {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM client";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Client c = new Client();
                c.setId(rs.getInt("id"));
                c.setNom(rs.getString("nom"));
                c.setPrenom(rs.getString("prenom"));
                c.setTelephone(rs.getString("telephone"));
                c.setEmail(rs.getString("email"));
                clients.add(c);
            }
        }
        return clients;
    }

    public void updateClient(Client client) throws SQLException {
        String sql = "UPDATE client SET nom=?, prenom=?, telephone=?, email=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, client.getNom());
            stmt.setString(2, client.getPrenom());
            stmt.setString(3, client.getTelephone());
            stmt.setString(4, client.getEmail());
            stmt.setInt(5, client.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteClient(int id) throws SQLException {
        String sql = "DELETE FROM client WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public Client getClientById(int id) throws SQLException {
        String sql = "SELECT * FROM client WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Client c = new Client();
                c.setId(rs.getInt("id"));
                c.setNom(rs.getString("nom"));
                c.setPrenom(rs.getString("prenom"));
                c.setTelephone(rs.getString("telephone"));
                c.setEmail(rs.getString("email"));
                return c;
            }
            return null;
        }
    }
}