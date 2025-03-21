/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.dasi.metier.modele;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author oisinnolan
 */
@Entity
public class Consultation implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateCreation;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date debut;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fin;
    private String commentaire;
    
    @ManyToOne(cascade = CascadeType.MERGE)
    private Employe employe;
    @ManyToOne(cascade = CascadeType.MERGE)
    private Client client;
    @ManyToOne()
    private Medium medium;
    
    public Consultation() {};
    
    public Consultation(Employe employe, Client client, Medium medium) {
        this.employe = employe;
        this.client = client;
        this.medium = medium;
        this.dateCreation = new Date();
    }

    public Long getId() {
        return id;
    }

    public Date getDebut() {
        return debut;
    }

    public void setDebut(Date debut) {
        this.debut = debut;
    }

    public Date getFin() {
        return fin;
    }

    public void setFin(Date fin) {
        this.fin = fin;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Employe getEmploye() {
        return employe;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    public Medium getMedium() {
        return medium;
    }

    public void setMedium(Medium medium) {
        this.medium = medium;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    @Override
    public String toString() {
        return "Consultation{" + "id=" + id + ", dateCreation=" + dateCreation + ", debut=" + debut + ", fin=" + fin + ", commentaire=" + commentaire + ", employe=" + employe + ", client=" + client + ", medium=" + medium + '}';
    }
    
}
