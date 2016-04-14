/*
 * Copyright 2016 Mindfire Solutions
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mindfire.bicyclesharing.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.mindfire.bicyclesharing.security.CustomAuthenticationFailureHandler;
import com.mindfire.bicyclesharing.security.CustomAuthenticationSuccessHandler;

/**
 * This is the Security Configuration file for Spring Security.
 * 
 * @author mindfire
 * @version 1.0 {@link WebSecurityConfigurerAdapter}
 * @since 10/03/2016
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomAuthenticationSuccessHandler successHandler;
	
	@Autowired
	private CustomAuthenticationFailureHandler failureHandler;

	@Autowired
	private UserDetailsService userDetailsService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.config.annotation.web.configuration.
	 * WebSecurityConfigurerAdapter#configure(org.springframework.security.
	 * config.annotation.web.builders.HttpSecurity)
	 */
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.exceptionHandling().accessDeniedPage("/403").and().authorizeRequests().antMatchers("/user/**")
				.hasAnyAuthority("ADMIN", "USER", "MANAGER").and().authorizeRequests().antMatchers("/manager/**")
				.hasAnyAuthority("MANAGER").and().authorizeRequests().antMatchers("/admin/**").hasAuthority("ADMIN")
				.anyRequest().permitAll().and().formLogin().loginPage("/login").successHandler(successHandler)
				.failureHandler(failureHandler).usernameParameter("email").permitAll().and().logout().logoutUrl("/logout")
				.deleteCookies("remember-me").logoutSuccessUrl("/login?logout").permitAll().and().rememberMe().and()
				.csrf().disable();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.config.annotation.web.configuration.
	 * WebSecurityConfigurerAdapter#configure(org.springframework.security.
	 * config.annotation.web.builders.HttpSecurity)
	 */
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
	}
}
