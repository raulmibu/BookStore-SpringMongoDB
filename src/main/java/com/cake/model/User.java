package com.cake.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import com.cake.auth.Gender;
import com.cake.auth.Role;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
@Document(collection = "users")
public class User {
	@Id
	private String id;
	private String name;
	private String lastname;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate birthday;
	private String adress;
	private Country country;
	private Set<String> favoriteTopics;
	private Set<Category> favoriteCategories;
	
    @Indexed(unique = true)
	private String email;
	private String password;
	
	private Gender gender;
	
	private Role role;
	private String profilePicture;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDateTime created;
	private boolean enabled;
	private Set<Sale> bought;
	private List<Book> history;
	///constructors
	public User(){
		this.role=Role.CUSTOMER;
		this.gender=Gender.OTHER;
		this.enabled=true;
		this.birthday=LocalDate.now();
	}

	///getters and setters
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public LocalDate getBirthday() {
		return birthday;
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}

	public String getAdress() {
		return adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public Set<String> getFavoriteTopics() {
		return favoriteTopics;
	}

	public void setFavoriteTopics(Set<String> favoriteTopics) {
		this.favoriteTopics = favoriteTopics;
	}

	public Set<Category> getFavoriteCategories() {
		return favoriteCategories;
	}

	public void setFavoriteCategories(Set<Category> favoriteCategories) {
		this.favoriteCategories = favoriteCategories;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		if(created!=null) {
			this.created = created;
		}
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public List<Book> getHistory() {
		if(history==null) {
			return new ArrayList<Book>();
		}else {
			List<Book> listhistory=new ArrayList<Book>(history);
			Collections.reverse(listhistory);
			return listhistory;
		}
	}

	public void setHistory(List<Book> history) {
		this.history = history;
	}
	
	public boolean addHistory(Book book) {
		if(history==null) {
			history=new ArrayList<Book>();
		}
		history.removeIf( bo -> bo.getId().equals(book.getId()));
		if(history.size()>=10) {
			history.remove(0);
		}
		Book newbook=new Book("empty");
		newbook.setId(book.getId());
		newbook.setAuthor(book.getAuthor());
		newbook.setTitle(book.getTitle());
		newbook.setCoverPicture(book.getCoverPicture());
		history.add(newbook);
		return true;
	}
	
	public boolean addBought(Sale sale) {
		if(bought==null) {
			bought=new HashSet<Sale>();
		}
		return bought.add(sale);
	}
	public Set<Sale> getBought() {
		if(bought==null) {
			bought=new HashSet<Sale>();
		}
		return bought;
	}

	public void setBought(Set<Sale> bought) {
		this.bought = bought;
	}
	
	public int getManyBought() {
		int total=0;
		Iterator<Sale> it=getBought().iterator();
		while(it.hasNext()) {
			total+=it.next().getQuanty();
		}
		return total;
	}
	public double getTotalSpent() {
		double total=0;
		Iterator<Sale> it=getBought().iterator();
		while(it.hasNext()) {
			total+=it.next().getTotal();
		}
		return total;
	}
	
	public String isEnabledString() {
		if (enabled) {
			return "Enabled";
		}
		return "Disabled";
	}
}
