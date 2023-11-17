package com.library.dao;

import java.util.List;
import java.util.Map;

import com.library.model.custom.displayDto.MemberTopAuthors;
import com.library.model.custom.displayDto.RetrivingDto;
import com.library.model.custom.insertDto.transactionSearch.TransactionSearchDto;
import com.library.model.entity.BookTransaction;

public interface TransactionDAO
{	
	Integer checkoutBook(BookTransaction bookTransaction);

	BookTransaction getTransactionsById(Integer transactionId);
	
	List<MemberTopAuthors> getTopAuthorsForAllMembers(Integer memberId);

	List<RetrivingDto> getNotReturnedTransactionsAndSendMail(Integer memberId);

	Map<String, Object> getTransactionsBySearchAndFilter(TransactionSearchDto transactionSearchDto);
}





