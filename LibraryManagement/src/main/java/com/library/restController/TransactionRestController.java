package com.library.restController;

import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.library.model.custom.displayDto.FilteredResponse;
import com.library.model.custom.displayDto.Response;
import com.library.model.custom.insertDto.CheckInDto;
import com.library.model.custom.insertDto.TransactionDto;
import com.library.model.custom.insertDto.transactionSearch.TransactionSearchDto;
import com.library.service.TransactionService;

@CrossOrigin
@RestController
@RequestMapping(value = "transaction")
public class TransactionRestController
{	
	@Autowired
	private TransactionService transactionService;

	@CrossOrigin
	@RequestMapping(value = "checkout", method = RequestMethod.POST)
	public ResponseEntity<Response> checkoutBooks(@RequestBody TransactionDto transactionDtos)
	{
		return new ResponseEntity<>(transactionService.checkoutBooks(transactionDtos), HttpStatus.OK);
	}

	@CrossOrigin
	@RequestMapping(value = "checkin", method = RequestMethod.POST)
	public ResponseEntity<Response> checkinBooks(@RequestBody List<CheckInDto> checkInDtos)
	{
		return new ResponseEntity<>(transactionService.checkinBooks(checkInDtos), HttpStatus.OK);
	}
	
	@CrossOrigin
	@RequestMapping(value = "favorite/{memberId}", method = RequestMethod.POST)
	public ResponseEntity<Response> getFavoriteAuthor(@PathVariable Integer memberId)
	{
		return new ResponseEntity<>(transactionService.getTopAuthorsForAllMembers(memberId), HttpStatus.OK);
	}

	@CrossOrigin
	@RequestMapping(value = "sendingEmail", method = RequestMethod.POST)
	public ResponseEntity<Response> sendOverdueEmails(@RequestParam(required = false) Integer memberId) throws MessagingException
	{
		return new ResponseEntity<>(transactionService.getNotReturnedTransactionsAndSendMail(memberId), HttpStatus.OK);
	}
	
	@CrossOrigin
	@RequestMapping(value = "transactionsBySearchAndFilter", method = RequestMethod.POST)
	public ResponseEntity<FilteredResponse> getTransactionsBySearchAndFilter(@RequestBody TransactionSearchDto transactionSearchDto)
	{
		return new ResponseEntity<>(transactionService.getTransactionsBySearchAndFilter(transactionSearchDto), HttpStatus.OK);
	}
	
	@CrossOrigin
	@RequestMapping(value = "getMostBorrowedBooks", method = RequestMethod.POST)
	public ResponseEntity<Response> getMostBorrowedBooks(@RequestParam(required = false) Integer memberId,
			@RequestParam Integer start, @RequestParam Integer limit)
	{
		return new ResponseEntity<>(transactionService.getMostBorrowedBooks(memberId, start, limit), HttpStatus.OK);
	}
}
