package com.example.projetdevops.Services.Etudiant;

import com.example.projetdevops.DAO.Entities.Etudiant;
import com.example.projetdevops.DAO.Repositories.EtudiantRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EtudiantService implements IEtudiantService {
    EtudiantRepository repo;

    @Override
    public Etudiant addOrUpdate(Etudiant e) {
        return repo.save(e);
    }

    @Override
    public List<Etudiant> findAll() {
        List<Etudiant> etudiants = (List<Etudiant>) repo.findAll();
        System.out.println("Fetched Etudiants: " + etudiants); // Logging for debugging
        return etudiants;}

    @Override
    public Etudiant findById(Integer id) {
        return repo.findById(id).get();
    }

    @Override
    public void deleteById(Integer id) {
        repo.deleteById(id);
    }

    @Override
    public void delete(Etudiant e) {
        repo.delete(e);
    }


}