package com.library.model.custom.displayDto;

import java.util.List;

public class MemberTopAuthors
{
	private Integer memberId;
	
	private String author;
	
	private Long borrowedCount;
	
	private List<BookInfo> bookInfo;
	

	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Long getBorrowedCount() {
		return borrowedCount;
	}

	public void setBorrowedCount(Long borrowedCount) {
		this.borrowedCount = borrowedCount;
	}

	public List<BookInfo> getBookInfo() {
		return bookInfo;
	}

	public void setBookInfo(List<BookInfo> bookInfo) {
		this.bookInfo = bookInfo;
	}
	
	
}

