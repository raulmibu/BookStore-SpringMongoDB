package com.cake.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cake.auth.ApplicationUser;
import com.cake.auth.Gender;
import com.cake.auth.Role;
import com.cake.model.Book;
import com.cake.model.Category;
import com.cake.model.User;
import com.cake.service.BookService;
import com.cake.service.CategoryService;
import com.cake.service.UserService;

@Controller
public class PublicController {
	@Autowired
	private UserService userservice;
	@Autowired
	private BookService bookservice;
	@Autowired
	private CategoryService categoryservice;
	
//	@Autowired
//	private List<Category> categories;
	
//	index and search results
	
	
	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("results", bookservice.findAllActive(0,10,"title"));
		model.addAttribute("categories", categoryservice.getAll());
//		model.addAttribute("categories", categories);
		return "index";
	}
	
	@GetMapping("search")
	public String search(Model model, @RequestParam("searching") String searching, Authentication authentication) {
		List<Book> books=bookservice.search(searching);
		
		if(authentication!=null && authentication.isAuthenticated()) {
			User user = ((ApplicationUser)  authentication.getPrincipal()).getUser();
			if(user.getFavoriteCategories()!=null) {
				
				Iterator<Category> it=user.getFavoriteCategories().iterator();
				while(it.hasNext()) {
					Category cat=it.next();
					for (int i=0;i<books.size();i++) {
						if(books.get(i).getCategory().getName().compareTo(cat.getName())==0) {
							books.get(i).addWeight(i);
						}
					}
					
				}
			}
			Collections.sort(books);
		}
		model.addAttribute("results",books);
		return "index";
	}
	
	
	@GetMapping("search/author/{author}")
	public String searchauthors(Model model,@PathVariable("author") String searching) {
		List<Book> books=bookservice.findQuery("author",searching);
		model.addAttribute("results",books);
		return "index";
	}
	
	@GetMapping("search/category/{category}")
	public String searchcategories(Model model,@PathVariable("category") String searching) {
		List<Book> books=bookservice.findQuery("category.name",searching);
		model.addAttribute("results",books);
		return "index";
	}
//	public book access
	@GetMapping("book/detail/{id}")
	public String showSave(@PathVariable("id") String id , Model model, Authentication authentication) {
		if(id != null && id != "") {
			Book book=bookservice.findByid(id);
			model.addAttribute("book", book);
			if(book==null) {
				return "redirect:/";
			}
			if(authentication!=null && authentication.isAuthenticated()) {
				User currentUser = ((ApplicationUser)  authentication.getPrincipal()).getUser();
				if(currentUser.addHistory(book)) {
					userservice.updateHistory(currentUser);
				}
			}
			return "book/bookdetail";
		}
		return "redirect:/";
	}
	
	@GetMapping("/categories")
	public String fetchCategories(Model model) {
		model.addAttribute("categories", categoryservice.getAll());
//		model.addAttribute("categories", categories);
		return "book/categorylist";
	}

	///////public user mappings

	@GetMapping("/login")
	public String loginForm() {
		return "public/login";
	}
	
	
	@GetMapping("/signup")
	public String registerForm(Model model) {
		model.addAttribute("genders",Gender.values());
		model.addAttribute("usuario",new User());
		model.addAttribute("roles",Role.values());
		return "public/signup";
	}
	
	
	@PostMapping("/signingup")
	public String registerFormPost(@Validated @ModelAttribute User usuario,@RequestParam("file") MultipartFile file, 
			String userrole,String gender, BindingResult result, 
			Model model) throws IOException {
		System.out.println(usuario.getName()+userrole+gender);
		if(result.hasErrors()) {
			return "redirect:/signup";
		}else {
			if(usuario.getId().isEmpty() || usuario.getId()=="") {
				usuario.setId(null);
				User temp=userservice.findByemail(usuario.getEmail());
				if(temp!=null&&usuario.getId()!=temp.getId()) {
					return "redirect:/signup?error=true";
				}
			}
			try {
				usuario.setRole(Role.valueOf(userrole));
			} catch (Exception e) {
				usuario.setRole(Role.CUSTOMER);
			}
			try {
				usuario.setGender(Gender.valueOf(gender));
			} catch (Exception e) {
				usuario.setGender(Gender.OTHER);
			}
			if(!file.isEmpty()) {
				if(file.getBytes()!=null) {
					usuario.setProfilePicture(Base64.getEncoder().encodeToString(file.getBytes())); 
				}
			}
			userservice.save(usuario);
			//authentication.setAuthenticated(false);
			return "redirect:/";
		}
	}
	
}
