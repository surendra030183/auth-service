package com.user.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.user.auth.model.DAOUser;
import com.user.auth.model.UserDTO;
import com.user.auth.service.JwtUserDetailsService;

@RestController
public class UserController {
	
	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;
	
	@PostMapping("/register-test")
	public ResponseEntity<DAOUser> registerUser(@RequestBody UserDTO user) {
		
		System.out.println("register called.......");
		
		return ResponseEntity.ok(jwtUserDetailsService.save(user));
		
	}
	
	@GetMapping("/user/{userid}")
	public ResponseEntity<DAOUser> getUser(@PathVariable long userid) {
		
		System.out.println("=======getUser======"+userid);
		
		return ResponseEntity.ok(jwtUserDetailsService.getUserByUserId(userid));
	}
	
	
	@GetMapping("hello")
	public String hello() {
		return "hello user controller";
	}

}
