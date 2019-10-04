package com.iktpreobuka.jobster.controllers.util;

public class RESTError {

	private Integer code;
	private String message;
	
	public RESTError(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public RESTError() {
		super();
		// TODO Auto-generated constructor stub
	}
}
