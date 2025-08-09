package ru.job4j.socialmediaapi.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.socialmediaapi.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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
            var user = new User(null, "email" + i, "password" + i, "name" + i, Set.of(), Set.of());
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
                .ignoringFields("friends", "subscriptions")
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
                .ignoringFields("friends", "subscriptions")
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
}