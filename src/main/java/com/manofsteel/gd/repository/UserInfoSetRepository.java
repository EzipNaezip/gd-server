package com.manofsteel.gd.repository;

import com.manofsteel.gd.type.entity.UserInfoSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoSetRepository extends JpaRepository<UserInfoSet, Long> {
}
