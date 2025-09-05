package com.tacocloud;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity(name = "tacos")
@RestResource(rel = "tacos", path = "tacos")
public class Taco {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private Date createdAt;

    @NotNull
    @Size(min = 5, message = "Name must be at least 5 characters long")
    private String name;

    @Size(min=1, message="You must choose at least 1 ingredient")
    @ManyToMany(targetEntity =  Ingredient.class)
    private List<Ingredient> ingredients = new ArrayList<>();

    @PrePersist
    void createdAt() {
        this.createdAt = new Date();
    }}
