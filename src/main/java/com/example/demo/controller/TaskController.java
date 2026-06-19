package com.example.demo.controller;

import com.example.demo.domain.model.Task;
import com.example.demo.domain.model.TaskStatistics;
import com.example.demo.domain.model.TaskStatus;
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
        logger.debug("Task creation request: title='{}', priority='{}', status='{}'", 
                    task.getTitle(), task.getPriority(), task.getStatus());
        
        Task createdTask = this.taskService.createTask(task, idUser);
        logger.info("Task created successfully: ID={}, status={}", createdTask.getId(), createdTask.getStatus());
        
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

    @GetMapping(path = {"list-task-by-status/{status}"})
    public List<Task> listByStatus(@PathVariable String status, HttpServletRequest request) {
        UUID idUser = (UUID) request.getAttribute("idUser");
        logger.info("Received list tasks by status request from user ID: {}, status: {}", idUser, status);
        
        TaskStatus taskStatus;
        try {
            taskStatus = TaskStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid status value: {}", status);
            throw new RuntimeException("Status inválido. Use: PENDING, IN_PROGRESS ou COMPLETED");
        }
        
        List<Task> tasks = this.taskService.listTaskByUserAndStatus(idUser, taskStatus);
        logger.info("Returning {} task(s) with status {} for user ID: {}", tasks.size(), taskStatus, idUser);
        
        return tasks;
    }

    @GetMapping(path = {"statistics"})
    public TaskStatistics getStatistics(HttpServletRequest request) {
        UUID idUser = (UUID) request.getAttribute("idUser");
        logger.info("Received task statistics request from user ID: {}", idUser);
        
        TaskStatistics statistics = this.taskService.getTaskStatistics(idUser);
        logger.info("Returning statistics for user ID: {} - PENDING: {}, IN_PROGRESS: {}, COMPLETED: {}, TOTAL: {}",
                    idUser, statistics.pendingCount(), statistics.inProgressCount(), 
                    statistics.completedCount(), statistics.totalCount());
        
        return statistics;
    }

    @PutMapping(path = {"update-task/{idTask}"})
    public Task update(@RequestBody TaskUpd taskUpd, @PathVariable UUID idTask) throws Exception {
        logger.info("Received update task request for task ID: {}", idTask);
        logger.debug("Task update request: title='{}', priority='{}', status='{}'", 
                    taskUpd.title(), taskUpd.priority(), taskUpd.status());
        
        Task updatedTask = this.taskService.updateTask(taskUpd, idTask);
        logger.info("Task updated successfully: ID={}, status={}", idTask, updatedTask.getStatus());
        
        return updatedTask;
    }

    @PatchMapping(path = {"start-task/{idTask}"})
    public Task start(@PathVariable UUID idTask) {
        logger.info("Received start task request for task ID: {}", idTask);
        
        Task startedTask = this.taskService.startTask(idTask);
        logger.info("Task marked as IN_PROGRESS successfully: ID={}", idTask);
        
        return startedTask;
    }

    @PatchMapping(path = {"complete-task/{idTask}"})
    public Task complete(@PathVariable UUID idTask) {
        logger.info("Received complete task request for task ID: {}", idTask);
        
        Task completedTask = this.taskService.completeTask(idTask);
        logger.info("Task marked as completed successfully: ID={}", idTask);
        
        return completedTask;
    }

    @DeleteMapping(path = {"delete-task/{idTask}"})
    public void delete(@PathVariable UUID idTask) {
        logger.info("Received delete task request for task ID: {}", idTask);
        
        this.taskService.deleteTask(idTask);
        logger.info("Task deleted successfully: ID={}", idTask);
    }
}
