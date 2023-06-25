package com.manofsteel.gd.service;

import com.manofsteel.gd.exception.NotFoundException;
import com.manofsteel.gd.repository.FollowRepository;
import com.manofsteel.gd.repository.UserRepository;
import com.manofsteel.gd.type.dto.UserDto;
import com.manofsteel.gd.type.entity.Follow;
import com.manofsteel.gd.type.entity.User;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FollowService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final ModelMapper modelMapper;

    public FollowService(UserRepository userRepository, FollowRepository followRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.followRepository = followRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public void follow(Long followerId ,Long followingId) {
        User follower = userRepository.findByUserId(followerId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        User following = userRepository.findByUserId(followingId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (followRepository.findByFollowerAndFollowing(follower, following).isPresent()) {
            throw new IllegalStateException("Already following");
        }

        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowing(following);
        followRepository.save(follow);

        follower.setFollowCount(follower.getFollowCount() + 1);
        following.setFollowerCount(following.getFollowerCount() + 1);

    }

    @Transactional
    public void unfollow(Long followerId, Long followingId) {
        User follower = userRepository.findByUserId(followerId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        User following = userRepository.findByUserId(followingId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Follow follow = followRepository.findByFollowerAndFollowing(follower, following)
                .orElseThrow(() -> new NotFoundException("팔로우한 사람이 아닙니다."));

        followRepository.delete(follow);

        follower.setFollowCount(follower.getFollowCount() - 1);
        following.setFollowerCount(following.getFollowerCount() - 1);

    }

    public List<User> getFollowersForUser(Long userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        return followRepository.findByFollowing(user).stream()
                .map(follow -> modelMapper.map(follow.getFollower(), User.class))
                .collect(Collectors.toList());
    }

    public List<User> getFollowingForUser(Long userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        return followRepository.findByFollower(user).stream()
                .map(follow -> modelMapper.map(follow.getFollowing(), User.class))
                .collect(Collectors.toList());
    }

    public Boolean isFollow(User user, User following) {

       return followRepository.existsByFollowerIdAndFollowingId(user.getUserId(), following.getUserId());
    }


    public void FollowUserDelete(Long userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        List<Follow> followingList = followRepository.findByFollowing(user);
        List<Follow> followerList = followRepository.findByFollower(user);

        for(Follow follow :  followingList) {
            User following =  follow.getFollower();
            following.setFollowCount(following.getFollowCount() - 1);
            userRepository.save(following);

        }
        for(Follow follow :  followerList) {
            User follower =  follow.getFollowing();
            follower.setFollowerCount(follower.getFollowerCount() - 1);
            userRepository.save(follower);
        }
    }

}
