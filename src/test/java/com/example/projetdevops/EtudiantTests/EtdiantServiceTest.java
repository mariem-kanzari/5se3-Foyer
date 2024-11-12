package com.example.projetdevops.EtudiantTests;
import com.example.projetdevops.DAO.Entities.Etudiant;
import com.example.projetdevops.DAO.Repositories.EtudiantRepository;
import com.example.projetdevops.Services.Etudiant.EtudiantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest // Utilise une base de données en mémoire
@Import(EtudiantService.class) // Importer le service pour qu'il soit testé avec le repo réel
class EtdiantServiceTest {

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private EtudiantService etudiantService;

    @BeforeEach
    void setUp() {
        etudiantRepository.deleteAll(); // Nettoyer la base de données avant chaque test
    }

    @Test
    void testSaveEtudiant() {
        Etudiant etudiant = Etudiant.builder()
                .nomEt("John")
                .prenomEt("Doe")
                .cin(123456)
                .ecole("Ecole1")
                .dateNaissance(LocalDate.of(1990, 1, 1))
                .build();

        Etudiant savedEtudiant = etudiantService.addOrUpdate(etudiant);

        assertNotNull(savedEtudiant.getIdEtudiant());
        assertEquals("John", savedEtudiant.getNomEt());
    }

    @Test
    void testGetAllEtudiants() {
        Etudiant etudiant1 = Etudiant.builder().nomEt("John").prenomEt("Doe").build();
        Etudiant etudiant2 = Etudiant.builder().nomEt("Jane").prenomEt("Doe").build();

        etudiantService.addOrUpdate(etudiant1);
        etudiantService.addOrUpdate(etudiant2);

        List<Etudiant> etudiants = etudiantService.findAll();

        assertEquals(2, etudiants.size());
    }
}
