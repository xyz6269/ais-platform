package com.example.chatserver.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ParticipantRepository extends JpaRepository<ParticipantRepository, UUID> {

}
