package com.example.projetdevops.RestControllers;

import com.example.projetdevops.DAO.Entities.Etudiant;
import com.example.projetdevops.Services.Etudiant.IEtudiantService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost"})

@RestController
@RequestMapping("/etudiant/")
@AllArgsConstructor
public class EtudiantRestController {
    IEtudiantService service;

    @PostMapping("addOrUpdate")
    Etudiant addOrUpdate(@RequestBody Etudiant e) {
        return service.addOrUpdate(e);
    }

    @GetMapping("find")
    List<Etudiant> findAll() {
        return  service.findAll();
    }

    @GetMapping("findById/{id}")
    Etudiant findById(@PathVariable("id") Integer id) {
        return service.findById(id);
    }

    @DeleteMapping("delete")
    void delete(@RequestBody Etudiant e) {
        service.delete(e);
    }

    @DeleteMapping("deleteById/{id}")
    void deleteById(@PathVariable("id") Integer id) {
        service.deleteById(id);
    }
}