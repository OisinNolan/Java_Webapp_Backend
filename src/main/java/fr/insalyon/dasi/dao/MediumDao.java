/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.dasi.dao;

import fr.insalyon.dasi.metier.modele.Genre;
import fr.insalyon.dasi.metier.modele.Medium;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author oisinnolan
 */
public class MediumDao {
   
    public void creer(Medium medium) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        em.persist(medium);
    }
    
    public Medium chercherParId(Long id) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        return em.find(Medium.class, id); // renvoie null si l'identifiant n'existe pas
    }
    
    public Medium chercherParDenomination(String denomination) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Medium> query = em.createQuery(
                "SELECT m FROM Medium m WHERE m.denomination = :denomination", Medium.class);
        query.setParameter("denomination", denomination); // correspond au paramètre ":denomination" dans la requête
        List<Medium> mediums = query.getResultList();
        Medium result = null;
        if (!mediums.isEmpty()) {
            result = mediums.get(0); // premier de la liste
        }
        return result;
    }
    
    public List<Medium> listerMediums() {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Medium> query = em.createQuery(
                "SELECT m FROM Medium m ORDER BY m.denomination ASC", Medium.class);
        return query.getResultList();
    }
    
    public List<Medium> listerMediumsDeGenre(Genre genre) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Medium> query = em.createQuery(
                "SELECT m FROM Medium m WHERE m.genre = :genre ORDER BY m.denomination ASC", Medium.class);
        query.setParameter("genre", genre);
        return query.getResultList();
    }
}
