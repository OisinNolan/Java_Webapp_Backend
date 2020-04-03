/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.dasi.metier.modele;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 *
 * @author oisinnolan
 */

@Entity
@DiscriminatorValue("employe")
public class Employe extends Utilisateur {
    
    protected Employe() {};
    
    public Employe(String nom, String prenom, String mail, 
                  String motDePasse, String noTelephone, Genre genre) {
        super(nom, prenom, mail, motDePasse, noTelephone, genre);  
        this.noTelephone = noTelephone;
        this.genre = genre;
    }    
}
