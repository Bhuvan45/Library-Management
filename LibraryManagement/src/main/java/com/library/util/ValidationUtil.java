package com.library.util;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtil 
{
	public static boolean isValidName(String name) 
	{
        Pattern pattern = Pattern.compile("^[A-Za-z\\s]+$");
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }
	
	public static boolean isValidEmail(String email)
	{
		Pattern pattern = Pattern.compile("^[A-Za-z0-9.-]+@([a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$");
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}
	public static boolean isValidPhoneNumber(String phoneNumber)
	{
		Pattern pattern = Pattern.compile("^\\d{10}$");
		Matcher matcher = pattern.matcher(phoneNumber);
		return matcher.matches();
	}
	
	public static boolean isValidAddressLine(String addressLine)
	{
	    if (addressLine == null || addressLine.isEmpty() || addressLine.isBlank())
	    {
	        return false;
	    }
	    return addressLine.matches("^[a-zA-Z0-9\\s,.\\/-]+$");
	}
	
	public static boolean isValidTitle(String title)
	{
	    return title.matches("^[a-zA-Z0-9\\s,\\/-]+$");
	}

	public static boolean isContainsOnlyNumber(int value) 
	{
	    Pattern pattern = Pattern.compile("^\\d+$");
	    Matcher matcher = pattern.matcher(Integer.toString(value));
	    return matcher.matches();
	}


}
