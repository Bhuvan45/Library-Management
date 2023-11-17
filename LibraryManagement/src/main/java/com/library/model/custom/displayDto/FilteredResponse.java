package com.library.model.custom.displayDto;

public class FilteredResponse
{
	private String status;
	
	private Long totalRecords;
	
	private Long filteredRecords;
	
	private Object data;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(Long totalRecords) {
		this.totalRecords = totalRecords;
	}

	public Long getFilteredRecords() {
		return filteredRecords;
	}

	public void setFilteredRecords(Long filteredRecords) {
		this.filteredRecords = filteredRecords;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
