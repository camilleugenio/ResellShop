package com.siw.resell.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.siw.resell.controller.validator.UserValidator;
import com.siw.resell.model.Credentials;
import com.siw.resell.model.User;
import com.siw.resell.service.CredentialsService;
import com.siw.resell.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	
	
	@Autowired
	private CredentialsService credentialsService;

	@Autowired
	private UserValidator userValidator;
	

	@GetMapping("/user/all")
	public String getUsers(Model model) {
		List<User> users = userService.getAllUsers();
		model.addAttribute("users", users);

		return "users.html";
	}

	@GetMapping("/user/{id}")
	public String getUser(@PathVariable("id") Long id, Model model) {
		User user = userService.getUser(id);
		model.addAttribute("user", user);
		
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		User currentUser = credentials.getUser();
		model.addAttribute("currentUserId", currentUser.getId());

		return "user.html";
	}
	
	@GetMapping("/user/profile")
	public String getUserProfile(Model model) {
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		User currentUser = credentials.getUser();

		model.addAttribute("user", currentUser);
		model.addAttribute("currentUserId", currentUser.getId());
		
		return "user.html";
	}
	
	@GetMapping("/user/edit/form/{id}")
	public String editUserForm(@PathVariable Long id, Model model) {
		User user = userService.getUser(id);
		model.addAttribute("user", user);
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		User currentUser = credentials.getUser();
		if (user.equals(currentUser) || credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
			return "userEditForm.html";
		}
		return "unauthorized.html";
	}

	@PostMapping("/user/edit/{id}")
	public String editUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, @PathVariable Long id,
			Model model) {

		User vecchioUser = this.userService.getUser(id);

		if (!vecchioUser.equals(user))
			this.userValidator.validate(user, bindingResult);

		if (!bindingResult.hasErrors()) {

			vecchioUser.setNome(user.getNome());
			vecchioUser.setCognome(user.getCognome());
			
			User userSalvato = this.userService.saveUser(vecchioUser);
		
			model.addAttribute("user", userSalvato);
			
			return "redirect:/user/"+userSalvato.getId();
		}

		return "userEditForm.html";
	}
}