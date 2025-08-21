package com.example.tacocloud.data;

import com.example.tacocloud.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

@RestResource(exported = false)
public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
