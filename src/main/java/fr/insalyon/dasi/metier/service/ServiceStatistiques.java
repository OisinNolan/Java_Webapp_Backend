/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.dasi.metier.service;

import fr.insalyon.dasi.dao.JpaUtil;
import fr.insalyon.dasi.dao.StatistiquesDao;
import fr.insalyon.dasi.metier.modele.Client;
import fr.insalyon.dasi.metier.modele.Consultation;
import fr.insalyon.dasi.metier.modele.Employe;
import fr.insalyon.dasi.metier.modele.Medium;
import fr.insalyon.dasi.metier.modele.Statistiques;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.eclipse.persistence.jpa.jpql.JPAVersion.value;

/**
 *
 * @author pitf9
 */
public class ServiceStatistiques {
    
    protected StatistiquesDao  statDao = new StatistiquesDao();
    
    private void lireStatistiques() {
        JpaUtil.creerContextePersistance();
        
        try {
                statDao.lireStatistiques();
            } catch (Exception ex) {
                Logger.getAnonymousLogger().log(Level.WARNING,
                        "Exception lors de l'appel au Service listerStatistiques()", ex);
            } finally {
                JpaUtil.fermerContextePersistance();
            }
    }
    
    private void calculerEtMettreAJour() {   
        if (Statistiques.getConsultations() == null) {
            lireStatistiques();
        }
        
        Map<Medium, Integer> consultationsParMedium =
                Statistiques.getConsultationsParMediumMap();
        Map<Employe, List<Client>> repartition = 
                Statistiques.getClientRepartitionMap();
        
        Consultation derniere = Statistiques.getDernierePersistee();
        int derniereIndex;
        if (derniere == null) {
            derniereIndex = 0;
        } else {
            // Get the index of the last calculated consultation
            derniereIndex = Statistiques.getConsultations().indexOf(derniere);
        }
        // Get the list of not yet calculated consultations
        List<Consultation> aCalculer = Statistiques.getConsultations().subList(derniereIndex+1, Statistiques.getNbConsultations()-1);
        
        for (Consultation c : aCalculer) {
            // Collecting data to store number of consultations per Meduim
            Medium m = c.getMedium();
            if (consultationsParMedium.containsKey(m)) {
                int nbConsultations = consultationsParMedium.get(m);
                consultationsParMedium.replace(m, nbConsultations+1);
            } else {
                consultationsParMedium.put(m, 1);
            }
                     
            // Collecting data to store client repartition
            Employe e = c.getEmploye();
            Client cl = c.getClient();
            if (!repartition.containsKey(e)) {
                List<Client> newList = new ArrayList<Client>();
                newList.add(cl);
                repartition.put(e, newList);
            } else if (!repartition.get(e).contains(cl)) {
                repartition.get(e).add(cl);
            }
        }
        
        // Updating top 5 mediums
        ServiceMedium sm = new ServiceMedium();
        List<Medium> topMediums = sm.listerMediums();
        for (Medium m : topMediums) {
            int n = consultationsParMedium.get(m);
            int count = 0;
            for (Medium m2 : topMediums) {
                int n2 = consultationsParMedium.get(m2);
                if (n < n2) ++count;
                if (count == 5) topMediums.remove(m);
            }
        }
        
        Statistiques.setTopCinqueMediums(topMediums);
        Statistiques.setConsultationsParMediumMap(consultationsParMedium);
        Statistiques.setClientRepartitionMap(repartition);
        Statistiques.setDernierePersistee(Statistiques.getDerniereConsultation());
        
        statDao.creer();
    }
    
    public Map<Medium, Integer> listerNombreParMedium() {
        if (!Statistiques.miseAJour()) {
            calculerEtMettreAJour();
        }
        return Statistiques.getConsultationsParMediumMap();
    }
    
    public Map<Employe, List<Client>> listerRepartitionClients() {
        if (!Statistiques.miseAJour()) {
            calculerEtMettreAJour();
        }
        return Statistiques.getClientRepartitionMap();
    }
    
    public List<Medium> listerTopCinqueMediums() {
        if (!Statistiques.miseAJour()) {
            calculerEtMettreAJour();
        }
        return Statistiques.getTopCinqueMediums();
    }
    
}
