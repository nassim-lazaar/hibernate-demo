package com.example;

import com.example.model.Produit;
import org.h2.tools.Server;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        // --- ÉTAPE 10 : Console Web H2 ---
        try {
            Server.createWebServer("-web", "-webPort", "8082").start();
            System.out.println("Console H2 disponible sur : http://localhost:8082");
        } catch (Exception e) {
            System.out.println("Erreur lors du démarrage de la console H2");
            e.printStackTrace();
        }

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hibernate-demo");

        insererProduits(emf);
        lireProduits(emf);

        System.out.println("\nApplication en attente... Vous pouvez consulter http://localhost:8082");
        System.out.println("Appuyez sur ENTRÉE dans cette console pour quitter.");
        new Scanner(System.in).nextLine();

        emf.close();
    }

    private static void insererProduits(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            Produit p1 = new Produit("Laptop", new BigDecimal("999.99"));
            Produit p2 = new Produit("Smartphone", new BigDecimal("499.99"));
            Produit p3 = new Produit("Tablette", new BigDecimal("299.99"));

            em.persist(p1);
            em.persist(p2);
            em.persist(p3);

            em.getTransaction().commit();
            System.out.println("Produits insérés avec succès !");
            
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    private static void lireProduits(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        try {
            List<Produit> produits = em.createQuery("SELECT p FROM Produit p", Produit.class).getResultList();
            
            System.out.println("\nListe des produits :");
            for (Produit produit : produits) {
                System.out.println(produit);
            }

            System.out.println("\nRecherche du produit avec ID=2 :");
            Produit produitTrouve = em.find(Produit.class, 2L);
            
            if (produitTrouve != null) {
                System.out.println(produitTrouve);
            } else {
                System.out.println("Produit non trouvé");
            }
        } finally {
            em.close();
        }
    }
}
