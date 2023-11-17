package com.library.service;

import java.util.List;

import com.library.model.custom.displayDto.FilteredResponse;
import com.library.model.custom.displayDto.Response;
import com.library.model.custom.insertDto.BookDto;
import com.library.model.custom.insertDto.bookSearch.BookSearchDto;

public interface BookService
{	
	Response addOrUpdateBooks(BookDto bookDto);

	FilteredResponse getBookById(Integer memberId);

	Response getMostBorrowedBooks(Integer memberId, Integer start, Integer limit);

	FilteredResponse searchBooksBySearchValue(BookSearchDto searchValue);

	Response deleteBooksByIds(List<Integer> bookIds);
}
