package com.example.projetdevops.UniversiteTests;

import com.example.projetdevops.DAO.Entities.Universite;
import com.example.projetdevops.DAO.Repositories.UniversiteRepository;
import com.example.projetdevops.Services.Universite.UniversiteService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(properties = "spring.test.context.timeout=30000")

@SpringBootTest
@Transactional

@TestPropertySource(properties = "spring.test.context.timeout=10000")  // 10 seconds timeout
@SpringBootTest(properties = "spring.test.context.failure-threshold=2")
@Transactional // This ensures tests are wrapped in a transaction

public class UniversiteServiceTest {

    @Autowired
    private UniversiteRepository universiteRepository;

    @Autowired // Inject EntityManager here if needed
    private EntityManager entityManager;

    private UniversiteService universiteService;

    @BeforeEach
    public void setUp() {
        universiteService = new UniversiteService(universiteRepository);
    }

    @AfterEach
    public void tearDown() {
        universiteRepository.deleteAll(); // Clear universities
    }

    @Test
    @Order(1)
    public void testAddOrUpdate() {
        Universite universite = new Universite();
        universite.setNomUniversite("Test Université");

        Universite savedUniversite = universiteService.addOrUpdate(universite);

        Universite fetchedUniversite = universiteRepository.findById(savedUniversite.getIdUniversite()).orElse(null);
        assertThat(fetchedUniversite).isNotNull(); // Ensure fetchedUniversite is not null
        assertEquals(universite.getNomUniversite(), fetchedUniversite.getNomUniversite());
    }

    @Test
    @Order(2)
    public void testFindAll() {
        Universite universite1 = new Universite();
        universite1.setNomUniversite("Université A");
        universiteRepository.save(universite1);

        Universite universite2 = new Universite();
        universite2.setNomUniversite("Université B");
        universiteRepository.save(universite2);

        List<Universite> universites = universiteService.findAll();

        assertThat(universites).hasSize(2);
        assertThat(universites).extracting(Universite::getNomUniversite).containsExactlyInAnyOrder("Université A",
                "Université B");
    }

    @Test
    @Order(3)
    public void testFindById() {
        Universite universite = new Universite();
        universite.setNomUniversite("Université A");
        Universite savedUniversite = universiteRepository.save(universite);

        Universite foundUniversite = universiteService.findById(savedUniversite.getIdUniversite());

        assertThat(foundUniversite).isNotNull();
        assertEquals(savedUniversite.getIdUniversite(), foundUniversite.getIdUniversite());
        assertEquals("Université A", foundUniversite.getNomUniversite());
    }

    @Test
    @Order(4)
    public void testFindById_NotFound() {
        assertThrows(IllegalArgumentException.class, () -> universiteService.findById(999L)); // ID inexistant
    }

    @Test
    @Order(5)
    public void testDeleteById() {
        Universite universite = new Universite();
        universite.setNomUniversite("Université A");
        Universite savedUniversite = universiteRepository.save(universite);

        universiteService.deleteById(savedUniversite.getIdUniversite());

        // Modifier le test pour vérifier l'IllegalArgumentException
        assertThrows(IllegalArgumentException.class,
                () -> universiteService.findById(savedUniversite.getIdUniversite())); // ID de l'université supprimée
    }

    @Test
    @Order(6)
    public void testDelete() {
        Universite universite = new Universite();
        universite.setNomUniversite("Université A");
        Universite savedUniversite = universiteRepository.save(universite);

        // Delete the university
        universiteService.delete(savedUniversite);

        // Expect IllegalArgumentException since UniversiteNotFoundException is not
        // thrown by the service
        assertThrows(IllegalArgumentException.class,
                () -> universiteService.findById(savedUniversite.getIdUniversite()));
    }
}
