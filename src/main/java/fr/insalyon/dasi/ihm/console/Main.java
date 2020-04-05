package fr.insalyon.dasi.ihm.console;

import fr.insalyon.dasi.util.Message;
import fr.insalyon.dasi.dao.JpaUtil;
import fr.insalyon.dasi.metier.modele.Client;
import fr.insalyon.dasi.metier.modele.Consultation;
import fr.insalyon.dasi.metier.modele.Employe;
import fr.insalyon.dasi.util.Genre;
import fr.insalyon.dasi.metier.modele.Medium;
import fr.insalyon.dasi.metier.service.ServiceUtilisateur;
import fr.insalyon.dasi.metier.service.ServiceConsultation;
import fr.insalyon.dasi.metier.service.ServiceMedium;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Oisin Nolan, Piotr Fr¹tczak
 */
public class Main {

    public static void main(String[] args) {

        JpaUtil.init();
        
        testAuthentication();
        testConsultation();
        
        JpaUtil.destroy();
        
    }
    
    /*
    
        The code used in the front end (client side) will resemble
        the code used in these test functions
    
    */
    
    public static void testAuthentication() {
        ServiceUtilisateur sa = new ServiceUtilisateur();
        Client c = new Client("CLI", "ent", "cli@ent.mail", "passwrd", "0871234567", Genre.M, new Date(), "57 greenfield drive");        
        Employe e = new Employe("EMP", "loye", "emp@loye.mail", "pass", "0871234567", Genre.F);
        sa.inscrire(c);
        sa.inscrire(e);
        Client found = sa.rechercherClientParId(1L);
        System.out.println(found.getPrenom());
    }
    
    public static void testConsultation() {
        ServiceConsultation sc = new ServiceConsultation();
        ServiceUtilisateur sa = new ServiceUtilisateur();
        ServiceMedium sm = new ServiceMedium();
        // Here the client would be the currently logged in user
        Client selectedClient = sa.rechercherClientParId(1L);
        sm.setupMediums();
        Medium chosenMedium = sm.rechercherParId(1L);

        Employe employe = sa.choisirEmployePourTravail(chosenMedium);
        Consultation consultation = new Consultation(employe, selectedClient, chosenMedium);
        
        System.out.println("Consultation: " + sc.creerConsultation(consultation));
        
        employe.setTravailActuel(consultation);
        employe.setNoTravail(employe.getNoTravail()+1);
        sa.mettreAJour(employe);
        
        sc.notifierEmploye(consultation);
        
        // This function will be called when the employe indicates
        // that they're ready to start the consultation
        sc.notifierClient(consultation);
        
        // These functions can be called to begin / end the consultation
        sc.commencerConsultation(consultation);
        
        // Example of auto-generated prediction
        System.out.println(sc.genererPrediction(consultation, 1, 2, 3));
        
        sc.validerConsultation(consultation, "Consultation went great !");
    }
    
    public static void testHistoriqueClient() {
        // Create more consultations to list
        testConsultation();
        testConsultation();
        
        ServiceConsultation sc = new ServiceConsultation();
        ServiceUtilisateur sa = new ServiceUtilisateur();
        // Here the client would have been selected by the employee using the GUI
        Client selectedClient = sa.rechercherClientParId(1L);
        List<Consultation> historique = sc.listerHistoriqueConsultations(selectedClient);
        
        System.out.println("\n\n Historique de consultations du client: " + historique);
    }
}