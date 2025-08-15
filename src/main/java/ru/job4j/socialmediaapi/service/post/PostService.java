package ru.job4j.socialmediaapi.service.post;

import org.springframework.transaction.annotation.Transactional;
import ru.job4j.socialmediaapi.model.Post;

import java.util.List;

public interface PostService {

    List<Post> findAll();

    @Transactional
    boolean save(Post post);

    @Transactional
    boolean update(Post post);

    @Transactional
    boolean delete(Post post);
}
