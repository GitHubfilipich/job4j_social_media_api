package ru.job4j.socialmediaapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.job4j.socialmediaapi.model.validation.Operation;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@ToString(exclude = {"images"})
@EqualsAndHashCode(exclude = {"images"})
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "ID не может быть null при обновлении",
            groups = {Operation.OnUpdate.class, Operation.OnDelete.class})
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @NotNull(message = "Автор поста не может быть null")
    private User author;

    @NotBlank(message = "Заголовок не может быть пустым")
    private String title;

    @NotBlank(message = "Содержимое не может быть пустым")
    private String content;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "post_id")
    private Set<Image> images;

    @NotNull(message = "Дата создания не может быть null",
            groups = {Operation.OnUpdate.class})
    private LocalDateTime createdAt;
}
