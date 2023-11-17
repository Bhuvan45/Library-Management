package com.library.model.custom.insertDto.bookSearch;

import com.library.model.custom.insertDto.Ord;

public class BookSearchDto
{	
	private BookSearchFilter filter;
	
    private Ord order;
    
    private String searchBy;
    
    private String searchValue;
	
	private Integer start;

	private Integer limit;
	
	public BookSearchFilter getFilter() {
		return filter;
	}

	public void setFilter(BookSearchFilter filter) {
		this.filter = filter;
	}

	public Ord getOrder() {
		return order;
	}

	public void setOrder(Ord order) {
		this.order = order;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public String getSearchBy() {
		return searchBy;
	}

	public void setSearchBy(String searchBy) {
		this.searchBy = searchBy;
	}

	public String getSearchValue() {
		return searchValue;
	}

	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}
	
}
