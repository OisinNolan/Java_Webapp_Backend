package fr.insalyon.dasi.metier.modele;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;

/**
 *
 * @author DASI Team
 */
@Entity
public class Client extends Utilisateur implements Serializable{

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateNaissance;
    private String adresse;

    public Client() {
    }
    
    public Client(String nom, String prenom, String mail, 
                  String motDePasse, String noTelephone,
                  Date dateNaissance, String adresse) {
        this.nom = nom;
        this.prenom = prenom;
        this.mail = mail;
        this.motDePasse = motDePasse;
        this.noTelephone = noTelephone;
        this.dateNaissance = dateNaissance;
        this.adresse = adresse;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
}
