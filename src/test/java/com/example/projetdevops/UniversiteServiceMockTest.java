package com.example.projetdevops;



import com.example.projetdevops.DAO.Entities.Universite;
import com.example.projetdevops.DAO.Repositories.UniversiteRepository;
import com.example.projetdevops.Services.Universite.UniversiteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UniversiteServiceMockTest {

    private UniversiteService universiteService;
    private UniversiteRepository universiteRepositoryMock;

    @BeforeEach
    public void setUp() {
        universiteRepositoryMock = Mockito.mock(UniversiteRepository.class);
        universiteService = new UniversiteService(universiteRepositoryMock);
    }

    @Test
    public void testAddOrUpdate() {
        Universite universite = new Universite();
        universite.setNomUniversite("Université Mock");

        when(universiteRepositoryMock.save(any(Universite.class))).thenReturn(universite);

        Universite result = universiteService.addOrUpdate(universite);

        assertEquals("Université Mock", result.getNomUniversite());
        verify(universiteRepositoryMock, times(1)).save(universite);
    }

    @Test
    public void testFindById() {
        Universite universite = new Universite();
        universite.setNomUniversite("Université Mock");
        universite.setIdUniversite(1L);

        when(universiteRepositoryMock.findById(1L)).thenReturn(Optional.of(universite));

        Universite result = universiteService.findById(1L);

        assertNotNull(result);
        assertEquals("Université Mock", result.getNomUniversite());
        assertEquals(1L, result.getIdUniversite());
        verify(universiteRepositoryMock, times(1)).findById(1L);
    }

    @Test
    public void testDeleteById() {
        Universite universite = new Universite();
        universite.setNomUniversite("Université Mock");
        universite.setIdUniversite(1L);

        doNothing().when(universiteRepositoryMock).deleteById(1L);

        universiteService.deleteById(1L);

        verify(universiteRepositoryMock, times(1)).deleteById(1L);
    }

    @Test
    public void testFindByIdNotFound() {
        when(universiteRepositoryMock.findById(2L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            universiteService.findById(2L);
        });
    }
}
