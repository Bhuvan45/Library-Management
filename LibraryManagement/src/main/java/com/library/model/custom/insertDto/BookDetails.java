package com.library.model.custom.insertDto;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.library.model.DateDeserializer;
import com.library.model.DateSerialize;

public class BookDetails
{
	private Integer bookId;
    
    private Integer borrowedCount;
    
    private Integer returnedCount;
    
    @JsonDeserialize(using = DateDeserializer.class)
	@JsonSerialize(using = DateSerialize.class)
    private Date borrowedDate;
    
    @JsonDeserialize(using = DateDeserializer.class)
	@JsonSerialize(using = DateSerialize.class)
    private Date returnDueDate;
    
    @JsonDeserialize(using = DateDeserializer.class)
	@JsonSerialize(using = DateSerialize.class)
    private Date actualReturnedDate;
    
    private String rentalStatus;

	public Integer getBookId() {
		return bookId;
	}

	public void setBookId(Integer bookId) {
		this.bookId = bookId;
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

	public Date getBorrowedDate() {
		return borrowedDate;
	}

	public void setBorrowedDate(Date borrowedDate) {
		this.borrowedDate = borrowedDate;
	}

	public Date getReturnDueDate() {
		return returnDueDate;
	}

	public void setReturnDueDate(Date returnDueDate) {
		this.returnDueDate = returnDueDate;
	}

	public Date getActualReturnedDate() {
		return actualReturnedDate;
	}

	public void setActualReturnedDate(Date actualReturnedDate) {
		this.actualReturnedDate = actualReturnedDate;
	}

	public String getRentalStatus() {
		return rentalStatus;
	}

	public void setRentalStatus(String rentalStatus) {
		this.rentalStatus = rentalStatus;
	}
    
    

}
