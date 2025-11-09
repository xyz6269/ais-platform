package com.example.projectservice.repository;

import com.example.projectservice.entity.Participant;
import com.example.projectservice.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {

    @Query("SELECT DISTINCT p FROM Project p JOIN p.participants r WHERE r = :target")
    List<Project> getProjectsByParticipant(@Param("target") Participant participant);

}
