package ru.job4j.socialmediaapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.job4j.socialmediaapi.model.User;
import ru.job4j.socialmediaapi.model.validation.ValidOperation;
import ru.job4j.socialmediaapi.service.user.UserService;

@Tag(name = "UserController", description = "UserController management APIs")
@Validated
@AllArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Retrieve a User by userId",
            description = "Get a User object by specifying its userId. "
                    + "The response is User object with userId, username and date of created.",
            tags = {"User", "get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = User.class),
                    mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})})
    @GetMapping("/{id}")
    public ResponseEntity<User> get(@PathVariable("id")
                                    @NotNull
                                    @Min(value = 1, message = "номер ресурса должен быть 1 и более")
                                    int id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create a new User",
            description = "Create a new User object by specifying its fields. "
                    + "The response is User object with userId, username and date of created.",
            tags = {"User", "post"})
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {@Content(schema = @Schema(implementation = User.class),
                    mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})})
    @PostMapping
    @Validated(ValidOperation.OnCreate.class)
    public ResponseEntity<User> save(@Valid @RequestBody User user) {
        if (userService.save(user)) {
            var uri = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(user.getId())
                    .toUri();
            return ResponseEntity.status(HttpStatus.CREATED)
                    .location(uri)
                    .body(user);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Update a User",
            description = "Update a User object by specifying its fields. "
                    + "The response is status 200.",
            tags = {"User", "put"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})})
    @PutMapping
    @Validated(ValidOperation.OnUpdate.class)
    public ResponseEntity<Void> update(@Valid @RequestBody User user) {
        if (userService.update(user)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Change a User",
            description = "Change a User object by specifying its fields. "
                    + "The response is status 200.",
            tags = {"User", "patch"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})})
    @PatchMapping
    @Validated(ValidOperation.OnUpdate.class)
    public ResponseEntity<Void> change(@Valid @RequestBody User user) {
        if (userService.update(user)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Remove a User",
            description = "Remove a User object by id. "
                    + "The response is status 200.",
            tags = {"User", "delete"})
    @ApiResponses({
            @ApiResponse(responseCode = "204", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeById(@PathVariable("id")
                                           @NotNull
                                           @Min(value = 1, message = "номер ресурса должен быть 1 и более")
                                           int id) {
        if (userService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
