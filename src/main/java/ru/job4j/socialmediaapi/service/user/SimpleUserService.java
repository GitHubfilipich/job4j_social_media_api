package ru.job4j.socialmediaapi.service.user;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.job4j.socialmediaapi.model.User;
import ru.job4j.socialmediaapi.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class SimpleUserService implements UserService {

    @Autowired
    private UserRepository userRepository;

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
}
