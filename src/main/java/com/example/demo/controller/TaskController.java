package com.example.demo.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Category;
import com.example.demo.entity.Tasks;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.TaskRepository;

@Controller
public class TaskController {

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	TaskRepository taskRepository;

	@GetMapping("/tasks")
	public String index(
			@RequestParam(name = "categoryId", defaultValue = "") Integer categoryId,
			Model model) {

		List<Category> categoryList = categoryRepository.findAll();
		model.addAttribute("categories", categoryList);

		List<Tasks> taskList = taskRepository.findAll();
		model.addAttribute("tasks", taskList);

		return "tasks";
	}

	@GetMapping("/tasks/createTask")
	public String create() {
		return "createTask";
	}

	@PostMapping("/tasks/createTask")
	public String createAdd(
			@RequestParam("categoryId") Integer categoryId,
			@RequestParam("title") String title,
			@RequestParam("closingDate") LocalDate closigDate,
			@RequestParam("progress") Integer progress,
			@RequestParam("memo") String memo,
			Model model) {

		Tasks taskList = new Tasks(categoryId, title, closigDate, progress, memo);

		taskRepository.save(taskList);

		return "redirect:/tasks";
	}
}
