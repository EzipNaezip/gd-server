package com.manofsteel.gd.repository;

import com.manofsteel.gd.type.entity.User;

import java.util.List;
import java.util.Optional;

public interface QUserRepository {
    List<User> findAll(Long start, Long display);

    Optional<User> findByUserId(Long userId);
}
