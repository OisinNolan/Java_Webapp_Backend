/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.dasi.metier.service;

import fr.insalyon.dasi.dao.EmployeDao;
import fr.insalyon.dasi.dao.JpaUtil;
import fr.insalyon.dasi.metier.modele.Client;
import fr.insalyon.dasi.metier.modele.Employe;
import fr.insalyon.dasi.metier.modele.Genre;
import fr.insalyon.dasi.metier.modele.Medium;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pitf9
 */
public class ServiceEmploye {
    
    protected EmployeDao employeDao = new EmployeDao();
    
    public Long inscrireEmploye(Employe employe) {
        Long resultat = null;
        JpaUtil.creerContextePersistance();
        try {
            JpaUtil.ouvrirTransaction();
            employeDao.creer(employe);
            JpaUtil.validerTransaction();
            resultat = employe.getId();
            Logger.getAnonymousLogger().log(Level.FINE, "Employe successfully persisted");
        } catch (Exception ex) {
            Logger.getAnonymousLogger().log(Level.WARNING, 
                    "Exception lors de l'appel au Service inscrireEmploye(employe)", ex);
            JpaUtil.annulerTransaction();
            resultat = null;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return resultat;
    }

    public Employe rechercherEmployeParId(Long id) {
        Employe resultat = null;
        JpaUtil.creerContextePersistance();
        try {
            resultat = employeDao.chercherParId(id);
        } catch (Exception ex) {
            Logger.getAnonymousLogger().log(Level.WARNING, 
                    "Exception lors de l'appel au Service rechercherEmployeParId(id)", ex);
            resultat = null;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return resultat;
    }

    public Employe authentifierEmploye(String mail, String motDePasse) {
        Employe resultat = null;
        JpaUtil.creerContextePersistance();
        try {
            // Recherche du employe
            Employe employe = employeDao.chercherParMail(mail);
            if (employe != null) {
                // Vérification du mot de passe
                if (employe.getMotDePasse().equals(motDePasse)) {
                    resultat = employe;
                }
            }
        } catch (Exception ex) {
            Logger.getAnonymousLogger().log(Level.WARNING, 
                    "Exception lors de l'appel au Service authentifierEmploye(mail,motDePasse)", ex);
            resultat = null;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return resultat;
    }

    public List<Employe> listerEmployes() {
        List<Employe> resultat = null;
        JpaUtil.creerContextePersistance();
        try {
            resultat = employeDao.listerEmployes();
        } catch (Exception ex) {
            Logger.getAnonymousLogger().log(Level.WARNING, 
                    "Exception lors de l'appel au Service listerEmployes()", ex);
            resultat = null;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return resultat;
    }
    
    public String notifierConsultation(Employe employe, Medium medium, Client client){
        String titreClient = client.getGenre() == Genre.M ? "M " : "Mme ";
        String titreMedium = medium.getGenre() == Genre.M ? "M " : "Mme ";
        
        return "Pour : Camille MARTIN" + employe.getPrenom() + " " + employe.getNom()
                + ", Tel : " + employe.getNoTelephone()
                + "\n Message : Bonjour " + employe.getPrenom()
                + ". Consultation requise pour " + titreClient + client.getPrenom() + " " + client.getNom()
                + ". Médium à incarner : " + titreMedium + medium.getDenomination() + ".";
    }
    
    public Employe choisirEmploye(Medium medium){
        Employe resultat = null;
        JpaUtil.creerContextePersistance();
        try {
            resultat = employeDao.choisirParGenre(medium.getGenre());
        } catch (Exception ex) {
            Logger.getAnonymousLogger().log(Level.WARNING, 
                    "Exception lors de l'appel au Service choisirEmploye(Medium)", ex);
            resultat = null;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return resultat;
    }
}
