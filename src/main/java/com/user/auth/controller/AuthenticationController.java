package com.user.auth.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.auth.jwt.JwtTokenUtil;
import com.user.auth.model.DAOUser;
import com.user.auth.model.JwtRequest;
import com.user.auth.model.JwtResponse;
import com.user.auth.model.UserDTO;
import com.user.auth.service.JwtUserDetailsService;

@RestController
@RequestMapping("auth")
public class AuthenticationController {
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@PostMapping("/register")
	public ResponseEntity<DAOUser> registerUser(@RequestBody UserDTO user) {
		
		System.out.println("register called.......");
		
		return ResponseEntity.ok(jwtUserDetailsService.save(user));
		
	}
	
	@GetMapping("users")
	public ResponseEntity<List<DAOUser>> getAllUsers() {
		return ResponseEntity.ok(jwtUserDetailsService.getAllUser());
	}
	
	@PostMapping("/authenticate")
	private ResponseEntity<JwtResponse> createAuthenticationToken(@RequestBody JwtRequest request) throws Exception {
		
		System.out.println("authenticate api calling........");
		System.out.println("u:"+request.getUsername());
		System.out.println("p:"+request.getPassword());

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

		return ResponseEntity.ok(new JwtResponse(token));
	}
	
}
