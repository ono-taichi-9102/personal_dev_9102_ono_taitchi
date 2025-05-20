package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Tasks;

public interface TaskRepository extends JpaRepository<Tasks, Integer> {
	List<Tasks> findByCategoryId(Integer categoryId);

	List<Tasks> findByUserId(Integer userId);

	List<Tasks> findByUserIdAndCategoryId(Integer userId, Integer categoryId);

	List<Tasks> findByUserIdAndTitleLike(Integer userId, String title);
}
