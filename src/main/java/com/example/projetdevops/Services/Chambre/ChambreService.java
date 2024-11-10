package com.example.projetdevops.Services.Chambre;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.example.projetdevops.DAO.Entities.Bloc;
import com.example.projetdevops.DAO.Entities.Chambre;
import com.example.projetdevops.DAO.Entities.Reservation;
import com.example.projetdevops.DAO.Entities.TypeChambre;
import com.example.projetdevops.DAO.Repositories.BlocRepository;
import com.example.projetdevops.DAO.Repositories.ChambreRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ChambreService implements IChambreService {
    ChambreRepository repo;
    BlocRepository blocRepository;

    @Override
    public Chambre addOrUpdate(Chambre c) {
        return repo.save(c);
    }

    @Override
    public List<Chambre> findAll() {
        return repo.findAll();
    }

    @Override
    public Chambre findById(long id) {
        return repo.findById(id).get();
    }

    @Override
    public void deleteById(long id) {
        repo.deleteById(id);
    }

    @Override
    public void delete(Chambre c) {
        repo.delete(c);
    }

    @Override
    public List<Chambre> getChambresParNomBloc(String nomBloc) {
        return repo.findByBlocNomBloc(nomBloc);
    }

    @Override
    public long nbChambreParTypeEtBloc(TypeChambre type, long idBloc) {
        return repo.countByTypeCAndBlocIdBloc(type, idBloc);
    }

    @Override
    public List<Chambre> chambresNonReserve(String nomFoyer, TypeChambre type) {
        LocalDate dateDebutAU = getDateDebutAU();
        LocalDate dateFinAU = getDateFinAU();

        List<Chambre> chambresDisponibles = new ArrayList<>();

        // Parcourir les chambres disponibles dans le foyer et du bon type
        for (Chambre chambre : repo.findAll()) {
            if (isChambreValide(chambre, nomFoyer, type) && isChambreDisponible(chambre, dateDebutAU, dateFinAU)) {
                chambresDisponibles.add(chambre);
            }
        }

        return chambresDisponibles;
    }

    private LocalDate getDateDebutAU() {
        int year = LocalDate.now().getYear() % 100;
        return LocalDate.of(
                Integer.parseInt("20" + (LocalDate.now().getMonthValue() <= 7 ? year - 1 : year)),
                9, 15);
    }

    private LocalDate getDateFinAU() {
        int year = LocalDate.now().getYear() % 100;
        return LocalDate.of(
                Integer.parseInt("20" + (LocalDate.now().getMonthValue() <= 7 ? year : year + 1)),
                6, 30);
    }

    private boolean isChambreValide(Chambre chambre, String nomFoyer, TypeChambre type) {
        return chambre.getTypeC().equals(type) && chambre.getBloc().getFoyer().getNomFoyer().equals(nomFoyer);
    }

    private boolean isChambreDisponible(Chambre chambre, LocalDate dateDebutAU, LocalDate dateFinAU) {
        int numReservation = countReservationsForAU(chambre, dateDebutAU, dateFinAU);

        switch (chambre.getTypeC()) {
            case SIMPLE:
                return numReservation == 0;
            case DOUBLE:
                return numReservation < 2;
            case TRIPLE:
                return numReservation < 3;
            default:
                return false;
        }
    }

    private int countReservationsForAU(Chambre chambre, LocalDate dateDebutAU, LocalDate dateFinAU) {
        int numReservation = 0;
        for (Reservation reservation : chambre.getReservations()) {
            if (reservation.getAnneeUniversitaire().isBefore(dateFinAU)
                    && reservation.getAnneeUniversitaire().isAfter(dateDebutAU)) {
                numReservation++;
            }
        }
        return numReservation;
    }

    @Override
    public void listeChambresParBloc() {
        for (Bloc b : blocRepository.findAll()) {
            log.info("Bloc => " + b.getNomBloc() + " ayant une capacité " + b.getCapaciteBloc());
            if (!b.getChambres().isEmpty()) {
                log.info("La liste des chambres pour ce bloc: ");
                for (Chambre c : b.getChambres()) {
                    log.info("NumChambre: " + c.getNumeroChambre() + " type: " + c.getTypeC());
                }
            } else {
                log.info("Pas de chambre disponible dans ce bloc");
            }
            log.info("********************");
        }
    }

    @Override
    public void pourcentageChambreParTypeChambre() {
        long totalChambre = repo.count();
        double pSimple = (repo.countChambreByTypeC(TypeChambre.SIMPLE) * 100.0) / totalChambre;
        double pDouble = (repo.countChambreByTypeC(TypeChambre.DOUBLE) * 100.0) / totalChambre;
        double pTriple = (repo.countChambreByTypeC(TypeChambre.TRIPLE) * 100.0) / totalChambre;

        log.info("Nombre total des chambres: " + totalChambre);
        log.info("Le pourcentage des chambres pour le type SIMPLE est égal à " + pSimple);
        log.info("Le pourcentage des chambres pour le type DOUBLE est égal à " + pDouble);
        log.info("Le pourcentage des chambres pour le type TRIPLE est égal à " + pTriple);
    }

    @Override
    public void PlacesDispo() {
        // Récupérer l'année universitaire actuelle
        LocalDate[] dateRange = getCurrentAcademicYear();
        LocalDate dateDebutAU = dateRange[0];
        LocalDate dateFinAU = dateRange[1];

        for (Chambre c : repo.findAll()) {
            long nbReservation = repo
                    .countReservationsByIdChambreAndReservationsEstValideAndReservationsAnneeUniversitaireBetween(
                            c.getIdChambre(), true, dateDebutAU, dateFinAU);
            handleChambreAvailability(c, nbReservation);
        }
    }

    private LocalDate[] getCurrentAcademicYear() {
        int year = LocalDate.now().getYear() % 100;
        LocalDate dateDebutAU;
        LocalDate dateFinAU;

        if (LocalDate.now().getMonthValue() <= 7) {
            dateDebutAU = LocalDate.of(Integer.parseInt("20" + (year - 1)), 9, 15);
            dateFinAU = LocalDate.of(Integer.parseInt("20" + year), 6, 30);
        } else {
            dateDebutAU = LocalDate.of(Integer.parseInt("20" + year), 9, 15);
            dateFinAU = LocalDate.of(Integer.parseInt("20" + (year + 1)), 6, 30);
        }

        return new LocalDate[] { dateDebutAU, dateFinAU };
    }

    private void handleChambreAvailability(Chambre c, long nbReservation) {
        String laChambre = "Le nombre de place disponible pour la chambre ";
        String estComplete = "est complète";

        int maxReservations = getMaxReservationsForType(c.getTypeC());
        if (nbReservation < maxReservations) {
            log.info(laChambre + c.getTypeC() + " " + c.getNumeroChambre() + " est "
                    + (maxReservations - nbReservation));
        } else {
            log.info(laChambre + c.getTypeC() + " " + c.getNumeroChambre() + estComplete);
        }
    }

    private int getMaxReservationsForType(TypeChambre type) {
        switch (type) {
            case SIMPLE:
                return 1;
            case DOUBLE:
                return 2;
            case TRIPLE:
                return 3;
            default:
                throw new IllegalArgumentException("Unknown type: " + type);
        }
    }
}
