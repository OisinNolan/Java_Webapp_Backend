/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.dasi.metier.service;


import fr.insalyon.dasi.dao.ConsultationDao;
import fr.insalyon.dasi.dao.JpaUtil;
import fr.insalyon.dasi.metier.modele.Client;
import fr.insalyon.dasi.metier.modele.Consultation;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author oisinnolan
 */

public class ServiceConsultation {
    
    protected ConsultationDao consultationDao = new ConsultationDao();
    
    public Long validerConsultation(Consultation consultation) {
        Long resultat = null;
        JpaUtil.creerContextePersistance();
        try {
            JpaUtil.ouvrirTransaction();
            consultationDao.creer(consultation);
            JpaUtil.validerTransaction();
            resultat = consultation.getId();
            Logger.getAnonymousLogger().log(Level.FINE, "Consultation successfully persisted");
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
    
    public String evaluerConsultation(int amour, int sante, int travail){
        return "Prediction amour: " + (new Random(amour).nextInt(4)+1)
                + "\nPrediction santé: " + (new Random(sante).nextInt(4)+1)
                + "\nPrediction travail: " + (new Random(travail).nextInt(4)+1);
    }
}
