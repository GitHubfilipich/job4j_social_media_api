package ru.job4j.socialmediaapi.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import ru.job4j.socialmediaapi.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends ListCrudRepository<User, Integer> {
    @Query("""
            select user from User user
            where user.email = :email and user.password = :password
            """)
    Optional<User> findByEmailAndPassword(@Param("email") String email, @Param("password") String password);

    @Query("""
            select user from User user
            join user.subscriptions s
            where s.id = :id
            """)
    List<User> findSubscribersByPublisherId(@Param("id") int id);

    @Query(value = """
            select u.id as id, u.email as email, u.password as password, u.name as name
            from media_user u
            join user_friend uf on u.id = uf.friend_id
            where uf.user_id = :id
            union
            select u.id as id, u.email as email, u.password as password, u.name as name
            from media_user u
            join user_friend uf on u.id = uf.user_id
            where uf.friend_id = :id
            """, nativeQuery = true)
    List<User> findFriendsByUserId(@Param("id") int id);

    @EntityGraph(attributePaths = {"friends", "subscriptions"})
    @Query("SELECT user FROM User user")
    List<User> findAllWithFriendsAndSubscriptions();

    @EntityGraph(attributePaths = {"friends", "subscriptions"})
    @Query("SELECT user FROM User user WHERE user.id = :id")
    Optional<User> findByIdWithFriendsAndSubscriptions(@Param("id") int id);
}