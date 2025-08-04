package ru.job4j.socialmediaapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "media_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String email;

    private String name;

    @ManyToMany
    @JoinTable(
            name = "user_friend",
            joinColumns = {@JoinColumn(name = "friend_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private Set<User> friends;

    @ManyToMany
    @JoinTable(
            name = "user_subscription",
            joinColumns = {@JoinColumn(name = "publisher_id")},
            inverseJoinColumns = {@JoinColumn(name = "subscriber_id")}
    )
    private Set<User> subscriptions;
}
