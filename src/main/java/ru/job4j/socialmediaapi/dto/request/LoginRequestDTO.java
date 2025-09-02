package ru.job4j.socialmediaapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
