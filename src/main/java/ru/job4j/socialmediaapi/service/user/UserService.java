package ru.job4j.socialmediaapi.service.user;

import org.springframework.transaction.annotation.Transactional;
import ru.job4j.socialmediaapi.dto.request.SignupRequestDTO;
import ru.job4j.socialmediaapi.dto.response.RegisterDTO;
import ru.job4j.socialmediaapi.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findAll();

    List<User> findAllWithFriendsAndSubscriptions();

    Optional<User> findById(int id);

    @Transactional
    boolean save(User user);

    @Transactional
    boolean update(User user);

    @Transactional
    boolean delete(User user);

    Optional<User> findByIdWithFriendsAndSubscriptions(int id);

    boolean deleteById(int id);

    RegisterDTO signUp(SignupRequestDTO signUpRequest);
}
