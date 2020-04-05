/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.dasi.dao;

import fr.insalyon.dasi.metier.modele.Consultation;
import fr.insalyon.dasi.metier.modele.Statistiques;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author pitf9
 */
public class StatistiquesDao {
    
    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
    
    public void creer() {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        em.persist(new Statistiques());
    }
    
    public void lireStatistiques() {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Statistiques> query = em.createQuery(
                "SELECT s FROM Statistiques s ORDER BY s.Id ASC", Statistiques.class);
        Statistiques Statistiques = query.getResultList().get(0);
    }
    
}
