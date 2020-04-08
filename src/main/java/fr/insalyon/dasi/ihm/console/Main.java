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

    public static Employe EMPLOYE_CHOISI;
    
    public static void main(String[] args) {

        JpaUtil.init();
        
        setupDB();
        
        // Demonstration of backend functionality
        // Assertions used for demo, assertions must be enabled in VM options for this to work
        
        // CLIENT Functionality
        inscription();
        connexion();
        chercherProfilAstral();
        chercherMediumsDisponibles();
        choisirMedium();
        
        // EMPLOYE Functionality
        connexionEmploye();
        chercherTravailActuel();
        chercherHistoriqueConsultationsClient();
        commencerConsultation();
        chercherPredictions();
        validerConsultation();
        
        // This function runs X simulated consultations and displays for each one
        // the chosen employee along with their gender and the number of consultations
        // they have already done.
        //
        // This demonstrates our algorithm equally distributing the workload.
        int X = 100;
        simulationConsultations(X);
        
        printTitle("Fin demo");
        
        JpaUtil.destroy();
        
    }
    
    /*
    
        CLIENT FUNCTIONS
    
    */
    
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
        
        // We keep track of the employee chosen by our algorithm for use
        // in further demo functions.
        EMPLOYE_CHOISI = employeChoisi;
        
        // The employee is notified about the new consultation
        sc.notifierEmploye(consultation);
    }
    
    /*
    
        EMPLOYEE FUNCTIONS
    
    */
    
    public static void connexionEmploye() {
        printTitle("Connexion Employe");
        // Data will be input on frontend
        String mail = "alice.voyret@hotmail.com";
        String mdp = "motDePasse";
        
        ServiceUtilisateur su = new ServiceUtilisateur();
        Employe employe = su.authentifierEmploye(mail, mdp);
        
        System.out.println(employe.toString());
    }
    
    public static void chercherTravailActuel() {
        printTitle("Chercher Travail Actuel");
        
        Employe employe = EMPLOYE_CHOISI;
        
        // We can use the travailActuel attribute of Employe to see if the employee
        // has a job at the moment.
        if(employe.getTravailActuel() == -1) {
            System.out.println("\nPas de travail en ce moment!");
        } else {
            ServiceConsultation sc = new ServiceConsultation();
            Consultation travailActuel = sc.rechercherConsultationParId(employe.getTravailActuel());
            
            System.out.println(travailActuel.toString());
        } 
    }
    
    public static void chercherHistoriqueConsultationsClient() {
        printTitle("Chercher historique consultations");
        
        Employe employe = EMPLOYE_CHOISI;
        
        ServiceConsultation sc = new ServiceConsultation();
        Consultation travailActuel = sc.rechercherConsultationParId(employe.getTravailActuel());
        // We get all consultations associated with client travailActuel.getClient()
        List<Consultation> historiqueConsultations = sc.listerHistoriqueConsultations(travailActuel.getClient());
        
        if(historiqueConsultations.size() > 1) {
            for(Consultation c : historiqueConsultations) {
                // We do not want to display the current consultation!
                if(c.getId() != travailActuel.getId()) {
                    System.out.println("\n" + c.toString());
                }
            }
        } else {
            System.out.println("\nAucune consultation antérieure");
        }
        
    }
    
    public static void commencerConsultation() {
        printTitle("Commencer Consultation");
        
        Employe employe = EMPLOYE_CHOISI;
        
        ServiceConsultation sc = new ServiceConsultation();
        Consultation consultation = sc.rechercherConsultationParId(employe.getTravailActuel());
        
        sc.commencerConsultation(consultation);
        sc.notifierClient(consultation);
    }
    
    public static void chercherPredictions() {
        printTitle("Chercher predictions");
        
        Employe employe = EMPLOYE_CHOISI;
        
        ServiceConsultation sc = new ServiceConsultation();
        Consultation consultation = sc.rechercherConsultationParId(employe.getTravailActuel());
        
        String prediction = sc.genererPrediction(consultation, new Random().nextInt(4)+1, new Random().nextInt(4)+1, new Random().nextInt(4)+1);
        System.out.println(prediction);
    }
    
    public static void validerConsultation() {
        printTitle("Valider consultation");
        
        Employe employe = EMPLOYE_CHOISI;
        
        ServiceConsultation sc = new ServiceConsultation();
        Consultation consultation = sc.rechercherConsultationParId(employe.getTravailActuel());
        
        sc.validerConsultation(consultation, "La consultation s'est bien déroulée !");
        
        System.out.println(consultation.toString());
    }
    
    public static void simulationConsultations(int nombreConsultations) {
        printTitle("Simulation de " + nombreConsultations + " consultations");
        System.out.println("--------------------------------------------------------------------------------------------------------");
        System.out.printf ("%-6s%-16s%-24s%-16s%-8s\n", "ID", "Prénom employe", "Nom employe", "Genre employe", "Nombre de consultations faites par employe");
        System.out.println("--------------------------------------------------------------------------------------------------------");
        for(int i=0; i<nombreConsultations; i++) {
            simulerConsultation();
        }
    }
    
    /*
    
        HELPER FUNCTIONS
    
    */
    
    public static void simulerConsultation() {
        String mail = "moez.woagner@laposte.net";
        String mdp = "password123";
        ServiceUtilisateur su = new ServiceUtilisateur();
        Client client = su.authentifierClient(mail, mdp);
        ServiceConsultation sc = new ServiceConsultation();
        ServiceMedium sm = new ServiceMedium();
        List<Medium> mediumsDisponibles = sm.listerMediums();
        Medium mediumChoisi = mediumsDisponibles.get(new Random().nextInt(mediumsDisponibles.size()));
        Employe employeChoisi = su.choisirEmployePourTravail(mediumChoisi);
        Consultation consultation = new Consultation(employeChoisi, client, mediumChoisi);
        sc.creerConsultation(consultation);
        sc.commencerConsultation(consultation);
        sc.validerConsultation(consultation, "");
        String genre = consultation.getEmploye().getGenre() == Genre.F ? "F" : "H";
        System.out.printf ("%-6s%-16s%-24s%-16s%-8s\n", consultation.getId().toString(), consultation.getEmploye().getPrenom(), 
                consultation.getEmploye().getNom(), genre, consultation.getEmploye().getNoTravail());
        System.out.println("--------------------------------------------------------------------------------------------------------");
        //System.out.println(consultation.getId() + "  : " + consultation.getEmploye().getPrenom() 
        //        + " " + consultation.getEmploye().getNom() + ",                                " + (consultation.getEmploye().getGenre() == Genre.F ? "F" : "H") + ", "+ consultation.getEmploye().getNoTravail());
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
        Employe e5 = new Employe("SING", "Ainhoa", "asing8183@free.fr", "motDePasse", "0705224200", Genre.F);
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
}
