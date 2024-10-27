package com.example.projetdevops.BlocTests;

import com.example.projetdevops.DAO.Entities.Bloc;
import com.example.projetdevops.DAO.Entities.Chambre;
import com.example.projetdevops.DAO.Entities.Foyer;
import com.example.projetdevops.DAO.Entities.TypeChambre;
import com.example.projetdevops.DAO.Repositories.BlocRepository;
import com.example.projetdevops.DAO.Repositories.ChambreRepository;
import com.example.projetdevops.DAO.Repositories.FoyerRepository;
import com.example.projetdevops.Exceptions.BlocNotFoundException;
import com.example.projetdevops.Services.Bloc.BlocService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BlocServiceMockTest {

    @Mock
    private BlocRepository blocRepository;

    @Mock
    private ChambreRepository chambreRepository;

    @Mock
    private FoyerRepository foyerRepository;

    @InjectMocks
    private BlocService blocService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddOrUpdate() {
        Bloc bloc = new Bloc();
        bloc.setNomBloc("Bloc A");
        bloc.setCapaciteBloc(100);
        List<Chambre> chambres = new ArrayList<>();
        Chambre chambre = new Chambre();
        chambre.setNumeroChambre(101L);
        chambre.setTypeC(TypeChambre.SIMPLE); // Assuming TypeChambre enum exists
        chambres.add(chambre);
        bloc.setChambres(chambres);

        when(blocRepository.save(any(Bloc.class))).thenReturn(bloc);
        when(chambreRepository.save(any(Chambre.class))).thenReturn(chambre);

        Bloc savedBloc = blocService.addOrUpdate(bloc);

        assertNotNull(savedBloc);
        assertEquals("Bloc A", savedBloc.getNomBloc());
        assertEquals(100, savedBloc.getCapaciteBloc());
        verify(chambreRepository, times(1)).save(chambre);
    }

    @Test
    public void testFindAll() {
        Bloc bloc1 = new Bloc();
        bloc1.setNomBloc("Bloc A");
        bloc1.setCapaciteBloc(100);

        Bloc bloc2 = new Bloc();
        bloc2.setNomBloc("Bloc B");
        bloc2.setCapaciteBloc(150);

        List<Bloc> blocs = List.of(bloc1, bloc2);

        when(blocRepository.findAll()).thenReturn(blocs);

        List<Bloc> result = blocService.findAll();

        assertEquals(2, result.size());
        verify(blocRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        Bloc bloc = new Bloc();
        bloc.setNomBloc("Bloc A");
        bloc.setCapaciteBloc(100);
        bloc.setIdBloc(1L);

        when(blocRepository.findById(1L)).thenReturn(Optional.of(bloc));

        Bloc foundBloc = blocService.findById(1L);

        assertNotNull(foundBloc);
        assertEquals("Bloc A", foundBloc.getNomBloc());
        verify(blocRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindByIdNotFound() {
        when(blocRepository.findById(999L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(BlocNotFoundException.class, () -> {
            blocService.findById(999L);
        });

        assertEquals("Bloc not found with id: 999", exception.getMessage());
        verify(blocRepository, times(1)).findById(999L);
    }

    @Test
    public void testDeleteById() {
        Bloc bloc = new Bloc();
        bloc.setIdBloc(1L);

        when(blocRepository.findById(1L)).thenReturn(Optional.of(bloc));

        blocService.deleteById(1L);

        verify(blocRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDelete() {
        Bloc bloc = new Bloc();
        bloc.setIdBloc(1L);
        List<Chambre> chambres = new ArrayList<>();
        Chambre chambre = new Chambre();
        chambre.setNumeroChambre(101L);
        chambres.add(chambre);
        bloc.setChambres(chambres);

        // No need for a return value, since delete is a void method
        when(blocRepository.findById(1L)).thenReturn(Optional.of(bloc));

        blocService.delete(bloc);

        // Verify that delete was called on both ChambreRepository and BlocRepository
        verify(chambreRepository, times(1)).delete(chambre);
        verify(blocRepository, times(1)).delete(bloc);
    }

    @Test
    public void testAffecterChambresABloc() {
        Bloc bloc = new Bloc();
        bloc.setNomBloc("Bloc A");
        when(blocRepository.findByNomBloc("Bloc A")).thenReturn(bloc);

        Chambre chambre1 = new Chambre();
        chambre1.setNumeroChambre(101L);
        chambre1.setTypeC(TypeChambre.SIMPLE); // Assuming you have a TypeChambre enum
        when(chambreRepository.findByNumeroChambre(101L)).thenReturn(chambre1);

        List<Long> chambreIds = List.of(101L);
        Bloc updatedBloc = blocService.affecterChambresABloc(chambreIds, "Bloc A");

        assertNotNull(updatedBloc);
        assertEquals("Bloc A", updatedBloc.getNomBloc());
        verify(chambreRepository, times(1)).findByNumeroChambre(101L);
        verify(chambreRepository, times(1)).save(chambre1);
    }

    @Test
    public void testAffecterBlocAFoyer() {
        Bloc bloc = new Bloc();
        bloc.setNomBloc("Bloc A");
        when(blocRepository.findByNomBloc("Bloc A")).thenReturn(bloc);

        Foyer foyer = new Foyer();
        foyer.setNomFoyer("Foyer A");
        when(foyerRepository.findByNomFoyer("Foyer A")).thenReturn(foyer);

        when(blocRepository.save(any(Bloc.class))).thenReturn(bloc);

        Bloc updatedBloc = blocService.affecterBlocAFoyer("Bloc A", "Foyer A");

        assertNotNull(updatedBloc);
        assertEquals(foyer.getNomFoyer(), updatedBloc.getFoyer().getNomFoyer());
        verify(blocRepository, times(1)).save(bloc);
    }
}
