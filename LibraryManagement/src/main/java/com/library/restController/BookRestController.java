package com.library.restController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.library.model.custom.displayDto.FilteredResponse;
import com.library.model.custom.displayDto.Response;
import com.library.model.custom.insertDto.BookDto;
import com.library.model.custom.insertDto.bookSearch.BookSearchDto;
import com.library.service.BookService;

@CrossOrigin
@RestController
@RequestMapping(value = "/book")
public class BookRestController
{	
	@Autowired
	private BookService bookService;
	
	/**
	 * This API is used to add or update books.
	 * 
	 * @param bookDto
	 * @return
	 */
	
	@CrossOrigin
	@RequestMapping(value = "addOrUpdateBook", method = RequestMethod.POST)
	public ResponseEntity<Response> addingBooks(@RequestBody BookDto bookDto ) 
	{
		return new ResponseEntity<>(bookService.addOrUpdateBooks(bookDto), HttpStatus.OK);
	}
	
	/**
	 * This API is used to get book by it's ID.
	 * 
	 * @param bookId
	 * @return
	 */
	
	@CrossOrigin
	@RequestMapping(value = "getBookById", method = RequestMethod.GET) 
	public ResponseEntity<FilteredResponse> getBookById(@RequestParam Integer bookId)
	{
		return new ResponseEntity<>(bookService.getBookById(bookId), HttpStatus.OK);	
	}
	
	@CrossOrigin
	@RequestMapping(value = "getBooksBySearch", method = RequestMethod.POST)
	public ResponseEntity<FilteredResponse> searchBooksBySearchValue(@RequestBody BookSearchDto searchValueDto)
	{
		return new ResponseEntity<>(bookService.searchBooksBySearchValue(searchValueDto), HttpStatus.OK);
	}

	@CrossOrigin
	@RequestMapping(value = "getMostBorrowedBooks", method = RequestMethod.GET)
	public ResponseEntity<Response> getMostBorrowedBooks(@RequestParam(required = false) Integer memberId,
			@RequestParam Integer start, @RequestParam Integer limit)
	{
		return new ResponseEntity<>(bookService.getMostBorrowedBooks(memberId, start, limit), HttpStatus.OK);
	}

	@CrossOrigin
	@RequestMapping(value = "deleteBook", method = RequestMethod.POST)
	public ResponseEntity<Response> deleteBooks(@RequestParam List<Integer> bookIds)
	{
		return new ResponseEntity<>(bookService.deleteBooksByIds(bookIds), HttpStatus.OK);
	}
}
