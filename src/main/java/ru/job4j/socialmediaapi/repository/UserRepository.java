package ru.job4j.socialmediaapi.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.socialmediaapi.model.User;

public interface UserRepository extends CrudRepository<User, Integer> {
}