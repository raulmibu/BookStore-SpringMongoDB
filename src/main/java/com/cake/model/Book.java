package com.cake.model;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.TextScore;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
@Document(collection = "books")
public class Book implements Comparable<Book>{
	@Id
	private String id;
    @Indexed(unique = true)
    private String isbn;
    
    @TextIndexed(weight=6)
	private String title;
    @TextIndexed(weight=3)
	private String author;
	private String publisher;
	@TextIndexed(weight=2)
	private String overview;
	@TextIndexed(weight=1)
	private String synopsis;
	private int edition;
	private int pages;
	private int year;
	private int stock;
	
	private double price;
	private double discount;

	private Country country;
	@TextIndexed(weight=5)
	private Category category;
	@TextIndexed(weight=4)
	private Set<String> topics;
	private String coverPicture;
	private boolean enabled;
	private long bought;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDateTime created;
	@TextScore
	private float score;
	@Transient
	private int weitgh;
	
	public Book() {
		this.edition=1;
		this.stock=999;
		this.enabled=true;
		this.year=1999;
		this.price=9.99;
		this.pages=99;
	}
	
	public Book(String empty) {
		
	}
    ///getters and setters
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getOverview() {
		return overview;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}

	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	public int getEdition() {
		return edition;
	}

	public void setEdition(int edition) {
		this.edition = edition;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Set<String> getTopics() {
		return topics;
	}

	public void setTopics(Set<String> topics) {
		this.topics = topics;
	}

	public String getCoverPicture() {
		return coverPicture;
	}

	public void setCoverPicture(String coverPicture) {
		this.coverPicture = coverPicture;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public long getBought() {
		return bought;
	}

	public void setBought(long bought) {
		this.bought = bought;
	}
	
	@Override
	public int hashCode() {
	    int hash = 0;
	    hash = this.isbn == null ? 0 : this.isbn.hashCode();
	    return hash;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Book) {
			return this.isbn==((Book) obj).isbn;
		}
		return false;
	}
	public String isEnabledString() {
		if (enabled) {
			return "Enabled";
		}
		return "Disabled";
	}


	public void addWeight(int weitgh) {
		this.weitgh+=weitgh;
		
	}
	
	@Override
	public int compareTo(Book e) {
	        return -this.getWeitgh()+e.getWeitgh();
	}

	public int getWeitgh() {
		return weitgh;
	}

	public void setWeitgh(int weitgh) {
		this.weitgh = weitgh;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public float getScore() {
		return score;
	}
}