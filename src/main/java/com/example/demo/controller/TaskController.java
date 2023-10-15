package com.example.demo.controller;

import com.example.demo.domain.model.Task;
import com.example.demo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("atividade")
public class TaskController {

    @Autowired
    private TaskService taskService;


    @PostMapping(path = {"create-task"})
    public Task create(@RequestBody Task task) {
        return this.taskService.createTask(task);
    }
}
