package ru.job4j.socialmediaapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import ru.job4j.socialmediaapi.model.validation.ValidOperation;
import ru.job4j.socialmediaapi.service.post.PostService;

import java.util.List;

@Tag(name = "PostController", description = "PostController management APIs")
@Validated
@AllArgsConstructor
@RestController
@RequestMapping("/api/post")
public class PostController {
    private final PostService postService;

    @Operation(
            summary = "Retrieve a Post by id",
            description = "Get a Post object by specifying its id. "
                    + "The response is Post object with its fields.",
            tags = {"Post", "get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Post.class),
                    mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})})
    @GetMapping("/{id}")
    public ResponseEntity<Post> get(@PathVariable("id")
                                    @NotNull
                                    @Min(value = 1, message = "номер ресурса должен быть 1 и более")
                                    int id) {
        return postService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Retrieve a UserPostsDto by user's id",
            description = "Get a UserPostsDto object by specifying its user's id. "
                    + "The response is list of UserPostsDto objects with its fields.",
            tags = {"UserPostsDto", "get"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful retrieval",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserPostsDto.class))
                    )
            )
    })
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

    @Operation(
            summary = "Create a new Post",
            description = "Create a new Post object by specifying its fields. "
                    + "The response is Post object with its fields.",
            tags = {"Post", "post"})
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {@Content(schema = @Schema(implementation = Post.class),
                    mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})})
    @PostMapping
    @Validated(ValidOperation.OnCreate.class)
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

    @Operation(
            summary = "Update a Post",
            description = "Update a Post object by specifying its fields. "
                    + "The response is status 200.",
            tags = {"Post", "put"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})})
    @PutMapping
    @Validated(ValidOperation.OnUpdate.class)
    public ResponseEntity<Void> update(@Valid @RequestBody Post post) {
        if (postService.update(post)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Change a Post",
            description = "Change a Post object by specifying its fields. "
                    + "The response is status 200.",
            tags = {"Post", "patch"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})})
    @PatchMapping
    @Validated(ValidOperation.OnUpdate.class)
    public ResponseEntity<Void> change(@Valid @RequestBody Post post) {
        if (postService.update(post)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Remove a Post",
            description = "Remove a Post object by id. "
                    + "The response is status 200.",
            tags = {"Post", "delete"})
    @ApiResponses({
            @ApiResponse(responseCode = "204", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})})
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
