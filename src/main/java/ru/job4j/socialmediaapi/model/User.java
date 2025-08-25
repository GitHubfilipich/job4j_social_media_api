package ru.job4j.socialmediaapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.validation.annotation.Validated;
import ru.job4j.socialmediaapi.model.validation.Operation;

import java.util.Set;

@Data
@ToString(exclude = {"friends", "subscriptions"})
@EqualsAndHashCode(exclude = {"friends", "subscriptions"})
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "media_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "ID не может быть null при обновлении",
            groups = {Operation.OnUpdate.class, Operation.OnDelete.class})
    private Integer id;

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Email должен быть валидным")
    private String email;

    @NotBlank(message = "Пароль не может быть пустым")
    private String password;

    @NotBlank(message = "Имя не может быть пустым")
    private String name;

    @ManyToMany
    @JoinTable(
            name = "user_friend",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "friend_id")}
    )
    private Set<User> friends;

    @ManyToMany
    @JoinTable(
            name = "user_subscription",
            joinColumns = {@JoinColumn(name = "subscriber_id")},
            inverseJoinColumns = {@JoinColumn(name = "publisher_id")}
    )
    private Set<User> subscriptions;
}
