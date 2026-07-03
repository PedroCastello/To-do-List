package com.todolist.todolist;

import org.springframework.boot.SpringApplication;

public class TestTodolistApplication {

	public static void main(String[] args) {
		SpringApplication.from(TodolistApplication::main).run(args);
	}

}
