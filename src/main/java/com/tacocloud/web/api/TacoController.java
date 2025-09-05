package com.tacocloud.web.api;

import com.tacocloud.Taco;
import com.tacocloud.data.TacoRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RestController
@RequestMapping(value = "/api/tacos", produces = "application/json")
class TacoController {
    private final TacoRepository tacoRepository;

    public TacoController(TacoRepository tacoRepository) {
        this.tacoRepository = tacoRepository;
    }

    @GetMapping
    public List<Taco> findAll() {
        return tacoRepository.findAll();
    }

    @GetMapping("/recent")
    public List<Taco> recent() {
        PageRequest pageRequest = PageRequest.of(0, 12, Sort.by("createdAt").descending());
        return tacoRepository.findAll(pageRequest).getContent();
    }
}
