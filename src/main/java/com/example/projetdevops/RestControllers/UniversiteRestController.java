package com.example.projetdevops.RestControllers;

import com.example.projetdevops.DAO.Entities.Universite;
import com.example.projetdevops.Services.Universite.IUniversiteService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("universite")
@AllArgsConstructor
public class UniversiteRestController {
    IUniversiteService service;
//PostMappingg111
    @PostMapping("addOrUpdate")
    public Universite addOrUpdate(@RequestBody Universite u) {
        return service.addOrUpdate(u);
    }

    @GetMapping("findAll")
    public List<Universite> findAll() {
        return service.findAll();
    }

    @GetMapping("findById")
    public Universite findById(@RequestParam long id) {
        return service.findById(id);
    }

    @DeleteMapping("delete")
    public void delete(@RequestBody Universite u) {
        service.delete(u);
    }

    @DeleteMapping("deleteById")
    public void deleteById(@RequestParam long id) {
        service.deleteById(id);
    }


}

