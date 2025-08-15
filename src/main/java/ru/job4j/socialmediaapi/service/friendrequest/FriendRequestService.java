package ru.job4j.socialmediaapi.service.friendrequest;

import ru.job4j.socialmediaapi.model.FriendRequest;

import java.util.List;

public interface FriendRequestService {
    boolean save(FriendRequest friendRequest);

    boolean update(FriendRequest friendRequest);

    boolean delete(FriendRequest friendRequest);

    void deleteAll();

    List<FriendRequest> findAll();
}
