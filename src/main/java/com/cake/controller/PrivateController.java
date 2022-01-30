package com.cake.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cake.auth.ApplicationUser;
import com.cake.auth.Gender;
import com.cake.auth.Role;
import com.cake.model.Book;
import com.cake.model.Category;
import com.cake.model.Sale;
import com.cake.model.User;
import com.cake.service.BookService;
import com.cake.service.CategoryService;
import com.cake.service.SaleService;
import com.cake.service.UserService;

@Controller
public class PrivateController {
	@Autowired
	private UserService userservice;
	@Autowired
	private BookService bookservice;
	@Autowired
	private CategoryService categoryservice;
	
	@Autowired
	private SaleService saleservice;
	
	
	@GetMapping("/profile")
	public String profilePage(Model model, Authentication authentication) {
		if(authentication!=null && authentication.isAuthenticated()) {
			User currentUser = ((ApplicationUser)  authentication.getPrincipal()).getUser();
			model.addAttribute("user", currentUser);
			model.addAttribute("results",new ArrayList<Book>(currentUser.getHistory()));
			return "private/profile";
		}else {
			return "redirect:/login";
		}
	}

	
	@GetMapping("profile/edit")
	public String editUser(Model model, Authentication authentication) {
		if(authentication!=null && authentication.isAuthenticated()) {
			User user = ((ApplicationUser)  authentication.getPrincipal()).getUser();
			model.addAttribute("roles",Role.values());
			model.addAttribute("genders",Gender.values());
			model.addAttribute("usuario", user);
			model.addAttribute("editing",true);
			return "public/signup";
		}
		return "redirect:/login";
	}
	
	@PostMapping("profile/updatephoto")
	public String updatePhoto(Model model, @RequestParam("file") MultipartFile file, User user, Authentication authentication) throws IOException {
		if(authentication!=null && authentication.isAuthenticated()) {
			User currentuser = ((ApplicationUser)  authentication.getPrincipal()).getUser();
			if(!file.isEmpty()) {
				if(file.getBytes()!=null) {
					if(user.getId()!=null) {
						user.setProfilePicture(Base64.getEncoder().encodeToString(file.getBytes())); 
						currentuser.setProfilePicture(user.getProfilePicture());
						userservice.updateStringType("profilePicture",user.getProfilePicture(), user.getId());
					}
				}
			}
		}			
		return "redirect:/profile";
	}
	
	@PostMapping("profile/updatepassword")
	public String updatePassword(Model model, User user, Authentication authentication) {
		if(user.getId()!=null && user.getPassword()!=null) {
			if(!user.getPassword().isEmpty()) {
				user.setPassword(userservice.encodePassword(user.getPassword()));
				userservice.updateStringType("password",user.getPassword(), user.getId());
				if(((ApplicationUser)  authentication.getPrincipal()).getUser().getId().compareTo(user.getId())==0){
					return "redirect:/logout";
				}
			}
		}			
		return "redirect:/profile";
	}
}
