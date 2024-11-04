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
import java.util.NoSuchElementException;
import java.util.Optional;

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
        Optional<Chambre> optionalChambre = repo.findById(id);
        if (optionalChambre.isPresent()) {
            return optionalChambre.get();
        } else {
            throw new NoSuchElementException("No Chambre found with id: " + id);
        }
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
    public List<Chambre> getChambresNonReserveParNomFoyerEtTypeChambre(String nomFoyer, TypeChambre type) {
        LocalDate[] academicYearDates = calculateAcademicYearDates();
        List<Chambre> listChambreDispo = new ArrayList<>();

        for (Chambre c : repo.findAll()) {
            if (isChambreEligible(c, nomFoyer, type)) {
                int numReservations = countReservationsInAcademicYear(c, academicYearDates);
                if (isChambreAvailable(c, numReservations)) {
                    listChambreDispo.add(c);
                }
            }
        }
        return listChambreDispo;
    }

    private LocalDate[] calculateAcademicYearDates() {
        LocalDate dateDebutAU;
        LocalDate dateFinAU;
        int year = LocalDate.now().getYear() % 100;

        if (LocalDate.now().getMonthValue() <= 7) {
            dateDebutAU = LocalDate.of(Integer.parseInt("20" + (year - 1)), 9, 15);
            dateFinAU = LocalDate.of(Integer.parseInt("20" + year), 6, 30);
        } else {
            dateDebutAU = LocalDate.of(Integer.parseInt("20" + year), 9, 15);
            dateFinAU = LocalDate.of(Integer.parseInt("20" + (year + 1)), 6, 30);
        }

        return new LocalDate[]{dateDebutAU, dateFinAU};
    }

    private boolean isChambreEligible(Chambre chambre, String nomFoyer, TypeChambre type) {
        return chambre.getTypeC().equals(type) && chambre.getBloc().getFoyer().getNomFoyer().equals(nomFoyer);
    }

    private int countReservationsInAcademicYear(Chambre chambre, LocalDate[] academicYearDates) {
        int numReservation = 0;
        LocalDate dateDebutAU = academicYearDates[0];
        LocalDate dateFinAU = academicYearDates[1];

        for (Reservation reservation : chambre.getReservations()) {
            if (reservation.getAnneeUniversitaire().isBefore(dateFinAU) && reservation.getAnneeUniversitaire().isAfter(dateDebutAU)) {
                numReservation++;
            }
        }
        return numReservation;
    }

    private boolean isChambreAvailable(Chambre chambre, int numReservations) {
        if (chambre.getTypeC().equals(TypeChambre.SIMPLE)) {
            return numReservations == 0;
        } else if (chambre.getTypeC().equals(TypeChambre.DOUBLE)) {
            return numReservations < 2;
        } else if (chambre.getTypeC().equals(TypeChambre.TRIPLE)) {
            return numReservations < 3;
        }
        return false;
    }


    @Override
    public void listeChambresParBloc() {
        for (Bloc b : blocRepository.findAll()) {
            log.info("Bloc => " + b.getNomBloc() + " ayant une capacité " + b.getCapaciteBloc());
            if (b.getChambres().size() != 0) {
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
        double pSimple = (repo.countChambreByTypeC(TypeChambre.SIMPLE) * 100) / totalChambre;
        double pDouble = (repo.countChambreByTypeC(TypeChambre.DOUBLE) * 100) / totalChambre;
        double pTriple = (repo.countChambreByTypeC(TypeChambre.TRIPLE) * 100) / totalChambre;
        log.info("Nombre total des chambre: " + totalChambre);
        log.info("Le pourcentage des chambres pour le type SIMPLE est égale à " + pSimple);
        log.info("Le pourcentage des chambres pour le type DOUBLE est égale à " + pDouble);
        log.info("Le pourcentage des chambres pour le type TRIPLE est égale à " + pTriple);

    }

    @Override
    public void nbPlacesDisponibleParChambreAnneeEnCours() {
        // Constants for log messages
        final String DISPO_MESSAGE = "Le nombre de place disponible pour la chambre ";
        final String COMPLETE_MESSAGE = "La chambre ";
        final String IS_COMPLETE = " est complete";

        // Début "récuperer l'année universitaire actuelle"
        LocalDate dateDebutAU;
        LocalDate dateFinAU;
        int year = LocalDate.now().getYear() % 100;

        if (LocalDate.now().getMonthValue() <= 7) {
            dateDebutAU = LocalDate.of(Integer.parseInt("20" + (year - 1)), 9, 15);
            dateFinAU = LocalDate.of(Integer.parseInt("20" + year), 6, 30);
        } else {
            dateDebutAU = LocalDate.of(Integer.parseInt("20" + year), 9, 15);
            dateFinAU = LocalDate.of(Integer.parseInt("20" + (year + 1)), 6, 30);
        }
        // Fin "récuperer l'année universitaire actuelle"

        for (Chambre c : repo.findAll()) {
            long nbReservation = repo.countReservationsByIdChambreAndReservationsEstValideAndReservationsAnneeUniversitaireBetween(
                    c.getIdChambre(), true, dateDebutAU, dateFinAU
            );

            logChambreAvailability(c, nbReservation, DISPO_MESSAGE, COMPLETE_MESSAGE, IS_COMPLETE);
        }
    }

    private void logChambreAvailability(Chambre c, long nbReservation, String dispoMessage, String completeMessage, String isComplete) {
        switch (c.getTypeC()) {
            case SIMPLE:
                if (nbReservation == 0) {
                    log.info(dispoMessage + c.getTypeC() + " " + c.getNumeroChambre() + " est 1 ");
                } else {
                    log.info(completeMessage + c.getTypeC() + " " + c.getNumeroChambre() + isComplete);
                }
                break;
            case DOUBLE:
                if (nbReservation < 2) {
                    log.info(dispoMessage + c.getTypeC() + " " + c.getNumeroChambre() + " est " + (2 - nbReservation));
                } else {
                    log.info(completeMessage + c.getTypeC() + " " + c.getNumeroChambre() + isComplete);
                }
                break;
            case TRIPLE:
                if (nbReservation < 3) {
                    log.info(dispoMessage + c.getTypeC() + " " + c.getNumeroChambre() + " est " + (3 - nbReservation));
                } else {
                    log.info(completeMessage + c.getTypeC() + " " + c.getNumeroChambre() + isComplete);
                }
                break;
        }
    }

}