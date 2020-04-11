/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.dasi.metier.service;

import fr.insalyon.dasi.dao.JpaUtil;
import fr.insalyon.dasi.dao.MediumDao;
import fr.insalyon.dasi.dao.UtilisateurDao;
import fr.insalyon.dasi.metier.modele.Medium;
import fr.insalyon.dasi.metier.modele.Spirite;
import fr.insalyon.dasi.metier.modele.Astrologue;
import fr.insalyon.dasi.metier.modele.Cartomancien;
import fr.insalyon.dasi.util.Genre;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author oisinnolan
 */
public class ServiceMedium {
    
    protected MediumDao mediumDao = new MediumDao();
    protected UtilisateurDao utilisateurDao = new UtilisateurDao();
    
    public boolean setupMediums() {
        boolean resultat = false;
        JpaUtil.creerContextePersistance();
        try {
            JpaUtil.ouvrirTransaction();
            // Hard coded medium data
            Medium m1 = new Spirite("Gwenaëlle", Genre.F, "Spécialiste des grandes conversations au-delà de TOUTES les frontières.", "Boule de cristal");
            Medium m2 = new Spirite("Professeur Tran", Genre.M, "Votre avenir est devant vous : regardons-le ensemble !", "Marc de café, boule de cristal, oreilles de lapin");
            Medium m3 = new Cartomancien("Mme Irma", Genre.F, "Comprenez votre entourage grâce à mes cartes ! Résultats rapides");
            Medium m4 = new Cartomancien("Endora", Genre.F, "Mes cartes répondront à toutes vos questions personnelles.");
            Medium m5 = new Astrologue("Serena", Genre.F, " Basée à Champigny-sur-Marne, Serena vous révèlera votre avenir pour éclairer votre passé.", "École Normale Supérieure d’Astrologie (ENS-Astro)", 2006);
            Medium m6 = new Astrologue("Mr M", Genre.M, "Avenir, avenir, que nous réserves-tu ? N'attendez plus, demandez à me consulter!", "Institut des Nouveaux Savoirs Astrologiques", 2010);
            
            mediumDao.creer(m1);
            mediumDao.creer(m2);
            mediumDao.creer(m3);
            mediumDao.creer(m4);
            mediumDao.creer(m5);
            mediumDao.creer(m6);
            
            JpaUtil.validerTransaction();
            Logger.getAnonymousLogger().log(Level.FINE, "Medium data initialised successfully");
            resultat = true;
        } catch (Exception ex) {
            Logger.getAnonymousLogger().log(Level.WARNING, "Exception during call to setupMediums() in ServiceMedium", ex);
            // Add switch statement here to handle various error types 
            // i.e unique constraint violated on email column
            JpaUtil.annulerTransaction();
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return resultat;
    }
    
    public Medium rechercherParId(Long id) {
        Medium resultat = null;
        JpaUtil.creerContextePersistance();
        try {
            resultat = mediumDao.chercherParId(id);
        } catch (Exception ex) {
            Logger.getAnonymousLogger().log(Level.WARNING, "Exception lors de l'appel au Service rechercherParId(id)", ex);
            resultat = null;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return resultat;
    }
    
    public Medium rechercherParDenomination(String denomination) {
        Medium resultat = null;
        JpaUtil.creerContextePersistance();
        try {
            resultat = mediumDao.chercherParDenomination(denomination);
        } catch (Exception ex) {
            Logger.getAnonymousLogger().log(Level.WARNING, "Exception lors de l'appel au Service rechercherParDenomination(String)", ex);
            resultat = null;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return resultat;
    }
    
    public List<Medium> listerMediums() {
        List<Medium> resultat = null;
        JpaUtil.creerContextePersistance();
        try {
            // This code ensures that mediums of a certain gender
            // are only shown in the case that there is one of more 
            // employees of that gender currently available
            long noHommes = utilisateurDao.nombreEmployesHommeDisponible();
            long noFemmes = utilisateurDao.nombreEmployesFemmeDisponible();
            
            if(noHommes == 0) {
                resultat = mediumDao.listerMediumsDeGenre(Genre.F);
            } else if(noFemmes == 0) {
                resultat = mediumDao.listerMediumsDeGenre(Genre.M);
            } else {
                resultat = mediumDao.listerMediums();
            }
        } catch (Exception ex) {
            Logger.getAnonymousLogger().log(Level.WARNING, "Exception lors de l'appel au Service listerMediums()", ex);
            resultat = null;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return resultat;
    }
    
    public Map<String, List<Medium>> trierMediumsParType(List<Medium> listeMediums) {
        Map<String, List<Medium>> typesMediums = new HashMap<>();
        typesMediums.put("Spirite", new ArrayList<>());
        typesMediums.put("Cartomancien", new ArrayList<>());
        typesMediums.put("Astrologue", new ArrayList<>());
        
        for(Medium m : listeMediums) {
            if(m instanceof Spirite) {
                typesMediums.get("Spirite").add(m);
            } else if(m instanceof Cartomancien) {
                typesMediums.get("Cartomancien").add(m);
            } else if(m instanceof Astrologue) {
                typesMediums.get("Astrologue").add(m);
            }
        }
        
        return typesMediums;
    }
   

}
