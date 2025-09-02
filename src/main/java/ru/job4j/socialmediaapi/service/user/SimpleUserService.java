package ru.job4j.socialmediaapi.service.user;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.job4j.socialmediaapi.dto.request.SignupRequestDTO;
import ru.job4j.socialmediaapi.dto.response.RegisterDTO;
import ru.job4j.socialmediaapi.model.Role;
import ru.job4j.socialmediaapi.model.User;
import ru.job4j.socialmediaapi.model.enums.ERole;
import ru.job4j.socialmediaapi.repository.RoleRepository;
import ru.job4j.socialmediaapi.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

@AllArgsConstructor
@Service
public class SimpleUserService implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private PasswordEncoder encoder;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public List<User> findAllWithFriendsAndSubscriptions() {
        return userRepository.findAllWithFriendsAndSubscriptions();
    }

    @Override
    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByIdWithFriendsAndSubscriptions(int id) {
        return userRepository.findByIdWithFriendsAndSubscriptions(id);
    }

    @Override
    public boolean save(User user) {
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean update(User user) {
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean delete(User user) {
        userRepository.delete(user);
        return true;
    }

    @Override
    public boolean deleteById(int id) {
        userRepository.deleteById(id);
        return true;
    }

    @Override
    public RegisterDTO signUp(SignupRequestDTO signUpRequest) {
        if (userRepository.existsByName(signUpRequest.getUsername())
                || userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new RegisterDTO(HttpStatus.BAD_REQUEST, "Error: Username or Email is already taken!");
        }

        User user = new User(null, signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getUsername(), new HashSet<>(), new HashSet<>(), new HashSet<>());

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();
        Supplier<RuntimeException> supplier = () -> new RuntimeException("Error: Role is not found.");

        if (strRoles == null) {
            roles.add(roleRepository.findByName(ERole.ROLE_USER).orElseThrow(supplier));
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin" -> roles.add(roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow(supplier));
                    case "mod" -> roles.add(roleRepository.findByName(ERole.ROLE_MODERATOR).orElseThrow(supplier));
                    default -> roles.add(roleRepository.findByName(ERole.ROLE_USER).orElseThrow(supplier));
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);
        return new RegisterDTO(HttpStatus.OK, "User registered successfully!");
    }
}
