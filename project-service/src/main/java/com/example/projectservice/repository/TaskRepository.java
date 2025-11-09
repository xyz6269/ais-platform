package com.example.projectservice.repository;

import com.example.projectservice.entity.Task;
import com.example.projectservice.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

    @Query("SELECT DISTINCT t FROM Task t JOIN t.taskProject id WHERE id = :target")
    List<Task> findTaskByProjectId(@Param("target") UUID projectId);

    List<Task> findByStatus(TaskStatus status);
}
