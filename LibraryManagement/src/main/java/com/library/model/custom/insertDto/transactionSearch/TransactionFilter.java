package com.library.model.custom.insertDto.transactionSearch;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.library.model.DateWithoutTimeDeserializer;

public class TransactionFilter
{	
	@JsonDeserialize(using = DateWithoutTimeDeserializer.class)
    private Date fromDate;
    
	@JsonDeserialize(using = DateWithoutTimeDeserializer.class)
    private Date toDate;
    
    private String rentalStatus;	

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String getRentalStatus() {
		return rentalStatus;
	}

	public void setRentalStatus(String rentalStatus) {
		this.rentalStatus = rentalStatus;
	}

}
