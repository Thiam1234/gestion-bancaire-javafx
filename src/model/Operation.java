package model;

import java.sql.Timestamp;

public class Operation {
    private int id;
    private String typeOperation; // "depot", "retrait", "virement"
    private double montant;
    private Timestamp dateOperation;
    private int idCompte;

    public Operation() {}

    public Operation(int id, String typeOperation, double montant, Timestamp dateOperation, int idCompte) {
        this.id = id;
        this.typeOperation = typeOperation;
        this.montant = montant;
        this.dateOperation = dateOperation;
        this.idCompte = idCompte;
    }

    // Getters et setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTypeOperation() { return typeOperation; }
    public void setTypeOperation(String typeOperation) { this.typeOperation = typeOperation; }

    public double getMontant() { return montant; }
    public void setMontant(double montant) { this.montant = montant; }

    public Timestamp getDateOperation() { return dateOperation; }
    public void setDateOperation(Timestamp dateOperation) { this.dateOperation = dateOperation; }

    public int getIdCompte() { return idCompte; }
    public void setIdCompte(int idCompte) { this.idCompte = idCompte; }
}