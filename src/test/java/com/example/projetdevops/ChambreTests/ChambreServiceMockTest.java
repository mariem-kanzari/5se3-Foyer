package com.example.projetdevops.ChambreTests;

import com.example.projetdevops.DAO.Entities.Chambre;
import com.example.projetdevops.DAO.Entities.TypeChambre;
import com.example.projetdevops.DAO.Repositories.ChambreRepository;
import com.example.projetdevops.Services.Chambre.ChambreService;
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
public class ChambreServiceMockTest {

    @Mock
    private ChambreRepository chambreRepository;

    @InjectMocks
    private ChambreService chambreService;

    private Chambre chambre;

    @BeforeEach
    public void setUp() {
        chambre = new Chambre();
        chambre.setNumeroChambre(1L);
        chambre.setTypeC(TypeChambre.SIMPLE);
        // Add any other properties you need to set
    }

    @Test
    public void testAddOrUpdate() {
        when(chambreRepository.save(any(Chambre.class))).thenReturn(chambre);

        Chambre result = chambreService.addOrUpdate(chambre);

        assertNotNull(result);
        assertEquals(chambre.getNumeroChambre(), result.getNumeroChambre());
        verify(chambreRepository, times(1)).save(chambre);
    }

    @Test
    public void testFindAll() {
        List<Chambre> chambres = Arrays.asList(chambre, new Chambre());
        when(chambreRepository.findAll()).thenReturn(chambres);

        List<Chambre> result = chambreService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(chambreRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        when(chambreRepository.findById(chambre.getNumeroChambre())).thenReturn(Optional.of(chambre));

      Chambre result = chambreService.findById(chambre.getNumeroChambre());


        verify(chambreRepository, times(1)).findById(chambre.getNumeroChambre());
    }

    @Test
    public void testDeleteById() {
        doNothing().when(chambreRepository).deleteById(chambre.getNumeroChambre());

        chambreService.deleteById(chambre.getNumeroChambre());

        verify(chambreRepository, times(1)).deleteById(chambre.getNumeroChambre());
    }

    @Test
    public void testDelete() {
        doNothing().when(chambreRepository).delete(chambre);

        chambreService.delete(chambre);

        verify(chambreRepository, times(1)).delete(chambre);
    }

    @Test
    public void testNbChambreParTypeEtBloc() {
        long idBloc = 1L;

        long result = chambreService.nbChambreParTypeEtBloc(TypeChambre.SIMPLE, idBloc);

        assertEquals(0L, result);
        verify(chambreRepository, times(1)).countByTypeCAndBlocIdBloc(TypeChambre.SIMPLE, idBloc);
    }
}
