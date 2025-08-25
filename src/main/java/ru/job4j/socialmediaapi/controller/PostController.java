package ru.job4j.socialmediaapi.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.job4j.socialmediaapi.dto.UserPostsDto;
import ru.job4j.socialmediaapi.model.Post;
import ru.job4j.socialmediaapi.model.validation.Operation;
import ru.job4j.socialmediaapi.service.post.PostService;

import java.util.List;

@Validated
@AllArgsConstructor
@RestController
@RequestMapping("/api/post")
public class PostController {
    private final PostService postService;

    @GetMapping("/{id}")
    public ResponseEntity<Post> get(@PathVariable("id")
                                    @NotNull
                                    @Min(value = 1, message = "номер ресурса должен быть 1 и более")
                                    int id) {
        return postService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/by-users")
    public ResponseEntity<List<UserPostsDto>> getPostsByUsers(
            @RequestParam("id")
            @NotEmpty(message = "список id пользователей не может быть пустым")
            List<
                @NotNull(message = "id пользователя не может быть null")
                @Min(value = 1, message = "id пользователя должен быть 1 и более")
                Integer
            > ids
    ) {
        List<UserPostsDto> postsDtos = postService.getUserPostsDtosByUsers(ids);
        return ResponseEntity.ok(postsDtos);
    }

    @PostMapping
    @Validated(Operation.OnCreate.class)
    public ResponseEntity<Post> save(@Valid @RequestBody Post post) {
        if (postService.save(post)) {
            var uri = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(post.getId())
                    .toUri();
            return ResponseEntity.status(HttpStatus.CREATED)
                    .location(uri)
                    .body(post);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping
    @Validated(Operation.OnUpdate.class)
    public ResponseEntity<Void> update(@Valid @RequestBody Post post) {
        if (postService.update(post)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping
    @Validated(Operation.OnUpdate.class)
    public ResponseEntity<Void> change(@Valid @RequestBody Post post) {
        if (postService.update(post)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeById(@PathVariable("id")
                                           @NotNull
                                           @Min(value = 1, message = "номер ресурса должен быть 1 и более")
                                           int id) {
        if (postService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
