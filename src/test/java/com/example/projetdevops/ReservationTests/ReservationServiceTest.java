package com.example.projetdevops.ReservationTests;

import com.example.projetdevops.DAO.Entities.Chambre;
import com.example.projetdevops.DAO.Entities.Etudiant;
import com.example.projetdevops.DAO.Entities.Reservation;
import com.example.projetdevops.DAO.Entities.TypeChambre;
import com.example.projetdevops.DAO.Repositories.ChambreRepository;
import com.example.projetdevops.DAO.Repositories.EtudiantRepository;
import com.example.projetdevops.DAO.Repositories.ReservationRepository;
import com.example.projetdevops.Services.Reservation.ReservationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ChambreRepository chambreRepository;

    @Mock
    private EtudiantRepository etudiantRepository;

    @InjectMocks
    private ReservationService reservationService;

    private Reservation reservation;
    private Chambre chambre;
    private Etudiant etudiant;

    @BeforeEach
    void setUp() {
        chambre = new Chambre();
        chambre.setIdChambre(1L);
        chambre.setNumeroChambre(101);
        chambre.setTypeC(TypeChambre.SIMPLE);

        etudiant = new Etudiant();
        etudiant.setCin(123456L);
        etudiant.setNomEt("John");

        reservation = new Reservation();
        reservation.setIdReservation("2023/2024-Bloc A-101-123456");
        reservation.setEstValide(true);
        reservation.setAnneeUniversitaire(LocalDate.now());
    }

    @Test
    void testAddOrUpdate() {
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        Reservation savedReservation = reservationService.addOrUpdate(reservation);

        assertNotNull(savedReservation);
        assertEquals("2023/2024-Bloc A-101-123456", savedReservation.getIdReservation());
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void testFindAll() {
        when(reservationRepository.findAll()).thenReturn(List.of(reservation));

        List<Reservation> reservations = reservationService.findAll();

        assertNotNull(reservations);
        assertEquals(1, reservations.size());
        assertEquals(reservation, reservations.get(0));
    }

    @Test
    void testFindById() {
        when(reservationRepository.findById("2023/2024-Bloc A-101-123456")).thenReturn(Optional.of(reservation));

        Reservation foundReservation = reservationService.findById("2023/2024-Bloc A-101-123456");

        assertNotNull(foundReservation);
        assertEquals("2023/2024-Bloc A-101-123456", foundReservation.getIdReservation());
    }

    @Test
    void testDeleteById() {
        doNothing().when(reservationRepository).deleteById("2023/2024-Bloc A-101-123456");

        reservationService.deleteById("2023/2024-Bloc A-101-123456");

        verify(reservationRepository, times(1)).deleteById("2023/2024-Bloc A-101-123456");
    }

    @Test
    void testAnnulerReservation() {
        when(reservationRepository.findByEtudiantsCinAndEstValide(123456L, true)).thenReturn(reservation);
        when(chambreRepository.findByReservationsIdReservation("2023/2024-Bloc A-101-123456")).thenReturn(chambre);

        String result = reservationService.annulerReservation(123456L);

        assertEquals("La réservation 2023/2024-Bloc A-101-123456 est annulée avec succés", result);
        verify(reservationRepository, times(1)).delete(reservation);
        verify(chambreRepository, times(1)).save(chambre);
    }

    @Test
    void testAffectReservationAChambre() {
        when(reservationRepository.findById("2023/2024-Bloc A-101-123456")).thenReturn(Optional.of(reservation));
        when(chambreRepository.findById(1L)).thenReturn(Optional.of(chambre));

        reservationService.affectReservationAChambre("2023/2024-Bloc A-101-123456", 1L);

        verify(chambreRepository, times(1)).save(chambre);
        assertTrue(chambre.getReservations().contains(reservation));
    }
}
