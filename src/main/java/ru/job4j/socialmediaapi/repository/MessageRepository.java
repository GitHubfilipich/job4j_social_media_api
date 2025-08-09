package ru.job4j.socialmediaapi.repository;

import org.springframework.data.repository.ListCrudRepository;
import ru.job4j.socialmediaapi.model.Message;

public interface MessageRepository extends ListCrudRepository<Message, Integer> {
}