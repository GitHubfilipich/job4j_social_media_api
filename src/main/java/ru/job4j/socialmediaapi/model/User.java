package ru.job4j.socialmediaapi.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.job4j.socialmediaapi.model.validation.ValidOperation;

import java.util.Set;

@Data
@ToString(exclude = {"friends", "subscriptions", "roles"})
@EqualsAndHashCode(exclude = {"friends", "subscriptions", "roles"})
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "media_user")
@Schema(description = "User Model Information")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "ID не может быть null при обновлении",
            groups = {ValidOperation.OnUpdate.class, ValidOperation.OnDelete.class})
    @Schema(description = "User ID", example = "1")
    private Integer id;

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Email должен быть валидным")
    @Schema(description = "User email", example = "email@email.com")
    private String email;

    @NotBlank(message = "Пароль не может быть пустым")
    @Schema(description = "User password", example = "qwerty123")
    private String password;

    @NotBlank(message = "Имя не может быть пустым")
    @Schema(description = "User name", example = "Ivan")
    private String name;

    @ManyToMany
    @JoinTable(
            name = "user_friend",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "friend_id")}
    )
    @Schema(description = "User friends",
            example = "[{\"id\":2,\"email\":\"friend@email.com\",\"password\":\"pass123\",\"name\":\"Petr\"}, "
                    + "{\"id\":3,\"email\":\"friend2@email.com\",\"password\":\"pass1234\",\"name\":\"Nick\"}]")
    private Set<User> friends;

    @ManyToMany
    @JoinTable(
            name = "user_subscription",
            joinColumns = {@JoinColumn(name = "subscriber_id")},
            inverseJoinColumns = {@JoinColumn(name = "publisher_id")}
    )
    @Schema(description = "User subscriptions",
            example = "[{\"id\":4,\"email\":\"sub@email.com\",\"password\":\"pass456\",\"name\":\"Olga\"}, "
                    + "{\"id\":5,\"email\":\"friend3@email.com\",\"password\":\"pass12345\",\"name\":\"John\"}]")
    private Set<User> subscriptions;

    @ManyToMany
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;
}
