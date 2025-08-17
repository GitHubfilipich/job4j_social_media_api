package ru.job4j.socialmediaapi.model;

import jakarta.persistence.*;
import lombok.*;

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
    private Integer id;

    private String email;

    private String password;

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
