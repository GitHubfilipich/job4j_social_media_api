package ru.job4j.socialmediaapi.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.job4j.socialmediaapi.model.Message;
import ru.job4j.socialmediaapi.model.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MessageRepositoryTest {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        messageRepository.deleteAll();
        userRepository.deleteAll();
    }

    private List<Message> getMessages(int count) {
        var messages = new ArrayList<Message>();
        for (int i = 0; i < count; i++) {
            var author = new User(null, "author" + i + "@mail.com", "pass" + i, "author" + i, Set.of(), Set.of());
            var receiver = new User(null, "receiver" + i + "@mail.com", "pass" + i, "receiver" + i, Set.of(), Set.of());
            userRepository.save(author);
            userRepository.save(receiver);
            var message = new Message(null, author, receiver, "content" + i, LocalDateTime.now());
            messages.add(message);
        }
        return messages;
    }

    /**
     * Проверяет успешный сценарий сохранения данных методом {@code save}
     */
    @Test
    public void whenSaveThenGet() {
        var messages = getMessages(1);
        var message = messages.get(0);

        messageRepository.save(message);

        var result = messageRepository.findAll();

        assertThat(result)
                .isNotEmpty()
                .hasSize(1)
                .first()
                .usingRecursiveComparison()
                .withComparatorForType(Comparator.comparing((LocalDateTime dt) -> dt.truncatedTo(ChronoUnit.SECONDS)),
                        LocalDateTime.class)
                .ignoringFields("author.friends", "author.subscriptions", "receiver.friends", "receiver.subscriptions")
                .isEqualTo(message);
    }

    /**
     * Проверяет успешный сценарий получения данных методом {@code findById}
     */
    @Test
    public void whenFindByIdThenGet() {
        var messages = getMessages(1);
        messages.forEach(messageRepository::save);
        var message = messages.get(0);

        var result = messageRepository.findById(message.getId());

        assertThat(result).isNotEmpty();
        assertThat(result.get())
                .usingRecursiveComparison()
                .withComparatorForType(Comparator.comparing((LocalDateTime dt) -> dt.truncatedTo(ChronoUnit.SECONDS)),
                        LocalDateTime.class)
                .ignoringFields("author.friends", "author.subscriptions", "receiver.friends", "receiver.subscriptions")
                .isEqualTo(message);
    }

    /**
     * Проверяет неуспешный сценарий получения данных методом {@code findById}
     */
    @Test
    public void whenFindByIdWithoutDataThenGetEmpty() {
        var result = messageRepository.findById(1);

        assertThat(result).isEmpty();
    }

    /**
     * Проверяет успешный сценарий обновления данных методом {@code save}
     */
    @Test
    public void whenSaveModifiedThenGetModified() {
        var messages = getMessages(1);
        messages.forEach(messageRepository::save);
        var message = messages.get(0);

        message.setContent("new content");
        messageRepository.save(message);

        var result = messageRepository.findById(message.getId());

        assertThat(result).isNotEmpty();
        assertThat(result.get().getContent()).isEqualTo("new content");
    }

    /**
     * Проверяет успешный сценарий удаления данных методом {@code delete}
     */
    @Test
    public void whenDeleteThenGetEmpty() {
        var messages = getMessages(1);
        messages.forEach(messageRepository::save);
        var message = messages.get(0);

        messageRepository.delete(message);

        var result = messageRepository.findById(message.getId());

        assertThat(result).isEmpty();
    }
}