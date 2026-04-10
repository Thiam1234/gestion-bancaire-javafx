package model;

public class Compte {
    private int id;
    private String numeroCompte;
    private double solde;
    private String typeCompte; // "courant" ou "epargne"
    private int idClient;

    public Compte() {}

    public Compte(int id, String numeroCompte, double solde, String typeCompte, int idClient) {
        this.id = id;
        this.numeroCompte = numeroCompte;
        this.solde = solde;
        this.typeCompte = typeCompte;
        this.idClient = idClient;
    }

    // Getters et setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNumeroCompte() { return numeroCompte; }
    public void setNumeroCompte(String numeroCompte) { this.numeroCompte = numeroCompte; }

    public double getSolde() { return solde; }
    public void setSolde(double solde) { this.solde = solde; }

    public String getTypeCompte() { return typeCompte; }
    public void setTypeCompte(String typeCompte) { this.typeCompte = typeCompte; }

    public int getIdClient() { return idClient; }
    public void setIdClient(int idClient) { this.idClient = idClient; }

    @Override
    public String toString() {
        return numeroCompte + " - " + typeCompte + " (" + solde + ")";
    }
}