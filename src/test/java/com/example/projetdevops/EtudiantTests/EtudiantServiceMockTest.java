package com.example.projetdevops.EtudiantTests;


import com.example.projetdevops.DAO.Entities.Etudiant;
import com.example.projetdevops.DAO.Repositories.EtudiantRepository;
import com.example.projetdevops.Services.Etudiant.EtudiantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EtudiantServiceMockTest {

    @Mock
    private EtudiantRepository etudiantRepository;

    @InjectMocks
    private EtudiantService etudiantService;

    private Etudiant etudiant;

    @BeforeEach
    public void setUp() {
        etudiant = new Etudiant();
        etudiant.setIdEtudiant(1L);
        etudiant.setNomEt("Test Etudiant");
    }

    @Test
    public void testAddOrUpdate() {
        when(etudiantRepository.save(any(Etudiant.class))).thenReturn(etudiant);

        Etudiant result = etudiantService.addOrUpdate(etudiant);

        assertNotNull(result);
        assertEquals(etudiant.getIdEtudiant(), result.getIdEtudiant());
        assertEquals(etudiant.getNomEt(), result.getNomEt());
        verify(etudiantRepository, times(1)).save(etudiant);
    }

    @Test
    public void testFindAll() {
        List<Etudiant> etudiants = Arrays.asList(etudiant, new Etudiant());
        when(etudiantRepository.findAll()).thenReturn(etudiants);

        List<Etudiant> result = etudiantService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(etudiantRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        when(etudiantRepository.findById(etudiant.getIdEtudiant())).thenReturn(Optional.of(etudiant));

        Etudiant result = etudiantService.findById(etudiant.getIdEtudiant());

        assertNotNull(result);
        assertEquals(etudiant.getIdEtudiant(), result.getIdEtudiant());
        verify(etudiantRepository, times(1)).findById(etudiant.getIdEtudiant());
    }

    @Test
    public void testDeleteById() {
        doNothing().when(etudiantRepository).deleteById(etudiant.getIdEtudiant());

        etudiantService.deleteById(etudiant.getIdEtudiant());

        verify(etudiantRepository, times(1)).deleteById(etudiant.getIdEtudiant());
    }

    @Test
    public void testDelete() {
        doNothing().when(etudiantRepository).delete(etudiant);

        etudiantService.delete(etudiant);

        verify(etudiantRepository, times(1)).delete(etudiant);
    }
}