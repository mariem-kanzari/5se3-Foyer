package com.example.projetdevops.RestControllers;

import com.example.projetdevops.DAO.Entities.Etudiant;
import com.example.projetdevops.DAO.Entities.Universite;
import com.example.projetdevops.Services.Etudiant.IEtudiantService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")

@RestController
@RequestMapping("/etudiant/")
@AllArgsConstructor
public class EtudiantRestController {
    IEtudiantService service;

    @PostMapping("addOrUpdate")
    public Etudiant addEtudiant(@RequestBody Etudiant c) {
        Etudiant etudiant = service.addOrUpdate(c);
        return etudiant;
    }

    @GetMapping("find")
    public List<Etudiant> findAll() {
        return service.findAll();
    }

    @GetMapping("findById/{id}")
    Etudiant findById(@PathVariable("id") Long id) {
        return service.retrieveEtudiant(id);
    }

    @DeleteMapping("delete/{etudiant-id}")
    public void removeEtudiant(@PathVariable("etudiant-id") Long chId) {
        service.removeEtudiant(chId);
    }
}