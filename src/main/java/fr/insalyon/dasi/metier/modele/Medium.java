/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.dasi.metier.modele;

import fr.insalyon.dasi.util.Genre;
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
@DiscriminatorColumn(name="Medium_Type", discriminatorType = DiscriminatorType.STRING)
public abstract class Medium {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected String denomination;
    protected Genre genre;
    protected String presentation;
    
    public Medium() {};
    
    public Medium(String denomination, Genre genre,
                    String presentation) {
        this.denomination = denomination;
        this.genre = genre;
        this.presentation = presentation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDenomination() {
        return denomination;
    }

    public void setDenomination(String denomination) {
        this.denomination = denomination;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public String getPresentation() {
        return presentation;
    }

    public void setPresentation(String presentation) {
        this.presentation = presentation;
    }

    @Override
    public String toString() {
        return "id=" + id + ", denomination=" + denomination + ", genre=" + genre + ", presentation=" + presentation + ", ";
    }
    
    // Overriden for the HashMap in Statistiques class
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
            append(id).
            append(denomination).
            append(genre).
            append(presentation).
            toHashCode();
    }
    
    // Overriden in order to properly compare while computing statistics
    @Override
    public boolean equals(Object obj) {
       if (!(obj instanceof Medium))
            return false;
        if (obj == this)
            return true;

        Medium rhs = (Medium) obj;
        if (this.id.equals(rhs.id) &&
                this.denomination.equals(rhs.denomination) &&
                this.genre.equals(rhs.genre) &&
                this.presentation.equals(rhs.presentation)
                ) return true;
        else return false;
    }
    
    
}
