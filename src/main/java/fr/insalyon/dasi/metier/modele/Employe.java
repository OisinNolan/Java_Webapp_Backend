/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.dasi.metier.modele;

import fr.insalyon.dasi.util.Genre;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 *
 * @author oisinnolan
 */

@Entity
@DiscriminatorValue("employe")
public class Employe extends Utilisateur {
    
    private int noTravail;
    
    // -1 if no current work
    private Long travailActuel;
    
    protected Employe() {};
    
    public Employe(String nom, String prenom, String mail, 
                  String motDePasse, String noTelephone, Genre genre) {
        super(nom, prenom, mail, motDePasse, noTelephone, genre);
        this.noTravail = 0;
        this.travailActuel = -1L;
    }    

    public int getNoTravail() {
        return noTravail;
    }

    public void setNoTravail(int noTravail) {
        this.noTravail = noTravail;
    }

    public Long getTravailActuel() {
        return travailActuel;
    }

    public void setTravailActuel(Long travailActuel) {
        this.travailActuel = travailActuel;
    }

    @Override
    public String toString() {
        return "Employe{" + super.toString() + "noTravail=" + noTravail + ", travailActuel=" + travailActuel + '}';
    }
    
    
}
