package com.manofsteel.gd.repository;

import com.manofsteel.gd.type.entity.OAuthUser;
import com.manofsteel.gd.type.entity.User;
import com.manofsteel.gd.type.etc.OAuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface OAuthUserRepository extends JpaRepository<OAuthUser, Long> {
	Optional<OAuthUser> findByProviderUserIdAndOap(String providerUserId, OAuthProvider oap);

	List<OAuthUser> findByUserId(Long userId);

    Optional<OAuthUser> findByUser(User user);
}
