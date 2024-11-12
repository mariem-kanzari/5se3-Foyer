package com.example.projetdevops.BlocTests;

import com.example.projetdevops.DAO.Entities.Bloc;
import com.example.projetdevops.DAO.Entities.Chambre;
import com.example.projetdevops.DAO.Entities.Foyer;
import com.example.projetdevops.DAO.Repositories.BlocRepository;
import com.example.projetdevops.DAO.Repositories.ChambreRepository;
import com.example.projetdevops.DAO.Repositories.FoyerRepository;
import com.example.projetdevops.Services.Bloc.BlocService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(BlocService.class)
public class BlocServiceTest {

    @Autowired
    private BlocService blocService;

    @Autowired
    private BlocRepository blocRepository;

    @Autowired
    private ChambreRepository chambreRepository;

    @Autowired
    private FoyerRepository foyerRepository;

    private Bloc bloc;
    private Chambre chambre1, chambre2;
    private Foyer foyer;

//    @BeforeEach
//    public void setup() {
//        bloc = Bloc.builder().nomBloc("Bloc A").capaciteBloc(10).build();
//        bloc = blocRepository.save(bloc);
//
//        chambre1 = Chambre.builder().numeroChambre(101L).bloc(bloc).build();
//        chambre2 = Chambre.builder().numeroChambre(102L).bloc(bloc).build();
//        chambre1 = chambreRepository.save(chambre1);
//        chambre2 = chambreRepository.save(chambre2);
//
//        foyer = Foyer.builder().nomFoyer("Foyer Central").build();
//        foyer = foyerRepository.save(foyer);
//    }
//
//


    @BeforeEach
    public void setUp() {
        // Setup test data
        bloc = new Bloc();
        bloc.setNomBloc("Bloc A");

        Chambre chambre1 = new Chambre();
        chambre1.setNumeroChambre(101);

        Chambre chambre2 = new Chambre();
        chambre2.setNumeroChambre(102);

        bloc.setChambres(Arrays.asList(chambre1, chambre2));
    }

    @Test
    public void testAddOrUpdate() {
        // Execute the method under test
        Bloc savedBloc = blocService.addOrUpdate(bloc);

        // Assert the Bloc is saved and returned correctly
        assertEquals("Bloc A", savedBloc.getNomBloc());

        // Assert that the Bloc's chambres are saved and linked correctly
        assertEquals(2, savedBloc.getChambres().size());
        assertEquals(101, savedBloc.getChambres().get(0).getNumeroChambre());
        assertEquals(102, savedBloc.getChambres().get(1).getNumeroChambre());

        // Assert that the Bloc and its chambres are actually persisted in the database
        Bloc fetchedBloc = blocRepository.findById(savedBloc.getIdBloc()).orElseThrow();
        assertEquals("Bloc A", fetchedBloc.getNomBloc());
        assertEquals(2, fetchedBloc.getChambres().size());
    }



    @Test
    public void testFindAll() {
        // Save a bloc to have data in the repository
        blocService.addOrUpdate(bloc);

        // Add a second bloc for testing
        Bloc blocB = new Bloc();
        blocB.setNomBloc("Bloc B");
        Chambre chambre3 = new Chambre();
        chambre3.setNumeroChambre(201);
        blocB.setChambres(Arrays.asList(chambre3));
        blocService.addOrUpdate(blocB);

        // Call the findAll method
        List<Bloc> blocs = blocService.findAll();

        // Assert the expected number of blocs and their contents
        assertEquals(2, blocs.size(), "Should find 2 blocs");

        // Check each bloc’s name and chambre details
        Bloc retrievedBlocA = blocs.stream().filter(b -> "Bloc A".equals(b.getNomBloc())).findFirst().orElseThrow();
        Bloc retrievedBlocB = blocs.stream().filter(b -> "Bloc B".equals(b.getNomBloc())).findFirst().orElseThrow();

        // Assertions for Bloc A
        assertEquals("Bloc A", retrievedBlocA.getNomBloc());
        assertEquals(2, retrievedBlocA.getChambres().size());
        assertEquals(101, retrievedBlocA.getChambres().get(0).getNumeroChambre());
        assertEquals(102, retrievedBlocA.getChambres().get(1).getNumeroChambre());

        // Assertions for Bloc B
        assertEquals("Bloc B", retrievedBlocB.getNomBloc());
        assertEquals(1, retrievedBlocB.getChambres().size());
        assertEquals(201, retrievedBlocB.getChambres().get(0).getNumeroChambre());
    }
    @Test
    public void testFindById() {
        // Setup: Save the Bloc before calling findById
        Bloc savedBloc = blocService.addOrUpdate(bloc);

        // Ensure the Bloc has a valid ID (non-zero) after saving
        assertTrue(savedBloc.getIdBloc() > 0, "The Bloc should have a valid, non-zero ID after saving");

        // Execute the findById method using the saved Bloc's ID
        Bloc foundBloc = blocService.findById(savedBloc.getIdBloc());

        // Assert that the returned Bloc is not null
        assertNotNull(foundBloc, "The bloc should not be null");

        // Assert the Bloc's name matches the expected value
        assertEquals("Bloc A", foundBloc.getNomBloc(), "The bloc name should be 'Bloc A'");

        // Assert the Bloc's chambres are correctly associated
        assertEquals(2, foundBloc.getChambres().size(), "The bloc should have 2 chambres");
        assertEquals(101, foundBloc.getChambres().get(0).getNumeroChambre(), "The first chambre number should be 101");
        assertEquals(102, foundBloc.getChambres().get(1).getNumeroChambre(), "The second chambre number should be 102");
    }



    @Test
    public void testDeleteById() {
        blocService.deleteById(bloc.getIdBloc());
        assertFalse(blocRepository.findById(bloc.getIdBloc()).isPresent());
    }

    @Test
    public void testDelete() {
        blocService.delete(bloc);
        assertFalse(blocRepository.findById(bloc.getIdBloc()).isPresent());
        assertTrue(chambreRepository.findAll().isEmpty());
    }


}
