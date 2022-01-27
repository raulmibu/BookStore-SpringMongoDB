package com.cake;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.cake.model.Country;
import com.cake.service.CountryService;

@SpringBootApplication
public class CakeApplication {
	@Autowired
	CountryService countryService;
//	@Autowired
//	CategoryService categoryService;
//	
	@Bean
	public List<Country> countries(){
		return countryService.getAll();
	}
	
//	@Bean
//	public List<Category> categories(){
//		return categoryService.getAll();
//	}
	public static void main(String[] args) {
		SpringApplication.run(CakeApplication.class, args);
	}

}
