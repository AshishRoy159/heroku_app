package com.mindfire.bicyclesharing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

	@RequestMapping(value={"/","index"})
	public String index(){
		return "index";
	}
	
	@RequestMapping("register")
	public String reg(){
		return "registration";
	}
	
	@RequestMapping("login")
	public String signIn(){
		return "signIn";
	}
}
