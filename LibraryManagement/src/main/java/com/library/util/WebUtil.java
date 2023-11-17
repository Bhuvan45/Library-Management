package com.library.util;

public final class WebUtil
{
	public static final Character INVALID_NO = 'N';
	public static final Character INVALID_YES = 'Y';
	public static final int DEFAULT_INT_VALUE = 0;

	public static final String STATUS_F = "Failure";
	public static final String STATUS_S = "Success";
	
	public static final String BOOK_STATUS_AVAILABLE = "Available";
	public static final String BOOK_STATUS_NOT_AVAILABLE = "Not Available";
	public static final String BOOK_STATUS_BORROWED = "Borrowed";
	public static final String BOOK_STATUS_DELETED = "Deleted";
	public static final String BOOK_STATUS_RETURNED = "Returned";
	
	
	
	public static final String MEMBER_STATUS_ACTIVE = "Active";
	public static final String MEMBER_STATUS_INACTIVE = "Inactive";
	
	public static String formatFullName(String fullName)
	{
		if (fullName == null || fullName.trim().length() <= 0) {
			return null;
		}		
		
		String[] splitedName = fullName.split("\\s");		
		String formattedFullName = "";
		for(String single : splitedName)
		{			
			if(single.length() > 0)
			{
				formattedFullName += single.substring(0,1).toUpperCase() + single.substring(1).toLowerCase().concat(" ");
			}
		}  
		return formattedFullName;
	}

}
