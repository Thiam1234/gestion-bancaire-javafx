package dao;

import model.Operation;
import database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OperationDAO {

    public void addOperation(Operation operation) throws SQLException {
        String sql = "INSERT INTO operation (type_operation, montant, date_operation, id_compte) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, operation.getTypeOperation());
            stmt.setDouble(2, operation.getMontant());
            stmt.setTimestamp(3, operation.getDateOperation());
            stmt.setInt(4, operation.getIdCompte());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                operation.setId(rs.getInt(1));
            }
        }
    }

    public List<Operation> getOperationsByCompteId(int idCompte) throws SQLException {
        List<Operation> operations = new ArrayList<>();
        String sql = "SELECT * FROM operation WHERE id_compte = ? ORDER BY date_operation DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCompte);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Operation op = new Operation();
                op.setId(rs.getInt("id"));
                op.setTypeOperation(rs.getString("type_operation"));
                op.setMontant(rs.getDouble("montant"));
                op.setDateOperation(rs.getTimestamp("date_operation"));
                op.setIdCompte(rs.getInt("id_compte"));
                operations.add(op);
            }
        }
        return operations;
    }

    public List<Operation> getAllOperations() throws SQLException {
        List<Operation> operations = new ArrayList<>();
        String sql = "SELECT * FROM operation ORDER BY date_operation DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Operation op = new Operation();
                op.setId(rs.getInt("id"));
                op.setTypeOperation(rs.getString("type_operation"));
                op.setMontant(rs.getDouble("montant"));
                op.setDateOperation(rs.getTimestamp("date_operation"));
                op.setIdCompte(rs.getInt("id_compte"));
                operations.add(op);
            }
        }
        return operations;
    }
}
