package com.iktpreobuka.jobster.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Encryption {
	

	public static String getPassEncoded(String pass) {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return "{bcrypt}" + bCryptPasswordEncoder.encode(pass);
	}
	
	public static Boolean comparePassword(String normalPass,String crypPass) { //Not sure if this works
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder.matches(normalPass, crypPass);
	}

	public static void main(String[] args) {
		System.out.println(getPassEncoded("test"));
	}


}
