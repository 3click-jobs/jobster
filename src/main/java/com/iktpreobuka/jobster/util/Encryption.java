package com.iktpreobuka.jobster.util;

public class Encryption {

	public static String getPassEncoded(String pass) {
		/*BCryptPasswordEncoder bCryptPasswordEncoder= new BCryptPasswordEncoder();
		return bCryptPasswordEncoder.encode(pass);*/
		return getPassEncoded(pass);
		}
	
	/* public static void main(String[] args) {
		System.out.println(getPassEncoded("admin"));
		} */

}
