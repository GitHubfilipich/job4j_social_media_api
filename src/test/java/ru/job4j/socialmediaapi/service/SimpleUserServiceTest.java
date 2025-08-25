package ru.job4j.socialmediaapi.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.socialmediaapi.model.User;
import ru.job4j.socialmediaapi.service.user.UserService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SimpleUserServiceTest {

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
        var users = userService.findAllWithFriendsAndSubscriptions();
        users.forEach(user -> {
            user.getFriends().clear();
            user.getSubscriptions().clear();
        });
        users.forEach(userService::update);
        users.forEach(userService::delete);
    }

    private List<User> getUsers(int count) {
        var users = new ArrayList<User>();
        for (int i = 0; i < count; i++) {
            var user = new User(null, "email@email.com" + i, "password" + i, "name" + i, new HashSet<>(),
                    new HashSet<>());
            users.add(user);
        }
        return users;
    }

    /**
     * Проверяет успешный сценарий сохранения пользователя методом {@code save}
     */
    @Test
    void whenSaveThenGetData() {
        var users = getUsers(1);
        var user = users.get(0);

        var result = userService.save(user);
        var allUsers = userService.findAll();

        assertThat(result).isTrue();
        assertThat(allUsers)
                .isNotEmpty()
                .hasSize(1)
                .first()
                .usingRecursiveComparison()
                .ignoringFields("friends", "subscriptions")
                .isEqualTo(user);
    }

    /**
     * Проверяет успешный сценарий обновления пользователя методом {@code update}
     */
    @Test
    void whenUpdateThenGetModifiedData() {
        var users = getUsers(1);
        var user = users.get(0);

        userService.save(user);

        user.setName("newName");
        var result = userService.update(user);
        var updatedUser = userService.findAll().get(0);

        assertThat(result).isTrue();
        assertThat(updatedUser)
                .usingRecursiveComparison()
                .ignoringFields("friends", "subscriptions")
                .isEqualTo(user);
    }

    /**
     * Проверяет успешный сценарий удаления пользователя методом {@code delete}
     */
    @Test
    void whenDeleteThenGetEmpty() {
        var users = getUsers(1);
        var user = users.get(0);

        userService.save(user);

        var allUsers = userService.findAll();
        var userToDelete = allUsers.get(0);

        var result = userService.delete(userToDelete);

        var afterDelete = userService.findAll();
        assertThat(result).isTrue();
        assertThat(afterDelete).isEmpty();
    }
}