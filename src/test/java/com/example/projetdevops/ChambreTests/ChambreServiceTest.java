package com.example.projetdevops.ChambreTests;

import com.example.projetdevops.DAO.Entities.Chambre;
import com.example.projetdevops.DAO.Entities.TypeChambre;
import com.example.projetdevops.DAO.Repositories.ChambreRepository;
import com.example.projetdevops.DAO.Repositories.BlocRepository;
import com.example.projetdevops.Services.Chambre.ChambreService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@Transactional // This ensures tests are wrapped in a transaction
public  class ChambreServiceTest {

    @Autowired
    private ChambreRepository chambreRepository;

    @Autowired
    private BlocRepository blocRepository;

    @Autowired // Inject EntityManager here
    private EntityManager entityManager;

    private ChambreService chambreService;

    @BeforeEach
    public void setUp() {
        // Pass dependencies to the ChambreService constructor
        chambreService = new ChambreService(chambreRepository, blocRepository);
    }

    @AfterEach
    public void tearDown() {
        chambreRepository.deleteAll(); // Clear chambres
        blocRepository.deleteAll(); // Clear blocs
    }

    @Test
    @Order(1)
    public void testAddOrUpdate() {
        Chambre chambre = new Chambre();
        chambre.setNumeroChambre(101);
        chambre.setTypeC(TypeChambre.SIMPLE); // Exemple d'énumération

        Chambre savedChambre = chambreService.addOrUpdate(chambre);

        Chambre fetchedChambre = chambreRepository.findById(savedChambre.getIdChambre()).orElse(null);
        assertThat(fetchedChambre).isNotNull(); // Ensure fetchedChambre is not null
        assertEquals(chambre.getNumeroChambre(), fetchedChambre.getNumeroChambre());
    }

    @Test
    @Order(2)
    public void testFindAll() {
        Chambre chambre1 = new Chambre();
        chambre1.setNumeroChambre(102);
        chambreRepository.save(chambre1);

        Chambre chambre2 = new Chambre();
        chambre2.setNumeroChambre(103);
        chambreRepository.save(chambre2);

        List<Chambre> chambres = chambreService.findAll();

        assertThat(chambres).hasSize(2);
    }

    @Test
    @Order(3)
    public void testFindById() {
        Chambre chambre = new Chambre();
        chambre.setNumeroChambre(104);
        Chambre savedChambre = chambreRepository.save(chambre);

        Chambre foundChambre = chambreService.findById(savedChambre.getIdChambre());

        assertThat(foundChambre).isNotNull();
        assertEquals(savedChambre.getIdChambre(), foundChambre.getIdChambre());
        assertEquals(104, foundChambre.getNumeroChambre());
    }










}
