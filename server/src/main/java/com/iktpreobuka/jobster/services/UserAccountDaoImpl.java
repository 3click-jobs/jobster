package com.iktpreobuka.jobster.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iktpreobuka.jobster.entities.UserAccountEntity;
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.entities.dto.CompanyDTO;
import com.iktpreobuka.jobster.entities.dto.PersonDTO;
import com.iktpreobuka.jobster.enumerations.EUserRole;
import com.iktpreobuka.jobster.repositories.UserAccountRepository;
import com.iktpreobuka.jobster.util.Encryption;


@Service
public class UserAccountDaoImpl implements UserAccountDao {

	@Autowired
	private UserAccountRepository userAccountRepository;
	
	
	@Override
	public UserAccountEntity addNewUserAccount(UserEntity loggedUser, UserEntity user, String username, EUserRole role, String password) throws Exception {
		if (username != null && userAccountRepository.getByUsername(username) != null) {
	         throw new Exception("Username already exists.");
	      }
		UserAccountEntity account = new UserAccountEntity();
		try {
			account.setAccessRole(role);
			account.setUsername(username);
			account.setPassword(Encryption.getPassEncoded(password));
			account.setCreatedById(loggedUser.getId());
			account.setStatusActive();
			account.setUser(user);
			userAccountRepository.save(account);
		} catch (Exception e) {
			throw new Exception("AddNewUserAccount failed on saving.");
		}
		return account;
	}
		
	@Override
	public void modifyAccount(UserEntity loggedUser, UserAccountEntity account, PersonDTO updatePerson) throws Exception {
		if (updatePerson.getUsername() != null && userAccountRepository.getByUsername(updatePerson.getUsername()) != null) {
	         throw new Exception("Username already exists.");
	      }
		if (updatePerson.getAccessRole() != null && !updatePerson.getAccessRole().equals("ROLE_ADMIN")) {
	         throw new Exception("Access role must be ROLE_ADMIN.");
		}		
		try {
			Integer i = 0;
			if (updatePerson.getUsername() != null && !updatePerson.getUsername().equals(account.getUsername()) && !updatePerson.getUsername().equals(" ") && !updatePerson.getUsername().equals("")) {
				account.setUsername(updatePerson.getUsername());
				i++;
			}
			if (updatePerson.getPassword() != null && !Encryption.getPassEncoded(updatePerson.getPassword()).equals(account.getPassword()) && !updatePerson.getPassword().equals(" ") && !updatePerson.getPassword().equals("")) {
				account.setPassword(Encryption.getPassEncoded(updatePerson.getPassword()));
				i++;
			}
			if (i>0) {
				account.setUpdatedById(loggedUser.getId());
				userAccountRepository.save(account);
			}
		} catch (Exception e) {
			throw new Exception("ModifyAccount for Admin failed on saving.");
		}
	}
	
	@Override
	public void modifyAccount(UserEntity loggedUser, UserAccountEntity account, CompanyDTO updateCompany) throws Exception {
		if (updateCompany.getUsername() != null && userAccountRepository.getByUsername(updateCompany.getUsername()) != null) {
	         throw new Exception("Username already exists.");
	      }
		if (updateCompany.getAccessRole() != null && !updateCompany.getAccessRole().equals("ROLE_PARENT")) {
	         throw new Exception("Access role must be ROLE_PARENT.");
		}		
		try {
			Integer i = 0;
			if (updateCompany.getUsername() != null && !updateCompany.getUsername().equals(account.getUsername()) && !updateCompany.getUsername().equals(" ") && !updateCompany.getUsername().equals("")) {
				account.setUsername(updateCompany.getUsername());
				i++;
			}
			if (updateCompany.getPassword() != null && !Encryption.getPassEncoded(updateCompany.getPassword()).equals(account.getPassword()) && !updateCompany.getPassword().equals(" ") && !updateCompany.getPassword().equals("")) {
				account.setPassword(Encryption.getPassEncoded(updateCompany.getPassword()));
				i++;
			}
			if (i>0) {
				account.setUpdatedById(loggedUser.getId());
				userAccountRepository.save(account);
			}
		} catch (Exception e) {
			throw new Exception("ModifyAccount for Parent failed on saving.");
		}
	}

	@Override
	public void modifyAccountUsername(UserEntity loggedUser, UserAccountEntity account, String username) throws Exception {
		if (username != null && userAccountRepository.getByUsername(username) != null) {
	         throw new Exception("Username already exists.");
	      }
		try {
			if (username != null && !username.equals(account.getUsername()) && !username.equals(" ") && !username.equals("")) {
				account.setUsername(username);
				account.setUpdatedById(loggedUser.getId());
				userAccountRepository.save(account);
			}
		} catch (Exception e) {
			throw new Exception("ModifyAccountUsername failed on saving.");
		}
	}

	@Override
	public void modifyAccountAccessRole(UserEntity loggedUser, UserAccountEntity account, EUserRole role) throws Exception {
		try {
			if (role != null && (role == EUserRole.ROLE_ADMIN || role == EUserRole.ROLE_USER )) {
				account.setAccessRole(role);
				account.setUpdatedById(loggedUser.getId());
				userAccountRepository.save(account);
			}
		} catch (Exception e) {
			throw new Exception("modifyAccountAccessRole failed on saving.");
		}
	}
	
	@Override
	public void modifyAccountUserAndAccessRole(UserEntity loggedUser, UserAccountEntity account, UserEntity user, EUserRole role) throws Exception {
		try {
			if (user != null && role != null) {
				account.setUser(user);
				account.setAccessRole(role);
				account.setUpdatedById(loggedUser.getId());
				userAccountRepository.save(account);
			}
		} catch (Exception e) {
			throw new Exception("modifyAccountUserAndAccessRole failed on saving.");
		}
	}
	
	@Override
	public void modifyAccountUser(UserEntity loggedUser, UserAccountEntity account, UserEntity user) throws Exception {
		try {
			if (user != null) {
				account.setUser(user);
				account.setUpdatedById(loggedUser.getId());
				userAccountRepository.save(account);
			}
		} catch (Exception e) {
			throw new Exception("modifyAccountUser failed on saving.");
		}
	}
	
	@Override
	public void modifyAccountPassword(UserEntity loggedUser, UserAccountEntity account, String password) throws Exception {
		try {
			if (password != null && !Encryption.getPassEncoded(password).equals(account.getPassword()) && !password.equals(" ") && !password.equals("")) {
				account.setPassword(Encryption.getPassEncoded(password));
				account.setUpdatedById(loggedUser.getId());
				userAccountRepository.save(account);
			}
		} catch (Exception e) {
			throw new Exception("ModifyAccountPassword failed on saving.");
		}
	}
	
	@Override
	public void modifyAccount(UserEntity loggedUser, UserAccountEntity account, String username, String password) throws Exception {
		if (username != null && userAccountRepository.getByUsername(username) != null) {
	         throw new Exception("Username already exists.");
	      }
		if (username == null && password == null)
			throw new Exception("Username and password are null.");
		try {
			Integer i = 0;
			if (!username.equals(account.getUsername()) && username != null && !username.equals(" ") && !username.equals("")) {
				account.setUsername(username);
				i++;
			}
			if (!Encryption.getPassEncoded(password).equals(account.getPassword()) && password != null && !password.equals(" ") && !password.equals("")) {
				account.setPassword(Encryption.getPassEncoded(password));
				i++;
			}
			if (i>0) {
				account.setUpdatedById(loggedUser.getId());
				userAccountRepository.save(account);
			}
		} catch (Exception e) {
			throw new Exception("ModifyAccountUsername failed on saving.");
		}

	}

	@Override
	public void deleteAccount(UserEntity loggedUser, UserAccountEntity account) throws Exception {
		try {
			account.setStatusInactive();
			account.setUpdatedById(loggedUser.getId());
			userAccountRepository.save(account);
		} catch (Exception e) {
			throw new Exception("DeleteAccount failed on saving.");
		}
	}

	@Override
	public void undeleteAccount(UserEntity loggedUser, UserAccountEntity account) throws Exception {
		try {
			account.setStatusActive();
			account.setUpdatedById(loggedUser.getId());
			userAccountRepository.save(account);
		} catch (Exception e) {
			throw new Exception("UndeleteAccount failed on saving.");
		}		
	}
	
	@Override
	public void archiveAccount(UserEntity loggedUser, UserAccountEntity account) throws Exception {
		try {
			account.setStatusArchived();
			account.setUpdatedById(loggedUser.getId());
			userAccountRepository.save(account);
		} catch (Exception e) {
			throw new Exception("ArchiveDeleteAccount failed on saving.");
		}				
	}

}