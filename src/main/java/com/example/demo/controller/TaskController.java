package com.example.demo.controller;

import com.example.demo.domain.model.Task;
import com.example.demo.domain.model.TaskUpd;
import com.example.demo.service.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("atividade")
public class TaskController {

    @Autowired
    private TaskService taskService;


    @PostMapping(path = {"create-task"})
    public Task create(@RequestBody Task task, HttpServletRequest request) {
        UUID idUser = (UUID) request.getAttribute("idUser");
        return this.taskService.createTask(task, idUser);
    }

    @GetMapping(path = {"list-task"})
    public List<Task> list(HttpServletRequest request) {
        UUID idUser = (UUID) request.getAttribute("idUser");
        return this.taskService.listTaskByUser(idUser);
    }


    @PutMapping(path = {"update-task/{idTask}"})
    public Task update(@RequestBody TaskUpd taskUpd, @PathVariable UUID idTask) throws Exception {
        return this.taskService.updateTask(taskUpd, idTask);
    }

    @DeleteMapping(path = {"delete-task/{idTask}"})
    public void update(@PathVariable UUID idTask) {
        this.taskService.getTaskRepository().deleteById(idTask);
    }
}
