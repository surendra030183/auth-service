package com.user.auth.controller;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.user.auth.jwt.JwtTokenUtil;
import com.user.auth.model.DAOUser;
import com.user.auth.model.JwtRequest;
import com.user.auth.model.JwtResponse;
import com.user.auth.model.UserDTO;
import com.user.auth.service.JwtUserDetailsService;

@RestController
public class AuthenticationController {
	protected final Log logger = LogFactory.getLog(AuthenticationController.class);

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@PostMapping("/api/v1.0/user/register")
	public ResponseEntity<DAOUser> registerUser(@RequestBody UserDTO user) {
		
		return ResponseEntity.ok(jwtUserDetailsService.save(user));
		
	}
	
    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@GetMapping("/api/v1.0/user/all")
	public ResponseEntity<List<DAOUser>> getAllUsers() {
		return ResponseEntity.ok(jwtUserDetailsService.getAllUser());
	}
	
	@GetMapping("/api/v1.0/user/username/{username}")
	public ResponseEntity<DAOUser> getUserByUserName(@PathVariable String username) {
		return ResponseEntity.ok(jwtUserDetailsService.getUserByUserName(username));
	}
	
	@PostMapping("/api/v1.0/user/authenticate")
	private ResponseEntity<JwtResponse> createAuthenticationToken(@RequestBody JwtRequest request) throws Exception {
		
		logger.info("authenticate api calling........");
		logger.info("username:"+request.getUsername());

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		} catch (Exception e) {
			throw new Exception("EXCEPTION", e);
		}
		
		final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(request.getUsername());
		
		final String token = jwtTokenUtil.generateToken(userDetails);
		logger.info("JWT Token created successfully");
		
		return ResponseEntity.ok(new JwtResponse(token));
	}
	
	@GetMapping("/api/v1.0/user/userid/{userid}")
	public ResponseEntity<DAOUser> getUserByUserId(@PathVariable long userid) {
		return ResponseEntity.ok(jwtUserDetailsService.getUserByUserId(userid));
	}
	
}
