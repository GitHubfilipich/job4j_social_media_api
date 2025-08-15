package ru.job4j.socialmediaapi.service.subscribe;

import org.springframework.transaction.annotation.Transactional;
import ru.job4j.socialmediaapi.model.FriendRequest;
import ru.job4j.socialmediaapi.model.User;

public interface SubscribeService {

    @Transactional
    boolean subscribe(User subscriber, User publisher);

    @Transactional
    boolean unsubscribe(User subscriber, User publisher);

    @Transactional
    boolean sendFriendRequest(User sender, User receiver);

    @Transactional
    boolean acceptFriendRequest(FriendRequest friendRequest);

    boolean rejectFriendRequest(FriendRequest friendRequest);

    @Transactional
    boolean deleteFriend(User user, User friend);

}
