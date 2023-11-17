package com.library.serviceImpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.dao.BookDAO;
import com.library.dao.MemberDAO;
import com.library.dao.TransactionDAO;
import com.library.model.custom.displayDto.FilteredResponse;
import com.library.model.custom.displayDto.MemberTopAuthors;
import com.library.model.custom.displayDto.MostBorrowed;
import com.library.model.custom.displayDto.Response;
import com.library.model.custom.displayDto.RetrivingDto;
import com.library.model.custom.errorDto.ErrorDto;
import com.library.model.custom.errorDto.TransactionResult;
import com.library.model.custom.insertDto.BookDetails;
import com.library.model.custom.insertDto.CheckInDto;
import com.library.model.custom.insertDto.TransactionDto;
import com.library.model.custom.insertDto.transactionSearch.TransactionSearchDto;
import com.library.model.entity.BookMaintenance;
import com.library.model.entity.BookTransaction;
import com.library.model.entity.MembershipMaintenance;
import com.library.service.TransactionService;
import com.library.util.WebUtil;

@Service
public class TransactionServiceImpl implements TransactionService
{
	@Autowired
	private TransactionDAO transactionDao;

	@Autowired
	private BookDAO bookDao;

	@Autowired
	private MemberDAO memberDao;
	
	@Autowired
	private EmailService emailService;

	@Override
	@Transactional
	public Response checkoutBooks(TransactionDto transactionDto)
	{
	    Response response = new Response();
	    List<TransactionResult> transactionResults = new ArrayList<>();
	    boolean overallTransaction = false;
	    
		for (BookDetails bookdetails : transactionDto.getBookDetails()) 
		{			
			TransactionResult transactionResult = new TransactionResult();
			
			BookMaintenance bookMaintenance = bookDao.getBookById(bookdetails.getBookId());
			MembershipMaintenance membershipMaintenance = memberDao.getMemberById(transactionDto.getMemberId());

			String errorMessage = ValidateTransaction.validateTransaction(bookdetails, transactionDto, bookMaintenance,
																					membershipMaintenance);
			if (errorMessage != null) 
			{
				transactionResult.setStatus(WebUtil.STATUS_F);
				transactionResult.setMessage(errorMessage);
			}

			else 
			{
				BookTransaction bookTransaction = new BookTransaction();
				if(bookdetails.getReturnDueDate().before(new Date()))
				{
					SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					response.setStatus(WebUtil.STATUS_F);
					response.setData("Return Due Date should be greater than or equal to " + dateFormat.format(new Date()));
					return response;
				}
				
				if (bookdetails.getReturnDueDate() == null)
				{
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(new Date());
					calendar.add(Calendar.DAY_OF_MONTH, 7);
					bookTransaction.setReturnDueDate(calendar.getTime());
				} 
				else 
				{
					bookTransaction.setReturnDueDate(bookdetails.getReturnDueDate());
				}

				bookTransaction.setMembershipMaintenance(membershipMaintenance);
				bookTransaction.setBookMaintenance(bookMaintenance);
				bookTransaction.setRentalStatus(WebUtil.BOOK_STATUS_BORROWED);
				bookTransaction.setBorrowedCount(bookdetails.getBorrowedCount());
				bookTransaction.setReturnedCount(WebUtil.DEFAULT_INT_VALUE);
				bookTransaction.setBorrowedDate(new Date());
				bookTransaction.setCreatedDate(new Date());
				bookTransaction.setUpdatedDate(new Date());

				bookMaintenance.setStockCount(bookMaintenance.getStockCount() - bookdetails.getBorrowedCount());
				bookMaintenance.setBorrowedCount(bookMaintenance.getBorrowedCount() + bookdetails.getBorrowedCount());
				bookMaintenance.setUpdatedDate(new Date());
				
				
				transactionDao.checkoutBook(bookTransaction);
				transactionResult.setMessage("Approval Granted for Book: " + bookMaintenance.getTitle());
				transactionResult.setStatus(WebUtil.STATUS_S);

				if (bookMaintenance.getStockCount() == 0)
				{
					bookMaintenance.setBookStatus(WebUtil.BOOK_STATUS_NOT_AVAILABLE);
				}	
			}
			transactionResults.add(transactionResult);
			overallTransaction = true;
		}
	    
	    if(overallTransaction)
	    	response.setStatus(WebUtil.STATUS_S);
	    else
	    	response.setStatus(WebUtil.STATUS_F);
	    
	    response.setData(transactionResults);
	    return response;
	}

	@Override
	@Transactional
	public Response checkinBooks(List<CheckInDto> checkInDtos)
	{
	    Response response = new Response();
	    Map<Integer, String> messages = new HashMap<>();
	    
	    for(CheckInDto checkInDto : checkInDtos)
	    {
	    	 BookTransaction bookTransaction = transactionDao.getTransactionsById(checkInDto.getTransactionId());
	         
	         if(bookTransaction != null && bookTransaction.getRentalStatus().equalsIgnoreCase(WebUtil.BOOK_STATUS_BORROWED))
	         {
	         	if(bookTransaction.getBorrowedCount() >= checkInDto.getReturnedCount())
	         	{
	         		bookTransaction.setReturnedCount(bookTransaction.getReturnedCount() + checkInDto.getReturnedCount());
	             	bookTransaction.setActualReturnedDate(new Date());
	             	if(bookTransaction.getBorrowedCount() == bookTransaction.getReturnedCount())
	             	{
	             		bookTransaction.setRentalStatus(WebUtil.BOOK_STATUS_RETURNED);
	             	}
	             	bookTransaction.setUpdatedDate(new Date());
	             	
	             	response.setStatus(WebUtil.STATUS_S);		             	
	             	messages.put(checkInDto.getTransactionId(), "Book Returned Successfully");
	         	}
	         	else
	         	{
	         		response.setStatus(WebUtil.STATUS_F);
	         		messages.put(checkInDto.getTransactionId(), "Specified Count should be less than or equal to borrowed Count");
	         	}        	
	         }
	         else
	         {
	         	response.setStatus(WebUtil.STATUS_F);
	         	messages.put(checkInDto.getTransactionId(), "No Transaction Found");
	         }
	    }
	    response.setData(messages);
	    return response;
	}

	@Override
	@Transactional
	public Response getTopAuthorsForAllMembers(Integer memberId) 
	{
		Response response = new Response();
		try
		{
			List<MemberTopAuthors> topAuthors =  transactionDao.getTopAuthorsForAllMembers(memberId);
			if (!topAuthors.isEmpty())
			{
				response.setData(topAuthors);
				response.setRecordsCount(topAuthors.size());
			} 
			else
			{
				response.setData("No Transactions Yet");
			}
			
			response.setStatus(WebUtil.STATUS_S);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			response.setData(e.getMessage());
			response.setStatus(WebUtil.STATUS_F);			
		}
		return response;
	}
	
	@Override
	@Transactional
	public Response getNotReturnedTransactionsAndSendMail(Integer memberId)
	{
		Response response = new Response();
		List<RetrivingDto> transactions = transactionDao.getNotReturnedTransactionsAndSendMail(memberId);

		Map<Integer, RetrivingDto> finalCombinedTransactions = new HashMap<>();

		for (RetrivingDto transaction : transactions)
		{

		    if (finalCombinedTransactions.containsKey(transaction.getMemberId()))
		    {
		        RetrivingDto combinedTransaction = finalCombinedTransactions.get(memberId);
		        
		        combinedTransaction.setAuthor(combinedTransaction.getAuthor() +", "+ transaction.getAuthor());
		        combinedTransaction.setTitle(combinedTransaction.getTitle() +", "+ transaction.getTitle());
		        combinedTransaction.setLanguage(combinedTransaction.getLanguage() +", "+ transaction.getLanguage());
		    } 
		    else
		    {
		        RetrivingDto combinedTransaction = new RetrivingDto();
		        
		        combinedTransaction.setMemberId(memberId);
		        combinedTransaction.setFirstName(transaction.getFirstName());
		        combinedTransaction.setLastName(transaction.getLastName());
		        combinedTransaction.setEmail(transaction.getEmail());
		        combinedTransaction.setAuthor(transaction.getAuthor());
		        combinedTransaction.setTitle(transaction.getTitle());
		        combinedTransaction.setLanguage(transaction.getLanguage());		
		        
		        finalCombinedTransactions.put(memberId, combinedTransaction);
		    }
		}
		for (RetrivingDto finalCombinedTransaction : finalCombinedTransactions.values()) 
		{
			try
			{
				emailService.sendEmail(finalCombinedTransaction);
			}
			catch (MessagingException e)
			{
				response.setStatus(WebUtil.STATUS_F);
				response.setData("Error occurs in sending mails");
			}
		}
		
		response.setStatus(WebUtil.STATUS_S);
		response.setData("Mail Sent Sucessfully");
		return response;
	}
	
	@Override
	@Transactional
	public FilteredResponse getTransactionsBySearchAndFilter(TransactionSearchDto transactionSearchDto)
	{
		FilteredResponse filteredResponse = new FilteredResponse();
		
		if(transactionSearchDto.getFilter() != null)
		{
			if(transactionSearchDto.getFilter().getFromDate() != null 
					&& transactionSearchDto.getFilter().getToDate() == null)
			{
				filteredResponse.setStatus(WebUtil.STATUS_F);
				filteredResponse.setData("End Date should not be null, when Start Date is provided.");
			}
			
			else if (transactionSearchDto.getFilter().getFromDate() == null && 
					transactionSearchDto.getFilter().getToDate() != null)
			{
			    filteredResponse.setStatus(WebUtil.STATUS_F);
			    filteredResponse.setData("Start Date should not be null, when End Date is provided.");
			}
			
			else if (transactionSearchDto.getFilter().getFromDate() != null 
					&& transactionSearchDto.getFilter().getToDate() != null 
					&& transactionSearchDto.getFilter().getFromDate().after(transactionSearchDto.getFilter().getToDate()))
			{
			    filteredResponse.setStatus(WebUtil.STATUS_F);
			    filteredResponse.setData("End Date should be greater than or equal to Start Date.");
			}
		}
		
		if(filteredResponse.getStatus() != null && filteredResponse.getStatus().equalsIgnoreCase(WebUtil.STATUS_F))
		{
			return filteredResponse;
		}
		
		List<ErrorDto> errorList = new ArrayList<ErrorDto>();
		FieldValidation.validateStartAndLimitField(transactionSearchDto.getStart(),
				transactionSearchDto.getLimit(), errorList);
		if(!errorList.isEmpty())
		{
			filteredResponse.setStatus(WebUtil.STATUS_F);
			filteredResponse.setData(errorList);
			return filteredResponse;
		}
		
		Long filteredRecords = 0L;
		
		Map<String, Object> transactions = transactionDao.getTransactionsBySearchAndFilter(transactionSearchDto);
		
		if ((filteredRecords = (Long) transactions.get("filteredRecords")) > 0) 
		{
			filteredResponse.setData(transactions.get("data"));
			filteredResponse.setFilteredRecords(filteredRecords);
			filteredResponse.setTotalRecords((Long) transactions.get("totalRecords"));
		} 
		else
		{
			filteredResponse.setData("No books are in borrowed status");
		}
		filteredResponse.setStatus(WebUtil.STATUS_S);
		return filteredResponse;
	}
	
	@Override
	@Transactional
	public Response getMostBorrowedBooks(Integer memberId, Integer start, Integer limit)
	{
		Response response = new Response();
		List<ErrorDto> errorList = new ArrayList<ErrorDto>();	

		try
		{
			FieldValidation.validateStartAndLimitField(start, limit, errorList);
			if(!errorList.isEmpty())
			{
				response.setStatus(WebUtil.STATUS_F);
				response.setData(errorList);
				return response;
			}
			else
			{
				List<MostBorrowed> books = bookDao.getMostBorrowedBooks(memberId, start, limit);
				if(books.size() > 0)
				{
					response.setData(books);
					response.setRecordsCount(books.size());
				}
				else
				{
					response.setData("No Books Found");
				}
				response.setStatus(WebUtil.STATUS_S);
			}
		} 
		catch (Exception e) 
		{
			response.setStatus(WebUtil.STATUS_F);
			response.setData("Error occurred while fetching");
			throw e;
		}
		return response;
	}

}
