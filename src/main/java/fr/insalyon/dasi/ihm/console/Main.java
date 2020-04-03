package fr.insalyon.dasi.ihm.console;

import fr.insalyon.dasi.dao.JpaUtil;
import fr.insalyon.dasi.metier.modele.Client;
import fr.insalyon.dasi.metier.modele.Consultation;
import fr.insalyon.dasi.metier.modele.Employe;
import fr.insalyon.dasi.metier.modele.Genre;
import fr.insalyon.dasi.metier.modele.Medium;
import fr.insalyon.dasi.metier.service.ServiceAuthentification;
import fr.insalyon.dasi.metier.service.ServiceConsultation;
import fr.insalyon.dasi.metier.service.ServiceMedium;
import java.util.Date;
import java.util.List;

/**
 *
 * @author DASI Team
 */
public class Main {

    public static void main(String[] args) {

        JpaUtil.init();
        
        testAuthentication();
        testConsultation();

        testHistoriqueClient();
        
       
        JpaUtil.destroy();
        
    }
    
    public static void testAuthentication() {
        ServiceAuthentification sa = new ServiceAuthentification();
        Client c = new Client("CLI", "ent", "cli@ent.mail", "passwrd", "0871234567", Genre.M, new Date(), "57 greenfield drive");        
        Employe e = new Employe("EMP", "loye", "emp@loye.mail", "pass", "0871234567", Genre.F);
        sa.inscrire(c);
        sa.inscrire(e);
        Client found = sa.rechercherClientParId(1L);
        System.out.println(found.getPrenom());
    }
    
    public static void testConsultation() {
        ServiceConsultation sc = new ServiceConsultation();
        ServiceAuthentification sa = new ServiceAuthentification();
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
    
    public static void testHistoriqueClient() {
        // Create more consultations to list
        testConsultation();
        testConsultation();
        
        ServiceConsultation sc = new ServiceConsultation();
        ServiceAuthentification sa = new ServiceAuthentification();
        // Here the client would have been selected by the employee using the GUI
        Client selectedClient = sa.rechercherClientParId(1L);
        List<Consultation> historique = sc.listerHistoriqueConsultations(selectedClient);
        
        System.out.println("\n\n Historique de consultations du client: " + historique);
    }
}
