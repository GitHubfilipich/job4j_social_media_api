package ru.job4j.socialmediaapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.job4j.socialmediaapi.model.validation.Operation;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "image")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "ID не может быть null при обновлении",
            groups = {Operation.OnUpdate.class, Operation.OnDelete.class})
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    @NotNull(message = "Пост не может быть null")
    private Post post;

    @NotBlank(message = "Имя не может быть пустым")
    private String name;

    @NotBlank(message = "URL не может быть пустым")
    private String url;
}
