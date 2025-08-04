package ru.job4j.socialmediaapi.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.socialmediaapi.model.Image;

public interface ImageRepository extends CrudRepository<Image, Integer> {
}