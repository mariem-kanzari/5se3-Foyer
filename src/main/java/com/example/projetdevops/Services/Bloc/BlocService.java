package com.example.projetdevops.Services.Bloc;

import com.example.projetdevops.DAO.Entities.Bloc;
import com.example.projetdevops.DAO.Entities.Chambre;
import com.example.projetdevops.DAO.Entities.Foyer;
import com.example.projetdevops.DAO.Repositories.BlocRepository;
import com.example.projetdevops.DAO.Repositories.ChambreRepository;
import com.example.projetdevops.DAO.Repositories.FoyerRepository;
import com.example.projetdevops.Exceptions.BlocNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BlocService implements IBlocService {
    BlocRepository repo;
    ChambreRepository chambreRepository;
    BlocRepository blocRepository;
    FoyerRepository foyerRepository;

    @Override
    public Bloc addOrUpdate2(Bloc b) { //Cascade
        List<Chambre> chambres= b.getChambres();
        for (Chambre c: chambres) {
            c.setBloc(b);
            chambreRepository.save(c);
        }
        return b;
    }

    @Override
    public Bloc addOrUpdate(Bloc b) {
        List<Chambre> chambres= b.getChambres();
        b= repo.save(b);
        for (Chambre chambre: chambres) {
            chambre.setBloc(b);
            chambreRepository.save(chambre);
        }
        return b;
    }

    @Override
    public List<Bloc> findAll() {
        return repo.findAll();
    }

    @Override
    public Bloc findById(long id) {
        Optional<Bloc> optionalBloc = repo.findById(id);
        return optionalBloc.orElseThrow(() -> new BlocNotFoundException(id));
    }

    @Override
    public void deleteById(long id) {
        repo.deleteById(id);
    }

    @Override
    public void delete(Bloc b) {
        List<Chambre> chambres= b.getChambres();
        for (Chambre chambre: chambres) {
            chambreRepository.delete(chambre);
        }
        repo.delete(b);
    }

    @Override
    public Bloc affecterChambresABloc(List<Long> numChambre, String nomBloc) {
        //1
        Bloc b = repo.findByNomBloc(nomBloc);
        List<Chambre> chambres= new ArrayList<>();
        for (Long nu: numChambre) {
            Chambre chambre=chambreRepository.findByNumeroChambre(nu);
            chambres.add(chambre);
        }
        // Keyword (2ème méthode)
        //2 Parent==>Chambre  Child==> Bloc
        for (Chambre cha : chambres) {
            //3 On affecte le child au parent
            cha.setBloc(b);
            //4 save du parent
            chambreRepository.save(cha);
        }
        return b;
    }

    @Override
    public Bloc affecterBlocAFoyer(String nomBloc, String nomFoyer) {
        Bloc b = blocRepository.findByNomBloc(nomBloc); //Parent
        Foyer f = foyerRepository.findByNomFoyer(nomFoyer); //Child
        //On affecte le child au parent
        b.setFoyer(f);
        return blocRepository.save(b);
    }
}