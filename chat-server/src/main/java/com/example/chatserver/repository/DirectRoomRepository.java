package com.example.chatserver.repository;

import com.example.chatserver.entity.DirectRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DirectRoomRepository extends JpaRepository<DirectRoom, UUID> {
    @Override
    Optional<DirectRoom> findById(UUID id);
}
