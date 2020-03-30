/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.dasi.metier.service;

import fr.insalyon.dasi.dao.UtilisateurDao;
import fr.insalyon.dasi.dao.JpaUtil;
import fr.insalyon.dasi.metier.modele.Utilisateur;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author oisinnolan
 */
public class ServiceAuthentication {
    
    protected UtilisateurDao utilisateurDao = new UtilisateurDao();

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
        }
        return resultat;
    }
    
    public Utilisateur rechercherClientParId(Long id) {
        Utilisateur resultat = null;
        JpaUtil.creerContextePersistance();
        try {
            resultat = utilisateurDao.chercherParId(id);
        } catch (Exception ex) {
            Logger.getAnonymousLogger().log(Level.WARNING, "Exception lors de l'appel au Service rechercherClientParId(id)", ex);
            resultat = null;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return resultat;
    }
    
}
