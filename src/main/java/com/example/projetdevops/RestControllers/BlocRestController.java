package com.example.projetdevops.RestControllers;
import com.example.projetdevops.DAO.Entities.Bloc;
import com.example.projetdevops.Services.Bloc.IBlocService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("bloc")
@AllArgsConstructor
public class BlocRestController {
    IBlocService service;

    @PostMapping("addOrUpdate")
    public Bloc addOrUpdate(@RequestBody Bloc b) {
        return service.addOrUpdate(b);
    }

    @GetMapping("findAll")
    public List<Bloc> findAll() {
        return service.findAll();
    }

    @GetMapping("findById")
    public Bloc findById(@RequestParam long id) {
        return service.findById(id);
    }

    @DeleteMapping("delete")
    public void delete(@RequestBody Bloc b) {
        service.delete(b);
    }

    @DeleteMapping("deleteById")
    public void deleteById(@RequestParam long id) {
        service.deleteById(id);
    }

    @PutMapping("affecterChambresABloc")
    public Bloc affecterChambresABloc(@RequestBody List<Long> numChambre, @RequestParam String nomBloc) {
        return service.affecterChambresABloc(numChambre, nomBloc);
    }

    @PutMapping("affecterBlocAFoyer")
    public Bloc affecterBlocAFoyer(@RequestParam String nomBloc, @RequestParam String nomFoyer) {
        return service.affecterBlocAFoyer(nomBloc, nomFoyer);
    }
}
