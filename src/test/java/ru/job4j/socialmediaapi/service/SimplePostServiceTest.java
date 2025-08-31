package ru.job4j.socialmediaapi.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.socialmediaapi.model.Post;
import ru.job4j.socialmediaapi.model.User;
import ru.job4j.socialmediaapi.service.post.PostService;
import ru.job4j.socialmediaapi.service.user.UserService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SimplePostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setUp() {
        clearTables();
    }

    @AfterAll
    public void clear() {
        clearTables();
    }

    private void clearTables() {
        var posts = postService.findAll();
        posts.forEach(postService::delete);

        var users = userService.findAllWithFriendsAndSubscriptions();
        users.forEach(user -> {
            user.getFriends().clear();
            user.getSubscriptions().clear();
        });
        users.forEach(userService::update);
        users.forEach(userService::delete);
    }

    private User createUser(int idx) {
        var user = new User(null, "email@email.com" + idx, "password" + idx, "name" + idx, new HashSet<>(),
                new HashSet<>(), new HashSet<>());
        userService.save(user);
        return userService.findAll().stream()
                .filter(u -> u.getEmail().equals("email@email.com" + idx))
                .findFirst()
                .orElseThrow();
    }

    private List<Post> getPosts(int count) {
        var posts = new java.util.ArrayList<Post>();
        for (int i = 0; i < count; i++) {
            var author = createUser(i);
            var post = new Post(null, author, "title" + i, "content" + i, new HashSet<>(), null);
            posts.add(post);
        }
        return posts;
    }

    /**
     * Проверяет успешный сценарий сохранения поста методом {@code save}
     */
    @Test
    void whenSaveThenGetData() {
        var posts = getPosts(1);
        var post = posts.get(0);

        var result = postService.save(post);
        var allPosts = postService.findAll();

        assertThat(result).isTrue();
        assertThat(allPosts)
                .isNotEmpty()
                .hasSize(1)
                .first()
                .usingRecursiveComparison()
                .withComparatorForType(Comparator.comparing((LocalDateTime dt) -> dt.truncatedTo(ChronoUnit.SECONDS)),
                        LocalDateTime.class)
                .ignoringFields("images", "author.friends", "author.subscriptions", "author.roles")
                .isEqualTo(post);
    }

    /**
     * Проверяет успешный сценарий обновления поста методом {@code update}
     */
    @Test
    void whenUpdateThenGetModifiedData() {
        var posts = getPosts(1);
        var post = posts.get(0);

        postService.save(post);

        post.setTitle("newTitle");
        var result = postService.update(post);
        var updatedPost = postService.findAll().get(0);

        assertThat(result).isTrue();
        assertThat(updatedPost)
                .usingRecursiveComparison()
                .withComparatorForType(Comparator.comparing((LocalDateTime dt) -> dt.truncatedTo(ChronoUnit.SECONDS)),
                        LocalDateTime.class)
                .ignoringFields("images", "author.friends", "author.subscriptions", "author.roles")
                .isEqualTo(post);
    }

    /**
     * Проверяет успешный сценарий удаления поста методом {@code delete}
     */
    @Test
    void whenDeleteThenGetEmpty() {
        var posts = getPosts(1);
        var post = posts.get(0);

        postService.save(post);

        var allPosts = postService.findAll();
        var postToDelete = allPosts.get(0);

        var result = postService.delete(postToDelete);

        var afterDelete = postService.findAll();
        assertThat(result).isTrue();
        assertThat(afterDelete).isEmpty();
    }
}