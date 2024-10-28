package com.example.projetdevops.UniversiteTests;


 import com.example.projetdevops.DAO.Entities.Universite;
 import com.example.projetdevops.DAO.Repositories.UniversiteRepository;
 import com.example.projetdevops.Services.Universite.UniversiteService;
 import org.junit.jupiter.api.*;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.boot.test.context.SpringBootTest;
 import java.util.List;
 import static org.assertj.core.api.Assertions.assertThat;
 import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UniversiteServiceTest  {

    @Autowired
    private UniversiteRepository universiteRepository;

    private UniversiteService universiteService;

    @BeforeEach
    public void setUp() {
        universiteService = new UniversiteService(universiteRepository);
    }

    @AfterEach
    public void tearDown() {
        universiteRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void testAddOrUpdate() {
        Universite universite = new Universite();
        universite.setNomUniversite("Université Test");
        universite.setAdresse("123 Rue Test");

        Universite savedUniversite = universiteService.addOrUpdate(universite);

        Universite fetchedUniversite = universiteRepository.findById(savedUniversite.getIdUniversite()).orElse(null);
        assertThat(fetchedUniversite).isNotNull();
        assertThat(fetchedUniversite.getNomUniversite()).isEqualTo("Université Test");
        assertThat(fetchedUniversite.getAdresse()).isEqualTo("123 Rue Test");
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
        assertThat(universites).extracting(Universite::getNomUniversite).containsExactlyInAnyOrder("Université A", "Université B");
    }

    @Test
    @Order(3)
    public void testFindById() {
        Universite universite = new Universite();
        universite.setNomUniversite("Université A");
        Universite savedUniversite = universiteRepository.save(universite);

        Universite foundUniversite = universiteService.findById(savedUniversite.getIdUniversite());

        assertThat(foundUniversite).isNotNull();
        assertThat(foundUniversite.getNomUniversite()).isEqualTo("Université A");
    }

    @Test
    @Order(4)
    public void testDeleteById() {
        Universite universite = new Universite();
        universite.setNomUniversite("Université A");
        Universite savedUniversite = universiteRepository.save(universite);

        universiteService.deleteById(savedUniversite.getIdUniversite());

        assertThrows(IllegalArgumentException.class, () -> {
            universiteService.findById(savedUniversite.getIdUniversite());
        });
    }
}
