package com.example.chatservice.repository;


import com.example.chatservice.entity.Participant;
import com.example.chatservice.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoomRepository extends JpaRepository<Room, UUID> {

    Optional<Room> findRoomById(UUID id);

    @Query("SELECT DISTINCT Participant FROM Room r JOIN r.participants p WHERE r.id = :id")
    List<Participant> findParticipantByRoomId(UUID id);

    @Query("SELECT DISTINCT r FROM Room r JOIN r.participants p WHERE p.email = :email")
    List<Room> findRoomsByUserEmail(@Param("email") String userEmail);

    UUID id(UUID id);
}