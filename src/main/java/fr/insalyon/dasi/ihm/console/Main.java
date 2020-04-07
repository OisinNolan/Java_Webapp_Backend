package fr.insalyon.dasi.ihm.console;

import util.Message;
import fr.insalyon.dasi.dao.JpaUtil;
import fr.insalyon.dasi.metier.modele.Astrologue;
import fr.insalyon.dasi.metier.modele.Cartomancien;
import fr.insalyon.dasi.metier.modele.Client;
import fr.insalyon.dasi.metier.modele.Consultation;
import fr.insalyon.dasi.metier.modele.Employe;
import fr.insalyon.dasi.metier.modele.Genre;
import fr.insalyon.dasi.metier.modele.Medium;
import fr.insalyon.dasi.metier.modele.ProfilAstral;
import fr.insalyon.dasi.metier.modele.Spirite;
import fr.insalyon.dasi.metier.service.ServiceUtilisateur;
import fr.insalyon.dasi.metier.service.ServiceConsultation;
import fr.insalyon.dasi.metier.service.ServiceMedium;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author DASI Team
 */
public class Main {

    public static void main(String[] args) {

        JpaUtil.init();
        
        setupDB();
        
        // Demonstration of backend functionality
        // Assertions used for demo, assertions must be enabled in VM options for this to work
        inscription();
        connexion();
        chercherProfilAstral();
        chercherMediumsDisponibles();
        choisirMedium();
        
        printTitle("Fin demo");
        
        JpaUtil.destroy();
        
    }
    
    
    public static void printTitle(String title) {
        System.out.println("\n------------------------------------\n    " 
                + title + "\n------------------------------------");

    }
    
    public static void setupDB() {
        // Hard-coded employes
        Employe e1 = new Employe("VOYRET", "Alice", "alice.voyret@hotmail.com", "motDePasse", "0486856520", Genre.F);
        Employe e2 = new Employe("BORROTI MATIAS DANTAS", "Raphaël", "rborrotimatiasdantas4171@free.fr", "motDePasse", "0328178508", Genre.M);
        Employe e3 = new Employe("OLMEADA MARAIS", "Nor", "nolmeadamarais1551@gmail.com", "motDePasse", "0418932546", Genre.F);
        Employe e4 = new Employe("RAYES GEMEZ", "Olena", "orayesgemez5313@outlook.com", "motDePasse", "0532731620", Genre.F);
        Employe e5 = new Employe("SING", "Ainhoa", "asing8183@free.fr", "motDePasse", "0705224200	", Genre.F);
        Employe e6 = new Employe("ABDIULLINA", "David Alexander", "david-alexander.abdiullina@laposte.net", "motDePasse", "0590232772", Genre.M);
        
        ServiceUtilisateur su = new ServiceUtilisateur();
        su.inscrire(e1);
        su.inscrire(e2);
        su.inscrire(e3);
        su.inscrire(e4);
        su.inscrire(e5);
        su.inscrire(e6);
        
        // Hard-coded mediums
        ServiceMedium sm = new ServiceMedium();
        sm.setupMediums();
    }
    
    public static void inscription() {
        printTitle("Inscription");
        // Data will be input on frontend
        String nom = "WOAGNER";
        String prenom = "Moez";
        String mail = "moez.woagner@laposte.net";
        String mdp = "password123";
        String tel = "0832205629";
        Genre genre = Genre.M;
        Date dateNaissance = new Date();
        String adresse = "6 Rue Camille Koechlin, Villeurbanne";
        
        Client client = new Client(nom, prenom, mail, mdp, tel, genre, dateNaissance, adresse);
        
        ServiceUtilisateur su = new ServiceUtilisateur();
        // We can use the same method inscrire() to register employees if needs be.
        // We will assume that the employees have already registered for this demo (Hard-coded).
        su.inscrire(client);
        
        System.out.println(client.toString());
        
        // If we call inscrire() with a client whos email / id is already associated with
        // an entity on the database, the 'unique' constraint will fail an an exception will be thrown.
        // In the case that an exception occurs, inscrire() will return null and send the user
        // an 'error' email.
        
        // example:
        //assert su.inscrire(client) == null;
        
        // Data formatting will be validated on the frontend. (Empty fields, valid dates, confirm password etc.)
    }
    
    public static void connexion() {
        printTitle("Connexion");
        // Data will be input on frontend
        String mail = "moez.woagner@laposte.net";
        String mdp = "password123";
        
        ServiceUtilisateur su = new ServiceUtilisateur();
        Client client = su.authentifierClient(mail, mdp);
        
        System.out.println(client.toString());
        
//      If we enter the wrong password, or an email that does not exist
//      on the database, su.authentifierClient() simply returns null.

        mail = "doesNot@exi.st";
        mdp = "password123";
        Client client2 = su.authentifierClient(mail, mdp);
        
        assert client2 == null;
        
        mail = "moez.woagner@laposte.net";
        mdp = "password124";
        Client client3 = su.authentifierClient(mail, mdp);
        
        assert client3 == null;
    }
    
    public static void chercherProfilAstral() {
        printTitle("Chercher profil Astral");
        
        // Connexion
        String mail = "moez.woagner@laposte.net";
        String mdp = "password123";
        
        ServiceUtilisateur su = new ServiceUtilisateur();
        Client client = su.authentifierClient(mail, mdp);
        
        // Profil astral is generated using the Astro API when the user is being registered
        ProfilAstral profilAstral = client.getProfilAstral();
        System.out.println(profilAstral.toString());
    }
    
    public static void chercherMediumsDisponibles() {
        printTitle("Chercher mediums disponibles");
        
        ServiceMedium sm = new ServiceMedium();
        // This function ensures that there is at least one employee of a given
        // gender available before displaying mediums of that gender
        List<Medium> mediumsDisponibles = sm.listerMediums();
        
        Map<String, List<Medium>> typesMediums = sm.trierMediumsParType(mediumsDisponibles);
        
        for(String type : typesMediums.keySet()) {
            System.out.println("Mediums de type " + type + ":\n");
            for(Medium m : typesMediums.get(type)) {
                System.out.println(m.toString());
            }
            System.out.println();
        }
    }
    
    public static void choisirMedium() {
        printTitle("Choisir Medium");
        
        // Connexion
        String mail = "moez.woagner@laposte.net";
        String mdp = "password123";
        
        ServiceUtilisateur su = new ServiceUtilisateur();
        Client client = su.authentifierClient(mail, mdp);
        
        ServiceConsultation sc = new ServiceConsultation();
        ServiceMedium sm = new ServiceMedium();
        
        // The user navigates to the 'Mediums Disponibles' page and sees a list
        // of mediums
        List<Medium> mediumsDisponibles = sm.listerMediums();
        // The user chooses one of these mediums
        Medium mediumChoisi = mediumsDisponibles.get(new Random().nextInt(mediumsDisponibles.size()));
        // The ServiceUtilisateur chooses an employee to give this job based on the amount of
        // work that employee has done in the past, as well as their gender.
        Employe employeChoisi = su.choisirEmployePourTravail(mediumChoisi);
        
        Consultation consultation = new Consultation(employeChoisi, client, mediumChoisi);
        sc.creerConsultation(consultation);
        
        System.out.println(consultation.toString());
        
        // The employee's details are updated -- they have been given a new job.
        employeChoisi.setTravailActuel(consultation.getId());
        employeChoisi.setNoTravail(employeChoisi.getNoTravail()+1);
        su.mettreAJour(employeChoisi);
        
        // The employee is notified about the new consultation
        sc.notifierEmploye(consultation);
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
        System.out.println(found.toString());
    }
    
    public static void testConsultation() {
        
        ServiceConsultation sc = new ServiceConsultation();
        ServiceUtilisateur sa = new ServiceUtilisateur();
        ServiceMedium sm = new ServiceMedium();
        
        // Here the client would be the currently logged in user
        Client selectedClient = sa.rechercherClientParId(1L);
        sm.setupMediums();
        Medium chosenMedium = sm.listerMediums().get(0);

        Employe employe = sa.choisirEmployePourTravail(chosenMedium);
        Consultation consultation = new Consultation(employe, selectedClient, chosenMedium);
        
        System.out.println("Consultation: " + sc.creerConsultation(consultation));
        
        employe.setTravailActuel(consultation.getId());
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
        
        System.out.println("Travail actuel during: " + consultation.getEmploye().getTravailActuel());
        
        sc.validerConsultation(consultation, "Consultation went great !");
        
        System.out.println("Travail actuel after: " + consultation.getEmploye().getTravailActuel());
        
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
