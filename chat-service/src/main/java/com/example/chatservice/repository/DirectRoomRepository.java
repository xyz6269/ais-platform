package com.example.chatservice.repository;

import com.example.chatservice.entity.DirectRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DirectRoomRepository extends JpaRepository<DirectRoom, UUID> {

    Optional<DirectRoom> findDirectRoomById(UUID id);

    @Query("SELECT DISTINCT r FROM DirectRoom r JOIN r.participants p WHERE p.email = :email")
    List<DirectRoom> findRoomsByUserEmail(@Param("email") String userEmail);
}
