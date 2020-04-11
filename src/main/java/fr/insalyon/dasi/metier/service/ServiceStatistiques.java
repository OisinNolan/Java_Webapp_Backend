/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.dasi.metier.service;

import fr.insalyon.dasi.metier.modele.Client;
import fr.insalyon.dasi.metier.modele.Consultation;
import fr.insalyon.dasi.metier.modele.Employe;
import fr.insalyon.dasi.metier.modele.Medium;
import fr.insalyon.dasi.metier.modele.Statistiques;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author pitf9
 */
public class ServiceStatistiques {
    
    private void calculerEtMettreAJour() {
                
        HashMap<Medium, Integer> consultationsParMedium =
                (HashMap<Medium, Integer>) Statistiques.getConsultationsParMediumMap();
        HashMap<Employe, List<Client>> repartition = 
                (HashMap<Employe, List<Client>>) Statistiques.getClientRepartitionMap();
        
        Consultation derniere = Statistiques.getDerniereCalculee();
        int derniereIndex;
        if (derniere == null) {
            derniereIndex = 0;
        } else {
            // Get the index of the last calculated consultation
            derniereIndex = Statistiques.getConsultations().indexOf(derniere);
        }
        // Get the list of not yet calculated consultations
        List<Consultation> aCalculer = Statistiques.getConsultations().subList(derniereIndex+1, Statistiques.getNbConsultations());
        
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
                ArrayList<Client> newList = new ArrayList<Client>();
                newList.add(cl);
                repartition.put(e, newList);
            } else if (!repartition.get(e).contains(cl)) {
                repartition.get(e).add(cl);
            }
        }
        
        // Updating top 5 mediums
        ServiceMedium sm = new ServiceMedium();
        ArrayList<Medium> allMediums = new ArrayList<>();
        allMediums.addAll(sm.listerMediums());
        ArrayList<Integer> vals = new ArrayList<>();
        for(Medium m : allMediums){
            Integer i = consultationsParMedium.get(m);
            vals.add(i);
        }
        Collections.sort(vals);
        Collections.reverse(vals);
        Integer threshhold = vals.get(5);
        ArrayList<Medium> topMediums = new ArrayList<>();
        for(Medium m : allMediums){
            Integer i = consultationsParMedium.get(m);
            if(i.compareTo(threshhold) > 0) topMediums.add(m);
        }
        
        // 0-4 in case last few Mediums have the same number of consultations
        Statistiques.setTopCinqueMediums(topMediums.subList(0, 4));
        Statistiques.setConsultationsParMediumMap(consultationsParMedium);
        Statistiques.setClientRepartitionMap(repartition);
        Statistiques.setDerniereCalculee(Statistiques.getDerniereConsultation());
        
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
