package com.mindfire.bicyclesharing.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mindfire.bicyclesharing.component.UserComponent;
import com.mindfire.bicyclesharing.dto.ManageRoleDTO;
import com.mindfire.bicyclesharing.service.PickUpPointService;

@Controller
public class ManageRoleController {

	@Autowired
	private UserComponent userComponent;
	
	@Autowired
	private PickUpPointService pickUpPointService;
	
	@RequestMapping("/admin/manageRole")
   public ModelAndView manageRole(){
	   return new ModelAndView("manageRole","pickUpPoints", pickUpPointService.getAllPickupPoints());
   }
	
	@RequestMapping(value = "admin/setPickUpPoint", method= RequestMethod.POST)
	public ModelAndView setPickUpPoint(@ModelAttribute("manageRoleData") ManageRoleDTO manageRoleDTO){
		if(userComponent.mapUpdateRole(manageRoleDTO) > 0 ){
			if(manageRoleDTO.getUserRoleId() == 2){
				userComponent.mapPickUpPointManagerDetails(manageRoleDTO);
			}
			return new ModelAndView("manageRole");
		}else{
			return new ModelAndView("manageRole");
		}
	}
}
