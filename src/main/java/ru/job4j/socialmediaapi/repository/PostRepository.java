package ru.job4j.socialmediaapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import ru.job4j.socialmediaapi.model.Post;
import ru.job4j.socialmediaapi.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends ListCrudRepository<Post, Integer> {
    List<Post> findByAuthor(User author);

    List<Post> findByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(LocalDateTime start, LocalDateTime end);

    Page<Post> findByOrderByCreatedAtDesc(Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("""
            update Post post set post.title = :title, post.content = :content
            where post.id = :id
            """)
    int updatePostTitleAndContent(@Param("title") String title, @Param("content") String content,
                                  @Param("id") int id);

    @Modifying(clearAutomatically = true)
    @Query("""
            delete from Post post where post.id = :id
            """)
    int deletePostById(@Param("id") int id);

    @Query("""
            select post from Post post
            join post.author a
            join a.subscriptions s
            where s.id = :id
            order by post.createdAt desc
            """)
    List<Post> findByUserSubscribersSortedByCreatedAtDescPaged(@Param("id") int id, Pageable pageable);
}