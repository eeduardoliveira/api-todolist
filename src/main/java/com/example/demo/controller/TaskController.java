package com.example.demo.controller;

import com.example.demo.domain.model.Task;
import com.example.demo.domain.model.TaskUpd;
import com.example.demo.service.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("atividade")
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private TaskService taskService;


    @PostMapping(path = {"create-task"})
    public Task create(@RequestBody Task task, HttpServletRequest request) {
        UUID idUser = (UUID) request.getAttribute("idUser");
        logger.info("Received create task request from user ID: {}", idUser);
        logger.debug("Task creation request: title='{}', priority='{}'", task.getTitle(), task.getPriority());
        
        Task createdTask = this.taskService.createTask(task, idUser);
        logger.info("Task created successfully: ID={}", createdTask.getId());
        
        return createdTask;
    }

    @GetMapping(path = {"list-task"})
    public List<Task> list(HttpServletRequest request) {
        UUID idUser = (UUID) request.getAttribute("idUser");
        logger.info("Received list tasks request from user ID: {}", idUser);
        
        List<Task> tasks = this.taskService.listTaskByUser(idUser);
        logger.info("Returning {} task(s) for user ID: {}", tasks.size(), idUser);
        
        return tasks;
    }


    @PutMapping(path = {"update-task/{idTask}"})
    public Task update(@RequestBody TaskUpd taskUpd, @PathVariable UUID idTask) throws Exception {
        logger.info("Received update task request for task ID: {}", idTask);
        logger.debug("Task update request: title='{}', priority='{}'", taskUpd.title(), taskUpd.priority());
        
        Task updatedTask = this.taskService.updateTask(taskUpd, idTask);
        logger.info("Task updated successfully: ID={}", idTask);
        
        return updatedTask;
    }

    @DeleteMapping(path = {"delete-task/{idTask}"})
    public void delete(@PathVariable UUID idTask) {
        logger.info("Received delete task request for task ID: {}", idTask);
        
        this.taskService.deleteTask(idTask);
        logger.info("Task deleted successfully: ID={}", idTask);
    }
}
