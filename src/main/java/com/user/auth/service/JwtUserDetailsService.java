package com.user.auth.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

	protected final Log logger = LogFactory.getLog(JwtUserDetailsService.class);

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder bcryptEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		logger.info("Username:"+username+", calling db to validate user" );
		DAOUser dbuser = userRepository.findByUsername(username);
		
		if(dbuser != null && dbuser.getUsername().equals(username)) {
			return new User(dbuser.getUsername(), dbuser.getPassword(), getAuthority(dbuser));
		} else {
			throw new UsernameNotFoundException("user not found for user: "+ username);
		}
	}
	
	private Collection<? extends GrantedAuthority> getAuthority(DAOUser user) {
		
		Set<GrantedAuthority> authority = new HashSet<GrantedAuthority>();
		
		user.getRoles().forEach(role -> {
			authority.add(new SimpleGrantedAuthority("ROLE_"+ role.getName()));
		});
		
		return authority;
	}

	public DAOUser save(UserDTO user) {
		
		//check if user already registered
		DAOUser userdb = userRepository.findByUsername(user.getUsername());
		
		if(userdb != null) {
			logger.info("Username:"+user.getUsername()+", already exist in database" );

			userdb.setStatus("username already exist");
			return userdb;
		}
		
		DAOUser newUser = new DAOUser();
		newUser.setUsername(user.getUsername());
		newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
		newUser.setEmail(user.getEmail());
		newUser.setFullName(user.getFullName());
		newUser.setMobile(user.getMobile());
		newUser.setAddress(user.getAddress());
		return userRepository.save(newUser);
	}

	public List<DAOUser> getAllUser() {
		return userRepository.findAll();
	}

	public DAOUser getUserByUserName(String username) {
		logger.info("Username:"+username+", calling db to get user details" );
		return userRepository.findByUsername(username);
	}

	public DAOUser getUserByUserId(long userid) {
		
		return userRepository.findById(userid).get();
	}

}
