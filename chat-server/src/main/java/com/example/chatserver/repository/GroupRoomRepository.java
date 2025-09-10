package com.example.chatserver.repository;


import com.example.chatserver.entity.GroupRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GroupRoomRepository extends JpaRepository<GroupRoom, UUID> {
    @Override
    Optional<GroupRoom> findById(UUID uuid);
}
