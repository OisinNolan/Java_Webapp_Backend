/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.dasi.dao;

import fr.insalyon.dasi.metier.modele.Client;
import fr.insalyon.dasi.metier.modele.Employe;
import fr.insalyon.dasi.metier.modele.Genre;
import fr.insalyon.dasi.metier.modele.Medium;
import fr.insalyon.dasi.metier.modele.Utilisateur;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author oisinnolan
 */

public class UtilisateurDao {
    
    /*
    
        Fonctions pour Utilisateur Générique
    
    */
    
    public void creer(Utilisateur utilisateur) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        em.persist(utilisateur);
    }
    
    public void mettreAJour(Utilisateur utilisateur) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        em.merge(utilisateur);
    }
    
    public Utilisateur chercherParId(Long id) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        return em.find(Utilisateur.class, id); // renvoie null si l'identifiant n'existe pas
    }
    
    /*
    
        Fonctions pour Client
    
    */
    
    public Client chercherClientParId(Long id) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        return em.find(Client.class, id);
    }
    
    public Client chercherClientParMail(String clientMail) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Client> query = em.createQuery("SELECT c FROM Client c WHERE c.mail = :mail", Client.class);
        query.setParameter("mail", clientMail); // correspond au paramètre ":mail" dans la requête
        List<Client> clients = query.getResultList();
        Client result = null;
        if (!clients.isEmpty()) {
            result = clients.get(0); // premier de la liste
        }
        return result;
    }
    
    /*
    
        Fonctions pour Employe
    
    */
    
    public Employe chercherEmployeParId(Long id) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        return em.find(Employe.class, id);
    }
    
    public Employe chercherEmployeParMail(String employeMail) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Employe> query = em.createQuery("SELECT e FROM Employe e WHERE e.mail = :mail", Employe.class);
        query.setParameter("mail", employeMail); // correspond au paramètre ":mail" dans la requête
        List<Employe> employes = query.getResultList();
        Employe result = null;
        if (!employes.isEmpty()) {
            result = employes.get(0); // premier de la liste
        }
        return result;
    }
    
    public List<Employe> listerEmployes() {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Employe> query = em.createQuery("SELECT e FROM Employe e ORDER BY e.nom ASC, e.prenom ASC", Employe.class);
        return query.getResultList();
    }
    
    public Employe choisirParGenre(Genre genre) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        // This JPA query *should* get the employee of gender 'genre' with the smallest noTravail
        // (Needs to be tested)
        TypedQuery<Employe> query = em.createQuery("SELECT e FROM Employe e WHERE e.genre = :genre ORDER BY e.noTravail ASC", Employe.class).setMaxResults(1);
        query.setParameter("genre", genre); // correspond au paramètre ":genre" dans la requête
        
        Employe employe = query.getSingleResult();
        /*
        
            This is handled in the jpa query
        
        // Loop through Employees of the right gender sorted by the number of jobs ascending and choose the first one that is not currently busy
        for (Employe employe : employes) {
            if(employe.getTravailActuel() == null) {
                return employe;
            }
        }
        // If none was busy return the first Employee (with the smallest number of jobs)
        */
        return employe;
    }
    
    // These functions are used by the ListerMediums() function in the medium service
    public long nombreEmployesHommeDisponible() {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        Query query = em.createQuery("SELECT count(e) FROM Employe e WHERE e.travailActuel = :pasDeTravail AND e.genre = :genre", Employe.class);
        query.setParameter("pasDeTravail", -1L);
        query.setParameter("genre", Genre.M);
        long hommesDisponibles = (long) query.getSingleResult();
        return hommesDisponibles;
    }
    
    public long nombreEmployesFemmeDisponible() {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        Query query = em.createQuery("SELECT count(e) FROM Employe e WHERE e.travailActuel = :pasDeTravail AND e.genre = :genre", Employe.class);
        query.setParameter("pasDeTravail", -1L);
        query.setParameter("genre", Genre.F);
        long femmesDisponibles = (long) query.getSingleResult();
        return femmesDisponibles;
    }
    
}
