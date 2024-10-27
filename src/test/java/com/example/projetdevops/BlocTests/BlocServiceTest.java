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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
 class BlocServiceTest {

    @Autowired
    private BlocRepository blocRepository;

    @Autowired
    private ChambreRepository chambreRepository;

    @Autowired
    private FoyerRepository foyerRepository;

    private BlocService blocService;

    @BeforeEach
    public void setUp() {
        // Instantiate the service using the actual repository beans
        blocService = new BlocService(blocRepository, chambreRepository, blocRepository, foyerRepository);
    }

    @Test
     void testAddOrUpdate() {
        Bloc bloc = new Bloc();
        bloc.setNomBloc("Bloc A");
        bloc.setCapaciteBloc(100); // Assuming this is an int
        bloc.setChambres(new ArrayList<>());

        Bloc savedBloc = blocService.addOrUpdate(bloc);

        // Check if the saved Bloc ID is greater than zero (indicating it's been saved)
        assertTrue(savedBloc.getIdBloc() > 0, "Bloc ID should be greater than 0");

        // Validate the name of the Bloc
        assertEquals("Bloc A", savedBloc.getNomBloc());

        // Validate the capacity of the Bloc
        assertEquals(100, savedBloc.getCapaciteBloc());
    }


    @Test
     void testFindAll() {
        Bloc bloc1 = new Bloc();
        bloc1.setNomBloc("Bloc A");
        bloc1.setCapaciteBloc(100);
        blocRepository.save(bloc1);

        Bloc bloc2 = new Bloc();
        bloc2.setNomBloc("Bloc B");
        bloc2.setCapaciteBloc(150);
        blocRepository.save(bloc2);

        List<Bloc> blocs = blocService.findAll();

        assertEquals(2, blocs.size());
    }

    @Test
     void testFindById() {
        Bloc bloc = new Bloc();
        bloc.setNomBloc("Bloc A");
        bloc.setCapaciteBloc(100);
        Bloc savedBloc = blocRepository.save(bloc);

        Bloc foundBloc = blocService.findById(savedBloc.getIdBloc());

        assertNotNull(foundBloc);
        assertEquals(savedBloc.getIdBloc(), foundBloc.getIdBloc());
    }

    @Test
    void testFindByIdNotFound() {
        long nonExistentId = 999L;

        Exception exception = assertThrows(BlocNotFoundException.class, () -> {
            blocService.findById(nonExistentId);
        });

        assertEquals("Bloc not found with id: 999", exception.getMessage());
    }

    @Test
     void testDeleteById() {
        Bloc bloc = new Bloc();
        bloc.setNomBloc("Bloc A");
        bloc.setCapaciteBloc(100);
        Bloc savedBloc = blocRepository.save(bloc);
        blocService.deleteById(savedBloc.getIdBloc());

        // Store the bloc ID to avoid multiple calls in lambda
        long deletedBlocId = savedBloc.getIdBloc();
        assertThrows(BlocNotFoundException.class, () -> {
            blocService.findById(deletedBlocId);
        });
    }


    @Test
     void testDelete() {
        Bloc bloc = new Bloc();
        bloc.setNomBloc("Bloc A");
        bloc.setCapaciteBloc(100);
        List<Chambre> chambres = new ArrayList<>();
        Chambre chambre = new Chambre();
        chambre.setNumeroChambre(101L);
        chambre.setTypeC(TypeChambre.SIMPLE);
        chambre.setBloc(bloc);
        chambres.add(chambre);
        bloc.setChambres(chambres);
        blocRepository.save(bloc);
        chambreRepository.save(chambre);

        blocService.delete(bloc);

        // Store the bloc ID to avoid multiple calls in lambda
        long deletedBlocId = bloc.getIdBloc();
        assertThrows(BlocNotFoundException.class, () -> {
            blocService.findById(deletedBlocId);
        });
    }


    @Test
     void testAffecterChambresABloc() {
        Bloc bloc = new Bloc();
        bloc.setNomBloc("Bloc A");
        bloc.setCapaciteBloc(100);
        blocRepository.save(bloc);

        Chambre chambre1 = new Chambre();
        chambre1.setNumeroChambre(101L);
        chambre1.setTypeC(TypeChambre.SIMPLE);
        chambre1.setBloc(bloc);
        chambreRepository.save(chambre1);

        Chambre chambre2 = new Chambre();
        chambre2.setNumeroChambre(102L);
        chambre2.setTypeC(TypeChambre.DOUBLE);
        chambre2.setBloc(bloc);
        chambreRepository.save(chambre2);

        List<Long> chambreIds = List.of(101L, 102L);
        Bloc updatedBloc = blocService.affecterChambresABloc(chambreIds, "Bloc A");

        assertNotNull(updatedBloc);
        assertEquals("Bloc A", updatedBloc.getNomBloc());
        assertEquals(2, updatedBloc.getChambres().size());
    }

    @Test
     void testAffecterBlocAFoyer() {
        Bloc bloc = new Bloc();
        bloc.setNomBloc("Bloc A");
        bloc.setCapaciteBloc(100);
        blocRepository.save(bloc);

        Foyer foyer = new Foyer();
        foyer.setNomFoyer("Foyer A");
        foyerRepository.save(foyer);

        Bloc updatedBloc = blocService.affecterBlocAFoyer("Bloc A", "Foyer A");

        assertNotNull(updatedBloc);
        assertEquals(foyer.getNomFoyer(), updatedBloc.getFoyer().getNomFoyer());
    }
}
