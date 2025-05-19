package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

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

	//	@Autowired
	//	User user;

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

		List<String> errorList = new ArrayList<>();

		if (name.equals("")) {
			errorList.add("名前は必須です");
		}
		if (password.equals("")) {
			errorList.add("パスワードは必須です");
		}

		if (!errorList.isEmpty()) {
			model.addAttribute("errorList", errorList);
			return "login";
		}
		List<User> userList = userRepository.findByNameAndPassword(name, password);
		User user = userList.get(0);

		account.setName(name);
		account.setPassword(password);
		account.setId(user.getId());
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

		List<String> errorList = new ArrayList<>();

		if (name.equals("")) {
			errorList.add("名前は必須です");
		}
		if (password.equals("")) {
			errorList.add("パスワードは必須です");
		}

		List<User> userList = userRepository.findByName(name);

		if (!userList.isEmpty()) {
			errorList.add("登録済みです");
		}

		if (!errorList.isEmpty()) {
			model.addAttribute("errorList", errorList);
			return "createUser";
		}
		User user = new User(name, password);
		userRepository.save(user);

		return "login";
	}
}
