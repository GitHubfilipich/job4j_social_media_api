package ru.job4j.socialmediaapi.service.post;

import org.springframework.transaction.annotation.Transactional;
import ru.job4j.socialmediaapi.dto.UserPostsDto;
import ru.job4j.socialmediaapi.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostService {

    List<Post> findAll();

    List<UserPostsDto> getUserPostsDtosByUsers(List<Integer> ids);

    @Transactional
    boolean save(Post post);

    @Transactional
    boolean update(Post post);

    @Transactional
    boolean delete(Post post);

    Optional<Post> findById(int id);

    @Transactional
    boolean deleteById(int id);
}
