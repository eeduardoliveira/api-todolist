package com.example.demo.repository;

import com.example.demo.domain.model.Task;
import com.example.demo.domain.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ITaskRepository extends JpaRepository<Task, UUID> {

    List<Task> findAllByIdUser(UUID user);

    List<Task> findAllByIdUserAndStatus(UUID user, TaskStatus status);

    // Search by keyword in title or description (case-insensitive)
    @Query("SELECT t FROM tb_task t WHERE t.idUser = :idUser AND (LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Task> searchByKeyword(@Param("idUser") UUID idUser, @Param("keyword") String keyword);

    // Filter by priority
    List<Task> findAllByIdUserAndPriority(UUID idUser, String priority);

    // Filter by date range
    @Query("SELECT t FROM tb_task t WHERE t.idUser = :idUser AND t.startAt >= :startDate AND t.endAt <= :endDate")
    List<Task> findAllByIdUserAndDateRange(@Param("idUser") UUID idUser, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
