package com.manofsteel.gd.repository;

import com.manofsteel.gd.type.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, QUserRepository{

    Optional<User> findByUserId(Long userId);



    Optional<User> findByName(String name);

    Optional<User> findByEmail(String email);

    List<User> findAllByEmail(String email);
}
