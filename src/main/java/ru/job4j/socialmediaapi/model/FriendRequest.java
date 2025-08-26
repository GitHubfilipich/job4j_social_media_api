package ru.job4j.socialmediaapi.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.job4j.socialmediaapi.model.enums.RequestStatus;
import ru.job4j.socialmediaapi.model.validation.ValidOperation;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "friend_request")
@Schema(description = "FriendRequest Model Information")
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "ID не может быть null при обновлении",
            groups = {ValidOperation.OnUpdate.class, ValidOperation.OnDelete.class})
    @Schema(description = "FriendRequest ID", example = "1")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @NotNull(message = "Автор запроса не может быть null")
    @Schema(description = "FriendRequest author",
            example = "{\"id\":2,\"email\":\"friend@email.com\",\"password\":\"pass123\",\"name\":\"Petr\"}")
    private User author;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    @NotNull(message = "Получатель запроса не может быть null")
    @Schema(description = "FriendRequest receiver",
            example = "{\"id\":2,\"email\":\"friend@email.com\",\"password\":\"pass123\",\"name\":\"Petr\"}")
    private User receiver;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Статус запроса не может быть null")
    @Schema(description = "FriendRequest status", example = "RequestStatus.PENDING")
    private RequestStatus status;

    @NotNull(message = "Дата создания не может быть null",
            groups = {ValidOperation.OnUpdate.class})
    @Schema(description = "FriendRequest creation date", example = "2024-01-01T10:00:00")
    private LocalDateTime createdAt;
}
