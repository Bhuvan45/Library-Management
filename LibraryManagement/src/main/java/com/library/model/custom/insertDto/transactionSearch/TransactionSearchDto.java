package com.library.model.custom.insertDto.transactionSearch;

import com.library.model.custom.insertDto.Ord;

public class TransactionSearchDto
{
	private String searchBy;
	
    private String searchValue;
    
    private TransactionFilter filter;
    
    private Ord order;
    
    private Integer start;
    
    private Integer limit;

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

	public TransactionFilter getFilter() {
		return filter;
	}

	public void setFilter(TransactionFilter filter) {
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
}
