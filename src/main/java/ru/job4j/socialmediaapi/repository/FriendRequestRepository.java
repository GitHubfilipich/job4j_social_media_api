package ru.job4j.socialmediaapi.repository;

import org.springframework.data.repository.ListCrudRepository;
import ru.job4j.socialmediaapi.model.FriendRequest;

public interface FriendRequestRepository extends ListCrudRepository<FriendRequest, Integer> {
}