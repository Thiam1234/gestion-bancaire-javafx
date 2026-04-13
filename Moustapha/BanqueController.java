// Gérer par Moustapha Thiam,Ibrahima Faye,Alune Badara Sarr
package controller;

import dao.ClientDAO;
import dao.CompteDAO;
import dao.OperationDAO;
import model.Client;
import model.Compte;
import model.Operation;
import database.DatabaseConnection;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.sql.*;
import java.util.List;

public class BanqueController {

    @FXML private TextField txtNom, txtPrenom, txtTelephone, txtEmail, txtRechercheClient;
    @FXML private TableView<Client> tableClients;
    @FXML private TableColumn<Client, Integer> colClientId;
    @FXML private TableColumn<Client, String> colClientNom, colClientPrenom, colClientTel, colClientEmail;

    @FXML private TextField txtNumeroCompte, txtSoldeInitial;
    @FXML private ComboBox<String> comboTypeCompte;
    @FXML private ComboBox<Client> comboClient;
    @FXML private TableView<Compte> tableComptes;
    @FXML private TableColumn<Compte, Integer> colCompteId;
    @FXML private TableColumn<Compte, String> colCompteNumero, colCompteType;
    @FXML private TableColumn<Compte, Double> colCompteSolde;
    @FXML private TableColumn<Compte, Integer> colCompteIdClient;

    @FXML private ComboBox<Compte> comboCompteOperation, compteSource, compteDestination;
    @FXML private TextField txtMontant, txtMontantVirement;
    @FXML private TableView<Operation> tableOperations;
    @FXML private TableColumn<Operation, Integer> colOpId;
    @FXML private TableColumn<Operation, String> colOpType;
    @FXML private TableColumn<Operation, Double> colOpMontant;
    @FXML private TableColumn<Operation, String> colOpDate;

    private ClientDAO clientDAO = new ClientDAO();
    private CompteDAO compteDAO = new CompteDAO();
    private OperationDAO operationDAO = new OperationDAO();
    private ObservableList<Client> clientsList = FXCollections.observableArrayList();
    private ObservableList<Compte> comptesList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colClientId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colClientNom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
        colClientPrenom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPrenom()));
        colClientTel.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTelephone()));
        colClientEmail.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));

        colCompteId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colCompteNumero.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNumeroCompte()));
        colCompteSolde.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getSolde()).asObject());
        colCompteType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTypeCompte()));
        colCompteIdClient.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdClient()).asObject());

        colOpId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colOpType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTypeOperation()));
        colOpMontant.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getMontant()).asObject());
        colOpDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateOperation().toString()));

        comboTypeCompte.setItems(FXCollections.observableArrayList("courant", "epargne"));

        chargerClients();
        chargerComptes();

        
        tableClients.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            if (selected != null) {
                txtNom.setText(selected.getNom());
                txtPrenom.setText(selected.getPrenom());
                txtTelephone.setText(selected.getTelephone());
                txtEmail.setText(selected.getEmail());
            }
        });

        
        tableComptes.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            if (selected != null) {
                txtNumeroCompte.setText(selected.getNumeroCompte());
                txtSoldeInitial.setText(String.valueOf(selected.getSolde()));
                comboTypeCompte.setValue(selected.getTypeCompte());
                Client client = trouverClientParId(selected.getIdClient());
                if (client != null) comboClient.setValue(client);
            }
        });

        comboClient.setItems(clientsList);
        mettreAJourComboComptes();
    }

    private void chargerClients() {
        try {
            clientsList.setAll(clientDAO.getAllClients());
            tableClients.setItems(clientsList);
            comboClient.setItems(clientsList);
        } catch (SQLException e) {
            afficherErreur("Erreur chargement clients", e);
        }
    }

    private void chargerComptes() {
        try {
            comptesList.setAll(compteDAO.getAllComptes());
            tableComptes.setItems(comptesList);
        } catch (SQLException e) {
            afficherErreur("Erreur chargement comptes", e);
        }
    }

    private void mettreAJourComboComptes() {
        try {
            List<Compte> comptes = compteDAO.getAllComptes();
            comboCompteOperation.setItems(FXCollections.observableArrayList(comptes));
            compteSource.setItems(FXCollections.observableArrayList(comptes));
            compteDestination.setItems(FXCollections.observableArrayList(comptes));
        } catch (SQLException e) {
            afficherErreur("Erreur chargement comptes pour opérations", e);
        }
    }

    private Client trouverClientParId(int id) {
        return clientsList.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
    }

    @FXML
    private void ajouterClient() {
        Client client = new Client();
        client.setNom(txtNom.getText());
        client.setPrenom(txtPrenom.getText());
        client.setTelephone(txtTelephone.getText());
        client.setEmail(txtEmail.getText());
        try {
            clientDAO.addClient(client);
            chargerClients();
            viderChampsClient();
        } catch (SQLException e) {
            afficherErreur("Erreur ajout client", e);
        }
    }

    @FXML
    private void modifierClient() {
        Client selected = tableClients.getSelectionModel().getSelectedItem();
        if (selected == null) {
            alert("Veuillez sélectionner un client.");
            return;
        }
        selected.setNom(txtNom.getText());
        selected.setPrenom(txtPrenom.getText());
        selected.setTelephone(txtTelephone.getText());
        selected.setEmail(txtEmail.getText());
        try {
            clientDAO.updateClient(selected);
            chargerClients();
        } catch (SQLException e) {
            afficherErreur("Erreur modification client", e);
        }
    }

    @FXML
    private void supprimerClient() {
        Client selected = tableClients.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        try {
            clientDAO.deleteClient(selected.getId());
            chargerClients();
            viderChampsClient();
        } catch (SQLException e) {
            afficherErreur("Erreur suppression client", e);
        }
    }

    @FXML
    private void rechercherClient() {
        String mot = txtRechercheClient.getText().toLowerCase();
        if (mot.isEmpty()) {
            tableClients.setItems(clientsList);
            return;
        }
        ObservableList<Client> filtered = clientsList.filtered(c ->
                c.getNom().toLowerCase().contains(mot) ||
                c.getPrenom().toLowerCase().contains(mot) ||
                c.getEmail().toLowerCase().contains(mot));
        tableClients.setItems(filtered);
    }

    private void viderChampsClient() {
        txtNom.clear();
        txtPrenom.clear();
        txtTelephone.clear();
        txtEmail.clear();
    }

    @FXML
    private void ajouterCompte() {
        if (comboClient.getValue() == null) {
            alert("Sélectionnez un client.");
            return;
        }
        Compte compte = new Compte();
        compte.setNumeroCompte(txtNumeroCompte.getText());
        compte.setSolde(Double.parseDouble(txtSoldeInitial.getText()));
        compte.setTypeCompte(comboTypeCompte.getValue());
        compte.setIdClient(comboClient.getValue().getId());
        try {
            compteDAO.addCompte(compte);
            chargerComptes();
            mettreAJourComboComptes();
            viderChampsCompte();
        } catch (SQLException e) {
            afficherErreur("Erreur ajout compte", e);
        }
    }

    @FXML
    private void modifierCompte() {
        Compte selected = tableComptes.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        selected.setNumeroCompte(txtNumeroCompte.getText());
        selected.setSolde(Double.parseDouble(txtSoldeInitial.getText()));
        selected.setTypeCompte(comboTypeCompte.getValue());
        selected.setIdClient(comboClient.getValue().getId());
        try {
            compteDAO.updateCompte(selected);
            chargerComptes();
            mettreAJourComboComptes();
        } catch (SQLException e) {
            afficherErreur("Erreur modification compte", e);
        }
    }

    @FXML
    private void supprimerCompte() {
        Compte selected = tableComptes.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        try {
            compteDAO.deleteCompte(selected.getId());
            chargerComptes();
            mettreAJourComboComptes();
            viderChampsCompte();
        } catch (SQLException e) {
            afficherErreur("Erreur suppression compte", e);
        }
    }

    private void viderChampsCompte() {
        txtNumeroCompte.clear();
        txtSoldeInitial.clear();
        comboTypeCompte.setValue(null);
        comboClient.setValue(null);
    }

    @FXML
    private void depot() {
        Compte c = comboCompteOperation.getValue();
        if (c == null) { alert("Choisissez un compte"); return; }
        double montant;
        try {
            montant = Double.parseDouble(txtMontant.getText());
            if (montant <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            alert("Montant invalide");
            return;
        }
        try {
            c.setSolde(c.getSolde() + montant);
            compteDAO.updateCompte(c);
            Operation op = new Operation();
            op.setTypeOperation("depot");
            op.setMontant(montant);
            op.setDateOperation(new Timestamp(System.currentTimeMillis()));
            op.setIdCompte(c.getId());
            operationDAO.addOperation(op);
            chargerComptes();
            mettreAJourComboComptes();
            afficherHistorique(c);
            txtMontant.clear();
            alert("Dépôt effectué");
        } catch (SQLException e) {
            afficherErreur("Erreur dépôt", e);
        }
    }

    @FXML
    private void retrait() {
        Compte c = comboCompteOperation.getValue();
        if (c == null) { alert("Choisissez un compte"); return; }
        double montant;
        try {
            montant = Double.parseDouble(txtMontant.getText());
            if (montant <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            alert("Montant invalide");
            return;
        }
        if (c.getSolde() < montant) {
            alert("Solde insuffisant");
            return;
        }
        try {
            c.setSolde(c.getSolde() - montant);
            compteDAO.updateCompte(c);
            Operation op = new Operation();
            op.setTypeOperation("retrait");
            op.setMontant(montant);
            op.setDateOperation(new Timestamp(System.currentTimeMillis()));
            op.setIdCompte(c.getId());
            operationDAO.addOperation(op);
            chargerComptes();
            mettreAJourComboComptes();
            afficherHistorique(c);
            txtMontant.clear();
            alert("Retrait effectué");
        } catch (SQLException e) {
            afficherErreur("Erreur retrait", e);
        }
    }

    @FXML
    private void virement() {
        Compte source = compteSource.getValue();
        Compte dest = compteDestination.getValue();
        if (source == null || dest == null) {
            alert("Sélectionnez les deux comptes");
            return;
        }
        if (source.getId() == dest.getId()) {
            alert("Le compte source et destination doivent être différents");
            return;
        }
        double montant;
        try {
            montant = Double.parseDouble(txtMontantVirement.getText());
            if (montant <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            alert("Montant invalide");
            return;
        }
        if (source.getSolde() < montant) {
            alert("Solde insuffisant sur le compte source");
            return;
        }
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            source.setSolde(source.getSolde() - montant);
            compteDAO.updateCompte(source);
            Operation opDebit = new Operation();
            opDebit.setTypeOperation("virement");
            opDebit.setMontant(-montant);
            opDebit.setDateOperation(new Timestamp(System.currentTimeMillis()));
            opDebit.setIdCompte(source.getId());
            operationDAO.addOperation(opDebit);

            dest.setSolde(dest.getSolde() + montant);
            compteDAO.updateCompte(dest);
            Operation opCredit = new Operation();
            opCredit.setTypeOperation("virement");
            opCredit.setMontant(montant);
            opCredit.setDateOperation(new Timestamp(System.currentTimeMillis()));
            opCredit.setIdCompte(dest.getId());
            operationDAO.addOperation(opCredit);

            conn.commit();
            chargerComptes();
            mettreAJourComboComptes();
            afficherHistorique(source);
            txtMontantVirement.clear();
            alert("Virement effectué");
        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            afficherErreur("Erreur virement", e);
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    private void afficherHistorique(Compte compte) {
        try {
            List<Operation> ops = operationDAO.getOperationsByCompteId(compte.getId());
            tableOperations.setItems(FXCollections.observableArrayList(ops));
        } catch (SQLException e) {
            afficherErreur("Erreur chargement historique", e);
        }
    }

    private void alert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        alert.showAndWait();
    }

    private void afficherErreur(String titre, Exception e) {
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR, titre + " : " + e.getMessage(), ButtonType.OK);
        alert.showAndWait();
    }
}
