package ru.job4j.socialmediaapi.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.job4j.socialmediaapi.model.Image;
import ru.job4j.socialmediaapi.model.Post;
import ru.job4j.socialmediaapi.model.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ImageRepositoryTest {
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        imageRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    private List<Image> getImages(int count) {
        var images = new ArrayList<Image>();
        for (int i = 0; i < count; i++) {
            var user = new User(null, "email@email.com" + i, "password" + i, "name" + i, Set.of(), Set.of(), Set.of());
            userRepository.save(user);
            var post = new Post(null, user, "title" + i, "content" + i, Set.of(), LocalDateTime.now());
            postRepository.save(post);
            var image = new Image(null, post, "name" + i, "url" + i);
            images.add(image);
        }
        return images;
    }

    /**
     * Проверяет успешный сценарий сохранения данных методом {@code save}
     */
    @Test
    public void whenSaveThenGet() {
        var images = getImages(1);
        var image = images.get(0);

        imageRepository.save(image);

        var result = imageRepository.findAll();

        assertThat(result)
                .isNotEmpty()
                .hasSize(1)
                .first()
                .usingRecursiveComparison()
                .withComparatorForType(Comparator.comparing((LocalDateTime dt) -> dt.truncatedTo(ChronoUnit.SECONDS)),
                        LocalDateTime.class)
                .ignoringFields("post.author.friends", "post.author.subscriptions", "post.author.roles", "post.images")
                .isEqualTo(image);
    }

    /**
     * Проверяет успешный сценарий получения данных методом {@code findById}
     */
    @Test
    public void whenFindByIdThenGet() {
        var images = getImages(1);
        images.forEach(imageRepository::save);
        var image = images.get(0);

        var result = imageRepository.findById(image.getId());

        assertThat(result).isNotEmpty();
        assertThat(result.get())
                .usingRecursiveComparison()
                .withComparatorForType(Comparator.comparing((LocalDateTime dt) -> dt.truncatedTo(ChronoUnit.SECONDS)),
                        LocalDateTime.class)
                .ignoringFields("post.author.friends", "post.author.subscriptions", "post.author.roles", "post.images")
                .isEqualTo(image);
    }

    /**
     * Проверяет неуспешный сценарий получения данных методом {@code findById}
     */
    @Test
    public void whenFindByIdWithoutDataThenGetEmpty() {
        var result = imageRepository.findById(1);

        assertThat(result).isEmpty();
    }

    /**
     * Проверяет успешный сценарий обновления данных методом {@code save}
     */
    @Test
    public void whenSaveModifiedThenGetModified() {
        var images = getImages(1);
        images.forEach(imageRepository::save);
        var image = images.get(0);

        image.setName("new name");
        imageRepository.save(image);

        var result = imageRepository.findById(image.getId());

        assertThat(result).isNotEmpty();
        assertThat(result.get().getName()).isEqualTo("new name");
    }

    /**
     * Проверяет успешный сценарий удаления данных методом {@code delete}
     */
    @Test
    public void whenDeleteThenGetEmpty() {
        var images = getImages(1);
        images.forEach(imageRepository::save);
        var image = images.get(0);

        imageRepository.delete(image);

        var result = imageRepository.findById(image.getId());

        assertThat(result).isEmpty();
    }

    /**
     * Проверяет успешный сценарий удаления данных поста методом {@code deletePostImage}
     */
    @Test
    public void whenDeletePostImageThenGetDataWithoutDeleted() {
        var images = getImages(3);
        images.forEach(imageRepository::save);
        var postId = images.get(1).getPost().getId();

        var result = imageRepository.deletePostImage(postId);
        var remainingImages = imageRepository.findAll();

        assertThat(result).isEqualTo(1);
        assertThat(remainingImages)
                .isNotEmpty()
                .hasSize(2)
                .allSatisfy(image -> assertThat(image.getPost().getId()).isNotEqualTo(postId));
    }
}