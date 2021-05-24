package com.user.auth.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
	
	@PostMapping("/register")
	public ResponseEntity<DAOUser> registerUser(@RequestBody UserDTO user) {
		
		System.out.println("register called.......");
		
		return ResponseEntity.ok(jwtUserDetailsService.save(user));
		
	}
	
	@GetMapping("users")
	public ResponseEntity<List<DAOUser>> getAllUsers() {
		return ResponseEntity.ok(jwtUserDetailsService.getAllUser());
	}
	
	
	@GetMapping("hello")
	public String hello() {
		return "hello user controller";
	}

}
