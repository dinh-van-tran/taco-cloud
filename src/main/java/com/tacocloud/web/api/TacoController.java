package com.tacocloud.web.api;

import com.tacocloud.Taco;
import com.tacocloud.data.TacoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(
        value = "/api/tacos",
        produces = "application/json"
)
class TacoController {
    private final TacoRepository tacoRepository;

    public TacoController(TacoRepository tacoRepository) {
        this.tacoRepository = tacoRepository;
    }

    @GetMapping("/{id}")
    public Mono<Taco> findById(@PathVariable("id") Long id) {
        return tacoRepository.findById(id);
    }

    @GetMapping
    public Flux<Taco> findAll() {
        return tacoRepository.findAll();
    }

    @GetMapping("/recent")
    public Flux<Taco> recent() {
        return tacoRepository.findAll().take(12);
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Taco> createTaco(@RequestBody Mono<Taco> taco) {
        return tacoRepository.saveAll(taco).next();
    }
}
