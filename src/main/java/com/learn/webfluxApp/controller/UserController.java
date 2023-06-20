package com.learn.webfluxApp.controller;

import com.learn.webfluxApp.entity.User;
import com.learn.webfluxApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("")
    public Flux<User> getAllUsers() {
        return userRepository.findAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Mono<User> getUser(@PathVariable Integer id) {
        return userRepository.findById(id)
                .flatMap(Mono::just)
                .switchIfEmpty(Mono.error(new ResponseStatusException((HttpStatus.NOT_FOUND))));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public Mono<Object> make(@RequestBody User user) {
        return userRepository.findByUsername(user.getUsername())
                .flatMap(userFound -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "user exist");
                })
                .switchIfEmpty(userRepository.save(user));
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("/{id}")
    public Mono<User> updateUserById(@RequestBody User user, @PathVariable Integer id) {
        String newUsername = user.getUsername();

        Mono<User> existingUserMono = userRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")));

        return existingUserMono
                .flatMap(existingUser -> {
                    return userRepository.findByUsername(newUsername)
                            .flatMap(userWithSameUsername -> {
                                if (!userWithSameUsername.getId().equals(id)) {
                                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exist!");
                                }
                                return Mono.just(existingUser);
                            })
                            .switchIfEmpty(Mono.just(existingUser));
                })
                .map(existingUser -> {
                    existingUser.setName(user.getName());
                    existingUser.setPassword(user.getPassword());
                    existingUser.setUsername(newUsername);
                    return existingUser;
                })
                .flatMap(userRepository::save);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public Mono<String> deleteById(@PathVariable Integer id) {
        return userRepository.findById(id)
                .flatMap(user -> userRepository.deleteById(id).thenReturn("User deleted successfully"))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "id NOT FOUND: " + id)));
    }
}
