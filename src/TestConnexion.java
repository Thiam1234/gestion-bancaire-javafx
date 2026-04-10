import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import database.DatabaseConnection;

public class TestConnexion {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("Connexion réussie !");
            // Petit test : afficher les clients
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM client");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + " " + rs.getString("nom") + " " + rs.getString("prenom"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}