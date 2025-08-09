package ru.job4j.socialmediaapi.repository;

import org.springframework.data.repository.ListCrudRepository;
import ru.job4j.socialmediaapi.model.Image;

public interface ImageRepository extends ListCrudRepository<Image, Integer> {
}