package com.tee.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

//import com.tee.auth.ApplicationUserService;
import com.tee.jwt.JwtConfig;
import com.tee.jwt.JwtTokenVerifier;
import com.tee.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import com.tee.service.UserDetailsServiceImpl;

//import static  com.tee.security.ApplicationUserRole.*;

import javax.crypto.SecretKey;



@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter{

	
	private final PasswordEncoder passwordEncoder;
	private final SecretKey secretKey;
	private final JwtConfig jwtConfig;
//	private final ApplicationUserService applicationUserService;
	
	private UserDetailsServiceImpl userDetailsServiceImpl;
		

	
	@Autowired
	public ApplicationSecurityConfig(PasswordEncoder passwordEncoder, SecretKey secretKey, JwtConfig jwtConfig,
			UserDetailsServiceImpl userDetailsServiceImpl) {
		this.passwordEncoder = passwordEncoder;
		this.secretKey = secretKey;
		this.jwtConfig = jwtConfig;
		this.userDetailsServiceImpl = userDetailsServiceImpl;
	}

	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http
		

		 .csrf().disable()
		 .sessionManagement()
		       .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		 .and() 
		 .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig, secretKey))
		 .addFilterAfter(new JwtTokenVerifier(secretKey, jwtConfig), JwtUsernameAndPasswordAuthenticationFilter.class)
		 .authorizeRequests()
         .anyRequest()	          
         .authenticated();
      
		 
//     Use this for basicAuth    .httpBasic();

		}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth.authenticationProvider(daoAuthenticationProvider());
	}

	
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder);
		provider.setUserDetailsService(userDetailsServiceImpl);
		return provider;
	}
	
	}





