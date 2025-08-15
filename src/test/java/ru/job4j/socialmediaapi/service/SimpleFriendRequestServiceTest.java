package ru.job4j.socialmediaapi.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.socialmediaapi.model.FriendRequest;
import ru.job4j.socialmediaapi.model.User;
import ru.job4j.socialmediaapi.model.enums.RequestStatus;
import ru.job4j.socialmediaapi.service.friendrequest.SimpleFriendRequestService;
import ru.job4j.socialmediaapi.service.user.UserService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SimpleFriendRequestServiceTest {

    @Autowired
    private SimpleFriendRequestService friendRequestService;

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
        friendRequestService.deleteAll();
        var users = userService.findAll();
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
            var user = new User(null, "email" + i, "password" + i, "name" + i, new HashSet<>(), new HashSet<>());
            users.add(user);
        }
        return users;
    }

    /**
     * Проверяет успешный сценарий сохранения данных методом {@code save}
     */
    @Test
    void whenSaveThenGetData() {
        var users = getUsers(2);
        users.forEach(userService::save);

        var friendRequest = new FriendRequest();
        friendRequest.setAuthor(users.get(0));
        friendRequest.setReceiver(users.get(1));

        var result = friendRequestService.save(friendRequest);
        var requests = friendRequestService.findAll();

        assertThat(result).isTrue();
        assertThat(requests)
                .isNotEmpty()
                .hasSize(1)
                .first()
                .usingRecursiveComparison()
                .ignoringFields("author.friends", "author.subscriptions", "receiver.friends", "receiver.subscriptions")
                .withComparatorForType(Comparator.comparing((LocalDateTime dt) -> dt.truncatedTo(ChronoUnit.SECONDS)),
                        LocalDateTime.class)
                .isEqualTo(friendRequest);
    }

    /**
     * Проверяет успешный сценарий обновления заявки в друзья методом {@code update}
     */
    @Test
    void whenUpdateThenGetModifiedData() {
        var users = getUsers(2);
        users.forEach(userService::save);

        var friendRequest = new FriendRequest();
        friendRequest.setAuthor(users.get(0));
        friendRequest.setReceiver(users.get(1));
        friendRequestService.save(friendRequest);

        friendRequest.setStatus(RequestStatus.ACCEPTED);
        var result = friendRequestService.update(friendRequest);
        var updatedRequest = friendRequestService.findAll().get(0);

        assertThat(result).isTrue();
        assertThat(updatedRequest)
                .usingRecursiveComparison()
                .withComparatorForType(Comparator.comparing((LocalDateTime dt) -> dt.truncatedTo(ChronoUnit.SECONDS)),
                        LocalDateTime.class)
                .ignoringFields("author.friends", "author.subscriptions", "images")
                .isEqualTo(friendRequest);
    }

    /**
     * Проверяет успешный сценарий удаления заявки в друзья методом {@code delete}
     */
    @Test
    void whenDeleteThenGetEmpty() {
        var users = getUsers(2);
        users.forEach(userService::save);

        var friendRequest = new FriendRequest();
        friendRequest.setAuthor(users.get(0));
        friendRequest.setReceiver(users.get(1));
        friendRequestService.save(friendRequest);

        var requests = friendRequestService.findAll();
        var requestToDelete = requests.get(0);

        var result = friendRequestService.delete(requestToDelete);

        var afterDelete = friendRequestService.findAll();
        assertThat(result).isTrue();
        assertThat(afterDelete).isEmpty();
    }
}