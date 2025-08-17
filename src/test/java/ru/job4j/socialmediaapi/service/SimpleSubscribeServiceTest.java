package ru.job4j.socialmediaapi.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.socialmediaapi.model.User;
import ru.job4j.socialmediaapi.model.enums.RequestStatus;
import ru.job4j.socialmediaapi.service.friendrequest.FriendRequestService;
import ru.job4j.socialmediaapi.service.subscribe.SubscribeService;
import ru.job4j.socialmediaapi.service.user.UserService;

import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SimpleSubscribeServiceTest {

    @Autowired
    private SubscribeService subscribeService;

    @Autowired
    private UserService userService;

    @Autowired
    private FriendRequestService friendRequestService;

    @BeforeEach
    public void setUp() {
        clearTables();
    }

    @AfterAll
    public void clear() {
        clearTables();
    }

    private void clearTables() {
        friendRequestService.findAll().forEach(friendRequestService::delete);

        var users = userService.findAllWithFriendsAndSubscriptions();
        users.forEach(user -> {
            user.getFriends().clear();
            user.getSubscriptions().clear();
            userService.update(user);
        });

        users = userService.findAll();
        users.forEach(userService::delete);
    }

    private List<User> getUsers(int count) {
        var users = new java.util.ArrayList<User>();
        for (int i = 0; i < count; i++) {
            var user = new User(null, "email" + i, "password" + i, "name" + i, new HashSet<>(), new HashSet<>());
            userService.save(user);
            users.add(user);
        }
        return users;
    }

    /**
     * Проверяет успешный сценарий подписки пользователя на другого пользователя методом {@code subscribe}
     */
    @Test
    void whenSubscribeThenGetSubscription() {
        var users = getUsers(2);
        var subscriber = users.get(0);
        var publisher = users.get(1);

        var result = subscribeService.subscribe(subscriber, publisher);

        assertThat(result).isTrue();
        assertThat(subscriber.getSubscriptions()).contains(publisher);
    }

    /**
     * Проверяет успешный сценарий отписки пользователя от другого пользователя методом {@code unsubscribe}
     */
    @Test
    void whenUnsubscribeThenNoSubscription() {
        var users = getUsers(2);
        var subscriber = users.get(0);
        var publisher = users.get(1);

        subscribeService.subscribe(subscriber, publisher);
        var result = subscribeService.unsubscribe(subscriber, publisher);

        assertThat(result).isTrue();
        assertThat(subscriber.getSubscriptions()).doesNotContain(publisher);
    }

    /**
     * Проверяет успешный сценарий отправки заявки в друзья методом {@code sendFriendRequest}
     */
    @Test
    void whenSendFriendRequestThenRequestCreated() {
        var users = getUsers(2);
        var sender = users.get(0);
        var receiver = users.get(1);

        var result = subscribeService.sendFriendRequest(sender, receiver);

        var requests = friendRequestService.findAll();
        assertThat(result).isTrue();
        assertThat(requests)
                .isNotEmpty()
                .hasSize(1)
                .first()
                .satisfies(r -> {
                    assertThat(r.getAuthor()).isEqualTo(sender);
                    assertThat(r.getReceiver()).isEqualTo(receiver);
                    assertThat(r.getStatus()).isEqualTo(RequestStatus.PENDING);
                });
        assertThat(sender.getSubscriptions()).contains(receiver);
    }

    /**
     * Проверяет успешный сценарий принятия заявки в друзья методом {@code acceptFriendRequest}
     */
    @Test
    void whenAcceptFriendRequestThenUsersAreFriends() {
        var users = getUsers(2);
        var sender = users.get(0);
        var receiver = users.get(1);

        subscribeService.sendFriendRequest(sender, receiver);
        var request = friendRequestService.findAll().get(0);

        var result = subscribeService.acceptFriendRequest(request);
        sender = userService.findByIdWithFriendsAndSubscriptions(sender.getId()).orElseThrow();
        receiver = userService.findByIdWithFriendsAndSubscriptions(receiver.getId()).orElseThrow();

        assertThat(result).isTrue();
        assertThat(request.getStatus()).isEqualTo(RequestStatus.ACCEPTED);

        assertThat(sender.getFriends()).contains(receiver);
        assertThat(receiver.getFriends()).contains(sender);
        assertThat(receiver.getSubscriptions()).contains(sender);
    }

    /**
     * Проверяет успешный сценарий отклонения заявки в друзья методом {@code rejectFriendRequest}
     */
    @Test
    void whenRejectFriendRequestThenStatusRejected() {
        var users = getUsers(2);
        var sender = users.get(0);
        var receiver = users.get(1);

        subscribeService.sendFriendRequest(sender, receiver);
        var request = friendRequestService.findAll().get(0);

        var result = subscribeService.rejectFriendRequest(request);

        assertThat(result).isTrue();
        assertThat(request.getStatus()).isEqualTo(RequestStatus.REJECTED);
    }

    /**
     * Проверяет успешный сценарий удаления друга методом {@code deleteFriend}
     */
    @Test
    void whenDeleteFriendThenNoFriendRelation() {
        var users = getUsers(2);
        var user = users.get(0);
        var friend = users.get(1);

        user.getFriends().add(friend);
        user.getSubscriptions().add(friend);
        userService.update(user);

        friend.getFriends().add(user);
        friend.getSubscriptions().add(user);
        userService.update(friend);

        var result = subscribeService.deleteFriend(user, friend);

        assertThat(result).isTrue();
        assertThat(user.getFriends()).doesNotContain(friend);
        assertThat(user.getSubscriptions()).doesNotContain(friend);
        assertThat(friend.getFriends()).doesNotContain(user);
        assertThat(friend.getSubscriptions()).contains(user);
    }
}