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
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@Transactional // This ensures tests are wrapped in a transaction
public class BlocServiceTest {

    @Autowired
    private BlocRepository blocRepository;

    @Autowired
    private ChambreRepository chambreRepository;

    @Autowired
    private FoyerRepository foyerRepository;

    @Autowired // Inject EntityManager here
    private EntityManager entityManager;

    private BlocService blocService;

    @BeforeEach
    public void setUp() {
        // Pass all four dependencies to the BlocService constructor
        blocService = new BlocService(blocRepository, chambreRepository, foyerRepository, entityManager);
    }


    @AfterEach
    public void tearDown() {
        chambreRepository.deleteAll(); // Clear chambres first
        blocRepository.deleteAll(); // Clear blocs
        foyerRepository.deleteAll(); // Clear foyers
    }

    @Test
    @Order(1)
    public void testAddOrUpdate() {
        Bloc bloc = new Bloc();
        bloc.setNomBloc("Test Bloc");

        Bloc savedBloc = blocService.addOrUpdate(bloc);

        Bloc fetchedBloc = blocRepository.findById(savedBloc.getId()).orElse(null);
        assertThat(fetchedBloc).isNotNull(); // Ensure fetchedBloc is not null
        assertEquals(bloc.getNomBloc(), fetchedBloc.getNomBloc());
    }

    @Test
    @Order(2)
    public void testFindAll() {
        Bloc bloc1 = new Bloc();
        bloc1.setNomBloc("Bloc A");
        blocRepository.save(bloc1);

        Bloc bloc2 = new Bloc();
        bloc2.setNomBloc("Bloc B");
        blocRepository.save(bloc2);

        List<Bloc> blocs = blocService.findAll();

        assertThat(blocs).hasSize(2);
        assertThat(blocs).extracting(Bloc::getNomBloc).containsExactlyInAnyOrder("Bloc A", "Bloc B");
    }

    @Test
    @Order(3)
    public void testFindById() {
        Bloc bloc = new Bloc();
        bloc.setNomBloc("Bloc A");
        Bloc savedBloc = blocRepository.save(bloc);

        Bloc foundBloc = blocService.findById(savedBloc.getId());

        assertThat(foundBloc).isNotNull();
        assertEquals(savedBloc.getId(), foundBloc.getId());
        assertEquals("Bloc A", foundBloc.getNomBloc());
    }

    @Test
    @Order(4)
    public void testFindById_NotFound() {
        assertThrows(BlocNotFoundException.class, () -> blocService.findById(999L)); // Non-existing ID
    }

    @Test
    @Order(5)
    public void testDeleteById() {
        Bloc bloc = new Bloc();
        bloc.setNomBloc("Bloc A");
        Bloc savedBloc = blocRepository.save(bloc);

        blocService.deleteById(savedBloc.getId());

        assertThrows(BlocNotFoundException.class, () -> blocService.findById(savedBloc.getId())); // Check if it is deleted
    }

    @Test
    @Order(6)
    public void testDelete() {
        Bloc bloc = new Bloc();
        bloc.setNomBloc("Bloc A");
        List<Chambre> chambres = new ArrayList<>();

        Chambre chambre = Chambre.builder()
                .numeroChambre(103)
                .typeC(TypeChambre.TRIPLE) // Ensure a valid enum value
                .build();
        chambres.add(chambre);
        bloc.setChambres(chambres);
        Bloc savedBloc = blocRepository.save(bloc);

        blocService.delete(savedBloc);

        assertThrows(BlocNotFoundException.class, () -> blocService.findById(savedBloc.getId())); // Check if it is deleted
    }

    @Test
    @Order(7)
    public void testAffecterChambresABloc() {
        List<Long> numChambre = Arrays.asList(1L, 2L);
        String nomBloc = "Bloc Test";

        Bloc bloc = new Bloc();
        bloc.setNomBloc(nomBloc);
        bloc = blocRepository.save(bloc);

        Chambre chambre1 = new Chambre();
        chambre1.setNumeroChambre(1);
        chambre1.setBloc(bloc);
        chambreRepository.save(chambre1);

        Chambre chambre2 = new Chambre();
        chambre2.setNumeroChambre(2);
        chambre2.setBloc(bloc);
        chambreRepository.save(chambre2);

        Bloc updatedBloc = blocService.affecterChambresABloc(numChambre, nomBloc);

        Bloc fetchedBloc = blocService.findById(updatedBloc.getId());
        assertEquals(updatedBloc.getNomBloc(), fetchedBloc.getNomBloc());
    }

    @Test
    @Order(8)
    public void testAffecterBlocAFoyer() {
        Bloc bloc = new Bloc();
        bloc.setNomBloc("Bloc A");
        Bloc savedBloc = blocRepository.save(bloc);

        Foyer foyer = new Foyer();
        foyer.setNomFoyer("Foyer 1");
        foyerRepository.save(foyer);

        blocService.affecterBlocAFoyer("Bloc A", "Foyer 1");

        Bloc updatedBloc = blocService.findById(savedBloc.getId());
        assertThat(updatedBloc.getFoyer()).isEqualTo(foyer);
    }
}
