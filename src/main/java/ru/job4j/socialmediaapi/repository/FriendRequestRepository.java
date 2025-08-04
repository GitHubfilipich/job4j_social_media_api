package ru.job4j.socialmediaapi.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.socialmediaapi.model.FriendRequest;

public interface FriendRequestRepository extends CrudRepository<FriendRequest, Integer> {
}