package com.iktpreobuka.jobster.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

	@RequestMapping(value= "/jobster")
	public @ResponseBody String greeting() {
		return "Jobster";
	}

}
