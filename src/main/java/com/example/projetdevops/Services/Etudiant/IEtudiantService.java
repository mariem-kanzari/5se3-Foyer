package com.example.projetdevops.Services.Etudiant;



import com.example.projetdevops.DAO.Entities.Etudiant;

import java.util.List;

public interface IEtudiantService {

    public Etudiant retrieveEtudiant(Long etudiantId);
    public Etudiant addOrUpdate(Etudiant c);
    public void removeEtudiant(Long etudiantId);


    List<Etudiant> findAll();
}
