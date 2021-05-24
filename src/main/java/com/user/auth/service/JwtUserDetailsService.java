package com.user.auth.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.user.auth.model.DAOUser;
import com.user.auth.model.UserDTO;
import com.user.auth.repository.UserRepository;

@Service
public class JwtUserDetailsService implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder bcryptEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		DAOUser dbuser = userRepository.findByUsername(username);
		
		if(dbuser != null && dbuser.getUsername().equals(username)) {
			
			return new User(dbuser.getUsername(), dbuser.getPassword(), new ArrayList());
		} else {
			throw new UsernameNotFoundException("user not found for user: "+ username);
		}
	}
	
	public DAOUser save(UserDTO user) {
		DAOUser newUser = new DAOUser();
		newUser.setUsername(user.getUsername());
		newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
		return userRepository.save(newUser);
	}

	public List<DAOUser> getAllUser() {
		
		return userRepository.findAll();
	}

}
