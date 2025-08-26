package ru.job4j.socialmediaapi.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.job4j.socialmediaapi.model.validation.ValidOperation;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "image")
@Schema(description = "Image Model Information")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "ID не может быть null при обновлении",
            groups = {ValidOperation.OnUpdate.class, ValidOperation.OnDelete.class})
    @Schema(description = "Image ID", example = "1")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    @NotNull(message = "Пост не может быть null")
    @Schema(description = "Image post",
            example = "{\"id\":1,\"author\":"
                    + "{\"id\":2,\"email\":\"friend@email.com\",\"password\":\"pass123\",\"name\":\"Petr\"},"
                    + "\"title\":\"New post\",\"content\":\"New content\",\"createdAt\":\"2024-01-01T10:00:00\"}")
    private Post post;

    @NotBlank(message = "Имя не может быть пустым")
    @Schema(description = "Image name", example = "image1.jpg")
    private String name;

    @NotBlank(message = "URL не может быть пустым")
    @Schema(description = "Image URL", example = "http://example.com/image1.jpg")
    private String url;
}
