package com.example.projetdevops.DAO.Entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "T_ETUDIANT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Etudiant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long idEtudiant;
    String nomEt;
    String prenomEt;
    long cin;
    String ecole;
    LocalDate dateNaissance;
    @ManyToMany(mappedBy = "etudiants") // Reference to the 'etudiants' property in Reservation
    @JsonIgnore
    private List<Reservation> reservations = new ArrayList<>();

}
