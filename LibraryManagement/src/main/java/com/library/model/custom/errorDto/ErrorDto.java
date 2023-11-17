package com.library.model.custom.errorDto;

public class ErrorDto 
{
	private String fieldName;
	
	private String errorMessage;

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
//	public ErrorDto(String fieldName, String errorMessage) {
//		super();
//		this.fieldName = fieldName;
//		this.errorMessage = errorMessage;
//	}
}
