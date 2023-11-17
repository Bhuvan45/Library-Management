package com.library.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import com.library.model.custom.insertDto.BookDetails;
import com.library.model.custom.insertDto.TransactionDto;
import com.library.model.entity.BookMaintenance;
import com.library.model.entity.MembershipMaintenance;
import com.library.util.WebUtil;

public class ValidateTransaction 
{
	public static String validateTransaction(BookDetails bookdetails, TransactionDto transactionDto,
			BookMaintenance bookMaintenance, MembershipMaintenance membershipMaintenance) 
	{
	    List<String> errorMessages = new ArrayList<>();

	    if (membershipMaintenance == null) 
	    {
	        errorMessages.add("No Member found with ID: " + transactionDto.getMemberId());
	    }
	    if (bookMaintenance == null)
	    {
	        errorMessages.add("No Book found with ID: " + bookdetails.getBookId());
	    }
	    if (errorMessages.isEmpty()) 
	    {
	        if (!membershipMaintenance.getMembershipStatus().equalsIgnoreCase(WebUtil.MEMBER_STATUS_ACTIVE)) 
	        {
	            errorMessages.add("Member " + membershipMaintenance.getFirstName() + " " + membershipMaintenance.getLastName() + " is in Inactive Status");
	        }
	        if (bookMaintenance.getBookStatus().equalsIgnoreCase(WebUtil.BOOK_STATUS_AVAILABLE))
	        {
	            if (bookdetails.getBorrowedCount() > bookMaintenance.getStockCount()) 
	            {
	                errorMessages.add("Borrowed Count exceeds available stock for Book: " + bookMaintenance.getTitle());
	            }
	        } 
	        else
	        {
	            errorMessages.add(bookMaintenance.getTitle() + " is Currently Out of Stock");
	        }
	    }

	    return errorMessages.isEmpty() ? null : String.join("\n", errorMessages);
	}

}
