package com.example.demo.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Category;
import com.example.demo.entity.Tasks;
import com.example.demo.model.Account;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.TaskRepository;

@Controller
public class TaskController {

	@Autowired
	Account account;

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

		List<Tasks> taskList = null;
		if (categoryId == null) {
			taskList = taskRepository.findByUserId(account.getId());
		} else {
			taskList = taskRepository.findByUserIdAndCategoryId(account.getId(), categoryId);
		}
		model.addAttribute("tasks", taskList);

		return "tasks";
	}

	@GetMapping("/tasks/createTask")
	public String create() {
		return "createTask";
	}

	@PostMapping("/tasks/createTask")
	public String createAdd(
			@RequestParam(name = "categoryId", defaultValue = "99") Integer categoryId,
			@RequestParam("title") String title,
			@RequestParam(name = "closingDate", defaultValue = "") LocalDate closingDate,
			@RequestParam("progress") Integer progress,
			@RequestParam("memo") String memo,
			Model model) {

		List<String> errorList = new ArrayList<>();
		if (categoryId == 99) {
			errorList.add("カテゴリーを選択してください");
		}
		if (title.equals("")) {
			errorList.add("タイトルが未入力です");
		}
		if (progress == null) {
			errorList.add("進捗状況を選択してください");
		}
		if (!errorList.isEmpty()) {
			model.addAttribute("errorList", errorList);
			return "createTask";
		}

		Tasks taskList = new Tasks(categoryId, account.getId(), title, closingDate, progress, memo);

		taskRepository.save(taskList);

		return "redirect:/tasks";
	}

	//更新画面表示
	@GetMapping("/tasks/{id}/edit")
	public String edit(@PathVariable("id") Integer id,
			Model model) {
		Tasks task = taskRepository.findById(id).get();
		model.addAttribute("task", task);
		return "editTask";
	}

	@PostMapping("/tasks/{id}/edit")
	public String update(
			@PathVariable("id") Integer id,
			@RequestParam("categoryId") Integer categoryId,
			@RequestParam("title") String title,
			@RequestParam("progress") Integer progress,
			@RequestParam("closingDate") LocalDate closingDate,
			@RequestParam("memo") String memo,
			Model model) {

		Tasks task = taskRepository.findById(id).get();
		task.setCategoryId(categoryId);
		task.setTitle(title);
		task.setProgress(progress);
		task.setClosingDate(closingDate);
		task.setMemo(memo);

		taskRepository.save(task);

		return "redirect:/tasks";
	}

	@PostMapping("/tasks/{id}/delete")
	public String delete(
			@PathVariable("id") Integer id) {

		taskRepository.deleteById(id);

		return "redirect:/tasks";
	}

}
