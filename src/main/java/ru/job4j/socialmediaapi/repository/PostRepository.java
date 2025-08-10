package ru.job4j.socialmediaapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;
import ru.job4j.socialmediaapi.model.Post;
import ru.job4j.socialmediaapi.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends ListCrudRepository<Post, Integer> {
    List<Post> findByAuthor(User author);

    List<Post> findByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(LocalDateTime start, LocalDateTime end);

    Page<Post> findByOrderByCreatedAtDesc(Pageable pageable);
}