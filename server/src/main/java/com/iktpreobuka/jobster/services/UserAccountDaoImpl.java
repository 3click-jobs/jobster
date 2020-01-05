package com.iktpreobuka.jobster.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	
	@Override
	public UserAccountEntity addNewUserAccount(UserEntity loggedUser, UserEntity user, String username, EUserRole role, String password) throws Exception {
		if (username != null && userAccountRepository.getByUsername(username) != null) {
	         throw new Exception("Username already exists.");
	      }
		logger.info("addNewUserAccount validation Ok.");
		UserAccountEntity account = new UserAccountEntity();
		try {
			account.setAccessRole(role);
			account.setUsername(username);
			account.setPassword(Encryption.getPassEncoded(password));
			account.setCreatedById(loggedUser.getId());
			account.setStatusActive();
			account.setUser(user);
			logger.info("Atributes added.");
			userAccountRepository.save(account);
			logger.info("addNewUserAccount finished.");
		} catch (Exception e) {
			throw new Exception("AddNewUserAccount failed on saving." + e.getLocalizedMessage());
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
				//account.setPassword(Encryption.getPassEncoded(updatePerson.getPassword()));
				account.setPassword(updatePerson.getPassword());
				i++;
			}
			if (i>0) {
				account.setUpdatedById(loggedUser.getId());
				userAccountRepository.save(account);
			}
			logger.info("modifyAccount finished.");
		} catch (Exception e) {
			throw new Exception("ModifyAccount for Admin failed on saving." + e.getLocalizedMessage());
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
				//account.setPassword(Encryption.getPassEncoded(updateCompany.getPassword()));
				account.setPassword(updateCompany.getPassword());
				i++;
			}
			if (i>0) {
				account.setUpdatedById(loggedUser.getId());
				userAccountRepository.save(account);
			}
			logger.info("modifyAccount2 finished.");
		} catch (Exception e) {
			throw new Exception("ModifyAccount for Parent failed on saving." + e.getLocalizedMessage());
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
			logger.info("modifyAccountUsername finished.");
		} catch (Exception e) {
			throw new Exception("ModifyAccountUsername failed on saving." + e.getLocalizedMessage());
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
			logger.info("modifyAccountAccessRole finished.");
		} catch (Exception e) {
			throw new Exception("modifyAccountAccessRole failed on saving." + e.getLocalizedMessage());
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
			logger.info("modifyAccountUserAndAccessRole finished.");
		} catch (Exception e) {
			throw new Exception("modifyAccountUserAndAccessRole failed on saving." + e.getLocalizedMessage());
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
			logger.info("modifyAccountUser finished.");
		} catch (Exception e) {
			throw new Exception("modifyAccountUser failed on saving." + e.getLocalizedMessage());
		}
	}
	
	@Override
	public void modifyAccountPassword(UserEntity loggedUser, UserAccountEntity account, String password) throws Exception {
		try {
			if (password != null && /*!Encryption.getPassEncoded(password).equals(account.getPassword())*/!password.equals(account.getPassword()) && !password.equals(" ") && !password.equals("")) {
				//account.setPassword(Encryption.getPassEncoded(password));
				account.setPassword(password);
				account.setUpdatedById(loggedUser.getId());
				userAccountRepository.save(account);
			}
			logger.info("modifyAccountPassword finished.");
		} catch (Exception e) {
			throw new Exception("ModifyAccountPassword failed on saving." + e.getLocalizedMessage());
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
				//account.setPassword(Encryption.getPassEncoded(password));
				account.setPassword(password);
				i++;
			}
			if (i>0) {
				account.setUpdatedById(loggedUser.getId());
				userAccountRepository.save(account);
			}
			logger.info("modifyAccount3 finished.");
		} catch (Exception e) {
			throw new Exception("ModifyAccountUsername failed on saving." + e.getLocalizedMessage());
		}

	}

	@Override
	public void deleteAccount(UserEntity loggedUser, UserAccountEntity account) throws Exception {
		try {
			account.setStatusInactive();
			account.setUpdatedById(loggedUser.getId());
			userAccountRepository.save(account);
			logger.info("deleteAccount finished.");
		} catch (Exception e) {
			throw new Exception("DeleteAccount failed on saving." + e.getLocalizedMessage());
		}
	}

	@Override
	public void undeleteAccount(UserEntity loggedUser, UserAccountEntity account) throws Exception {
		try {
			account.setStatusActive();
			account.setUpdatedById(loggedUser.getId());
			userAccountRepository.save(account);
			logger.info("undeleteAccount finished.");
		} catch (Exception e) {
			throw new Exception("UndeleteAccount failed on saving." + e.getLocalizedMessage());
		}		
	}
	
	@Override
	public void archiveAccount(UserEntity loggedUser, UserAccountEntity account) throws Exception {
		try {
			account.setStatusArchived();
			account.setUpdatedById(loggedUser.getId());
			userAccountRepository.save(account);
			logger.info("archiveAccount finished.");
		} catch (Exception e) {
			throw new Exception("ArchiveDeleteAccount failed on saving." + e.getLocalizedMessage());
		}				
	}

}
