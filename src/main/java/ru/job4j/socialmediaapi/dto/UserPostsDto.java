package ru.job4j.socialmediaapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.job4j.socialmediaapi.model.Post;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserPostsDto {
    private int userId;
    private String username;
    private List<Post> posts;
}
