package com.library.model.custom.displayDto;

public class Response 
{
	private String status;
	
	private Integer recordsCount;
	
	private Object data;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Integer getRecordsCount() {
		return recordsCount;
	}

	public void setRecordsCount(Integer recordsCount) {
		this.recordsCount = recordsCount;
	}
	

}
