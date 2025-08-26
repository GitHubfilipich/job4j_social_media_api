package ru.job4j.socialmediaapi.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.job4j.socialmediaapi.model.validation.ValidOperation;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "message")
@Schema(description = "Message Model Information")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "ID не может быть null при обновлении",
            groups = {ValidOperation.OnUpdate.class, ValidOperation.OnDelete.class})
    @Schema(description = "Message ID", example = "1")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @NotNull(message = "Автор сообщения не может быть null")
    @Schema(description = "Message author",
            example = "{\"id\":2,\"email\":\"friend@email.com\",\"password\":\"pass123\",\"name\":\"Petr\"}")
    private User author;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    @NotNull(message = "Получатель сообщения не может быть null")
    @Schema(description = "Message receiver",
            example = "{\"id\":2,\"email\":\"friend@email.com\",\"password\":\"pass123\",\"name\":\"Petr\"}")
    private User receiver;

    @NotBlank(message = "Содержимое не может быть пустым")
    @Schema(description = "Message content", example = "New content")
    private String content;

    @NotNull(message = "Дата создания не может быть null",
            groups = {ValidOperation.OnUpdate.class})
    @Schema(description = "Message creation date", example = "2024-01-01T10:00:00")
    private LocalDateTime createdAt;
}
