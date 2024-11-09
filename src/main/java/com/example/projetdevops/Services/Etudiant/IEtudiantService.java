package com.example.projetdevops.Services.Etudiant;

import com.example.projetdevops.DAO.Entities.Etudiant;

import java.util.List;

public interface IEtudiantService {
    Etudiant addOrUpdate(Etudiant e);
    List<Etudiant> findAll();
    Etudiant findById(Integer id);
    void deleteById(Integer id);
    void delete(Etudiant e);
}