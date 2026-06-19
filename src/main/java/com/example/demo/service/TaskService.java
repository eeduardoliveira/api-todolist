package com.example.demo.service;

import com.example.demo.domain.model.Task;
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
        logger.info("Task created successfully: ID={}, Title='{}', User ID={}", 
                    savedTask.getId(), savedTask.getTitle(), idUser);
        logger.debug("Task details: {}", savedTask);
        
        return savedTask;
    }

    public List<Task> listTaskByUser(UUID idUser) {
        logger.info("Listing all tasks for user ID: {}", idUser);
        List<Task> tasks = this.taskRepository.findAllByIdUser(idUser);
        logger.debug("Found {} task(s) for user ID: {}", tasks.size(), idUser);
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
        
        Task updatedTask = this.taskRepository.save(task);
        logger.info("Task updated successfully: ID={}, Title='{}'", updatedTask.getId(), updatedTask.getTitle());
        logger.debug("Updated task details: {}", updatedTask);
        
        return updatedTask;
    }
    
    public void deleteTask(UUID idTask) {
        logger.info("Deleting task ID: {}", idTask);
        this.taskRepository.deleteById(idTask);
        logger.info("Task deleted successfully: ID={}", idTask);
    }
}
