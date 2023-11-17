package com.library.model.custom.insertDto.memberSearch;

import com.library.model.custom.insertDto.Ord;

public class MemberSearchDto
{
	private MemberSearchFilter filter;
	
    private Ord order;
    
    private String searchBy;
    
    private String searchValue;
    
    private Integer minAge;
    
    private Integer maxAge;
	
	private Integer start;

	private Integer limit;

	public MemberSearchFilter getFilter() {
		return filter;
	}

	public void setFilter(MemberSearchFilter filter) {
		this.filter = filter;
	}

	public Ord getOrder() {
		return order;
	}

	public void setOrder(Ord order) {
		this.order = order;
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

	public Integer getMinAge() {
		return minAge;
	}

	public void setMinAge(Integer minAge) {
		this.minAge = minAge;
	}

	public Integer getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(Integer maxAge) {
		this.maxAge = maxAge;
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
