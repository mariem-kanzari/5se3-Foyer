    package com.example.projetdevops.Services.Bloc;

    import com.example.projetdevops.DAO.Entities.Bloc;
    import com.example.projetdevops.DAO.Entities.Chambre;
    import com.example.projetdevops.DAO.Entities.Foyer;
    import com.example.projetdevops.DAO.Repositories.BlocRepository;
    import com.example.projetdevops.DAO.Repositories.ChambreRepository;
    import com.example.projetdevops.DAO.Repositories.FoyerRepository;
    import com.example.projetdevops.Exceptions.BlocNotFoundException;
    import jakarta.persistence.EntityManager;
    import jakarta.persistence.PersistenceContext;
    import jakarta.transaction.Transactional;
    import lombok.AllArgsConstructor;
    import org.springframework.stereotype.Service;

    import java.util.ArrayList;
    import java.util.List;
    import java.util.Optional;

    @Service
    @AllArgsConstructor
    public class BlocService implements IBlocService {
        private final BlocRepository repo;
        private final ChambreRepository chambreRepository;
        private final FoyerRepository foyerRepository;
        private final EntityManager entityManager; // Add EntityManager here
        @Transactional

        @Override
        public Bloc addOrUpdate(Bloc b) {
            // Use merge to persist changes
            b = entityManager.merge(b); // Use merge to persist changes

            List<Chambre> chambres = b.getChambres();
            for (Chambre chambre : chambres) {
                chambre.setBloc(b); // Associate the Chambre with the managed Bloc
                chambreRepository.save(chambre);
            }
            return b; // Return the managed entity
        }

        @Override
        public Bloc addOrUpdate2(Bloc b) { // Cascade
            List<Chambre> chambres = b.getChambres();
            for (Chambre c : chambres) {
                c.setBloc(b);
                chambreRepository.save(c);
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
            List<Chambre> chambres = b.getChambres();
            for (Chambre chambre : chambres) {
                chambreRepository.delete(chambre);
            }
            repo.delete(b);
        }

        @Override
        public Bloc affecterChambresABloc(List<Long> numChambre, String nomBloc) {
            // 1
            Bloc b = repo.findByNomBloc(nomBloc);
            List<Chambre> chambres = new ArrayList<>();
            for (Long nu : numChambre) {
                Chambre chambre = chambreRepository.findByNumeroChambre(nu);
                chambres.add(chambre);
            }
            // 2 Parent==>Chambre  Child==> Bloc
            for (Chambre cha : chambres) {
                // 3 On affecte le child au parent
                cha.setBloc(b);
                // 4 save du parent
                chambreRepository.save(cha);
            }
            return b;
        }
        @Transactional
        @Override
        public Bloc affecterBlocAFoyer(String nomBloc, String nomFoyer) {
            Bloc b = repo.findByNomBloc(nomBloc); // Find the Bloc
            if (b == null) {
                throw new IllegalArgumentException("Bloc not found");
            }

            Foyer f = foyerRepository.findByNomFoyer(nomFoyer); // Find the Foyer
            if (f == null) {
                throw new IllegalArgumentException("Foyer not found");
            }

            b.setFoyer(f); // Associate Foyer with Bloc
            return entityManager.merge(b); // Use merge to save the updated Bloc
        }

    }
