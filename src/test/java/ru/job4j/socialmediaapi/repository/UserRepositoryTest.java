package ru.job4j.socialmediaapi.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.job4j.socialmediaapi.model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }

    private List<User> getUsers(int count) {
        var users = new ArrayList<User>();
        for (int i = 0; i < count; i++) {
            var user = new User(null, "email@email.com" + i, "password" + i, "name" + i, new HashSet<>(),
                    new HashSet<>(), new HashSet<>());
            users.add(user);
        }
        return users;
    }

    /**
     * Проверяет успешный сценарий сохранения данных методом {@code save}
     */
    @Test
    public void whenSaveThenGet() {
        var users = getUsers(1);
        var user = users.get(0);

        userRepository.save(user);

        var result = userRepository.findAll();

        assertThat(result)
                .isNotEmpty()
                .hasSize(1)
                .first()
                .usingRecursiveComparison()
                .ignoringFields("friends", "subscriptions", "roles")
                .isEqualTo(user);
    }

    /**
     * Проверяет успешный сценарий получения данных методом {@code findById}
     */
    @Test
    public void whenFindByIdThenGet() {
        var users = getUsers(1);
        users.forEach(userRepository::save);
        var user = users.get(0);

        var result = userRepository.findById(user.getId());

        assertThat(result).isNotEmpty();
        assertThat(result.get())
                .usingRecursiveComparison()
                .ignoringFields("friends", "subscriptions", "roles")
                .isEqualTo(user);
    }

    /**
     * Проверяет неуспешный сценарий получения данных методом {@code findById}
     */
    @Test
    public void whenFindByIdWithoutDataThenGetEmpty() {
        var result = userRepository.findById(1);

        assertThat(result).isEmpty();
    }

    /**
     * Проверяет успешный сценарий обновления данных методом {@code save}
     */
    @Test
    public void whenSaveModifiedThenGetModified() {
        var users = getUsers(1);
        users.forEach(userRepository::save);
        var user = users.get(0);

        user.setName("new name");
        userRepository.save(user);

        var result = userRepository.findById(user.getId());

        assertThat(result).isNotEmpty();
        assertThat(result.get().getName()).isEqualTo("new name");
    }

    /**
     * Проверяет успешный сценарий удаления данных методом {@code delete}
     */
    @Test
    public void whenDeleteThenGetEmpty() {
        var users = getUsers(1);
        users.forEach(userRepository::save);
        var user = users.get(0);

        userRepository.delete(user);

        var result = userRepository.findById(user.getId());

        assertThat(result).isEmpty();
    }

    /**
     * Проверяет успешный сценарий получения данных методом {@code findByEmailAndPassword}
     */
    @Test
    void whenFindByEmailAndPasswordThenGetData() {
        var users = getUsers(3);
        var user = users.get(1);
        users.get(1).setEmail("NewEmail@email.com");
        users.get(1).setPassword("NewPassword");
        users.forEach(userRepository::save);

        var result = userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());

        assertThat(result).isNotEmpty();
        assertThat(result.get())
                .usingRecursiveComparison()
                .ignoringFields("friends", "subscriptions")
                .isEqualTo(user);
    }

    /**
     * Проверяет успешный сценарий получения данных методом {@code findSubscribersByPublisherId}
     */
    @Test
    void whenFindSubscribersByPublisherIdThenGetData() {
        var users = getUsers(4);
        users.forEach(userRepository::save);

        users.get(0).getSubscriptions().add(users.get(1));
        users.get(1).getSubscriptions().addAll(List.of(users.get(0), users.get(2), users.get(3)));
        users.get(2).getSubscriptions().addAll(List.of(users.get(0), users.get(1), users.get(3)));
        users.get(3).getSubscriptions().add(users.get(0));
        users.forEach(userRepository::save);

        var user = users.get(3);
        var expected = List.of(users.get(1), users.get(2));

        var result = userRepository.findSubscribersByPublisherId(user.getId());

        assertThat(result).containsExactlyInAnyOrderElementsOf(expected);
    }

    /**
     * Проверяет успешный сценарий получения данных методом {@code findFriendsByUserId}
     */
    @Test
    void whenFindFriendsByUserIdThenGetData() {
        var users = getUsers(4);
        users.forEach(userRepository::save);

        users.get(0).getFriends().add(users.get(1));
        users.get(1).getFriends().addAll(List.of(users.get(2), users.get(3)));
        users.get(2).getFriends().addAll(List.of(users.get(0), users.get(1), users.get(3)));
        users.get(3).getFriends().add(users.get(0));
        users.forEach(userRepository::save);

        var user = users.get(0);
        var expected = List.of(users.get(1), users.get(2), users.get(3));

        var result = userRepository.findFriendsByUserId(user.getId());

        assertThat(result).containsExactlyInAnyOrderElementsOf(expected);
    }
}