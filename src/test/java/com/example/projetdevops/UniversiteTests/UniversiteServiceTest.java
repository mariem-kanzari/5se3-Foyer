package com.example.projetdevops.UniversiteTests;

import com.example.projetdevops.DAO.Entities.Universite;
import com.example.projetdevops.DAO.Repositories.UniversiteRepository;
import com.example.projetdevops.Services.Universite.UniversiteService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UniversiteServiceTest {

    @Autowired
    private UniversiteRepository universiteRepository;

    @Autowired
    private UniversiteService universiteService;

    @BeforeEach
    public void setUp() {
        // Optionnel : Vous pouvez réinitialiser la base de données ou faire d'autres configurations ici.
    }

    @AfterEach
    public void tearDown() {
        universiteRepository.deleteAll(); // Réinitialise la base de données après chaque test
    }

    @Test
    @Order(1)
    public void testAddOrUpdate() {
        Universite universite = new Universite();
        universite.setNomUniversite("Test Université");

        Universite savedUniversite = universiteService.addOrUpdate(universite);
        Universite fetchedUniversite = universiteRepository.findById(savedUniversite.getIdUniversite()).orElse(null);

        assertThat(fetchedUniversite).isNotNull();
        Assertions.assertEquals(universite.getNomUniversite(), fetchedUniversite.getNomUniversite());
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

        List<Universite> result = universiteService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Universite::getNomUniversite).containsExactlyInAnyOrder("Université A", "Université B");
    }

    @Test
    @Order(3)
    public void testFindById() {
        Universite universite = new Universite();
        universite.setNomUniversite("Université A");
        Universite savedUniversite = universiteRepository.save(universite);

        Universite foundUniversite = universiteService.findById(savedUniversite.getIdUniversite());

        assertThat(foundUniversite).isNotNull();
        Assertions.assertEquals(savedUniversite.getIdUniversite(), foundUniversite.getIdUniversite());
        Assertions.assertEquals("Université A", foundUniversite.getNomUniversite());
    }

    @Test
    @Order(4)
    public void testFindById_NotFound() {
        Assertions.assertThrows(RuntimeException.class, () -> universiteService.findById(999L)); // ID non existant
    }

    @Test
    @Order(5)
    public void testDeleteById() {
        Universite universite = new Universite();
        universite.setNomUniversite("Université A");
        Universite savedUniversite = universiteRepository.save(universite);

        universiteService.deleteById(savedUniversite.getIdUniversite());

        Assertions.assertThrows(RuntimeException.class, () -> universiteService.findById(savedUniversite.getIdUniversite())); // Vérifie si supprimé
    }

    @Test
    @Order(6)
    public void testDelete() {
        Universite universite = new Universite();
        universite.setNomUniversite("Université A");
        Universite savedUniversite = universiteRepository.save(universite);

        universiteService.delete(savedUniversite);

        Assertions.assertThrows(RuntimeException.class, () -> universiteService.findById(savedUniversite.getIdUniversite())); // Vérifie si supprimé
    }
}