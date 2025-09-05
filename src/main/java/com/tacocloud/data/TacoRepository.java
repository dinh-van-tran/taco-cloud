package com.tacocloud.data;

import com.tacocloud.Taco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TacoRepository extends JpaRepository<Taco, Long> {
}