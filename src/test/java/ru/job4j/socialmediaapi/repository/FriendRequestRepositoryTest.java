package ru.job4j.socialmediaapi.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.job4j.socialmediaapi.model.FriendRequest;
import ru.job4j.socialmediaapi.model.User;
import ru.job4j.socialmediaapi.model.enums.RequestStatus;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class FriendRequestRepositoryTest {
    @Autowired
    private FriendRequestRepository friendRequestRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        friendRequestRepository.deleteAll();
        userRepository.deleteAll();
    }

    /**
     * Проверяет успешный сценарий сохранения данных методом {@code save}
     */
    @Test
    public void whenSaveThenGet() {

        var friendRequests = getFriendRequests(1);
        var friendRequest = friendRequests.get(0);

        friendRequestRepository.save(friendRequest);

        var result = friendRequestRepository.findAll();

        assertThat(result)
                .isNotEmpty()
                .hasSize(1)
                .first()
                .usingRecursiveComparison()
                .withComparatorForType(Comparator.comparing((LocalDateTime dt) -> dt.truncatedTo(ChronoUnit.SECONDS)),
                        LocalDateTime.class)
                .ignoringFields("author.friends", "author.subscriptions", "receiver.friends", "receiver.subscriptions")
                .isEqualTo(friendRequest);
    }

    private List<FriendRequest> getFriendRequests(int count) {
        var friendRequests = new ArrayList<FriendRequest>();
        for (int i = 0; i < count; i++) {
            var user1 = new User(null, "email1@email.com" + i, "password1" + i, "name1" + i, Set.of(), Set.of());
            userRepository.save(user1);
            var user2 = new User(null, "email2@email.com" + i, "password2" + i, "name2" + i, Set.of(), Set.of());
            userRepository.save(user2);
            var friendRequest = new FriendRequest(null, user1, user2, RequestStatus.PENDING, LocalDateTime.now());
            friendRequests.add(friendRequest);
        }
        return friendRequests;
    }

    /**
     * Проверяет успешный сценарий получения данных методом {@code findById}
     */
    @Test
    public void whenFindByIdThenGet() {

        var friendRequests = getFriendRequests(1);
        friendRequests.forEach(friendRequestRepository::save);
        var friendRequest = friendRequests.get(0);

        var result = friendRequestRepository.findById(friendRequest.getId());

        assertThat(result).isNotEmpty();
        assertThat(result.get())
                .usingRecursiveComparison()
                .withComparatorForType(Comparator.comparing((LocalDateTime dt) -> dt.truncatedTo(ChronoUnit.SECONDS)),
                        LocalDateTime.class)
                .ignoringFields("author.friends", "author.subscriptions", "receiver.friends", "receiver.subscriptions")
                .isEqualTo(friendRequest);
    }

    /**
     * Проверяет неуспешный сценарий получения данных методом {@code findById}
     */
    @Test
    public void whenFindByIdWithoutDataThenGetEmpty() {

        var result = friendRequestRepository.findById(1);

        assertThat(result).isEmpty();
    }

    /**
     * Проверяет успешный сценарий обновления данных методом {@code save}
     */
    @Test
    public void whenSaveModifiedThenGetModified() {
        var friendRequests = getFriendRequests(1);
        friendRequests.forEach(friendRequestRepository::save);
        var friendRequest = friendRequests.get(0);

        friendRequest.setStatus(RequestStatus.ACCEPTED);
        friendRequestRepository.save(friendRequest);

        var result = friendRequestRepository.findById(friendRequest.getId());

        assertThat(result).isNotEmpty();
        assertThat(result.get().getStatus()).isEqualTo(RequestStatus.ACCEPTED);
    }

    /**
     * Проверяет успешный сценарий удаления данных методом {@code delete}
     */
    @Test
    public void whenDeleteThenGetEmpty() {
        var friendRequests = getFriendRequests(1);
        friendRequests.forEach(friendRequestRepository::save);
        var friendRequest = friendRequests.get(0);

        friendRequestRepository.delete(friendRequest);

        var result = friendRequestRepository.findById(friendRequest.getId());

        assertThat(result).isEmpty();
    }

}