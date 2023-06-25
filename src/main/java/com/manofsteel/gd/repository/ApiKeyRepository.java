package com.manofsteel.gd.repository;

import com.manofsteel.gd.type.entity.ApiKey;
import com.manofsteel.gd.type.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {

    ApiKey findByUser(User user);


  //  Optional<ApiKey> findByUser(Long aLong);

    void deleteByUser(User user);
}