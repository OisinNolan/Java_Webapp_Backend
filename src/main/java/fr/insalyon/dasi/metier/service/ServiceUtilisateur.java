/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.dasi.metier.service;

import fr.insalyon.dasi.dao.UtilisateurDao;
import fr.insalyon.dasi.dao.JpaUtil;
import fr.insalyon.dasi.metier.modele.Client;
import fr.insalyon.dasi.metier.modele.Employe;
import fr.insalyon.dasi.metier.modele.Medium;
import fr.insalyon.dasi.metier.modele.ProfilAstral;
import fr.insalyon.dasi.metier.modele.Utilisateur;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import util.*;

/**
 *
 * @author oisinnolan
 */
public class ServiceUtilisateur {
    
    protected UtilisateurDao utilisateurDao = new UtilisateurDao();

    /* 
    
        Fonctions pour Utilisateur Générique
    
    */
    
    public Long inscrire(Utilisateur utilisateur) {
        Long resultat = null;
        JpaUtil.creerContextePersistance();
        try {
            JpaUtil.ouvrirTransaction();
            utilisateurDao.creer(utilisateur);
            JpaUtil.validerTransaction();
            resultat = utilisateur.getId();
            
            Logger.getAnonymousLogger().log(Level.FINE, "User successfully persisted");
        } catch (Exception ex) {
            Logger.getAnonymousLogger().log(Level.WARNING, "Exception lors de l'appel au Service inscrireClient(client)", ex);
            // Add switch statement here to handle various error types 
            // i.e unique constraint violated on email column
            JpaUtil.annulerTransaction();
            resultat = null;
        } finally {
            JpaUtil.fermerContextePersistance();
            
            if(utilisateur instanceof Client) {
                Client nouveauClient = (Client) utilisateur;
                String objet = resultat == null ? "Bonjour " + nouveauClient.getPrenom() + ", votre inscription au service PREDICT’IF a malencontreusement échoué...\n"
                        + "Merci de recommencer ultérieurement."
                        : "Bonjour " + nouveauClient.getPrenom() + ", nous vous confirmons votre inscription au service PREDICT’IF."
                        + "\nRendez-vous vite sur notre site pour consulter votre profil astrologique et profiter des dons incroyables de nos mediums";
                String corps = resultat == null ? "Echec de l’inscription chez PREDICT’IF" : "Bienvenue chez PREDICT’IF";
                Message.envoyerMail("contact@predict.if", nouveauClient.getMail(), objet, corps);

                this.creerProfilAstral(nouveauClient);
            }
        }
        return resultat;
    }
    
    public Long mettreAJour(Utilisateur utilisateur) {
        Long resultat = null;
        JpaUtil.creerContextePersistance();
        try {
            JpaUtil.ouvrirTransaction();
            utilisateurDao.mettreAJour(utilisateur);
            JpaUtil.validerTransaction();
            resultat = utilisateur.getId();
            Logger.getAnonymousLogger().log(Level.FINE, "User successfully persisted");
        } catch (Exception ex) {
            Logger.getAnonymousLogger().log(Level.WARNING, "Exception lors de l'appel au Service inscrireClient(client)", ex);
            // Add switch statement here to handle various error types 
            // i.e unique constraint violated on email column
            JpaUtil.annulerTransaction();
            resultat = null;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return resultat;
    }
    
    public Utilisateur rechercherUtilisateurParId(Long id) {
        Utilisateur resultat = null;
        JpaUtil.creerContextePersistance();
        try {
            resultat = utilisateurDao.chercherParId(id);
        } catch (Exception ex) {
            Logger.getAnonymousLogger().log(Level.WARNING, "Exception lors de l'appel au Service rechercherUtilisateurParId(id)", ex);
            resultat = null;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return resultat;
    }
    
    /* 
    
        Fonctions pour Clients
    
    */
    
    public Client rechercherClientParId(Long id) {
        Client resultat = null;
        JpaUtil.creerContextePersistance();
        try {
            resultat = utilisateurDao.chercherClientParId(id);
        } catch (Exception ex) {
            Logger.getAnonymousLogger().log(Level.WARNING, "Exception lors de l'appel au Service rechercherClientParId(id)", ex);
            resultat = null;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return resultat;
    }
    
    public Client authentifierClient(String mail, String motDePasse) {
        Client resultat = null;
        JpaUtil.creerContextePersistance();
        try {
            // Recherche du client
            Client client = utilisateurDao.chercherClientParMail(mail);
            if (client != null) {
                // Vérification du mot de passe
                if (client.getMotDePasse().equals(motDePasse)) {
                    resultat = client;
                }
            }
        } catch (Exception ex) {
            Logger.getAnonymousLogger().log(Level.WARNING, 
                    "Exception lors de l'appel au Service authentifierClient(mail,motDePasse)", ex);
            resultat = null;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return resultat;
    }
    
    
    public Long creerProfilAstral(Client client) {
        Long resultat = null;
        
        AstroTest APIAstro = new AstroTest();
        
        List<String> reponseAPI;
        try {
            reponseAPI = APIAstro.getProfil(client.getPrenom(), client.getDateNaissance());
            ProfilAstral profilAstral = new ProfilAstral(reponseAPI.get(0), reponseAPI.get(1), reponseAPI.get(2), reponseAPI.get(3));
            client.setProfilAstral(profilAstral);
            this.mettreAJour(client);
            resultat = profilAstral.getId();
        } catch(IOException ex) {
            Logger.getAnonymousLogger().log(Level.WARNING, 
                    "IOException lors de l'appel au Service creerProfilAstral()", ex);
        }
        
        return resultat;
    }
    
    /* 
    
        Fonctions pour Employe
    
    */
    
    public Employe rechercherEmployeParId(Long id) {
        Employe resultat = null;
        JpaUtil.creerContextePersistance();
        try {
            resultat = utilisateurDao.chercherEmployeParId(id);
        } catch (Exception ex) {
            Logger.getAnonymousLogger().log(Level.WARNING, "Exception lors de l'appel au Service rechercherEmployeParId(id)", ex);
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
            Employe employe = utilisateurDao.chercherEmployeParMail(mail);
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
    
    public Employe choisirEmployePourTravail(Medium medium){
        Employe resultat = null;
        JpaUtil.creerContextePersistance();
        try {
            resultat = utilisateurDao.choisirParGenre(medium.getGenre());
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
