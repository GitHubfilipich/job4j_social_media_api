package ru.job4j.socialmediaapi.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.socialmediaapi.model.Message;

public interface MessageRepository extends CrudRepository<Message, Integer> {
}