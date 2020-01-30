package com.iktpreobuka.jobster.util;

public class StringDateFormatValidator {
	
	public static Boolean checkIfStringIsValidDateFormat(String inputDate) {
		if(inputDate.matches("^(20)\\d\\d-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$")) {
			return true;
		}
		else return false;
		}
	}


