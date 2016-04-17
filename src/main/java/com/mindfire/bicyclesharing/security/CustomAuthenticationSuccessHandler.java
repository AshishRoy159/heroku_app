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

package com.mindfire.bicyclesharing.security;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

/**
 * This class extends {@link SimpleUrlAuthenticationSuccessHandler} and is used
 * to handle successful login. This redirects the user to the view from where
 * the login request was sent, if the request comes from index then the user is
 * redirected depending on his role.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	Logger logger = Logger.getLogger(getClass());

	private RequestCache requestCache = new HttpSessionRequestCache();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.web.authentication.
	 * SimpleUrlAuthenticationSuccessHandler#onAuthenticationSuccess(javax.
	 * servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 * org.springframework.security.core.Authentication)
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		logger.info("Successful login.");
		SavedRequest savedRequest = requestCache.getRequest(request, response);

		if (savedRequest == null) {
			Collection<? extends GrantedAuthority> auths = authentication.getAuthorities();
			if (auths.toArray()[0].toString().equals("ADMIN")) {
				String targetUrl = "admin/adminHome";
				getRedirectStrategy().sendRedirect(request, response, targetUrl);
			} else if (auths.toArray()[0].toString().equals("USER")) {
				String targetUrl = "index.html";
				getRedirectStrategy().sendRedirect(request, response, targetUrl);
			} else if (auths.toArray()[0].toString().equals("MANAGER")) {
				String targetUrl = "manager/managerHome";
				getRedirectStrategy().sendRedirect(request, response, targetUrl);
			}
		} else {
			String targetUrl = savedRequest.getRedirectUrl();
			getRedirectStrategy().sendRedirect(request, response, targetUrl);
		}
	}
}
