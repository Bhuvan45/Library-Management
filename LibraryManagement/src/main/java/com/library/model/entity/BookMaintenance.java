package com.library.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "book_maintenance")
public class BookMaintenance
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOMA_Book_Id")
    private Integer bookId;    

    @Column(name = "BOMA_Book_Title")
    private String title;

    @Column(name = "BOMA_Book_Language")
    private String language;

    @Column(name = "BOMA_Book_Author")
    private String author;
    
    @Column(name = "BOMA_Book_Registered_Date")
    private Date registeredDate;

    @Column(name = "BOMA_Book_Deleted_Date", insertable = false)
    private Date deletedDate;

	@Column(name = "BOMA_Book_Status")
    private String bookStatus;
	
	@Column(name = "BOMA_Stock_Count")
    private Integer stockCount;

    @Column(name = "BOMA_Borrowed_Count")
    private Integer borrowedCount;
    
    @Column(name = "BOMA_Invalid_Flag")	
    private Character invalidFlag;

    @Column(name = "BOMA_Created_Date", updatable = false)
    private Date createdDate;
    
    @Column(name = "BOMA_Updated_Date")
    private Date updatedDate;   

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

	public Date getDeletedDate() {
		return deletedDate;
	}

	public void setDeletedDate(Date deletedDate) {
		this.deletedDate = deletedDate;
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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Character getInvalidFlag() {
		return invalidFlag;
	}

	public void setInvalidFlag(Character invalidFlag) {
            this.invalidFlag = invalidFlag; 
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}	
}
