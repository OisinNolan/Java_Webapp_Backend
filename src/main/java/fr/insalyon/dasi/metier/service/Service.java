/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.dasi.metier.service;

import fr.insalyon.dasi.dao.ConsultationDao;
import fr.insalyon.dasi.dao.UtilisateurDao;
import fr.insalyon.dasi.dao.JpaUtil;
import fr.insalyon.dasi.dao.MediumDao;
import fr.insalyon.dasi.metier.modele.Astrologue;
import fr.insalyon.dasi.metier.modele.Cartomancien;
import fr.insalyon.dasi.metier.modele.Client;
import fr.insalyon.dasi.metier.modele.Consultation;
import fr.insalyon.dasi.metier.modele.Employe;
import fr.insalyon.dasi.metier.modele.Medium;
import fr.insalyon.dasi.metier.modele.ProfilAstral;
import fr.insalyon.dasi.metier.modele.Spirite;
import fr.insalyon.dasi.metier.modele.Statistiques;
import fr.insalyon.dasi.metier.modele.Utilisateur;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import fr.insalyon.dasi.util.AstroTest;
import fr.insalyon.dasi.util.Genre;
import fr.insalyon.dasi.util.Message;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author oisinnolan
 */
public class Service {
    
    protected UtilisateurDao utilisateurDao = new UtilisateurDao();

    /* 
    
        Fonctions pour Utilisateur Générique
    
    */
    
    public Long inscrire(Utilisateur utilisateur) {
        Long resultat = null;
        JpaUtil.creerContextePersistance();
        try {
            JpaUtil.ouvrirTransaction();
            utilisateurDao.creer(utilisateur);
            JpaUtil.validerTransaction();
            resultat = utilisateur.getId();
            
            Logger.getAnonymousLogger().log(Level.FINE, "User successfully persisted");
        } catch (Exception ex) {
            Logger.getAnonymousLogger().log(Level.WARNING, "Exception lors de l'appel au Service inscrireClient(client)", ex);
            // Add switch statement here to handle various error types 
            // i.e unique constraint violated on email column
            JpaUtil.annulerTransaction();
            resultat = null;
        } finally {
            JpaUtil.fermerContextePersistance();
            
            if(utilisateur instanceof Client) {
                Client nouveauClient = (Client) utilisateur;

                Long idProfilAstral = this.creerProfilAstral(nouveauClient);
                
                boolean echec = (resultat == null || idProfilAstral == null );
                
                String objet = echec ? "Bonjour " + nouveauClient.getPrenom() + ", votre inscription au service PREDICT’IF a malencontreusement échoué...\n"
                        + "Merci de recommencer ultérieurement."
                        : "Bonjour " + nouveauClient.getPrenom() + ", nous vous confirmons votre inscription au service PREDICT’IF."
                        + "\nRendez-vous vite sur notre site pour consulter votre profil astrologique et profiter des dons incroyables de nos mediums";

                String corps = echec ? "Echec de l’inscription chez PREDICT’IF" : "Bienvenue chez PREDICT’IF";
                Message.envoyerMail("contact@predict.if", nouveauClient.getMail(), objet, corps);

                
            }
        }
        return resultat;
    }
    
    public Long mettreAJour(Utilisateur utilisateur) {
        Long resultat = null;
        JpaUtil.creerContextePersistance();
        try {
            JpaUtil.ouvrirTransaction();
            utilisateurDao.mettreAJour(utilisateur);
            JpaUtil.validerTransaction();
            resultat = utilisateur.getId();
            Logger.getAnonymousLogger().log(Level.FINE, "User successfully persisted");
        } catch (Exception ex) {
            Logger.getAnonymousLogger().log(Level.WARNING, "Exception lors de l'appel au Service inscrireClient(client)", ex);
            // Add switch statement here to handle various error types 
            // i.e unique constraint violated on email column
            JpaUtil.annulerTransaction();
            resultat = null;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return resultat;
    }
    
    public Utilisateur rechercherUtilisateurParId(Long id) {
        Utilisateur resultat = null;
        JpaUtil.creerContextePersistance();
        try {
            resultat = utilisateurDao.chercherParId(id);
        } catch (Exception ex) {
            Logger.getAnonymousLogger().log(Level.WARNING, "Exception lors de l'appel au Service rechercherUtilisateurParId(id)", ex);
            resultat = null;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return resultat;
    }
    
    /* 
    
        Fonctions pour Clients
    
    */
    
    public Client rechercherClientParId(Long id) {
        Client resultat = null;
        JpaUtil.creerContextePersistance();
        try {
            resultat = utilisateurDao.chercherClientParId(id);
        } catch (Exception ex) {
            Logger.getAnonymousLogger().log(Level.WARNING, "Exception lors de l'appel au Service rechercherClientParId(id)", ex);
            resultat = null;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return resultat;
    }
    
    public Client authentifierClient(String mail, String motDePasse) {
        Client resultat = null;
        JpaUtil.creerContextePersistance();
        try {
            // Recherche du client
            Client client = utilisateurDao.chercherClientParMail(mail);
            if (client != null) {
                // Vérification du mot de passe
                if (client.getMotDePasse().equals(motDePasse)) {
                    resultat = client;
                }
            }
        } catch (Exception ex) {
            Logger.getAnonymousLogger().log(Level.WARNING, 
                    "Exception lors de l'appel au Service authentifierClient(mail,motDePasse)", ex);
            resultat = null;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return resultat;
    }
    
    
    public Long creerProfilAstral(Client client) {
        Long resultat = null;
        
        AstroTest APIAstro = new AstroTest();
        
        List<String> reponseAPI;
        try {
            reponseAPI = APIAstro.getProfil(client.getPrenom(), client.getDateNaissance());
            ProfilAstral profilAstral = new ProfilAstral(reponseAPI.get(0), reponseAPI.get(1), reponseAPI.get(2), reponseAPI.get(3));
            client.setProfilAstral(profilAstral);
            this.mettreAJour(client);
            resultat = profilAstral.getId();
        } catch(IOException ex) {
            Logger.getAnonymousLogger().log(Level.WARNING, 
                    "IOException lors de l'appel au Service creerProfilAstral()", ex);
        }
        
        return resultat;
    }
    
    /* 
    
        Fonctions pour Employe
    
    */
    
    public Employe rechercherEmployeParId(Long id) {
        Employe resultat = null;
        JpaUtil.creerContextePersistance();
        try {
            resultat = utilisateurDao.chercherEmployeParId(id);
        } catch (Exception ex) {
            Logger.getAnonymousLogger().log(Level.WARNING, "Exception lors de l'appel au Service rechercherEmployeParId(id)", ex);
            resultat = null;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return resultat;
    }
    
    public Employe authentifierEmploye(String mail, String motDePasse) {
        Employe resultat = null;
        JpaUtil.creerContextePersistance();
        try {
            // Recherche du employe
            Employe employe = utilisateurDao.chercherEmployeParMail(mail);
            if (employe != null) {
                // Vérification du mot de passe
                if (employe.getMotDePasse().equals(motDePasse)) {
                    resultat = employe;
                }
            }
        } catch (Exception ex) {
            Logger.getAnonymousLogger().log(Level.WARNING, 
                    "Exception lors de l'appel au Service authentifierEmploye(mail,motDePasse)", ex);
            resultat = null;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return resultat;
    }
    
    public Employe choisirEmployePourTravail(Medium medium){
        Employe resultat = null;
        JpaUtil.creerContextePersistance();
        try {
            resultat = utilisateurDao.choisirParGenre(medium.getGenre());
        } catch (Exception ex) {
            Logger.getAnonymousLogger().log(Level.WARNING, 
                    "Exception lors de l'appel au Service choisirEmploye(Medium)", ex);
            resultat = null;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return resultat;
    }
    
    // Consultation related Services
    protected ConsultationDao consultationDao = new ConsultationDao();
    
    public Long creerConsultation(Consultation consultation) {
        Long resultat = null;
        JpaUtil.creerContextePersistance();
        try {
            JpaUtil.ouvrirTransaction();
            consultationDao.creer(consultation);
            consultation.getClient().setIdConsultationActuel(consultation.getId());
            consultation.getEmploye().setTravailActuel(consultation.getId());
            consultation.getEmploye().setNoTravail(consultation.getEmploye().getNoTravail() + 1);
            consultationDao.mettreAJour(consultation);
            JpaUtil.validerTransaction();
            resultat = consultation.getId();
            Logger.getAnonymousLogger().log(Level.FINE, "Consultation successfully persisted");
        } catch (Exception ex) {
            Logger.getAnonymousLogger().log(Level.WARNING,
                    "Exception during call to creerConsultation() in ServiceConsultation", ex);
            JpaUtil.annulerTransaction();
            resultat = null;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return resultat;
    }
    
    public Consultation rechercherConsultationParId(Long id) {
        Consultation resultat = null;
        JpaUtil.creerContextePersistance();
        try {
            resultat = consultationDao.chercherParId(id);
        } catch (Exception ex) {
            Logger.getAnonymousLogger().log(Level.WARNING, 
                    "Exception lors de l'appel au Service rechercherConsultationParId(id)", ex);
            resultat = null;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return resultat;
    }
    
    public Long commencerConsultation(Consultation consultation) {
        Long resultat = null;
        JpaUtil.creerContextePersistance();
        try {
            JpaUtil.ouvrirTransaction();
            consultation.setDebut(new Date());
            consultationDao.mettreAJour(consultation);
            JpaUtil.validerTransaction();
            resultat = consultation.getId();
            Logger.getAnonymousLogger().log(Level.FINE, "Consultation successfully commenced");
        } catch (Exception ex) {
            Logger.getAnonymousLogger().log(Level.WARNING,
                    "Exception during call to commencerConsultation() in ServiceConsultation", ex);
            JpaUtil.annulerTransaction();
            resultat = null;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return resultat;
    }
    
    public Long validerConsultation(Consultation consultation, String commentaire) {
        Long resultat = null;
        JpaUtil.creerContextePersistance();
        try {
            JpaUtil.ouvrirTransaction();
            consultation.setFin(new Date());
            consultation.setCommentaire(commentaire);
            consultation.getEmploye().setTravailActuel(-1L);
            consultationDao.mettreAJour(consultation);
            JpaUtil.validerTransaction();
            resultat = consultation.getId();
            //Add consultation to statistiques
            Statistiques.ajouterConsultation(consultation);
            Logger.getAnonymousLogger().log(Level.FINE, "Consultation successfully validated");
        } catch (Exception ex) {
            Logger.getAnonymousLogger().log(Level.WARNING,
                    "Exception during call to validerConsultation() in ServiceConsultation", ex);
            JpaUtil.annulerTransaction();
            resultat = null;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return resultat;
    }
    
    // TODO update function signature in the docs
    public List<Consultation> listerHistoriqueConsultations(Client client) {
        List<Consultation> resultat = null;
        JpaUtil.creerContextePersistance();
        try {
            resultat = consultationDao.chercherParUtilisateur(client);
        } catch (Exception ex) {
            Logger.getAnonymousLogger().log(Level.WARNING,
                    "Exception lors de l'appel au Service listerHistoriqueConsultations()", ex);
            resultat = null;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return resultat;
    }
    
    public void notifierEmploye(Consultation consultation) {
        
        Employe employe = consultation.getEmploye();
        Client client = consultation.getClient();
        Medium medium = consultation.getMedium();
        
        String message = "Bonjour " + employe.getPrenom() + ". Consultation requise pour "
                + (client.getGenre() == Genre.F ? "Mme " : "M ") + client.getPrenom() + " "
                + client.getNom().toUpperCase() + ".\nMédium à incarner : " + medium.getDenomination();
        
        Message.envoyerNotification(employe.getNoTelephone(), message);
    }
    
    public void notifierClient(Consultation consultation) {
        
        Employe employe = consultation.getEmploye();
        Client client = consultation.getClient();
        Medium medium = consultation.getMedium();
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy à HH'h'mm");
        
        String message = "Bonjour " + client.getPrenom() + ". J'ai bien reçu votre demande de consultation du "
                +  dateFormat.format(consultation.getDateCreation()) + ".\nVous pouvez dès à présent me contacter au " + employe.getNoTelephone()
                + ". A tout de suite !\n\nMédiumiquement vôtre, \n\n" + medium.getDenomination();
        
        Message.envoyerNotification(client.getNoTelephone(), message);
    }
    
    public String genererPrediction(Consultation consultation, int amour, int sante, int travail){
        AstroTest APIAstro = new AstroTest();
        List<String> reponseAPI;
        String prediction = null;
        try {
            ProfilAstral profilClient = consultation.getClient().getProfilAstral();
            reponseAPI = APIAstro.getPredictions(profilClient.getCouleur(), profilClient.getAnimalTotem(), amour, sante, travail);
            prediction = "AMOUR:\n" + reponseAPI.get(0) + "\n\nSANTE:\n" + reponseAPI.get(1) + "\n\nTRAVAIL:\n" + reponseAPI.get(2);
        } catch(IOException ex) {
            Logger.getAnonymousLogger().log(Level.WARNING, 
                    "IOException lors de l'appel au fonction genererPrediction()", ex);
        }
        
        return prediction;
    }
    
    // Médium related services
    protected MediumDao mediumDao = new MediumDao();
    
    public boolean setupMediums() {
        boolean resultat = false;
        JpaUtil.creerContextePersistance();
        try {
            JpaUtil.ouvrirTransaction();
            // Hard coded medium data
            Medium m1 = new Spirite("Gwenaëlle", Genre.F, "Spécialiste des grandes conversations au-delà de TOUTES les frontières.", "Boule de cristal");
            Medium m2 = new Spirite("Professeur Tran", Genre.M, "Votre avenir est devant vous : regardons-le ensemble !", "Marc de café, boule de cristal, oreilles de lapin");
            Medium m3 = new Cartomancien("Mme Irma", Genre.F, "Comprenez votre entourage grâce à mes cartes ! Résultats rapides");
            Medium m4 = new Cartomancien("Endora", Genre.F, "Mes cartes répondront à toutes vos questions personnelles.");
            Medium m5 = new Astrologue("Serena", Genre.F, " Basée à Champigny-sur-Marne, Serena vous révèlera votre avenir pour éclairer votre passé.", "École Normale Supérieure d’Astrologie (ENS-Astro)", 2006);
            Medium m6 = new Astrologue("Mr M", Genre.M, "Avenir, avenir, que nous réserves-tu ? N'attendez plus, demandez à me consulter!", "Institut des Nouveaux Savoirs Astrologiques", 2010);
            
            mediumDao.creer(m1);
            mediumDao.creer(m2);
            mediumDao.creer(m3);
            mediumDao.creer(m4);
            mediumDao.creer(m5);
            mediumDao.creer(m6);
            
            JpaUtil.validerTransaction();
            Logger.getAnonymousLogger().log(Level.FINE, "Medium data initialised successfully");
            resultat = true;
        } catch (Exception ex) {
            Logger.getAnonymousLogger().log(Level.WARNING, "Exception during call to setupMediums() in ServiceMedium", ex);
            // Add switch statement here to handle various error types 
            // i.e unique constraint violated on email column
            JpaUtil.annulerTransaction();
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return resultat;
    }
    
    public Medium rechercherParId(Long id) {
        Medium resultat = null;
        JpaUtil.creerContextePersistance();
        try {
            resultat = mediumDao.chercherParId(id);
        } catch (Exception ex) {
            Logger.getAnonymousLogger().log(Level.WARNING, "Exception lors de l'appel au Service rechercherParId(id)", ex);
            resultat = null;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return resultat;
    }
    
    public Medium rechercherParDenomination(String denomination) {
        Medium resultat = null;
        JpaUtil.creerContextePersistance();
        try {
            resultat = mediumDao.chercherParDenomination(denomination);
        } catch (Exception ex) {
            Logger.getAnonymousLogger().log(Level.WARNING, "Exception lors de l'appel au Service rechercherParDenomination(String)", ex);
            resultat = null;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return resultat;
    }
    
    public List<Medium> listerMediums() {
        List<Medium> resultat = null;
        JpaUtil.creerContextePersistance();
        try {
            // This code ensures that mediums of a certain gender
            // are only shown in the case that there is one of more 
            // employees of that gender currently available
            long noHommes = utilisateurDao.nombreEmployesHommeDisponible();
            long noFemmes = utilisateurDao.nombreEmployesFemmeDisponible();
            
            if(noHommes == 0) {
                resultat = mediumDao.listerMediumsDeGenre(Genre.F);
            } else if(noFemmes == 0) {
                resultat = mediumDao.listerMediumsDeGenre(Genre.M);
            } else {
                resultat = mediumDao.listerMediums();
            }
        } catch (Exception ex) {
            Logger.getAnonymousLogger().log(Level.WARNING, "Exception lors de l'appel au Service listerMediums()", ex);
            resultat = null;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return resultat;
    }
    
    public Map<String, List<Medium>> trierMediumsParType(List<Medium> listeMediums) {
        Map<String, List<Medium>> typesMediums = new HashMap<>();
        typesMediums.put("Spirite", new ArrayList<>());
        typesMediums.put("Cartomancien", new ArrayList<>());
        typesMediums.put("Astrologue", new ArrayList<>());
        
        for(Medium m : listeMediums) {
            if(m instanceof Spirite) {
                typesMediums.get("Spirite").add(m);
            } else if(m instanceof Cartomancien) {
                typesMediums.get("Cartomancien").add(m);
            } else if(m instanceof Astrologue) {
                typesMediums.get("Astrologue").add(m);
            }
        }
        
        return typesMediums;
    }
    
    // Statistics related services
    
    private void calculerEtMettreAJour() {
                
        HashMap<Medium, Integer> consultationsParMedium =
                (HashMap<Medium, Integer>) Statistiques.getConsultationsParMediumMap();
        HashMap<Employe, List<Client>> repartition = 
                (HashMap<Employe, List<Client>>) Statistiques.getClientRepartitionMap();
        
        Consultation derniere = Statistiques.getDerniereCalculee();
        int derniereIndex;
        if (derniere == null) {
            derniereIndex = 0;
        } else {
            // Get the index of the last calculated consultation
            derniereIndex = Statistiques.getConsultations().indexOf(derniere);
        }
        // Get the list of not yet calculated consultations
        List<Consultation> aCalculer = Statistiques.getConsultations().subList(derniereIndex+1, Statistiques.getNbConsultations());
        
        for (Consultation c : aCalculer) {
            // Collecting data to store number of consultations per Meduim
            Medium m = c.getMedium();
            if (consultationsParMedium.containsKey(m)) {
                int nbConsultations = consultationsParMedium.get(m);
                consultationsParMedium.replace(m, nbConsultations+1);
            } else {
                consultationsParMedium.put(m, 1);
            }
                     
            // Collecting data to store client repartition
            Employe e = c.getEmploye();
            Client cl = c.getClient();
            if (!repartition.containsKey(e)) {
                ArrayList<Client> newList = new ArrayList<Client>();
                newList.add(cl);
                repartition.put(e, newList);
            } else if (!repartition.get(e).contains(cl)) {
                repartition.get(e).add(cl);
            }
        }
        
        // Updating top 5 mediums
        ArrayList<Medium> allMediums = new ArrayList<>();
        allMediums.addAll(listerMediums());
        ArrayList<Integer> vals = new ArrayList<>();
        for(Medium m : allMediums){
            Integer i = consultationsParMedium.get(m);
            vals.add(i);
        }
        Collections.sort(vals);
        Collections.reverse(vals);
        Integer threshhold = vals.get(5);
        ArrayList<Medium> topMediums = new ArrayList<>();
        for(Medium m : allMediums){
            Integer i = consultationsParMedium.get(m);
            if(i.compareTo(threshhold) > 0) topMediums.add(m);
        }
        
        // 0-4 in case last few Mediums have the same number of consultations
        Statistiques.setTopCinqueMediums(topMediums.subList(0, 4));
        Statistiques.setConsultationsParMediumMap(consultationsParMedium);
        Statistiques.setClientRepartitionMap(repartition);
        Statistiques.setDerniereCalculee(Statistiques.getDerniereConsultation());
        
    }
    
    public Map<Medium, Integer> listerNombreParMedium() {
        if (!Statistiques.miseAJour()) {
            calculerEtMettreAJour();
        }
        return Statistiques.getConsultationsParMediumMap();
    }
    
    public Map<Employe, List<Client>> listerRepartitionClients() {
        if (!Statistiques.miseAJour()) {
            calculerEtMettreAJour();
        }
        return Statistiques.getClientRepartitionMap();
    }
    
    public List<Medium> listerTopCinqueMediums() {
        if (!Statistiques.miseAJour()) {
            calculerEtMettreAJour();
        }
        return Statistiques.getTopCinqueMediums();
    }
}