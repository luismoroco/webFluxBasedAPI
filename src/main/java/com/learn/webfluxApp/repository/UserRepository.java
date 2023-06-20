package com.learn.webfluxApp.repository;

import com.learn.webfluxApp.entity.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, Integer> {
    /*
    * Interface, Reactive contain all the methods for this case
    * */

    Mono<User> findByUsername(String username);
}
