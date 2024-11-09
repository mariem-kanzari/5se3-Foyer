package com.example.projetdevops.DAO.Repositories;

import com.example.projetdevops.DAO.Entities.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface EtudiantRepository extends CrudRepository<Etudiant,Integer> {
    //select * from Etudiant where cin=...

    Etudiant findByCin(long cin);
    // Custom query to select all students (JPQL)
    @Query("SELECT e FROM Etudiant e")
    List<Etudiant> findAllEtudiants();
    // select * from Etudiant where nomEt like ...
    List<Etudiant> findByNomEtLike(String nom);
    List<Etudiant> findByNomEtContains(String nom);
    List<Etudiant> findByNomEtContaining(String nom);

    @Query(value = "select e from t_etudiant e " +
            "           join t_reservation_etudiants re on e.id_etudiant=re.etudiants_id_etudiant" +
            "           join t_reservation r on r.id_reservation= re.reservations_id_reservation" +
            "           where r.est_valide=?1 ", nativeQuery = true)
    List<Etudiant> e5erMethodeSQL(boolean estValide);
}