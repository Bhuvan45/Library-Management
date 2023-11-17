package com.library.model.custom.displayDto;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.library.model.DateSerialize;

public class RetrivingDto
{
	private Integer memberId;
	
	private String firstName;
	
	private String lastName;
	
	private String email;

	private Integer bookId;
	
	private String title;
	
	private String author;
	
	private String language;	
	
	private Integer borrowedCount;
	
	private Integer returnedCount;
	
	private String rentalStatus;
	
	@JsonSerialize(using = DateSerialize.class)
	private Date borrowedDate;
	
	@JsonSerialize(using = DateSerialize.class)
	private Date actualReturnedDate;
	
	@JsonSerialize(using = DateSerialize.class)
	private Date returnDueDate;

	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Integer getBookId() {
		return bookId;
	}

	public void setBookId(Integer bookId) {
		this.bookId = bookId;
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

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}	
	
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Integer getBorrowedCount() {
		return borrowedCount;
	}

	public void setBorrowedCount(Integer borrowedCount) {
		this.borrowedCount = borrowedCount;
	}

	public Integer getReturnedCount() {
		return returnedCount;
	}

	public void setReturnedCount(Integer returnedCount) {
		this.returnedCount = returnedCount;
	}

	public String getRentalStatus() {
		return rentalStatus;
	}

	public void setRentalStatus(String rentalStatus) {
		this.rentalStatus = rentalStatus;
	}

	public Date getBorrowedDate() {
		return borrowedDate;
	}

	public void setBorrowedDate(Date borrowedDate) {
		this.borrowedDate = borrowedDate;
	}

	public Date getActualReturnedDate() {
		return actualReturnedDate;
	}

	public void setActualReturnedDate(Date actualReturnedDate) {
		this.actualReturnedDate = actualReturnedDate;
	}

	public Date getReturnDueDate() {
		return returnDueDate;
	}

	public void setReturnDueDate(Date returnDueDate) {
		this.returnDueDate = returnDueDate;
	}
}
