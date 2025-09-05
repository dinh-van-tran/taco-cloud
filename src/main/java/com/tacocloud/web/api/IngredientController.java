package com.tacocloud.web.api;

import com.tacocloud.Ingredient;
import com.tacocloud.data.IngredientRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "/api/ingredients", produces = "application/json")
class IngredientController {
    private final IngredientRepository ingredientRepository;

    public IngredientController(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @GetMapping
    public List<Ingredient> findAll(Principal user) {
        return ingredientRepository.findAll();
    }

    @PutMapping
    @PreAuthorize("hasAuthority('SCOPE_taco-cloud-api/writeIngredients')")
    public Ingredient save(@RequestBody Ingredient ingredient) {
        return ingredientRepository.save(ingredient);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_taco-cloud-api/deleteIngredients')")
    public void deleteById(@PathVariable String id) {
        ingredientRepository.deleteById(id);
    }
}
