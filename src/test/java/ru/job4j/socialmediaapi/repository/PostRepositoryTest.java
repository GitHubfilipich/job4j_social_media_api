package ru.job4j.socialmediaapi.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.socialmediaapi.model.Post;
import ru.job4j.socialmediaapi.model.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PostRepositoryTest {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    private List<Post> getPosts(int count) {
        var posts = new ArrayList<Post>();
        for (int i = 0; i < count; i++) {
            var user = new User(null, "email" + i, "password" + i, "name" + i, Set.of(), Set.of());
            userRepository.save(user);
            var post = new Post(null, user, "title" + i, "content" + i, Set.of(), LocalDateTime.now());
            posts.add(post);
        }
        return posts;
    }

    /**
     * Проверяет успешный сценарий сохранения данных методом {@code save}
     */
    @Test
    public void whenSaveThenGet() {
        var posts = getPosts(1);
        var post = posts.get(0);

        postRepository.save(post);

        var result = postRepository.findAll();

        assertThat(result)
                .isNotEmpty()
                .hasSize(1)
                .first()
                .usingRecursiveComparison()
                .withComparatorForType(Comparator.comparing((LocalDateTime dt) -> dt.truncatedTo(ChronoUnit.SECONDS)),
                        LocalDateTime.class)
                .ignoringFields("author.friends", "author.subscriptions", "images")
                .isEqualTo(post);
    }

    /**
     * Проверяет успешный сценарий получения данных методом {@code findById}
     */
    @Test
    public void whenFindByIdThenGet() {
        var posts = getPosts(1);
        posts.forEach(postRepository::save);
        var post = posts.get(0);

        var result = postRepository.findById(post.getId());

        assertThat(result).isNotEmpty();
        assertThat(result.get())
                .usingRecursiveComparison()
                .withComparatorForType(Comparator.comparing((LocalDateTime dt) -> dt.truncatedTo(ChronoUnit.SECONDS)),
                        LocalDateTime.class)
                .ignoringFields("author.friends", "author.subscriptions", "images")
                .isEqualTo(post);
    }

    /**
     * Проверяет неуспешный сценарий получения данных методом {@code findById}
     */
    @Test
    public void whenFindByIdWithoutDataThenGetEmpty() {
        var result = postRepository.findById(1);

        assertThat(result).isEmpty();
    }

    /**
     * Проверяет успешный сценарий обновления данных методом {@code save}
     */
    @Test
    public void whenSaveModifiedThenGetModified() {
        var posts = getPosts(1);
        posts.forEach(postRepository::save);
        var post = posts.get(0);

        post.setTitle("new title");
        postRepository.save(post);

        var result = postRepository.findById(post.getId());

        assertThat(result).isNotEmpty();
        assertThat(result.get().getTitle()).isEqualTo("new title");
    }

    /**
     * Проверяет успешный сценарий удаления данных методом {@code delete}
     */
    @Test
    public void whenDeleteThenGetEmpty() {
        var posts = getPosts(1);
        posts.forEach(postRepository::save);
        var post = posts.get(0);

        postRepository.delete(post);

        var result = postRepository.findById(post.getId());

        assertThat(result).isEmpty();
    }
}