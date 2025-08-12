package ru.job4j.socialmediaapi.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import ru.job4j.socialmediaapi.model.Image;

public interface ImageRepository extends ListCrudRepository<Image, Integer> {
    @Modifying(clearAutomatically = true)
    @Query("""
            delete from Image image where image.post.id = :id
            """)
    int deletePostImage(@Param("id") int id);
}