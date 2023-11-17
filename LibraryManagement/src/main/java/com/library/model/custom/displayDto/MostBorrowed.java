package com.library.model.custom.displayDto;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.library.model.DateSerialize;

public class MostBorrowed
{	
	private Integer bookId;
	
	private String title;
	
	private String language;
	
	private String author;
	
	@JsonSerialize(using = DateSerialize.class)
	private Date registeredDate;
	
	private String bookStatus;
	
	private Integer stockCount;
	
	private Integer borrowedCount;
	
	private Long totalBorrowed;
	

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

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getRegisteredDate() {
		return registeredDate;
	}

	public void setRegisteredDate(Date registeredDate) {
		this.registeredDate = registeredDate;
	}

	public String getBookStatus() {
		return bookStatus;
	}

	public void setBookStatus(String bookStatus) {
		this.bookStatus = bookStatus;
	}

	public Integer getStockCount() {
		return stockCount;
	}

	public void setStockCount(Integer stockCount) {
		this.stockCount = stockCount;
	}

	public Integer getBorrowedCount() {
		return borrowedCount;
	}

	public void setBorrowedCount(Integer borrowedCount) {
		this.borrowedCount = borrowedCount;
	}

	public Long getTotalBorrowed() {
		return totalBorrowed;
	}

	public void setTotalBorrowed(Long totalBorrowed) {
		this.totalBorrowed = totalBorrowed;
	}
}
