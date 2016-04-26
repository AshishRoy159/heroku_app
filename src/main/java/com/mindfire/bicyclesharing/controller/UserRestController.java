package com.mindfire.bicyclesharing.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.mindfire.bicyclesharing.model.User;
import com.mindfire.bicyclesharing.repository.UserRepository;

@RestController
public class UserRestController {
	
	@Autowired
	private UserRepository userRepository;
	
	@JsonView(DataTablesOutput.View.class)
	@RequestMapping(value = {"/data/userList" }, method = RequestMethod.GET)
	public DataTablesOutput<User> userList(@Valid DataTablesInput input) {
		System.out.println("hi");
		return userRepository.findAll(input);
	}

}
