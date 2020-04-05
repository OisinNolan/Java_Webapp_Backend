/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.dasi.metier.modele;

import fr.insalyon.dasi.util.Genre;
import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 *
 * @author oisinnolan
 */
@Entity
@DiscriminatorValue("cartomancien")
public class Cartomancien extends Medium implements Serializable {
    public Cartomancien() {};
    
    public Cartomancien(String denomination, Genre genre, 
                        String presentation) {
        super(denomination, genre, presentation);
    }
}
