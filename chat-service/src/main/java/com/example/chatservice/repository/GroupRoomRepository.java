package com.example.chatservice.repository;


import com.example.chatservice.entity.GroupRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GroupRoomRepository extends JpaRepository<GroupRoom, UUID> {

    Optional<GroupRoom> findGroupRoomById(UUID uuid);

    @Query("SELECT DISTINCT r FROM GroupRoom r JOIN r.participants p WHERE p.email = :email")
    List<GroupRoom> findRoomsByUserEmail(@Param("email") String userEmail);

}
