package com.library.serviceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.library.model.custom.errorDto.ErrorDto;
import com.library.model.custom.insertDto.BookDto;
import com.library.model.custom.insertDto.MemberDto;
import com.library.model.entity.BookMaintenance;
import com.library.model.entity.MembershipMaintenance;
import com.library.util.ValidationUtil;
import com.library.util.WebUtil;

public class FieldValidation 
{
	public static List<ErrorDto> validateMemberFields(List<ErrorDto> errorList, MemberDto memberDto, MembershipMaintenance membershipMaintenance) 
	{
		
	    if(memberDto.getFirstName() == null || memberDto.getFirstName().isEmpty() || memberDto.getFirstName().isBlank() || 
	    		!ValidationUtil.isValidName(memberDto.getFirstName()))
		{
			ErrorDto errorMessage = new ErrorDto();
            errorMessage.setFieldName("firstName");
            errorMessage.setErrorMessage("First Name Should contain only Alphabets or should not be null");
            errorList.add(errorMessage);
		}
		
		if(memberDto.getLastName() == null || memberDto.getLastName().isEmpty() || memberDto.getLastName().isBlank() ||
				!ValidationUtil.isValidName(memberDto.getLastName()))
		{
			ErrorDto errorMessage = new ErrorDto();
			errorMessage.setFieldName("lastName");
            errorMessage.setErrorMessage("Last Name Should contain only Alphabets or should not be null");
			errorList.add(errorMessage);
		}
		
		if (Integer.toString(memberDto.getAge()).isEmpty() || Integer.toString(memberDto.getAge()) == null
				|| (memberDto.getAge() < 12 && memberDto.getAge() > 100))
		{
			ErrorDto errorMessage = new ErrorDto();
			errorMessage.setFieldName("age");
			errorMessage.setErrorMessage("Age should be greater than or equal to 12 and less than 100");
			errorList.add(errorMessage);
		}

		if (memberDto.getGender().isEmpty() || memberDto.getGender() == null || memberDto.getGender().isBlank() || 
				! Arrays.asList("male", "female", "other").contains(memberDto.getGender().toLowerCase()))
		{
			ErrorDto errorMessage = new ErrorDto();
			errorMessage.setFieldName("gender");
			errorMessage.setErrorMessage("Gender should be 'male', 'female', or 'other'");
			errorList.add(errorMessage);
		}
		  
		if(memberDto.getEmail().isEmpty() || memberDto.getEmail() == null || memberDto.getEmail().isBlank() ||
				! ValidationUtil.isValidEmail(memberDto.getEmail()))
		{
			ErrorDto errorMessage = new ErrorDto();
			errorMessage.setFieldName("email");
			errorMessage.setErrorMessage("Invalid Email Address");
			errorList.add(errorMessage);
		}
		
		if(memberDto.getMobileNumber().isEmpty() || memberDto.getMobileNumber() == null || memberDto.getMobileNumber().isBlank() ||
				!ValidationUtil.isValidPhoneNumber(memberDto.getMobileNumber()))
		{
			ErrorDto errorMessage = new ErrorDto();
			errorMessage.setFieldName("mobileNumber");
			errorMessage.setErrorMessage("Mobile should contain only Numbers with 10 digits");
			errorList.add(errorMessage);		
		}			
	
		if (memberDto.getWorkStatus().isEmpty() || memberDto.getWorkStatus() == null || memberDto.getWorkStatus().isBlank() ||
				! Arrays.asList("student", "employee", "senior citizen", "housewife").contains(memberDto.getWorkStatus().toLowerCase()))
		{
		    ErrorDto errorMessage = new ErrorDto();
		    errorMessage.setFieldName("workStatus");
		    errorMessage.setErrorMessage("Invalid work status");
		    errorList.add(errorMessage);
		} 

		if (memberDto.getAddressLine1().isEmpty() || memberDto.getAddressLine1() == null || memberDto.getAddressLine1().isBlank() ||
				!ValidationUtil.isValidAddressLine(memberDto.getAddressLine1()))
		{
		    ErrorDto errorMessage = new ErrorDto();
		    errorMessage.setFieldName("addressLine1");
		    errorMessage.setErrorMessage("Invalid address or should not be null");
		    errorList.add(errorMessage);
		} 

		if (!ValidationUtil.isValidAddressLine(memberDto.getAddressLine2())) 
		{
		    ErrorDto errorMessage = new ErrorDto();
		    errorMessage.setFieldName("addressLine2");
		    errorMessage.setErrorMessage("Invalid address");
		    errorList.add(errorMessage);
		} 
		
		if (memberDto.getMembershipEndDate() != null && !memberDto.getAddressLine1().isBlank() && memberDto.getMembershipEndDate().before(new Date()))
		{
	        ErrorDto errorMessage = new ErrorDto();
	        errorMessage.setFieldName("membershipEndDate");
	        errorMessage.setErrorMessage("Membership end date should be in the future or should be null");
	        errorList.add(errorMessage);
		}
		if (errorList.isEmpty()) 
		{
			membershipMaintenance.setFirstName(WebUtil.formatFullName(memberDto.getFirstName()));	
			membershipMaintenance.setLastName(WebUtil.formatFullName(memberDto.getLastName()));
			membershipMaintenance.setAge(memberDto.getAge());
			membershipMaintenance.setGender(memberDto.getGender());
			membershipMaintenance.setEmail(memberDto.getEmail());
			membershipMaintenance.setMobileNumber(memberDto.getMobileNumber());
			membershipMaintenance.setWorkStatus(memberDto.getWorkStatus());
		    membershipMaintenance.setAddressLine1(memberDto.getAddressLine1());
		    membershipMaintenance.setAddressLine2(memberDto.getAddressLine2());
		    membershipMaintenance.setMembershipEndDate(memberDto.getMembershipEndDate());
		}		
		return errorList;
	}
	
	 public static List<ErrorDto> validateBookFields(BookDto bookDto, BookMaintenance bookMaintenance)
	 {
        List<ErrorDto> errorList = new ArrayList<ErrorDto>();

        if (bookDto.getTitle() == null || bookDto.getTitle().isEmpty() || bookDto.getTitle().isBlank() ||
        		!ValidationUtil.isValidTitle(bookDto.getTitle())) 
        {
        	ErrorDto errorMessage = new ErrorDto();
            errorMessage.setFieldName("title");
            errorMessage.setErrorMessage("Title Should contain only Alphabets and should not be null");
            errorList.add(errorMessage);
        }

        if (bookDto.getAuthor() == null || bookDto.getAuthor().isEmpty() ||  bookDto.getAuthor().isBlank() ||
        		!ValidationUtil.isValidName(bookDto.getAuthor())) 
        {
        	ErrorDto errorMessage = new ErrorDto();
            errorMessage.setFieldName("author");
            errorMessage.setErrorMessage("Author Name Should contain only Alphabets and should not be null");
            errorList.add(errorMessage);
        }
        
        if (bookDto.getLanguage() == null || bookDto.getLanguage().isEmpty() || bookDto.getLanguage().isBlank()
        		|| !ValidationUtil.isValidName(bookDto.getLanguage())) 
        {
        	ErrorDto errorMessage = new ErrorDto();
            errorMessage.setFieldName("language");
            errorMessage.setErrorMessage("Language Should contain only Alphabets and should not be null");
            errorList.add(errorMessage);
        }
        
        if (bookDto.getStockCount() == null || bookDto.getStockCount() == 0
				|| !ValidationUtil.isContainsOnlyNumber(bookDto.getStockCount())) 
		{
			ErrorDto errorMessage = new ErrorDto();
			errorMessage.setFieldName("stockCount");
			errorMessage.setErrorMessage("Stock Count should be greater than 0");
			errorList.add(errorMessage);
		}
        
        if(errorList.isEmpty())
        {
            bookMaintenance.setTitle(WebUtil.formatFullName(bookDto.getTitle()));
            bookMaintenance.setAuthor(WebUtil.formatFullName(bookDto.getAuthor()));
            bookMaintenance.setLanguage(WebUtil.formatFullName(bookDto.getLanguage()));
        }
        
        return errorList;
	}
	 
	public static List<ErrorDto> validateStartAndLimitField(Integer start, Integer limit, List<ErrorDto> errorList)
	{
		if (start == null || start < 0) 
		{
	        ErrorDto error = new ErrorDto();
	        error.setFieldName("start");
	        error.setErrorMessage("Start value should be greater than or equal to zero");
	        errorList.add(error);
	    }

	    if (limit == null || limit <= 0)
	    {
	        ErrorDto error = new ErrorDto();
	        error.setFieldName("limit");
	        error.setErrorMessage("Limit value should be greater than zero.");
	        errorList.add(error);
	    }
	    return errorList;		   
	}
		  
}
