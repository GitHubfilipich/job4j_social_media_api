package ru.job4j.socialmediaapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.job4j.socialmediaapi.model.Post;

import java.util.List;

@Getter
@AllArgsConstructor
@Schema(description = "UserPostsDto Model Information")
public class UserPostsDto {
    @Schema(description = "UserPostsDto ID", example = "1")
    private int userId;

    @Schema(description = "UserPostsDto content", example = "New content")
    private String username;

    @Schema(description = "UserPostsDto posts",
            example = "[{\"id\":1,\"author\":"
                    + "{\"id\":1,\"email\":\"user@email.com\",\"password\":\"pass\",\"name\":\"Ivan\"},"
                    + "\"title\":\"First post\",\"content\":\"Hello world!\",\"createdAt\":\"2024-01-01T10:00:00\"}]")
    private List<Post> posts;
}
