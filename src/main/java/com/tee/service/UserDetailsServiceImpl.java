package com.tee.service;

import javax.persistence.EntityExistsException;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tee.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	 private UserRepository userRepository;

	    public UserDetailsServiceImpl(UserRepository userRepository) {
	        this.userRepository = userRepository;
	    }

	    @Override
	    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	        return userRepository.findByUsername(username)
	                .orElseThrow(() -> new EntityExistsException("User " + username + " doesn't exist"));
	    } }