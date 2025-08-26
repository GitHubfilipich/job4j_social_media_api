package ru.job4j.socialmediaapi.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.job4j.socialmediaapi.model.validation.ValidOperation;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@ToString(exclude = {"images"})
@EqualsAndHashCode(exclude = {"images"})
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "post")
@Schema(description = "Post Model Information")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "ID не может быть null при обновлении",
            groups = {ValidOperation.OnUpdate.class, ValidOperation.OnDelete.class})
    @Schema(description = "Post ID", example = "1")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @NotNull(message = "Автор поста не может быть null")
    @Schema(description = "Post author",
            example = "{\"id\":2,\"email\":\"friend@email.com\",\"password\":\"pass123\",\"name\":\"Petr\"}")
    private User author;

    @NotBlank(message = "Заголовок не может быть пустым")
    @Schema(description = "Post title", example = "New post")
    private String title;

    @NotBlank(message = "Содержимое не может быть пустым")
    @Schema(description = "Post content", example = "New content")
    private String content;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "post_id")
    @Schema(
        description = "Post images",
        example = "[{\"id\":1,\"post\":{\"id\":1},\"name\":\"image1.jpg\",\"url\":\"http://example.com/image1.jpg\"},"
                + "{\"id\":2,\"post\":{\"id\":1},\"name\":\"image2.png\",\"url\":\"http://example.com/image2.png\"}]"
    )
    private Set<Image> images;

    @NotNull(message = "Дата создания не может быть null",
            groups = {ValidOperation.OnUpdate.class})
    @Schema(description = "Post creation date", example = "2024-01-01T10:00:00")
    private LocalDateTime createdAt;
}
