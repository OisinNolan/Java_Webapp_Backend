package fr.insalyon.dasi.ihm.console;

import fr.insalyon.dasi.dao.JpaUtil;
import fr.insalyon.dasi.metier.modele.Client;
import fr.insalyon.dasi.metier.modele.Consultation;
import fr.insalyon.dasi.metier.modele.Employe;
import fr.insalyon.dasi.metier.modele.Genre;
import fr.insalyon.dasi.metier.modele.ProfilAstral;
import fr.insalyon.dasi.metier.modele.Astrologue;
import fr.insalyon.dasi.metier.modele.Medium;
import fr.insalyon.dasi.metier.service.ServiceAuthentication;
import fr.insalyon.dasi.metier.service.ServiceConsultation;
import fr.insalyon.dasi.metier.service.ServiceMedium;
import java.util.Date;

/**
 *
 * @author DASI Team
 */
public class Main {

    public static void main(String[] args) {

        JpaUtil.init();
        
        testAuthentication();
        testConsultation();
        
        JpaUtil.destroy();
        
    }
    
    public static void testAuthentication() {
        ServiceAuthentication sa = new ServiceAuthentication();
        Client c = new Client("CLI", "ent", "cli@ent.mail", "passwrd", "0871234567", new Date(), "57 greenfield drive");        
        Employe e = new Employe("EMP", "loye", "emp@loye.mail", "pass", "0871234567", Genre.F);
        sa.inscrire(c);
        sa.inscrire(e);
        Client found = sa.rechercherClientParId(1L);
        System.out.println(found.getPrenom());
    }
    
    public static void testConsultation() {
        ServiceConsultation sc = new ServiceConsultation();
        ServiceAuthentication sa = new ServiceAuthentication();
        ServiceMedium sm = new ServiceMedium();
        // Here the client would have been selected by the employee using the GUI
        Client selectedClient = sa.rechercherClientParId(1L);
        // Here we can get the employee's details using the Auth service
        // easily as they should be the user who is currently logged in locally
        Employe loggedInUser = sa.rechercherEmployeParId(2L);
        sm.setupMediums();
        Medium chosenMedium = sm.rechercherParId(1L);
        
        Consultation c = new Consultation(loggedInUser, selectedClient, chosenMedium);
        
        // starting the consultation
        c.setDebut(new Date());
        
        // ending the consultation
        c.setFin(new Date());
        
        c.setCommentaire("Great session !");
        
        System.out.println(sc.validerConsultation(c));
    }
}
