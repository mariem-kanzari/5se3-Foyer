package com.example.projetdevops.DAO.Repositories;

import com.example.projetdevops.DAO.Entities.Foyer;
import com.example.projetdevops.DAO.Entities.TypeChambre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoyerRepository extends JpaRepository<Foyer, Long> {
    Foyer findByNomFoyer(String nom);

    // select * from Foyer where capaciteFoyer > ....
    List<Foyer> findByCapaciteFoyerGreaterThan(int capacite);

    // select * from Foyer where capaciteFoyer < ....
    List<Foyer> findByCapaciteFoyerLessThan(int capacite);

    // select * from Foyer where capaciteFoyer < .... and capaciteFoyer> ....
    List<Foyer> findByCapaciteFoyerBetween(int min, int max);

    // Afficher le foyer de l'université dont son nom est passé en paramétre.
    // select f from Foyer f JOIN Universite u ON <Condition de jointure> where
    // <condition>
    Foyer findByUniversiteNomUniversite(String nom);

    // Afficher la liste des foyers qui comportent des chambres de meme type
    // que le type passé en paramétre
    // Foyer -- Bloc -- Chambre
    List<Foyer> findByBlocsChambresTypeC(TypeChambre typeChambre);
}
