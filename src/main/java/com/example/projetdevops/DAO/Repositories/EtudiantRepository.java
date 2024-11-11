package com.example.projetdevops.DAO.Repositories;

import com.example.projetdevops.DAO.Entities.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EtudiantRepository extends JpaRepository<Etudiant,Long> {

    Etudiant findByCin(long cin);

}
