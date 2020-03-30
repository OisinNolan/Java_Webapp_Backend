/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.dasi.metier.modele;

import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 *
 * @author oisinnolan
 */

@Entity
@DiscriminatorValue("Employe")
public class Employe extends Utilisateur implements Serializable {
    
    private Genre genre;
    
    protected Employe() {};
    
    public Employe(String nom, String prenom, String mail, 
                  String motDePasse, String noTelephone, Genre genre) {
        super(nom, prenom, mail, motDePasse, noTelephone);  
        this.noTelephone = noTelephone;
        this.genre = genre;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }
    
    
    
}
