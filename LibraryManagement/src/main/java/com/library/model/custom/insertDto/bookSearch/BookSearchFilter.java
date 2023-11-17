package com.library.model.custom.insertDto.bookSearch;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.library.model.DateWithoutTimeDeserializer;

public class BookSearchFilter
{
	private String author;

	private String language;

	private String status;
	
	@JsonDeserialize(using = DateWithoutTimeDeserializer.class)
	private Date fromDate;
	
	@JsonDeserialize(using = DateWithoutTimeDeserializer.class)
	private Date toDate;

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	
}
