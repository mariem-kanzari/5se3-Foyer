package com.example.projetdevops.EtudiantTests;

import com.example.projetdevops.DAO.Entities.Etudiant;
import com.example.projetdevops.DAO.Repositories.EtudiantRepository;
import com.example.projetdevops.Services.Etudiant.EtudiantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EtudiantServiceMockitoTest {

    @Mock
    private EtudiantRepository etudiantRepository;

    @InjectMocks
    private EtudiantService etudiantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialiser les mocks
    }

    @Test
    void testSaveEtudiant() {
        Etudiant etudiant = new Etudiant(1L, "John", "Doe", 123456, "Ecole1", LocalDate.of(1990, 1, 1), null);
        when(etudiantRepository.save(etudiant)).thenReturn(etudiant);

        Etudiant savedEtudiant = etudiantService.addOrUpdate(etudiant);

        assertNotNull(savedEtudiant);
        assertEquals("John", savedEtudiant.getNomEt());
        verify(etudiantRepository, times(1)).save(etudiant); // Vérifie que le repo a été appelé
    }

    @Test
    void testGetAllEtudiants() {
        Etudiant etudiant1 = new Etudiant(1L, "John", "Doe", 123456, "Ecole1", LocalDate.of(1990, 1, 1), null);
        Etudiant etudiant2 = new Etudiant(2L, "Jane", "Doe", 654321, "Ecole2", LocalDate.of(1992, 2, 2), null);

        when(etudiantRepository.findAll()).thenReturn(Arrays.asList(etudiant1, etudiant2));

        List<Etudiant> etudiants = etudiantService.findAll();

        assertEquals(2, etudiants.size());
        verify(etudiantRepository, times(1)).findAll();
    }
}
