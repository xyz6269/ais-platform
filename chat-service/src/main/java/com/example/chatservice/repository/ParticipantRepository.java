package com.example.chatservice.repository;


import com.example.chatservice.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    Optional<Participant> findParticipantByEmail(String email);

    @Query("SELECT p FROM Participant p WHERE p.email IN :emails")
    List<Participant> findParticipantsByEmails(@Param("emails") List<String> emails);

    List<Participant> findParticipantByIsAdmin(boolean admin);

}
