package com.iktpreobuka.jobster.services;

import com.iktpreobuka.jobster.entities.UserEntity;

public interface EmailDao {
	void sendCommentAddedEmail(UserEntity u) throws Exception;

	void sendCommentEditedEmail(UserEntity u) throws Exception;

	void sendCommentDeletedEmail(UserEntity u) throws Exception;

	void sendCommentUndeleteEmail(UserEntity u) throws Exception;
	
	void testEmailSending() throws Exception;

}
