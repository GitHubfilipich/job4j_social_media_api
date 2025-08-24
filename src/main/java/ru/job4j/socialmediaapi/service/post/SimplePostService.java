package ru.job4j.socialmediaapi.service.post;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.socialmediaapi.dto.UserPostsDto;
import ru.job4j.socialmediaapi.model.Post;
import ru.job4j.socialmediaapi.repository.PostRepository;
import ru.job4j.socialmediaapi.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@Service
public class SimplePostService implements PostService {

    private PostRepository postRepository;
    private UserRepository userRepository;

    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Override
    public List<UserPostsDto> getUserPostsDtosByUsers(List<Integer> ids) {
        return ids.stream()
                .map(userId -> userRepository.findById(userId)
                        .map(user -> {
                            List<Post> posts = postRepository.findByAuthor(user);
                            return new UserPostsDto(user.getId(), user.getName(), posts);
                        })
                        .orElse(null)
                )
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public Optional<Post> findById(int id) {
        return postRepository.findById(id);
    }

    @Override
    public boolean save(Post post) {
        post.setCreatedAt(LocalDateTime.now());
        postRepository.save(post);
        return true;
    }

    @Override
    public boolean update(Post post) {
        postRepository.save(post);
        return true;
    }

    @Override
    public boolean delete(Post post) {
        postRepository.delete(post);
        return true;
    }

    @Override
    public boolean deleteById(int id) {
        postRepository.deleteById(id);
        return true;
    }
}
