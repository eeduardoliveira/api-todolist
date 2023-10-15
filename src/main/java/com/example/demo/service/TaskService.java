package com.example.demo.service;

import com.example.demo.domain.model.Task;
import com.example.demo.repository.ITaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
    @Autowired
    private ITaskRepository taskRepository;

    public Task createTask(Task task) {
      return this.taskRepository.save(task);
    }
}
