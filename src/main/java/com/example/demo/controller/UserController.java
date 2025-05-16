package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.User;
import com.example.demo.model.Account;
import com.example.demo.repository.UserRepository;

@Controller

public class UserController {

	@Autowired
	HttpSession session;

	@Autowired
	Account account;

	@Autowired
	UserRepository userRepository;

	//ログイン画面の表示
	@GetMapping("/users/login")
	public String loginView() {
		return "login";
	}

	@GetMapping("/users/logout")
	public String logout() {
		return "login";
	}

	//ログインを実行
	@PostMapping("/users/login")
	public String login(
			@RequestParam("name") String name,
			@RequestParam("password") String password,
			Model model) {

		account.setName(name);

		return "redirect:/tasks";
	}

	@GetMapping("/users/create")
	public String createAccount1() {
		return "createUser";
	}

	@PostMapping("/users/add")
	public String addAccount(
			@RequestParam("name") String name,
			@RequestParam("password") String password,
			Model model) {

		User user = new User(name, password);
		userRepository.save(user);

		return "login";
	}
}
