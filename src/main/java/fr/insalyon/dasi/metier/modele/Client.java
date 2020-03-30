package fr.insalyon.dasi.metier.modele;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author DASI Team
 */
@Entity
@DiscriminatorValue("Client")
public class Client extends Utilisateur implements Serializable{

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateNaissance;
    private String adresse;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profil_astral", referencedColumnName = "id")
    private ProfilAstral profilAstral;
    
    protected Client() {};
    
    public Client(String nom, String prenom, String mail, 
                  String motDePasse, String noTelephone,
                  Date dateNaissance, String adresse) {
        super(nom, prenom, mail, motDePasse, noTelephone);     
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

    public ProfilAstral getProfilAstral() {
        return profilAstral;
    }

    public void setProfilAstral(ProfilAstral profilAstral) {
        this.profilAstral = profilAstral;
    }
    
}
