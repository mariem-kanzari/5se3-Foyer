package com.example.projetdevops.Services.Chambre;

import com.example.projetdevops.DAO.Entities.Chambre;
import com.example.projetdevops.DAO.Entities.TypeChambre;

import java.util.List;

public interface IChambreService {
    Chambre addOrUpdate(Chambre c);

    List<Chambre> findAll();

    Chambre findById(long id);

    void deleteById(long id);

    void delete(Chambre c);

    List<Chambre> getChambresParNomBloc(String nomBloc);

    long nbChambreParTypeEtBloc(TypeChambre type, long idBloc);

    List<Chambre> chambresNonReserve(String nomFoyer, TypeChambre type);

    void listeChambresParBloc();

    void pourcentageChambreParTypeChambre();

    void PlacesDispo();

}
