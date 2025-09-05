package com.tacocloud.data;

import com.tacocloud.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, String> {
}
