/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.dasi.dao;

import fr.insalyon.dasi.metier.modele.Utilisateur;
import javax.persistence.EntityManager;

/**
 *
 * @author oisinnolan
 */
public class UtilisateurDao {
    
    public void creer(Utilisateur utilisateur) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        em.persist(utilisateur);
    }
    
    public Utilisateur chercherParId(Long id) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        return em.find(Utilisateur.class, id); // renvoie null si l'identifiant n'existe pas
    }
    
}
