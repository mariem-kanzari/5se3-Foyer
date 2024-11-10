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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class FoyerMockitoTest {

    @Mock
    private FoyerRepository foyerRepository;

    @Mock
    private UniversiteRepository universiteRepository;

    @Mock
    private BlocRepository blocRepository;

    @InjectMocks
    private FoyerService foyerService;

    private Foyer testFoyer;
    private Universite testUniversite;

    @BeforeEach
    void setUp() {
        // Initialize test entities before each test
        testFoyer = new Foyer();
        testFoyer.setNomFoyer("Test Foyer");
        testFoyer.setCapaciteFoyer(100);
        testFoyer.setBlocs(new ArrayList<>());
        testUniversite = new Universite();
        testUniversite.setNomUniversite("Test Universite");
    }

    @Test
    public void testAddOrUpdate() {
        Foyer testFoyer1 = new Foyer();
        testFoyer1.setNomFoyer("Updated Test Foyer");
        testFoyer1.setCapaciteFoyer(150);

        // Mock the repository to return the updated foyer
        when(foyerRepository.save(testFoyer1)).thenReturn(testFoyer1);

        // Call the service method
        Foyer savedFoyer = foyerService.addOrUpdate(testFoyer1);

        assertEquals(testFoyer1.getNomFoyer(), savedFoyer.getNomFoyer());
        assertEquals(testFoyer1.getCapaciteFoyer(), savedFoyer.getCapaciteFoyer());

        // Verify interactions with the repository
        verify(foyerRepository, times(1)).save(testFoyer1);
    }

    @Test
    public void testFindAll() {
        // Mock the repository to return a list of foyers
        List<Foyer> mockedFoyers = List.of(testFoyer);
        when(foyerRepository.findAll()).thenReturn(mockedFoyers);

        // Call the service method
        List<Foyer> foyers = foyerService.findAll();

        // Verify that the list is not empty
        assertNotNull(foyers);
        assertFalse(foyers.isEmpty());
        assertEquals(1, foyers.size());

        // Verify the repository interaction
        verify(foyerRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        // Mock the repository to return a foyer when finding by ID
        when(foyerRepository.findById(testFoyer.getIdFoyer())).thenReturn(Optional.of(testFoyer));

        // Call the service method
        Foyer foundFoyer = foyerService.findById(testFoyer.getIdFoyer());

        assertNotNull(foundFoyer);
        assertEquals(testFoyer.getIdFoyer(), foundFoyer.getIdFoyer());

        // Verify repository interaction
        verify(foyerRepository, times(1)).findById(testFoyer.getIdFoyer());
    }

    @Test
    public void testDeleteById() {
        Long foyerId = testFoyer.getIdFoyer();

        // Mock the repository to do nothing on delete
        doNothing().when(foyerRepository).deleteById(foyerId);

        // Call the service method
        foyerService.deleteById(foyerId);

        // Verify that the repository's deleteById method was called
        verify(foyerRepository, times(1)).deleteById(foyerId);
    }


    @Test
    public void testAjoutFoyerEtBlocs() {
        // Mock the repository to save the foyer
        when(foyerRepository.save(any(Foyer.class))).thenReturn(testFoyer);

        // Perform the action of adding blocs to foyer
        Foyer returnedFoyer = foyerService.ajoutFoyerEtBlocs(testFoyer);

        // Assert that the returned foyer is not null
        assertNotNull(returnedFoyer);

        // Verify interactions
        verify(foyerRepository, times(1)).save(any(Foyer.class));
    }
}

