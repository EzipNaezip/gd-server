package com.manofsteel.gd.auth.service;

import com.manofsteel.gd.repository.UserRepository;
import com.manofsteel.gd.type.dto.UserPrincipal;
import com.manofsteel.gd.type.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findById(Long.parseLong(username));
		log.debug("User Data : {}", user);
		if (!user.isPresent()) {
			throw new UsernameNotFoundException(username);
		}
		return UserPrincipal.create(user.get());
	}

}
