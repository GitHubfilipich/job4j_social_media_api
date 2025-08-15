package ru.job4j.socialmediaapi.service.friendrequest;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.socialmediaapi.model.FriendRequest;
import ru.job4j.socialmediaapi.model.enums.RequestStatus;
import ru.job4j.socialmediaapi.repository.FriendRequestRepository;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class SimpleFriendRequestService implements FriendRequestService {

    private FriendRequestRepository friendRequestRepository;

    @Override
    public boolean save(FriendRequest friendRequest) {
        friendRequest.setCreatedAt(LocalDateTime.now());
        friendRequest.setStatus(RequestStatus.PENDING);
        friendRequestRepository.save(friendRequest);
        return true;
    }

    @Override
    public boolean update(FriendRequest friendRequest) {
        friendRequestRepository.save(friendRequest);
        return true;
    }

    @Override
    public boolean delete(FriendRequest friendRequest) {
        friendRequestRepository.delete(friendRequest);
        return true;
    }

    @Override
    public void deleteAll() {
        friendRequestRepository.deleteAll();
    }

    @Override
    public List<FriendRequest> findAll() {
        return friendRequestRepository.findAll();
    }
}
