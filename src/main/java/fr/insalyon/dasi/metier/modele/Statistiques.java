/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.dasi.metier.modele;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author pitf9
 */
@Entity
public class Statistiques implements Serializable {
    
    @Id
    private Long Id;
   
    // Mise à jour chaque fois une consultation finit
    private static ArrayList<Consultation> consultations;
    // Mise à jour chaque fois les statistiques sont calculées et persistées
    private static Consultation dernierePersistee;
    private static Map<Medium, Integer> consultationsParMediumMap;
    private static Map<Employe, List<Client>> clientRepartitionMap;
    private static List<Medium> topCinqueMediums;

    public Statistiques() {
    }
    
    
    public Long getId() {
        return Id;
    }

    public static Consultation getDerniereConsultation() {
        return consultations.get(consultations.size() - 1);
    }

    public static void ajouterConsultation(Consultation derniereConsultation) {
        consultations.add(derniereConsultation);
    }

    public static Consultation getDernierePersistee() {
        return dernierePersistee;
    }

    public static void setDernierePersistee(Consultation dernierePersistee) {
        Statistiques.dernierePersistee = dernierePersistee;
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
    
    public static boolean miseAJour() {
        return dernierePersistee!=null && dernierePersistee.equals(getDerniereConsultation());
    }
    
    public static int getNbConsultations() {
        return consultations.size();
    }
}
