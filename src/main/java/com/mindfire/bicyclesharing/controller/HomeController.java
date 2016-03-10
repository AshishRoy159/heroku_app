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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
	public String index() {
		return "index";
	}

	/**
	 * This method maps register request. Simply render the registration view.
	 * 
	 * @return the registration view.
	 */
	@RequestMapping("register")
	public String reg() {
		return "registration";
	}

	/**
	 * This method maps login request. Simply render the signIn view.
	 * 
	 * @return the signIn view.
	 */
	@RequestMapping("login")
	public String signIn() {
		return "signIn";
	}
}
