package com.example.demo.repository;

import com.example.demo.domain.model.Task;
import com.example.demo.domain.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ITaskRepository extends JpaRepository<Task, UUID> {

    List<Task> findAllByIdUser(UUID user);

    List<Task> findAllByIdUserAndStatus(UUID user, TaskStatus status);
}
