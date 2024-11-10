package com.example.projetdevops.UniversiteTests;

import com.example.projetdevops.DAO.Entities.Universite;
import com.example.projetdevops.DAO.Repositories.UniversiteRepository;
import com.example.projetdevops.Services.Universite.UniversiteService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UniversiteServiceMockTest {

    @Mock
    private UniversiteRepository universiteRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private UniversiteService universiteService;

    @Test
    void testAddOrUpdate() {
        Universite universite = new Universite();
        universite.setNomUniversite("Université A");

        when(universiteRepository.save(any(Universite.class))).thenReturn(universite);

        Universite savedUniversite = universiteService.addOrUpdate(universite);

        assertNotNull(savedUniversite);
        assertEquals("Université A", savedUniversite.getNomUniversite());
        verify(universiteRepository, times(1)).save(universite);
    }


    @Test
    void testFindAll() {
        Universite universite1 = new Universite();
        universite1.setNomUniversite("Université A");
        Universite universite2 = new Universite();
        universite2.setNomUniversite("Université B");

        when(universiteRepository.findAll()).thenReturn(List.of(universite1, universite2));

        List<Universite> result = universiteService.findAll();

        assertEquals(2, result.size());
        verify(universiteRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        Universite universite = new Universite();
        universite.setNomUniversite("Université A");
        universite.setIdUniversite(1L);

        when(universiteRepository.findById(1L)).thenReturn(Optional.of(universite));

        Universite foundUniversite = universiteService.findById(1L);

        assertNotNull(foundUniversite);
        assertEquals("Université A", foundUniversite.getNomUniversite());
        verify(universiteRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(universiteRepository.findById(999L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            universiteService.findById(999L);
        });

        assertEquals("Universite with id 999 not found", exception.getMessage());
        verify(universiteRepository, times(1)).findById(999L);
    }


    @Test
    void testDeleteById() {
        universiteService.deleteById(1L);

        verify(universiteRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDelete() {
        Universite universite = new Universite();
        universite.setIdUniversite(1L);

        universiteService.delete(universite);

        verify(universiteRepository, times(1)).delete(universite);
    }
}