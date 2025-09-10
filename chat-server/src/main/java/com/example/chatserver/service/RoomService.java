package com.example.chatserver.service;


import com.example.chatserver.entity.Participant;
import com.example.chatserver.entity.Room;
import com.example.chatserver.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomService {

    private final RoomRepository roomRepository;


    public void updateRoom(Room room) {
        roomRepository.save(room);
    }

    public Room getRoomById(UUID id) {
        return roomRepository.findById(id).orElseThrow(() -> new RuntimeException("no room with the given ID"));
    }

    public List<Room> getRoomsWhereUserIsParticipant(Participant participant) {
        return roomRepository.findRoomsByUserEmail(participant.getEmail());
    }
}
