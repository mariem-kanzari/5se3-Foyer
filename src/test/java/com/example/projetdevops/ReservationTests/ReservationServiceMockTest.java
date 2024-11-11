package com.example.projetdevops.ReservationTests;

import com.example.projetdevops.DAO.Entities.Reservation;
import com.example.projetdevops.DAO.Repositories.ChambreRepository;
import com.example.projetdevops.DAO.Repositories.EtudiantRepository;
import com.example.projetdevops.DAO.Repositories.ReservationRepository;
import com.example.projetdevops.Services.Reservation.ReservationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReservationServiceMockTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ChambreRepository chambreRepository;

    @Mock
    private EtudiantRepository etudiantRepository;

    @InjectMocks
    private ReservationService reservationService;

    private Reservation reservation;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        reservation = new Reservation();
        reservation.setEstValide(true);
        reservation.setAnneeUniversitaire(LocalDate.now());
    }

    @Test
    void testAddOrUpdateReservation() {
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        Reservation savedReservation = reservationService.addOrUpdate(reservation);
        assertNotNull(savedReservation);
        assertEquals(reservation.getAnneeUniversitaire(), savedReservation.getAnneeUniversitaire());

        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void testFindReservationById() {
        when(reservationRepository.findById("1")).thenReturn(Optional.of(reservation));

        Reservation foundReservation = reservationService.findById("1");
        assertNotNull(foundReservation);
        assertEquals(reservation.getIdReservation(), foundReservation.getIdReservation());

        verify(reservationRepository, times(1)).findById("1");
    }

    @Test
    void testDeleteReservation() {
        reservation.setIdReservation("1");
        doNothing().when(reservationRepository).deleteById("1");

        reservationService.deleteById("1");
        verify(reservationRepository, times(1)).deleteById("1");
    }
}
