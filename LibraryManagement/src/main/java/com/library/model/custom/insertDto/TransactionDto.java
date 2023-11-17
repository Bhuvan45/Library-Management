package com.library.model.custom.insertDto;

import java.util.List;

public class TransactionDto 
{
    private Integer memberId;
    
    private List<BookDetails> bookDetails;   
    
	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	public List<BookDetails> getBookDetails() {
		return bookDetails;
	}

	public void setBookDetails(List<BookDetails> bookDetails) {
		this.bookDetails = bookDetails;
	}
	
}
