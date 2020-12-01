package com.tee;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {

	public static void main(String [] args) {
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		String rawPassword = "password1234";
		
		String encodedPassword = encoder.encode(rawPassword);
		
		System.out.print(encodedPassword);
	}
}
