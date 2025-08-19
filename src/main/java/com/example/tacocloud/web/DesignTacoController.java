package com.example.tacocloud.web;

import com.example.tacocloud.Ingredient;
import com.example.tacocloud.Taco;
import com.example.tacocloud.TacoOrder;
import com.example.tacocloud.User;
import com.example.tacocloud.data.IngredientRepository;
import com.example.tacocloud.data.TacoRepository;
import com.example.tacocloud.data.UserRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

// automatically generate an SLF4J Logger static property for the class.
@Slf4j
@Controller
@RequestMapping("/design")
// This annotation indicates that the model attribute "tacoOrder" should be stored in the session.
// This means that the "tacoOrder" object will be available across multiple requests.
@SessionAttributes("tacoOrder")
public class DesignTacoController {
    private final IngredientRepository ingredientRepository;
    private final TacoRepository tacoRepository;
    private final UserRepository userRepository;

    @Autowired
    public DesignTacoController(IngredientRepository ingredientRepository, UserRepository userRepository, TacoRepository tacoRepository) {
        this.ingredientRepository = ingredientRepository;
        this.userRepository = userRepository;
        this.tacoRepository = tacoRepository;
    }

    @ModelAttribute
    public void addIngredientsToModel(Model model) {
        Iterable<Ingredient> ingredientList = ingredientRepository.findAll();

        Ingredient.Type[] types = Ingredient.Type.values();
        for (Ingredient.Type type : types) {
            model.addAttribute(type.toString().toLowerCase(),
                    filterByType(ingredientList, type));
        }
    }

    @ModelAttribute(name = "tacoOrder")
    public TacoOrder order() {
        return new TacoOrder();
    }

    @ModelAttribute("taco")
    public Taco taco() {
        return new Taco();
    }

    @ModelAttribute(name = "user")
    public User user(Principal principal) {
        if (principal == null) {
            return null; // No user is authenticated
        }

        String username = principal.getName();
        return userRepository.findByUsername(username);
    }

    @GetMapping
    public String showDesignForm() {
        return "design";
    }

    @PostMapping
    public String processTaco(@Valid Taco taco, Errors errors, @ModelAttribute TacoOrder tacoOrder) {
        if (errors.hasErrors()) {
            log.info("Validation errors found");
            return "design";
        }

        Taco saved = tacoRepository.save(taco);
        tacoOrder.addTaco(saved);

        log.info("Processing taco: {}", taco);
        return "redirect:/orders/current";
    }

    private Iterable<Ingredient> filterByType(Iterable<Ingredient> ingredientList, Ingredient.Type type) {
        return StreamSupport.stream(ingredientList.spliterator(), false)
                .filter(i -> i.getType().equals(type))
                .collect(Collectors.toList());
    }
}
