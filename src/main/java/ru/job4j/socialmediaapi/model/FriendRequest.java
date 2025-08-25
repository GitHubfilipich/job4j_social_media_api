package ru.job4j.socialmediaapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.job4j.socialmediaapi.model.enums.RequestStatus;
import ru.job4j.socialmediaapi.model.validation.Operation;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "friend_request")
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "ID не может быть null при обновлении",
            groups = {Operation.OnUpdate.class, Operation.OnDelete.class})
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @NotNull(message = "Автор запроса не может быть null")
    private User author;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    @NotNull(message = "Получатель запроса не может быть null")
    private User receiver;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Статус запроса не может быть null")
    private RequestStatus status;

    @NotNull(message = "Дата создания не может быть null",
            groups = {Operation.OnUpdate.class})
    private LocalDateTime createdAt;
}
