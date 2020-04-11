/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.dasi.metier.service;

import fr.insalyon.dasi.dao.ConsultationDao;
import fr.insalyon.dasi.dao.JpaUtil;
import fr.insalyon.dasi.metier.modele.Client;
import fr.insalyon.dasi.metier.modele.Employe;
import fr.insalyon.dasi.metier.modele.Consultation;
import fr.insalyon.dasi.util.Genre;
import fr.insalyon.dasi.util.Message;
import fr.insalyon.dasi.util.AstroTest;
import fr.insalyon.dasi.metier.modele.Medium;
import fr.insalyon.dasi.metier.modele.ProfilAstral;
import fr.insalyon.dasi.metier.modele.Statistiques;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author oisinnolan
 */

public class ServiceConsultation {
    
    protected ConsultationDao consultationDao = new ConsultationDao();
    protected ServiceUtilisateur serviceUtilisateur = new ServiceUtilisateur();
    
    public Long creerConsultation(Consultation consultation) {
        Long resultat = null;
        JpaUtil.creerContextePersistance();
        try {
            JpaUtil.ouvrirTransaction();
            consultationDao.creer(consultation);
            consultation.getClient().setIdConsultationActuel(consultation.getId());
            consultation.getEmploye().setTravailActuel(consultation.getId());
            consultation.getEmploye().setNoTravail(consultation.getEmploye().getNoTravail() + 1);
            consultationDao.mettreAJour(consultation);
            JpaUtil.validerTransaction();
            resultat = consultation.getId();
            Logger.getAnonymousLogger().log(Level.FINE, "Consultation successfully persisted");
        } catch (Exception ex) {
            Logger.getAnonymousLogger().log(Level.WARNING,
                    "Exception during call to creerConsultation() in ServiceConsultation", ex);
            JpaUtil.annulerTransaction();
            resultat = null;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return resultat;
    }
    
    public Consultation rechercherConsultationParId(Long id) {
        Consultation resultat = null;
        JpaUtil.creerContextePersistance();
        try {
            resultat = consultationDao.chercherParId(id);
        } catch (Exception ex) {
            Logger.getAnonymousLogger().log(Level.WARNING, 
                    "Exception lors de l'appel au Service rechercherConsultationParId(id)", ex);
            resultat = null;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return resultat;
    }
    
    public Long commencerConsultation(Consultation consultation) {
        Long resultat = null;
        JpaUtil.creerContextePersistance();
        try {
            JpaUtil.ouvrirTransaction();
            consultation.setDebut(new Date());
            consultationDao.mettreAJour(consultation);
            JpaUtil.validerTransaction();
            resultat = consultation.getId();
            Logger.getAnonymousLogger().log(Level.FINE, "Consultation successfully commenced");
        } catch (Exception ex) {
            Logger.getAnonymousLogger().log(Level.WARNING,
                    "Exception during call to commencerConsultation() in ServiceConsultation", ex);
            JpaUtil.annulerTransaction();
            resultat = null;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return resultat;
    }
    
    public Long validerConsultation(Consultation consultation, String commentaire) {
        Long resultat = null;
        JpaUtil.creerContextePersistance();
        try {
            JpaUtil.ouvrirTransaction();
            consultation.setFin(new Date());
            consultation.setCommentaire(commentaire);
            consultation.getEmploye().setTravailActuel(-1L);
            consultationDao.mettreAJour(consultation);
            JpaUtil.validerTransaction();
            resultat = consultation.getId();
            Statistiques.ajouterConsultation(consultation);
            Logger.getAnonymousLogger().log(Level.FINE, "Consultation successfully validated");
        } catch (Exception ex) {
            Logger.getAnonymousLogger().log(Level.WARNING,
                    "Exception during call to validerConsultation() in ServiceConsultation", ex);
            JpaUtil.annulerTransaction();
            resultat = null;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return resultat;
    }
    
    // TODO update function signature in the docs
    public List<Consultation> listerHistoriqueConsultations(Client client) {
        List<Consultation> resultat = null;
        JpaUtil.creerContextePersistance();
        try {
            resultat = consultationDao.chercherParUtilisateur(client);
        } catch (Exception ex) {
            Logger.getAnonymousLogger().log(Level.WARNING,
                    "Exception lors de l'appel au Service listerHistoriqueConsultations()", ex);
            resultat = null;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return resultat;
    }
    
    public void notifierEmploye(Consultation consultation) {
        
        Employe employe = consultation.getEmploye();
        Client client = consultation.getClient();
        Medium medium = consultation.getMedium();
        
        String message = "Bonjour " + employe.getPrenom() + ". Consultation requise pour "
                + (client.getGenre() == Genre.F ? "Mme " : "M ") + client.getPrenom() + " "
                + client.getNom().toUpperCase() + ".\nM�dium ? incarner : " + medium.getDenomination();
        
        Message.envoyerNotification(employe.getNoTelephone(), message);
    }
    
    public void notifierClient(Consultation consultation) {
        
        Employe employe = consultation.getEmploye();
        Client client = consultation.getClient();
        Medium medium = consultation.getMedium();
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy ? HH'h'mm");
        
        String message = "Bonjour " + client.getPrenom() + ". J�ai bien re�u votre demande de consultation du "
                +  dateFormat.format(consultation.getDateCreation()) + ".\nVous pouvez d?s ? pr�sent me contacter au " + employe.getNoTelephone()
                + ". A tout de suite !\n\nM�diumiquement " + "v�tre, \n\n" + medium.getDenomination();
        
        Message.envoyerNotification(client.getNoTelephone(), message);
    }
    
    public String genererPrediction(Consultation consultation, int amour, int sante, int travail){
        AstroTest APIAstro = new AstroTest();
        List<String> reponseAPI;
        String prediction = null;
        try {
            ProfilAstral profilClient = consultation.getClient().getProfilAstral();
            reponseAPI = APIAstro.getPredictions(profilClient.getCouleur(), profilClient.getAnimalTotem(), amour, sante, travail);
            prediction = "AMOUR:\n" + reponseAPI.get(0) + "\n\nSANTE:\n" + reponseAPI.get(1) + "\n\nTRAVAIL:\n" + reponseAPI.get(2);
        } catch(IOException ex) {
            Logger.getAnonymousLogger().log(Level.WARNING, 
                    "IOException lors de l'appel au fonction genererPrediction()", ex);
        }
        
        return prediction;
    }
        
}