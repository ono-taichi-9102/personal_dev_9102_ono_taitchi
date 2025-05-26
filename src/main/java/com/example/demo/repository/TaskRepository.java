package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Tasks;

public interface TaskRepository extends JpaRepository<Tasks, Integer> {
	List<Tasks> findByCategoryId(Integer categoryId);

	List<Tasks> findByUserId(Integer userId);

	List<Tasks> findByUserIdAndCategoryId(Integer userId, Integer categoryId);

	List<Tasks> findByUserIdAndTitleLike(Integer userId, String title);

	List<Tasks> findByUserIdOrderByClosingDateAsc(Integer userId);

	List<Tasks> findByUserIdAndCategoryIdOrderByClosingDateAsc(Integer userId, Integer categoryId);

	List<Tasks> findByUserIdAndTitleLikeOrderByClosingDateAsc(Integer userId, String title);

	List<Tasks> findByUserIdAndCategoryIdOrderByClosingDateDesc(Integer userId, Integer categoryId);

	List<Tasks> findByUserIdAndTitleLikeOrderByClosingDateDesc(Integer userId, String title);

	List<Tasks> findByUserIdOrderByClosingDateDesc(Integer userId);
}
