package com.example.demo.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.model.Account;

@Aspect
@Component
public class CheckLoginAspect {

	@Autowired
	Account account;

	// Controllerクラスの全メソッド処理の前処理
	@Before("execution(* com.example.demo.controller.*Controller.*(..))")
	public void writeLog(JoinPoint jp) {
		// セッションスコープに保持されたアカウント名を出力
		// 保持されていない場合は「ゲスト」とする
		if (account == null || account.getName() == null ||
				account.getName().length() == 0) {
			System.out.print("ゲスト：");
		} else {
			System.out.print(account.getName() + "：");
		}
		System.out.println(jp.getSignature());
	}

	// UserControllerで未ログインの場合はログインページにリダイレクト
	@Around("execution(* com.example.demo.controller.TaskController.*(..))")
	public Object checkLogin(ProceedingJoinPoint jp) throws Throwable {
		if (account == null || account.getName() == null ||
				account.getName().length() == 0) {
			System.err.println("ログインしていません!");
			return "login";
		}
		// Controller内のメソッドの実行
		return jp.proceed();
	}
}
