package com.example.demo.service;

import com.example.demo.domain.model.Task;
import com.example.demo.domain.model.TaskUpd;
import com.example.demo.domain.model.User;
import com.example.demo.repository.ITaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TaskService {
    @Autowired
    private ITaskRepository taskRepository;

    @Autowired
    private UserService userService;

    public ITaskRepository getTaskRepository() {
        return taskRepository;
    }

    public Task createTask(Task task, UUID idUser) {
        User user = userService.getUserRepository().findById(idUser).orElseThrow();
        task.setIdUser(user.getId());
        LocalDateTime currentDate = LocalDateTime.now();
        if (currentDate.isAfter(task.getStartAt()) || currentDate.isAfter(task.getEndAt())) {
            throw new RuntimeException("Data de inicio ou de fim anterior ao dia de hoje");
        }

        if (task.getStartAt().isAfter(task.getEndAt())) {
            throw new RuntimeException("Data de inicio deve ser antes do fim");
        }
      return this.taskRepository.save(task);
    }

    public List<Task> listTaskByUser(UUID idUser) {
       return this.taskRepository.findAllByIdUser(idUser);
    }

    public Task updateTask(TaskUpd taskUpd, UUID idTask) throws Exception {
        Task task = this.taskRepository.findById(idTask).orElseThrow();
        task.setDescription(taskUpd.description());
        task.setTitle(taskUpd.title());
        task.setStartAt(taskUpd.startAt());
        task.setEndAt(taskUpd.endAt());
        task.setPriority(taskUpd.priority());
        return this.taskRepository.save(task);
    }
}
