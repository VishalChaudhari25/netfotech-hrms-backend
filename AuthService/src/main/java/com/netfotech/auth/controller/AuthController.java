package com.netfotech.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netfotech.auth.dto.AuthRequest;
import com.netfotech.auth.dto.AuthResponse;
import com.netfotech.auth.security.JwtUtil;
import com.netfotech.auth.service.CustomUserDetailsService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Autowired
	private JwtUtil jwtUtil;

	@PostMapping("/login")
	public AuthResponse createAuthenticationToken(@RequestBody AuthRequest authRequest) throws Exception {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

		final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
		final String jwt = jwtUtil.generateToken(userDetails.getUsername());

		return new AuthResponse(jwt); // Now uses the constructor correctly
	}

	// Only SuperAdmin can access this endpoint
	@GetMapping("/admin")
	@PreAuthorize("hasRole('SuperAdmin')")
	public String superAdminArea() {
		return "You are accessing a SuperAdmin-only area!";
	}

	// Admin and SuperAdmin can access this
	@GetMapping("/admin-only")
	@PreAuthorize("hasAnyRole('Admin', 'SuperAdmin')")
	public String adminArea() {
		return "Welcome, Admin!";
	}
}
