package com.iktpreobuka.jobster.controllers.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.iktpreobuka.jobster.entities.dto.CompanyDTO;
import com.iktpreobuka.jobster.entities.dto.PersonDTO;
import com.iktpreobuka.jobster.entities.dto.UserAccountDTO;


@Component 
public class UserCustomValidator implements Validator {

	@Override 
	public boolean supports(Class <?> myClass) { 
		return CompanyDTO.class.equals(myClass) || PersonDTO.class.equals(myClass) || UserAccountDTO.class.equals(myClass);
		}
	
	@Override public void validate(Object target, Errors errors) { 
		if (target instanceof CompanyDTO) {
			CompanyDTO user = (CompanyDTO) target;
			if(user.getPassword() != null && !user.getPassword().equals(user.getConfirmedPassword())) { 
				errors.reject("400", "Passwords must be the same."); 
				}
			} else if (target instanceof PersonDTO) {
				PersonDTO user = (PersonDTO) target;
				if(user.getPassword() != null && !user.getPassword().equals(user.getConfirmedPassword())) { 
					errors.reject("400", "Passwords must be the same."); 
					}
			} else if (target instanceof UserAccountDTO) {
				UserAccountDTO user = (UserAccountDTO) target;
				if(user.getPassword() != null && !user.getPassword().equals(user.getConfirmedPassword())) { 
					errors.reject("400", "Passwords must be the same."); 
					}
			}

		}

}
