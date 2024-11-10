package com.example.projetdevops.EtudiantTests;

import com.example.projetdevops.DAO.Entities.Etudiant;
import com.example.projetdevops.DAO.Repositories.EtudiantRepository;
import com.example.projetdevops.Services.Etudiant.EtudiantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

public class EtdiantServiceTest {

    @Autowired
    private EtudiantRepository etudiantRepository;

    private EtudiantService etudiantService;

    @BeforeEach
    public void setUp() {
        etudiantService = new EtudiantService(etudiantRepository);
    }

    @Test
    public void testAddOrUpdate() {
        Etudiant etudiant = new Etudiant();
        etudiant.setCin(1478);

        Etudiant result = etudiantService.addOrUpdate(etudiant);

        assertNotNull(result.getIdEtudiant());
        assertEquals(1478, result.getCin());
    }

    @Test
    public void testFindAll() {
        Etudiant etudiant1 = new Etudiant();
        etudiant1.setNomEt("John Doe");

        Etudiant etudiant2 = new Etudiant();
        etudiant2.setNomEt("Jane Doe");

        etudiantService.addOrUpdate(etudiant1);
        etudiantService.addOrUpdate(etudiant2);

        List<Etudiant> etudiants = etudiantService.findAll();

        assertEquals(2, etudiants.size());
    }

    @Test
    public void testFindById() {
        Etudiant etudiant = new Etudiant();
        etudiant.setNomEt("John Doe");

        Etudiant savedEtudiant = etudiantService.addOrUpdate(etudiant);
        Etudiant foundEtudiant = etudiantService.findById(savedEtudiant.getIdEtudiant());

        assertEquals(savedEtudiant.getIdEtudiant(), foundEtudiant.getIdEtudiant());
    }

    @Test
    public void testDeleteById() {
        Etudiant etudiant = new Etudiant();
        etudiant.setNomEt("John Doe");

        Etudiant savedEtudiant = etudiantService.addOrUpdate(etudiant);
        etudiantService.deleteById(savedEtudiant.getIdEtudiant());

        assertFalse(etudiantRepository.findById(savedEtudiant.getIdEtudiant()).isPresent());
    }}
