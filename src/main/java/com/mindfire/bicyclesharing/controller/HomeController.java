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

package com.mindfire.bicyclesharing.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.mindfire.bicyclesharing.DTO.LoginDTO;

/**
 * This class contains all the Request Mappings related to the navigation of the
 * user's front-end.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Controller
public class HomeController {
	
	/**
	 * This method maps all root request. Simply render the index view.
	 * 
	 * @return the index view.
	 */
	@RequestMapping(value = { "/", "index" })
	public String getHomePage() {
		return "index";
	}

	/**
	 * This method maps register request. Simply render the registration view.
	 * 
	 * @return the registration view.
	 */
	@RequestMapping(value = {"register"}, method = RequestMethod.GET)
	public String getUserCreatePage() {
		return "registration";
	}

	/**
	 * This method maps login request. Simply render the signIn view.
	 * 
	 * @return the signIn view.
	 */
	@RequestMapping(value = {"login"}, method = RequestMethod.GET)
	public ModelAndView getUserSignInPage(@ModelAttribute("loginData") LoginDTO logiDTO, @RequestParam Optional<String> error) {
		return new ModelAndView("signIn", "error", error);
	}
	
	//This method is only for checking that all request to /users/** must have ADMIN authority to access.
	@RequestMapping(value = {"users/userDetails"})
	public String getUserDetails(){
		return "userDetails";
	}
	
	/**
	 * This method maps any request which is not authorized to the user. Simply render the Access Denied view.
	 * @return 403 view
	 */
	@RequestMapping(value = {"403"})
	public String getAccessDeniedPage(){
		return "403";
	}
}
