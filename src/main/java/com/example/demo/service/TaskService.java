package com.example.demo.service;

import com.example.demo.domain.model.Task;
import com.example.demo.domain.model.TaskStatus;
import com.example.demo.domain.model.TaskUpd;
import com.example.demo.domain.model.User;
import com.example.demo.repository.ITaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TaskService {
    
    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);
    
    @Autowired
    private ITaskRepository taskRepository;

    @Autowired
    private UserService userService;

    public ITaskRepository getTaskRepository() {
        return taskRepository;
    }

    public Task createTask(Task task, UUID idUser) {
        logger.info("Creating new task for user ID: {}", idUser);
        
        User user = userService.getUserRepository().findById(idUser).orElseThrow();
        task.setIdUser(user.getId());
        
        // Set default status if not provided
        if (task.getStatus() == null) {
            task.setStatus(TaskStatus.PENDING);
            logger.debug("No status provided, defaulting to PENDING");
        }
        
        LocalDateTime currentDate = LocalDateTime.now();
        if (currentDate.isAfter(task.getStartAt()) || currentDate.isAfter(task.getEndAt())) {
            logger.warn("Task creation failed: Start or end date is before current date (User ID: {})", idUser);
            throw new RuntimeException("Data de inicio ou de fim anterior ao dia de hoje");
        }

        if (task.getStartAt().isAfter(task.getEndAt())) {
            logger.warn("Task creation failed: Start date is after end date (User ID: {})", idUser);
            throw new RuntimeException("Data de inicio deve ser antes do fim");
        }
        
        Task savedTask = this.taskRepository.save(task);
        logger.info("Task created successfully: ID={}, Title='{}', User ID={}, Status={}", 
                    savedTask.getId(), savedTask.getTitle(), idUser, savedTask.getStatus());
        logger.debug("Task details: {}", savedTask);
        
        return savedTask;
    }

    public List<Task> listTaskByUser(UUID idUser) {
        logger.info("Listing all tasks for user ID: {}", idUser);
        List<Task> tasks = this.taskRepository.findAllByIdUser(idUser);
        logger.debug("Found {} task(s) for user ID: {}", tasks.size(), idUser);
        return tasks;
    }

    public List<Task> listTaskByUserAndStatus(UUID idUser, TaskStatus status) {
        logger.info("Listing tasks for user ID: {} with status: {}", idUser, status);
        List<Task> tasks = this.taskRepository.findAllByIdUserAndStatus(idUser, status);
        logger.debug("Found {} task(s) for user ID: {} with status: {}", tasks.size(), idUser, status);
        return tasks;
    }

    public Task updateTask(TaskUpd taskUpd, UUID idTask) throws Exception {
        logger.info("Updating task ID: {}", idTask);
        
        Task task = this.taskRepository.findById(idTask).orElseThrow();
        logger.debug("Current task state: {}", task);
        
        task.setDescription(taskUpd.description());
        task.setTitle(taskUpd.title());
        task.setStartAt(taskUpd.startAt());
        task.setEndAt(taskUpd.endAt());
        task.setPriority(taskUpd.priority());
        
        if (taskUpd.status() != null) {
            task.setStatus(taskUpd.status());
            logger.debug("Updating task status to: {}", taskUpd.status());
        }
        
        Task updatedTask = this.taskRepository.save(task);
        logger.info("Task updated successfully: ID={}, Title='{}', Status={}", 
                    updatedTask.getId(), updatedTask.getTitle(), updatedTask.getStatus());
        logger.debug("Updated task details: {}", updatedTask);
        
        return updatedTask;
    }

    public Task startTask(UUID idTask) {
        logger.info("Starting task (marking as IN_PROGRESS): ID={}", idTask);
        
        Task task = this.taskRepository.findById(idTask).orElseThrow();
        logger.debug("Current task status: {}", task.getStatus());
        
        task.setStatus(TaskStatus.IN_PROGRESS);
        
        Task startedTask = this.taskRepository.save(task);
        logger.info("Task started successfully: ID={}", startedTask.getId());
        logger.debug("Started task details: {}", startedTask);
        
        return startedTask;
    }

    public Task completeTask(UUID idTask) {
        logger.info("Marking task as completed: ID={}", idTask);
        
        Task task = this.taskRepository.findById(idTask).orElseThrow();
        logger.debug("Current task status: {}", task.getStatus());
        
        task.setStatus(TaskStatus.COMPLETED);
        
        Task completedTask = this.taskRepository.save(task);
        logger.info("Task marked as completed successfully: ID={}", completedTask.getId());
        logger.debug("Completed task details: {}", completedTask);
        
        return completedTask;
    }
    
    public void deleteTask(UUID idTask) {
        logger.info("Deleting task ID: {}", idTask);
        this.taskRepository.deleteById(idTask);
        logger.info("Task deleted successfully: ID={}", idTask);
    }
}
