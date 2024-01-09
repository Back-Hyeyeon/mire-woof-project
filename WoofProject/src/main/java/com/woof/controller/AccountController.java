package com.woof.controller;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.woof.domain.Account;
import com.woof.service.AccountService;
import com.woof.service.impl.AccountServiceImpl;

import lombok.extern.java.Log;

@Log
@Controller
@RequestMapping("/account")
@MapperScan(basePackages = "com.woof.mapper")
public class AccountController {

	@Autowired
	private AccountService service;
	@Autowired
	private AccountServiceImpl accountServiceImpl;

	@RequestMapping(value = "/loginForm", method = RequestMethod.GET)
	public String loginForm(Model model) {
		
		log.info("loginForm");
		return "/account/login/login";
	}
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(Account account, Model model) {
		
		log.info("login");
		return "/account/login/login";
	}

	

	@RequestMapping(value = "/createAccountForm", method = RequestMethod.GET)
	public String createAccountForm(Account account, Model model) throws Exception {
		log.info("createAccountForm");
		
		return "/account/login/createAccount";
	}
	
	@RequestMapping(value = "/createAccount", method = RequestMethod.POST)
	public String createAccount(Account account, Model model) throws Exception {
		service.registerAccount(account);
		
		log.info("createAccount");
		
		return "/account/login/login";
	}
	
	
	
	
}
