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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class BlocServiceMockTest {

    @Mock
    private BlocRepository blocRepository;

    @Mock
    private ChambreRepository chambreRepository;

    @Mock
    private FoyerRepository foyerRepository;

    @InjectMocks
    private BlocService blocService;

    private Bloc bloc;
    private Foyer foyer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        bloc = new Bloc();
        bloc.setIdBloc(1L);
        bloc.setNomBloc("Bloc A");

        Chambre chambre1 = new Chambre();
        chambre1.setNumeroChambre(101);

        Chambre chambre2 = new Chambre();
        chambre2.setNumeroChambre(102);

        bloc.setChambres(Arrays.asList(chambre1, chambre2));

        foyer = new Foyer();
        foyer.setNomFoyer("Foyer A");
    }

    @Test
    public void testAddOrUpdate() {
        when(blocRepository.save(any(Bloc.class))).thenReturn(bloc);
        when(chambreRepository.save(any(Chambre.class))).thenReturn(null);

        Bloc savedBloc = blocService.addOrUpdate(bloc);

        assertNotNull(savedBloc);
        assertEquals("Bloc A", savedBloc.getNomBloc());
        verify(blocRepository, times(1)).save(any(Bloc.class));
        verify(chambreRepository, times(2)).save(any(Chambre.class));
    }

    @Test
    public void testAddOrUpdate2() {
        when(chambreRepository.save(any(Chambre.class))).thenReturn(null);

        Bloc result = blocService.addOrUpdate2(bloc);

        assertNotNull(result);
        assertEquals(bloc.getNomBloc(), result.getNomBloc());
        verify(chambreRepository, times(2)).save(any(Chambre.class));
    }

    @Test
    public void testFindAll() {
        when(blocRepository.findAll()).thenReturn(Arrays.asList(bloc));

        List<Bloc> blocs = blocService.findAll();

        assertEquals(1, blocs.size());
        assertEquals("Bloc A", blocs.get(0).getNomBloc());
        verify(blocRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        when(blocRepository.findById(1L)).thenReturn(Optional.of(bloc));

        Bloc foundBloc = blocService.findById(1L);

        assertNotNull(foundBloc);
        assertEquals("Bloc A", foundBloc.getNomBloc());
        verify(blocRepository, times(1)).findById(1L);
    }

    @Test
    public void testDeleteById() {
        doNothing().when(blocRepository).deleteById(1L);

        blocService.deleteById(1L);

        verify(blocRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDelete() {
        doNothing().when(chambreRepository).delete(any(Chambre.class));
        doNothing().when(blocRepository).delete(any(Bloc.class));

        blocService.delete(bloc);

        verify(chambreRepository, times(2)).delete(any(Chambre.class));
        verify(blocRepository, times(1)).delete(bloc);
    }

    @Test
    public void testAffecterChambresABloc() {
        when(blocRepository.findByNomBloc("Bloc A")).thenReturn(bloc);
        Chambre chambre = new Chambre();
        chambre.setNumeroChambre(101L);
        when(chambreRepository.findByNumeroChambre(anyLong())).thenReturn(chambre);

        Bloc result = blocService.affecterChambresABloc(Arrays.asList(101L), "Bloc A");

        assertNotNull(result);
        assertEquals("Bloc A", result.getNomBloc());
        verify(chambreRepository, times(1)).save(any(Chambre.class));
    }

    @Test
    public void testAffecterBlocAFoyer() {
        when(blocRepository.findByNomBloc("Bloc A")).thenReturn(bloc);
        when(foyerRepository.findByNomFoyer("Foyer A")).thenReturn(foyer);
        when(blocRepository.save(any(Bloc.class))).thenReturn(bloc);

        Bloc result = blocService.affecterBlocAFoyer("Bloc A", "Foyer A");

        assertNotNull(result);
        assertEquals("Foyer A", result.getFoyer().getNomFoyer());
        verify(blocRepository, times(1)).findByNomBloc("Bloc A");
        verify(foyerRepository, times(1)).findByNomFoyer("Foyer A");
        verify(blocRepository, times(1)).save(bloc);
    }
}