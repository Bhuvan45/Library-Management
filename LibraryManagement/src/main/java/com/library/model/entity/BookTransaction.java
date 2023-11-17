package com.library.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "book_transaction")
public class BookTransaction 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOTR_Transaction_Id")
    private Integer transactionId;
    
    @ManyToOne
    @JoinColumn(name = "BOTR_Member_Id")
    private MembershipMaintenance membershipMaintenance;

    @ManyToOne
    @JoinColumn(name = "BOTR_Book_Id")
    private BookMaintenance bookMaintenance;
    
    @Column(name = "BOTR_Borrowed_Count")
    private Integer borrowedCount;
    
    @Column(name = "BOTR_Returned_Count")
    private Integer returnedCount;

    @Column(name = "BOTR_Borrowed_Date")
    private Date borrowedDate;

    @Column(name = "BOTR_Return_Due_Date")
    private Date returnDueDate;
    
    @Column(name = "BOTR_Actual_Returned_Date")
    private Date actualReturnedDate;

    @Column(name = "BOTR_Rental_Status")
    private String rentalStatus;

    @Column(name = "BOTR_Created_Date", updatable = false)
    private Date createdDate;
    
    @Column(name = "BOTR_Updated_Date", updatable = false)
    private Date updatedDate;


	public Integer getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Integer transactionId) {
		this.transactionId = transactionId;
	}

	public MembershipMaintenance getMembershipMaintenance() {
		return membershipMaintenance;
	}

	public void setMembershipMaintenance(MembershipMaintenance membershipMaintenance) {
		this.membershipMaintenance = membershipMaintenance;
	}

	public BookMaintenance getBookMaintenance() {
		return bookMaintenance;
	}

	public void setBookMaintenance(BookMaintenance bookMaintenance){
		this.bookMaintenance = bookMaintenance;
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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}	
}
