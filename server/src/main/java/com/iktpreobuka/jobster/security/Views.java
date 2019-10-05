package com.iktpreobuka.jobster.security;

public class Views {
	
	public static class Guest {}
	public static class User extends Guest {}
	public static class Admin extends User {}

}
