package com.tacocloud.data;

import com.tacocloud.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import reactor.core.publisher.Mono;

@RestResource(exported = false)
public interface UserRepository extends ReactiveCrudRepository<User, Long> {
    Mono<User> findByUsername(String username);
}
