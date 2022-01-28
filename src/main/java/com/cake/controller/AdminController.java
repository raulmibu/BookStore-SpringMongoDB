package com.cake.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import com.cake.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cake.auth.ApplicationUser;
import com.cake.auth.Gender;
import com.cake.auth.Role;
import com.cake.service.BookService;
import com.cake.service.CategoryService;
import com.cake.service.SaleService;
import com.cake.service.UserService;

@RequestMapping("admin")
@Controller
public class AdminController {
	
	@Autowired
	private BookService bookservice;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private List<Country> countries;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SaleService saleService;
	
	@PreAuthorize("hasAuthority('book:read')")
	@GetMapping("/books")
	public String fetchBooks(Model model ) {
		int page=1;
		model.addAttribute("books", bookservice.getAll());
		model.addAttribute("page",page);
		return "book/booklist";
	}
	
	@GetMapping("book/new")
	public String insert(Model model) {
		model.addAttribute("book", new Book());
		model.addAttribute("categories", categoryService.getAll());
		model.addAttribute("countries", countries);
		return "book/booksave";
	}
	
	@PreAuthorize("hasAuthority('book:write')")
	@PostMapping("book/save")
	public String save (@RequestParam("file") MultipartFile file, String categoryid, Book book, String countryid, Model model) throws IOException  {
		if(!file.isEmpty()) {
			if(file.getBytes()!=null) {
				book.setCoverPicture(Base64.getEncoder().encodeToString(file.getBytes())); 
			}
		}
		if(book.getId().isEmpty()||book.getId()=="") {
			book.setId(null);
		}
		if(book.getCategory()!=null) {
			if(book.getCategory().getId().compareTo(categoryid)!=0) {
				book.setCategory(categoryService.findByid(categoryid));
			}
		}else {
			book.setCategory(categoryService.findByid(categoryid));
		}
		if(book.getCountry()!=null) {
			if(book.getCountry().getId().compareTo(countryid)!=0) {
				book.setCountry(countries.stream()
						  .filter(country -> countryid.equals(country.getId()))
						  .findFirst()
						  .orElse(countries.get(0)));
			}
		}else {
			book.setCountry(countries.stream()
					  .filter(country -> countryid.equals(country.getId()))
					  .findFirst()
					  .orElse(countries.get(0)));
		}
		bookservice.save(book);
		return "redirect:/admin/books";
	}
	
	@PreAuthorize("hasAuthority('book:write')")
	@GetMapping("book/edit/{id}")
	public String showEdit(@PathVariable("id") String id , Model model) {
		if(id != null && id != "") {
			Book book=bookservice.findByid(id);
			model.addAttribute("book", book);
			
		}else {
			model.addAttribute("book", new Book());
		}
		model.addAttribute("countries", countries);
		model.addAttribute("categories", categoryService.getAll());
		return "book/booksave";
	}
	
	@PreAuthorize("hasAuthority('book:delete')")
	@GetMapping("book/delete/{id}")
	public String showDelete(@PathVariable("id") String id , Model model) {
		if(id != null && id != "") {
			Book book=bookservice.findByid(id);
			model.addAttribute("book", book);
			
		}else {
			return "redirect:/";
		}
		return "book/bookdelete";
	}
	@PreAuthorize("hasAuthority('book:delete')")
	@PostMapping("book/delete/{id}")
	public String confirmDelete(@PathVariable("id") String id) {
		bookservice.delete(id);
		return "redirect:/";
	}
	
	@PreAuthorize("hasAuthority('book:write')")
	@GetMapping("category/new")
	public String insertCategory(Model model) {
		model.addAttribute("category", new Category());
		return "book/categorysave";
	}
	
	@PreAuthorize("hasAuthority('book:write')")
	@GetMapping("category/edit/{id}")
	public String editCategory(@PathVariable("id") String id , Model model) {
		if(id != null && id != "") {
			model.addAttribute("category", categoryService.findByid(id));
		}else {
			model.addAttribute("category", new Category());
		}
		return "book/categorysave";
	}
	
	@PreAuthorize("hasAuthority('book:read')")
	@GetMapping("/categories")
	public String fetchCategories(Model model) {
		model.addAttribute("categories", categoryService.getAll());
//		model.addAttribute("categories", categories);
		return "book/categorylist";
	}
	
	@PreAuthorize("hasAuthority('book:write')")
	@PostMapping("category/saving")
	public String saveCategory (@RequestParam("file") MultipartFile file, Category category, Model model) throws IOException  {
		if(!file.isEmpty()) {
			if(file.getBytes()!=null) {
				category.setImage(Base64.getEncoder().encodeToString(file.getBytes())); 
			}
		}
		if(category.getId().isEmpty() || category.getId()=="") {
			category.setId(null);
		}
		categoryService.save(category);
		return "redirect:/admin/categories";
	}
	
	///////////// User admin
	@PreAuthorize("hasAuthority('user:write')")
	@GetMapping("user/edit/{id}")
	public String editUser(@PathVariable("id") String id , Model model, Authentication authentication) {
		if(authentication!=null && authentication.isAuthenticated()) {
			User user = ((ApplicationUser)  authentication.getPrincipal()).getUser();
			model.addAttribute("roles",Role.values());
			model.addAttribute("genders",Gender.values());
			model.addAttribute("usuario", userService.getOneUser(id,user.getRole()));
			model.addAttribute("editing",true);
			return "public/signup";
		}
		return "redirect:/login";
	}
	
	@PreAuthorize("hasAuthority('user:write')")
	@GetMapping("users")
	public String showUsers(Model model, Authentication authentication) {
		if(authentication!=null && authentication.isAuthenticated()) {
			User user = ((ApplicationUser)  authentication.getPrincipal()).getUser();
			model.addAttribute("users", userService.getUsers(user.getRole()));
			return "admin/userlist";
		}
		return "redirect:/login";
	}
	
	@PreAuthorize("hasAuthority('user:write')")
	@GetMapping("user/detail/{id}")
	public String showSave(@PathVariable("id") String id , Model model, Authentication authentication) {
		if(id != null && id != "") {
			User user=userService.findByid(id);
			model.addAttribute("user", user);
			if(user==null) {
				return "redirect:/";
			}
			model.addAttribute("results", new ArrayList<Book>(user.getHistory()));
			return "private/profile";
		}
		return "redirect:/";
	}

	@PreAuthorize("hasAuthority('user:write')")
	@GetMapping("sales")
	public String showSales(Model model) {
		model.addAttribute("sales", saleService.getAll());
		model.addAttribute("categories", categoryService.getAll());
		return "admin/saleslist";
	}

	@PostMapping("sales")
	public String showSalesByDate(Model model,String categoryid, String start, String end) {
		List<Sale> sales=new ArrayList<Sale>();

		LocalDate datestart=null;
		LocalDate dateend=null;
		if(!start.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			datestart = LocalDate.parse(start, formatter);
			if(!end.isEmpty()){
				dateend = LocalDate.parse(end, formatter);
			}else{
				dateend=LocalDate.now();
			}
			if(datestart.isAfter(dateend)){
				LocalDate temp=datestart;
				datestart=dateend;
				dateend=temp;
			}
		}

		if(categoryid.compareTo("no")==0) {
			sales= saleService.getSaleBeewtenDate(datestart,dateend,null);
		}else {
			sales= saleService.getSaleBeewtenDate(datestart,dateend,categoryid);
		}
		model.addAttribute("categoryid",categoryid);
		model.addAttribute("start",datestart);
		model.addAttribute("end",dateend);
		model.addAttribute("sales",sales);
		model.addAttribute("categories", categoryService.getAll());
		return "admin/saleslist";
	}
}
