/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.dasi.metier.modele;

import fr.insalyon.dasi.util.Genre;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 *
 * @author oisinnolan
 */

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="User_Type", discriminatorType = DiscriminatorType.STRING)
public abstract class Utilisateur  implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected String nom;
    protected String prenom;
    @Column(unique = true)
    protected String mail;
    protected String noTelephone;
    protected String motDePasse;
    protected Genre genre; //You need to know the gender of the client, title of the client is mentioned in the message for the employee

    public Utilisateur() {};
    
    public Utilisateur(String nom, String prenom, String mail, 
                       String motDePasse, String noTelephone, Genre genre) {
        this.nom = nom;
        this.prenom = prenom;
        this.mail = mail;
        this.motDePasse = motDePasse;
        this.noTelephone = noTelephone; 
        this.genre = genre;
    }
    
    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getNoTelephone() {
        return noTelephone;
    }

    public void setNoTelephone(String noTelephone) {
        this.noTelephone = noTelephone;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public Genre getGenre() {
        return genre;
    }

    @Override
    public String toString() {
        return "id=" + id + ", nom=" + nom + ", prenom=" + prenom + ", mail=" + mail + ", noTelephone=" + noTelephone + ", motDePasse=" + motDePasse + ", genre=" + genre;
    }
    
    // Overriden for the HashMap in Statistiques class
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
            // if deriving: appendSuper(super.hashCode()).
            append(id).
            append(nom).
            append(prenom).
            append(mail).
            append(noTelephone).
            append(motDePasse).
            append(genre).
            toHashCode();
    }

    // Overriden in order to properly compare while computing statistics
    @Override
    public boolean equals(Object obj) {
       if (!(obj instanceof Utilisateur))
            return false;
        if (obj == this)
            return true;

        Utilisateur rhs = (Utilisateur) obj;
        if(this.id.equals(rhs.id) &&
                this.nom.equals(rhs.nom) &&
                this.prenom.equals(rhs.prenom) &&
                this.mail.equals(rhs.mail) &&
                this.noTelephone.equals(rhs.noTelephone) &&
                this.motDePasse.equals(rhs.motDePasse) &&
                this.genre.equals(rhs.genre))
            return true;
        else return false;
    }
}
