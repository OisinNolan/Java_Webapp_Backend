/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.dasi.metier.modele;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author pitf9
 */
public class Statistiques {
    
   
    // Mise à jour chaque fois une consultation finit
    private static List<Consultation> consultations = new ArrayList<>();
    // Mise à jour chaque fois les statistiques sont calculées et persistées
    private static Consultation derniereCalculee = null;
    private static Map<Medium, Integer> consultationsParMediumMap = new HashMap<>();
    private static Map<Employe, List<Client>> clientRepartitionMap = new HashMap<>();
    private static List<Medium> topCinqueMediums = new ArrayList<>();
    private static boolean charge = false;

    public static boolean getCharge() {
        return charge;
    }
    
    public static void setCharge(boolean charge) {
        Statistiques.charge = charge;
    }
    
    public static Consultation getDerniereConsultation() {
        return consultations.get(consultations.size() - 1);
    }

    public static void ajouterConsultation(Consultation derniereConsultation) {
        consultations.add(derniereConsultation);
    }

    public static Consultation getDerniereCalculee() {
        return derniereCalculee;
    }

    public static void setDerniereCalculee(Consultation derniereCalculee) {
        Statistiques.derniereCalculee = derniereCalculee;
    }

    public static Map<Medium, Integer> getConsultationsParMediumMap() {
        return consultationsParMediumMap;
    }

    public static Map<Employe, List<Client>> getClientRepartitionMap() {
        return clientRepartitionMap;
    }

    public static void setConsultationsParMediumMap(Map<Medium, Integer> consultationsParMediumMap) {
        Statistiques.consultationsParMediumMap = consultationsParMediumMap;
    }

    public static void setClientRepartitionMap(Map<Employe, List<Client>> clientRepartitionMap) {
        Statistiques.clientRepartitionMap = clientRepartitionMap;
    }

    public static List<Medium> getTopCinqueMediums() {
        return topCinqueMediums;
    }

    public static void setTopCinqueMediums(List<Medium> topCinqueMediums) {
        Statistiques.topCinqueMediums = topCinqueMediums;
    }

    public static List<Consultation> getConsultations() {
        return consultations;
    }
    
    public static void setConsultations(List<Consultation> consultations) {
        Statistiques.consultations = consultations;
    }
    
    public static boolean miseAJour() {
        return derniereCalculee!=null && derniereCalculee.equals(getDerniereConsultation());
    }
    
    public static int getNbConsultations() {
        return consultations.size();
    }
    
    public Statistiques() {
    }
}
