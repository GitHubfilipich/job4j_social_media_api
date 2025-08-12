package ru.job4j.socialmediaapi.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.job4j.socialmediaapi.model.Post;
import ru.job4j.socialmediaapi.model.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
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
            var user = new User(null, "email" + i, "password" + i, "name" + i, new HashSet<>(), new HashSet<>());
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

    /**
     * Проверяет успешный сценарий получения данных методом {@code findByAuthor}
     */
    @Test
    public void whenFindByAuthorThenGetData() {
        var posts = getPosts(3);

        var author = posts.get(0).getAuthor();
        posts.get(1).setAuthor(author);

        posts.forEach(postRepository::save);

        var result = postRepository.findByAuthor(author);

        assertThat(result)
                .usingRecursiveComparison()
                .withComparatorForType(Comparator.comparing((LocalDateTime dt) -> dt.truncatedTo(ChronoUnit.SECONDS)),
                        LocalDateTime.class)
                .ignoringFields("author.friends", "author.subscriptions", "images")
                .isEqualTo(posts.subList(0, 2));
    }

    /**
     * Проверяет успешный сценарий получения данных методом
     * {@code findByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual}
     */
    @Test
    public void whenFindByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqualThenGetData() {
        var posts = getPosts(4);
        posts.get(0).setCreatedAt(LocalDateTime.now().minusDays(2));
        posts.get(3).setCreatedAt(LocalDateTime.now().plusDays(2));

        posts.forEach(postRepository::save);

        var start = LocalDateTime.now().minusDays(1);
        var end = LocalDateTime.now().plusDays(1);
        var result = postRepository.findByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(start, end);

        assertThat(result)
                .usingRecursiveComparison()
                .withComparatorForType(Comparator.comparing((LocalDateTime dt) -> dt.truncatedTo(ChronoUnit.SECONDS)),
                        LocalDateTime.class)
                .ignoringFields("author.friends", "author.subscriptions", "images")
                .isEqualTo(posts.subList(1, 3));
    }

    /**
     * Проверяет успешный сценарий получения данных методом {@code findByOrderByCreatedAtDesc}
     */
    @Test
    public void whenFindByOrderByCreatedAtDescThenGetData() {
        var posts = getPosts(4);
        posts.get(1).setCreatedAt(LocalDateTime.now().plusDays(1));
        posts.get(2).setCreatedAt(LocalDateTime.now().plusDays(2));
        posts.get(3).setCreatedAt(LocalDateTime.now().plusDays(3));
        posts.forEach(postRepository::save);

        var expected = List.of(posts.get(3), posts.get(2), posts.get(1));

        var result = postRepository.findByOrderByCreatedAtDesc(Pageable.ofSize(3));

        assertThat(result.getContent())
                .usingRecursiveComparison()
                .withComparatorForType(Comparator.comparing((LocalDateTime dt) -> dt.truncatedTo(ChronoUnit.SECONDS)),
                        LocalDateTime.class)
                .ignoringFields("author.friends", "author.subscriptions", "images")
                .isEqualTo(expected);
    }

    /**
     * Проверяет успешный сценарий обновления данных методом {@code updatePostTitleAndContent}
     */
    @Test
    public void whenUpdatePostTitleAndContentThenGetUpdatedData() {
        var posts = getPosts(3);
        posts.forEach(postRepository::save);

        var newTitle = "new title";
        var newContent = "new content";
        var result = postRepository.updatePostTitleAndContent(newTitle, newContent, posts.get(1).getId());
        var updatedPost = postRepository.findById(posts.get(1).getId());

        assertThat(result).isEqualTo(1);
        assertThat(updatedPost).isNotEmpty();
        assertThat(updatedPost.get())
                .hasFieldOrPropertyWithValue("title", newTitle)
                .hasFieldOrPropertyWithValue("content", newContent);
    }

    /**
     * Проверяет успешный сценарий удаления данных методом {@code deletePostById}
     */
    @Test
    public void whenDeletePostByIdThenGetEmpty() {
        var posts = getPosts(1);
        posts.forEach(postRepository::save);
        var post = posts.get(0);

        var result = postRepository.deletePostById(post.getId());

        var remainingPost = postRepository.findById(post.getId());

        assertThat(result).isEqualTo(1);
        assertThat(remainingPost).isEmpty();
    }

    /**
     * Проверяет успешный сценарий получения данных методом {@code findByUserSubscribersSortedByCreatedAtDescPaged}
     */
    @Test
    public void whenFindByUserSubscribersSortedByCreatedAtDescPagedThenGetData() {
        var posts = getPosts(5);
        posts.get(1).setCreatedAt(LocalDateTime.now().plusDays(1));
        posts.get(2).setCreatedAt(LocalDateTime.now().plusDays(2));
        posts.get(3).setCreatedAt(LocalDateTime.now().plusDays(3));
        posts.get(4).setCreatedAt(LocalDateTime.now().plusDays(4));

        posts.get(1).getAuthor().getSubscriptions().add(posts.get(0).getAuthor());
        posts.get(2).getAuthor().getSubscriptions().add(posts.get(0).getAuthor());
        posts.get(3).getAuthor().getSubscriptions().add(posts.get(0).getAuthor());
        posts.get(4).getAuthor().getSubscriptions().add(posts.get(1).getAuthor());

        posts.forEach(postRepository::save);

        var user = posts.get(0).getAuthor();
        PageRequest pageRequest = PageRequest.of(0, 2);

        var expected = List.of(posts.get(3), posts.get(2));

        var result = postRepository.findByUserSubscribersSortedByCreatedAtDescPaged(user.getId(), pageRequest);

        assertThat(result).containsExactlyElementsOf(expected);
    }
}