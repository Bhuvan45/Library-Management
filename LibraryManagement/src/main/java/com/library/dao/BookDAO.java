package com.library.dao;

import java.util.List;
import java.util.Map;

import com.library.model.custom.displayDto.MostBorrowed;
import com.library.model.custom.insertDto.bookSearch.BookSearchDto;
import com.library.model.entity.BookMaintenance;

public interface BookDAO 
{	
	Integer addBook(BookMaintenance bookMaintenance);

	BookMaintenance getBookById(Integer memberId);

	List<MostBorrowed> getMostBorrowedBooks(Integer memberId, Integer start, Integer limit);

	Integer isDuplicateBook(String author, String title, String language);

	Map<String, Object> getBookDetailsById(Integer memberId);

	Map<String, Object> searchBooksBySearchValue(BookSearchDto searchValue);
}
