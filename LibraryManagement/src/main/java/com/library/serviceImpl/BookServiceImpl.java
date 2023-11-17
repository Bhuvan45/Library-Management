package com.library.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.dao.BookDAO;
import com.library.model.custom.displayDto.FilteredResponse;
import com.library.model.custom.displayDto.MostBorrowed;
import com.library.model.custom.displayDto.Response;
import com.library.model.custom.errorDto.ErrorDto;
import com.library.model.custom.insertDto.BookDto;
import com.library.model.custom.insertDto.bookSearch.BookSearchDto;
import com.library.model.entity.BookMaintenance;
import com.library.service.BookService;
import com.library.util.WebUtil;

@Service
public class BookServiceImpl implements BookService 
{
	@Autowired
	private BookDAO bookDao;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BookServiceImpl.class);

	@Override
	@Transactional
	public Response addOrUpdateBooks(BookDto bookDto)
	{
		Response response = new Response();
		BookMaintenance bookMaintenance = new BookMaintenance();
		if (bookDto.getBookId() == null) 
		{
			Integer existBookDetail = bookDao.isDuplicateBook(WebUtil.formatFullName(bookDto.getAuthor()),
														  WebUtil.formatFullName(bookDto.getTitle()),
														  WebUtil.formatFullName(bookDto.getLanguage()));
			
			List<ErrorDto> errorList = FieldValidation.validateBookFields(bookDto, bookMaintenance);			

			if (existBookDetail != null)
			{
				bookMaintenance = bookDao.getBookById(existBookDetail);					
				bookMaintenance.setStockCount(bookMaintenance.getStockCount() + bookDto.getStockCount());
				bookMaintenance.setUpdatedDate(new Date());
				
				response.setStatus(WebUtil.STATUS_S);				
				response.setData("Stock Updated Successfully with book ID: " +bookMaintenance.getBookId());
		    }
			else
			{	
				if (errorList.isEmpty()) 
				{
					bookMaintenance.setStockCount(bookDto.getStockCount());
					bookMaintenance.setRegisteredDate(new Date());
					bookMaintenance.setBookStatus(WebUtil.BOOK_STATUS_AVAILABLE);
					bookMaintenance.setInvalidFlag(WebUtil.INVALID_NO);
					bookMaintenance.setBorrowedCount(WebUtil.DEFAULT_INT_VALUE);
					bookMaintenance.setCreatedDate(new Date());
					bookMaintenance.setUpdatedDate(new Date());
					
					int id = bookDao.addBook(bookMaintenance);
					response.setStatus(WebUtil.STATUS_S);
					response.setData("Book Added Succesfully with ID: " + id);
				}
				else
				{
					response.setStatus(WebUtil.STATUS_F);
					response.setData(errorList);
				}		 
			}
			return response;
		}
		else
		{			
			Integer existingBookId = bookDao.isDuplicateBook(
			        WebUtil.formatFullName(bookDto.getAuthor()),
			        WebUtil.formatFullName(bookDto.getTitle()),
			        WebUtil.formatFullName(bookDto.getLanguage()));
			
			if(existingBookId != null && existingBookId != bookDto.getBookId())
			{
				  response.setStatus(WebUtil.STATUS_F);
	              response.setData("A book with this title, language, author already exists");
	              return response;
			}
			bookMaintenance = bookDao.getBookById(bookDto.getBookId());
	        if (bookMaintenance != null)
	        {
	            List<ErrorDto> errorList = FieldValidation.validateBookFields(bookDto, bookMaintenance);

	            if (errorList.isEmpty())
	            {
	                bookMaintenance.setStockCount(bookDto.getStockCount() + bookMaintenance.getStockCount());
	                bookMaintenance.setUpdatedDate(new Date());
	                
	                response.setStatus(WebUtil.STATUS_S);
	                response.setData("Book Details Updated Successfully for ID: " + bookMaintenance.getBookId());
	            }
	            else
	            {
	                response.setStatus(WebUtil.STATUS_F);
	                response.setData(errorList);
	            }
	        }
	        else 
	        {
	            response.setStatus(WebUtil.STATUS_F);
	            response.setData("Book not Found");
	        }
	    }
	    return response;
	}
	
	@Override
	@Transactional
	public FilteredResponse getBookById(Integer bookId)
	{
		FilteredResponse filteredResponse = new FilteredResponse(); 
		try
		{
			Map<String, Object> books = bookDao.getBookDetailsById(bookId);	
			
			Long filteredRecords;
			if((filteredRecords = (Long) books.get("filteredRecords")) > 0)
			{
				filteredResponse.setData(books.get("data"));
				filteredResponse.setFilteredRecords(filteredRecords);
				filteredResponse.setTotalRecords((Long) books.get("totalRecords"));
			}
			else
			{
				filteredResponse.setData("No books Found");
			}
			filteredResponse.setStatus(WebUtil.STATUS_S);
		}
		catch (Exception e)
		{
			 filteredResponse.setStatus(WebUtil.STATUS_F);
	         filteredResponse.setData("Error occured while fetching");
		}
		return filteredResponse;
	}

	@Override
	@Transactional
	public FilteredResponse searchBooksBySearchValue(BookSearchDto bookSearchDto) 
	{
		FilteredResponse filteredResponse = new FilteredResponse(); 
		List<ErrorDto> errorList = new ArrayList<ErrorDto>();
		try
		{
			if(bookSearchDto.getFilter() != null)
			{
				ErrorDto error = new ErrorDto();
				if(bookSearchDto.getFilter().getFromDate() != null 
						&& bookSearchDto.getFilter().getToDate() == null)
				{
					error.setFieldName("membershipEndDate");
					error.setErrorMessage("End Date should not be null, when Start Date is provided.");
					errorList.add(error);
					filteredResponse.setStatus(WebUtil.STATUS_F);
				    filteredResponse.setData(errorList);
				}
				
				else if (bookSearchDto.getFilter().getFromDate() != null 
						&& bookSearchDto.getFilter().getToDate() != null 
						&& bookSearchDto.getFilter().getFromDate().after(bookSearchDto.getFilter().getToDate()))
				{
					error.setFieldName("membershipEndDate");
					error.setErrorMessage("End Date should be greater than or equal to Start Date.");
					errorList.add(error);
					filteredResponse.setStatus(WebUtil.STATUS_F);
				    filteredResponse.setData(errorList);
				}
			}
			if(! bookSearchDto.getSearchBy().equalsIgnoreCase("title") 
					&& ! bookSearchDto.getSearchBy().equalsIgnoreCase("bookId"))
			{
				 ErrorDto error = new ErrorDto();
				 error.setFieldName("searchBy");
				 error.setErrorMessage("Search By should be BookID or Title");
				 errorList.add(error);
				 filteredResponse.setStatus(WebUtil.STATUS_F);
				 filteredResponse.setData(errorList);
			}
			
			FieldValidation.validateStartAndLimitField(bookSearchDto.getStart(),
					bookSearchDto.getLimit(), errorList);
			
			if((filteredResponse.getStatus() != null && filteredResponse.getStatus().equals(WebUtil.STATUS_F))
					|| !errorList.isEmpty())
			{
				filteredResponse.setStatus(WebUtil.STATUS_F);
				filteredResponse.setData(errorList);
				return filteredResponse;
			}			
			
			Long filteredRecords;
			Map<String, Object> books = bookDao.searchBooksBySearchValue(bookSearchDto);
			
			if((filteredRecords = (Long) books.get("filteredRecords")) > 0)
			{
				filteredResponse.setData( books.get("data"));
				filteredResponse.setFilteredRecords(filteredRecords);
				filteredResponse.setTotalRecords((Long) books.get("totalRecords"));
			}
			else
			{
				filteredResponse.setData("No Books Found");
			}
			filteredResponse.setStatus(WebUtil.STATUS_S);
		}
		catch (Exception e)
		{
			 filteredResponse.setStatus(WebUtil.STATUS_F);
	         filteredResponse.setData("Error occured while fetching books");
		}
		return filteredResponse;
	}

	@Override
	@Transactional
	public Response getMostBorrowedBooks(Integer memberId, Integer start, Integer limit)
	{
		LOGGER.info("Viewing the Most Borrowed Books For Member ID: {}", memberId);

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

	@Override
	@Transactional
	public Response deleteBooksByIds(List<Integer> bookIds)
	{
	    Response response = new Response();

	    Map<Integer, String> booksDeleted = new HashMap<>();
	    Map<Integer, String> alreadyDeleted = new HashMap<>();
	    Map<Integer, String> cannotDelete = new HashMap<>();

	    try 
	    {
	        for (Integer bookId : bookIds)
	        {
	            BookMaintenance bookMaintenance = bookDao.getBookById(bookId);

	            if (bookMaintenance == null) 
	            {
	                cannotDelete.put(bookId, "No Book Found");
	            }
	            else if (bookMaintenance.getBookStatus().equalsIgnoreCase(WebUtil.BOOK_STATUS_DELETED))
	            {
	                alreadyDeleted.put(bookId, "Already Deleted");
	            }
	            else if (bookMaintenance.getBorrowedCount() > 0)
	            {
	                cannotDelete.put(bookId, "Book cannot be deleted. It has active transactions");
	            } 
	            else
	            {
	                bookMaintenance.setDeletedDate(new Date());
	                bookMaintenance.setInvalidFlag(WebUtil.INVALID_YES);
	                bookMaintenance.setBookStatus(WebUtil.BOOK_STATUS_DELETED);
	                bookMaintenance.setStockCount(WebUtil.DEFAULT_INT_VALUE);
	                bookMaintenance.setUpdatedDate(new Date());
	                booksDeleted.put(bookId, "Book Deleted");
	            }
	        }

	        Map<Integer, String> combinedResults = new HashMap<>();
	        combinedResults.putAll(booksDeleted);
	        combinedResults.putAll(alreadyDeleted);
	        combinedResults.putAll(cannotDelete);

	        response.setData(combinedResults);

	        if (!cannotDelete.isEmpty()) 
	        {
	            response.setStatus(WebUtil.STATUS_F);
	        }
	        else 
	        {
	            response.setStatus(WebUtil.STATUS_S);
	        }
	    } 
	    catch (Exception e)
	    {
	        response.setStatus(WebUtil.STATUS_F);
	        response.setData("Error occurred during deletion");
	    }
	    return response;
	}
}
