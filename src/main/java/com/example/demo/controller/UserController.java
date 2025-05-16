package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

@Controller

public class UserController {

	@Autowired
	HttpSession session;

	@Autowired
	User user;

	@Autowired
	UserRepository userRepository;

	//ログイン画面の表示
	@GetMapping("/users/create")
	public String createAccount() {
		return "users";
	}

	//ログインを実行
	@PostMapping("/users/add")
	public String addAccount(
			@RequestParam("name") String name,
			@RequestParam("password") String password,
			Model model) {

		if (name == null || name.length() == 0) {
			model.addAttribute("message", "名前を入力してください");
			return "login";
		}

		user.setName(name);

		return "tasks";
	}
}
