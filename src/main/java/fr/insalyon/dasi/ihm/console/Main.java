package fr.insalyon.dasi.ihm.console;

import fr.insalyon.dasi.dao.JpaUtil;
import fr.insalyon.dasi.metier.modele.Client;
import fr.insalyon.dasi.metier.modele.Employe;
import fr.insalyon.dasi.metier.modele.Genre;
import fr.insalyon.dasi.metier.modele.ProfilAstral;
import fr.insalyon.dasi.metier.service.ServiceAuthentication;
import java.util.Date;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author DASI Team
 */
public class Main {

    public static void main(String[] args) {

        // TODO : Pensez à créer une unité de persistance "DASI-PU" et à vérifier son nom dans la classe JpaUtil
        // Contrôlez l'affichage du log de JpaUtil grâce à la méthode log de la classe JpaUtil
        
        JpaUtil.init();
        
        ServiceAuthentication sa = new ServiceAuthentication();
        Client c = new Client("CLI", "cli", "cli@ent.mail", "passwrd", "some number!", new Date(), "57 rockfield grove");        
        
        ProfilAstral pa = new ProfilAstral("a", "b", "c", "d");
        pa.setClient(c);
        c.setProfilAstral(pa);
        
        System.out.println(sa.inscrire(c));
        
        JpaUtil.destroy();
        
    }
}
