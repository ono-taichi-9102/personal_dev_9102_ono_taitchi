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
import com.example.demo.entity.User;
import com.example.demo.model.Account;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.TaskRepository;
import com.example.demo.repository.UserRepository;

@Controller
public class TaskController {

	@Autowired
	Account account;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	TaskRepository taskRepository;

	@Autowired
	UserRepository userRepository;

	@GetMapping("/tasks")
	public String index(
			@RequestParam(name = "categoryId", defaultValue = "") Integer categoryId,
			@RequestParam(name = "keyword", defaultValue = "") String keyword,
			Model model) {

		model.addAttribute("keyword", keyword);
		List<Category> categoryList = categoryRepository.findAll();
		model.addAttribute("categories", categoryList);

		List<Tasks> taskList = null;
		if (categoryId == null && keyword.equals("")) {
			taskList = taskRepository.findByUserId(account.getId());
		} else {
			taskList = taskRepository.findByUserIdAndCategoryId(account.getId(), categoryId);
		}
		if (!(keyword.equals(""))) {
			taskList = taskRepository.findByUserIdAndTitleLike(account.getId(), "%" + keyword + "%");
		}
		model.addAttribute("taskList", taskList);

		taskList.removeIf(t -> t.getProgress() == 3);

		return "tasks";
	}

	@GetMapping("/tasks/complete")
	public String showcompleteTasks(
			@RequestParam(name = "categoryId", defaultValue = "") Integer categoryId,
			@RequestParam(name = "keyword", defaultValue = "") String keyword,
			Model model) {
		model.addAttribute("keyword", keyword);
		List<Category> categoryList = categoryRepository.findAll();
		model.addAttribute("categories", categoryList);

		List<Tasks> taskList = null;
		if (categoryId == null && keyword.equals("")) {
			taskList = taskRepository.findByUserId(account.getId());
		} else {
			taskList = taskRepository.findByUserIdAndCategoryId(account.getId(), categoryId);
		}
		if (!(keyword.equals(""))) {
			taskList = taskRepository.findByUserIdAndTitleLike(account.getId(), "%" + keyword + "%");
		}
		model.addAttribute("taskList", taskList);
		taskList.removeIf(t -> t.getProgress() == 2);

		return "completeTask";
	}

	@PostMapping("/tasks/{id}/complete")
	public String completeTasks(@PathVariable("id") Integer id,
			Model model) {
		Tasks task = taskRepository.findById(id).get();
		task.setProgress(3);
		taskRepository.save(task);
		return "redirect:/tasks";
	}

	@PostMapping("/tasks/{id}/cancelComplete")
	public String cancelComplete(
			@PathVariable("id") Integer id,
			Model model) {
		Tasks task = taskRepository.findById(id).get();
		task.setProgress(2);
		taskRepository.save(task);
		return "redirect:/tasks/complete";
	}

	@GetMapping("/tasks/createTask")
	public String create() {
		return "createTask";
	}

	@PostMapping("/tasks/createTask")
	public String createAdd(
			@RequestParam(name = "categoryId", defaultValue = "99") Integer categoryId,
			@RequestParam(name = "title", defaultValue = "") String title,
			@RequestParam(name = "closingDate", defaultValue = "") LocalDate closingDate,
			@RequestParam(name = "progress", defaultValue = "100") Integer progress,
			@RequestParam(name = "memo", defaultValue = "") String memo,
			Model model) {

		List<String> errorList = new ArrayList<>();
		if (categoryId == 99) {
			errorList.add("カテゴリーを選択してください");
		}
		if (title.equals("")) {
			errorList.add("タイトルが未入力です");
		}
		if (closingDate == null) {
			errorList.add("期限が未入力です");
		}
		if (progress == 100) {
			errorList.add("進捗状況が未入力です");
		}
		if (!errorList.isEmpty()) {
			model.addAttribute("errorList", errorList);
			return "createTask";
		}

		Tasks taskList = new Tasks(account.getId(), categoryId, title, closingDate, progress, memo);

		taskRepository.save(taskList);

		return "redirect:/tasks";
	}

	//共有タスク作成
	@GetMapping("/tasks/createShareTask")
	public String showCreateShareTask(Model model) {
		List<User> users = userRepository.findByIdNot(account.getId());
		model.addAttribute("users", users);
		return "createShareTask";
	}

	@PostMapping("/tasks/createShareTask")
	public String createShareTaskAdd(

			@RequestParam(name = "userId", defaultValue = "") Integer userId,
			@RequestParam(name = "categoryId", defaultValue = "99") Integer categoryId,
			@RequestParam(name = "title", defaultValue = "") String title,
			@RequestParam(name = "closingDate", defaultValue = "") LocalDate closingDate,
			@RequestParam(name = "progress", defaultValue = "100") Integer progress,
			@RequestParam(name = "memo", defaultValue = "") String memo,
			Model model) {
		List<String> errorList = new ArrayList<>();
		if (categoryId == 99) {
			errorList.add("カテゴリーを選択してください");
		}
		if (title.equals("")) {
			errorList.add("タイトルが未入力です");
		}
		if (closingDate == null) {
			errorList.add("期限が未入力です");
		}
		if (progress == 100) {
			errorList.add("進捗状況が未入力です");
		}
		if (!errorList.isEmpty()) {
			// usersを渡してあげる
			List<User> users = userRepository.findByIdNot(account.getId());
			model.addAttribute("users", users);
			model.addAttribute("userId", userId);
			model.addAttribute("categoryId", categoryId);
			model.addAttribute("title", title);
			model.addAttribute("closingDate", closingDate);
			model.addAttribute("progress", progress);
			model.addAttribute("memo", memo);

			model.addAttribute("errorList", errorList);
			return "createShareTask";
		}

		Tasks taskList = new Tasks(account.getId(), categoryId, title, closingDate, progress, memo);
		taskRepository.save(taskList);

		Tasks shareTaskList = new Tasks(userId, categoryId, title, closingDate, progress, memo);
		taskRepository.save(shareTaskList);

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
			@RequestParam(name = "categoryId", defaultValue = "99") Integer categoryId,
			@RequestParam("title") String title,
			@RequestParam(name = "progress", defaultValue = "100") Integer progress,
			@RequestParam(name = "closingDate", defaultValue = "") LocalDate closingDate,
			@RequestParam("memo") String memo,
			Model model) {

		List<String> errorList = new ArrayList<>();
		if (categoryId == 99) {
			errorList.add("カテゴリーを選択してください");
		}
		if (title.equals("")) {
			errorList.add("タイトルが未入力です");
		}
		if (closingDate == null) {
			errorList.add("期限が未入力です");
		}
		if (progress == 100) {
			errorList.add("進捗状況が未入力です");
		}
		if (!errorList.isEmpty()) {
			model.addAttribute("errorList", errorList);
			return "editTask";
		}

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
