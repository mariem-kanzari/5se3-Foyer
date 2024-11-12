package com.example.projetdevops.Services.Etudiant;

import com.example.projetdevops.DAO.Entities.Etudiant;
import com.example.projetdevops.DAO.Repositories.EtudiantRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class EtudiantService implements IEtudiantService {


    EtudiantRepository etudiantRepository;

    @Override
    public List<Etudiant> findAll() {
        return etudiantRepository.findAll();
    }
    @Override

    public Etudiant retrieveEtudiant(Long etudiantId) {
        return etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new NoSuchElementException("Etudiant with ID " + etudiantId + " not found"));
    }

    @Override

    public Etudiant addOrUpdate(Etudiant c) {
        return etudiantRepository.save(c);
    }
    public Etudiant modifyEtudiant(Etudiant c) {
        return etudiantRepository.save(c);
    }
    public void removeEtudiant(Long etudiantId) {
        etudiantRepository.deleteById(etudiantId);
    }



}