package com.cake.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
@Document(collection = "sales")
public class Sale {
	@Id
	private String id;
	private String customerId;
	private String customer;
	private String bookId;
	private String book;
	private Category category;
	private int quanty;
	private double discount;
	private double unitPrice;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDateTime date;
	
	
	public Sale() {
		
	}
	public Sale(User user, Book book, int quanty) {
		this.customerId=user.getId();
		this.customer=user.getName()+" "+user.getLastname();
		this.book=book.getTitle();
		this.bookId=book.getId();
		this.category=book.getCategory();
		this.quanty=quanty;
		this.date=LocalDateTime.now();
		this.unitPrice=book.getPrice();
		this.discount=book.getDiscount();
	}
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	public String getBook() {
		return book;
	}
	public void setBook(String book) {
		this.book = book;
	}
	public int getQuanty() {
		return quanty;
	}
	public void setQuanty(int quanty) {
		this.quanty = quanty;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	public double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public LocalDateTime getDate() {
		return date;
	}
	public void setDate(LocalDateTime date) {
		this.date = date;
	}
	
	public double getTotal() {
		return (quanty*unitPrice)-(quanty*unitPrice)*(discount/100);
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}


	public String getCustomerId() {
		return customerId;
	}


	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}


	public String getBookId() {
		return bookId;
	}


	public void setBookId(String bookId) {
		this.bookId = bookId;
	}
}
