package com.example.projetdevops;

import com.example.projetdevops.DAO.Entities.Bloc;
import com.example.projetdevops.DAO.Entities.Foyer;
import com.example.projetdevops.DAO.Entities.Universite;
import com.example.projetdevops.DAO.Repositories.BlocRepository;
import com.example.projetdevops.DAO.Repositories.FoyerRepository;
import com.example.projetdevops.DAO.Repositories.UniversiteRepository;
import com.example.projetdevops.Services.Foyer.FoyerService;
import com.example.projetdevops.Services.Foyer.IFoyerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FoyerUnitTest {

    @Autowired
    private IFoyerService foyerService;

    @Autowired
    private FoyerRepository foyerRepository;

    @Autowired
    private UniversiteRepository universiteRepository;

    @Autowired
    private BlocRepository blocRepository;

    private Foyer testFoyer;
    private Universite testUniversite;

    @BeforeEach
    void setUp() {
        // Initialize test entities before each test
        testFoyer = new Foyer();
        testFoyer.setNomFoyer("Test Foyer");
        testFoyer.setCapaciteFoyer(100);

        // Save the testFoyer to the repository
        testFoyer = foyerRepository.save(testFoyer);

        // If you also want to test the Universite, initialize and save it
        testUniversite = new Universite();
        testUniversite.setNomUniversite("Test gh ");
        testUniversite = universiteRepository.save(testUniversite);
    }

    @Test
    public void testAddOrUpdate() {
        Foyer testFoyer1 = new Foyer();
        testFoyer1.setNomFoyer("Updated Test Foyer");
        testFoyer1.setCapaciteFoyer(150);

        // Save the foyer using the service
        Foyer savedFoyer = foyerService.addOrUpdate(testFoyer1);

        assertEquals(testFoyer1.getNomFoyer(), savedFoyer.getNomFoyer());
        assertEquals(testFoyer1.getCapaciteFoyer(), savedFoyer.getCapaciteFoyer());
    }

    @Test
    public void testFindAll() {
        List<Foyer> foyers = foyerService.findAll();
        System.out.println("Foyers size: " + foyers.size());
        // Ensure that the list is not empty

    }

    @Test
    public void testFindById() {
        // Ensure testFoyer was saved and has an ID
        assertNotNull(testFoyer.getIdFoyer());

        Foyer foundFoyer = foyerService.findById(testFoyer.getIdFoyer());
        assertNotNull(foundFoyer);
        assertEquals(testFoyer.getIdFoyer(), foundFoyer.getIdFoyer());
    }

    @Test
    public void testDeleteById() {
        Long foyerId = testFoyer.getIdFoyer();

        // Delete the foyer by ID
        foyerService.deleteById(foyerId);

        // Ensure the foyer is deleted
        Optional<Foyer> deletedFoyer = foyerRepository.findById(foyerId);
        assertTrue(deletedFoyer.isEmpty());
    }

    /*@Test
    public void testAffecterFoyerAUniversite() {
        // Affecter foyer to Universite
        Universite updatedUniversite = foyerService.affecterFoyerAUniversite(testFoyer.getIdFoyer(), testUniversite.getNomUniversite());

        // Assert that the Universite has the Foyer assigned
        assertNotNull(updatedUniversite.getFoyer());
        assertEquals(testFoyer.getIdFoyer(), updatedUniversite.getFoyer().getIdFoyer());
    }

    @Test
    public void testDesaffecterFoyerAUniversite() {
        // First, assign the foyer to the universite
        Universite updatedUniversite = foyerService.affecterFoyerAUniversite(testFoyer.getIdFoyer(), testUniversite.getNomUniversite());

        // Now, desaffecter it
        Universite desaffecteUniversite = foyerService.desaffecterFoyerAUniversite(testUniversite.getIdUniversite());

        // Assert that the Foyer is now null in the Universite
        assertNull(desaffecteUniversite.getFoyer());
    }*/

    @Test
    public void testAjouterFoyerEtAffecterAUniversite() {
        Foyer newFoyer = new Foyer();
        newFoyer.setNomFoyer("New Foyer");
        newFoyer.setCapaciteFoyer(200);
        newFoyer = foyerRepository.save(newFoyer);

        // Affecter new foyer to the Universite
        Foyer affectedFoyer = foyerService.ajouterFoyerEtAffecterAUniversite(newFoyer, testUniversite.getIdUniversite());

        // Ensure that the foyer is assigned to the Universite
        assertNotNull(affectedFoyer);
        assertEquals(newFoyer.getIdFoyer(), affectedFoyer.getIdFoyer());
    }

    @Test
    public void testAjoutFoyerEtBlocs() {
        // Create a new Foyer with associated Blocs
        Foyer newFoyerWithBlocs = new Foyer();
        newFoyerWithBlocs.setNomFoyer("Foyer with Blocs");
        newFoyerWithBlocs.setCapaciteFoyer(300);

        // Save and associate Blocs (assuming Blocs are created or fetched)
        List<Bloc> blocs = newFoyerWithBlocs.getBlocs(); // assume Blocs are set before
        newFoyerWithBlocs = foyerRepository.save(newFoyerWithBlocs);

        // Perform the action of adding blocs to foyer
        Foyer returnedFoyer = foyerService.ajoutFoyerEtBlocs(newFoyerWithBlocs);

        // Assert that the returned foyer is the one saved
        assertNotNull(returnedFoyer);
        assertEquals(newFoyerWithBlocs.getNomFoyer(), returnedFoyer.getNomFoyer());
    }
}
