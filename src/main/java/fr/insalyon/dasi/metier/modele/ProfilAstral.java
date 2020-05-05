/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.dasi.metier.modele;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author oisinnolan
 */

@Entity
public class ProfilAstral implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String signeZodiaque;
    private String signeChinois;
    private String couleur; // enum ?
    private String animalTotem;
    
    @OneToOne(mappedBy = "profilAstral")
    private Client client;
    
    public ProfilAstral() {};

    public ProfilAstral(String signeZodiaque, String signeChinois, String couleur, String animalTotem) {
        this.signeZodiaque = signeZodiaque;
        this.signeChinois = signeChinois;
        this.couleur = couleur;
        this.animalTotem = animalTotem;
    }

    public String getSigneZodiaque() {
        return signeZodiaque;
    }

    public void setSigneZodiaque(String signeZodiaque) {
        this.signeZodiaque = signeZodiaque;
    }

    public String getSigneChinois() {
        return signeChinois;
    }

    public void setSigneChinois(String signeChinois) {
        this.signeChinois = signeChinois;
    }

    public String getCouleur() {
        return couleur;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    public String getAnimalTotem() {
        return animalTotem;
    }

    public void setAnimalTotem(String animalTotem) {
        this.animalTotem = animalTotem;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "ProfilAstral{" + "id=" + id + ", signeZodiaque=" + signeZodiaque + ", signeChinois=" + signeChinois + ", couleur=" + couleur + ", animalTotem=" + animalTotem + ", client=" + client + '}';
    }
}
