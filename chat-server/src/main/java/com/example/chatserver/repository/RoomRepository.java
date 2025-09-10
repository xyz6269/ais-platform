package com.example.chatserver.repository;


import com.example.chatserver.entity.Room;
import com.example.chatserver.enums.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoomRepository extends JpaRepository<Room, UUID> {

    List<Room> findByRoomType(RoomType roomType);

    @Override
    Optional<Room> findById(UUID id);

    @Query("SELECT DISTINCT r FROM Room r JOIN r.participants p WHERE p.email = :email")
    List<Room> findRoomsByUserEmail(@Param("email") String userEmail);
}