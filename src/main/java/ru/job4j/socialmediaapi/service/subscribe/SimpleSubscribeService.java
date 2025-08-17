package ru.job4j.socialmediaapi.service.subscribe;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.job4j.socialmediaapi.model.FriendRequest;
import ru.job4j.socialmediaapi.model.User;
import ru.job4j.socialmediaapi.model.enums.RequestStatus;
import ru.job4j.socialmediaapi.service.friendrequest.FriendRequestService;
import ru.job4j.socialmediaapi.service.user.UserService;

@AllArgsConstructor
@Service
public class SimpleSubscribeService implements SubscribeService {

    @Autowired
    private UserService userService;

    @Autowired
    private FriendRequestService friendRequestService;

    @Override
    public boolean subscribe(User subscriber, User publisher) {
        subscriber.getSubscriptions().add(publisher);
        return true;
    }

    @Override
    public boolean unsubscribe(User subscriber, User publisher) {
        subscriber.getSubscriptions().remove(publisher);
        return true;
    }

    @Override
    public boolean sendFriendRequest(User sender, User receiver) {
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setAuthor(sender);
        friendRequest.setReceiver(receiver);
        friendRequest.setStatus(RequestStatus.PENDING);
        friendRequestService.save(friendRequest);

        sender.getSubscriptions().add(receiver);
        return true;
    }

    @Override
    public boolean acceptFriendRequest(FriendRequest friendRequest) {
        friendRequest.setStatus(RequestStatus.ACCEPTED);

        User author = userService.findByIdWithFriendsAndSubscriptions(friendRequest.getAuthor().getId())
                .orElseThrow();
        User receiver = userService.findByIdWithFriendsAndSubscriptions(friendRequest.getReceiver().getId())
                .orElseThrow();

        author.getFriends().add(receiver);
        userService.update(author);

        receiver.getFriends().add(author);
        receiver.getSubscriptions().add(author);
        userService.update(receiver);

        return true;
    }

    @Override
    public boolean rejectFriendRequest(FriendRequest friendRequest) {
        friendRequest.setStatus(RequestStatus.REJECTED);
        return friendRequestService.update(friendRequest);
    }

    @Override
    public boolean deleteFriend(User user, User friend) {
        user.getFriends().remove(friend);
        user.getSubscriptions().remove(friend);
        friend.getFriends().remove(user);
        return true;
    }
}
