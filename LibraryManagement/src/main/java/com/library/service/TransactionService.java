package com.library.service;

import java.util.List;

import javax.mail.MessagingException;

import com.library.model.custom.displayDto.FilteredResponse;
import com.library.model.custom.displayDto.Response;
import com.library.model.custom.insertDto.CheckInDto;
import com.library.model.custom.insertDto.TransactionDto;
import com.library.model.custom.insertDto.transactionSearch.TransactionSearchDto;

public interface TransactionService 
{	
	Response checkoutBooks(TransactionDto transactionDtos);

	Response checkinBooks(List<CheckInDto> checkInDtos);

	Response getTopAuthorsForAllMembers(Integer memberId);

	Response getNotReturnedTransactionsAndSendMail(Integer memberId) throws MessagingException;

	FilteredResponse getTransactionsBySearchAndFilter(TransactionSearchDto transactionSearchDto);

	Response getMostBorrowedBooks(Integer memberId, Integer start, Integer limit);
}

