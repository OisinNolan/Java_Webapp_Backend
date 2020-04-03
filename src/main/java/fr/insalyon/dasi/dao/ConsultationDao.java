/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.dasi.dao;

import fr.insalyon.dasi.metier.modele.Client;
import fr.insalyon.dasi.metier.modele.Consultation;
import fr.insalyon.dasi.metier.modele.Employe;
import fr.insalyon.dasi.metier.modele.Utilisateur;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author oisinnolan
 */
public class ConsultationDao {
    
    public void creer(Consultation consultation) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        em.persist(consultation);
    }
    
    public List<Consultation> chercherParUtilisateur(Utilisateur utilisateur) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Consultation> query;
        if (utilisateur instanceof Client){
            query = em.createQuery("SELECT c FROM Consultation c WHERE c.client = :utilisateur ORDER BY c.debut ASC", Consultation.class);
        } else {
            query = em.createQuery("SELECT c FROM Consultation c WHERE c.employe = :utilisateur ORDER BY c.debut ASC", Consultation.class);
        }
        return query.setParameter("utilisateur", utilisateur).getResultList();
    }
}
