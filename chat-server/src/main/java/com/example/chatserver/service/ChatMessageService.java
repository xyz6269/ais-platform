package com.example.chatserver.service;

import com.example.chatserver.DTO.ChatMessageDTO;
import com.example.chatserver.entity.ChatMessage;
import com.example.chatserver.entity.Room;
import com.example.chatserver.mapper.ChatMessageMapper;
import com.example.chatserver.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final RoomService roomService;

    public void saveMessage(ChatMessageDTO dto) {
        ChatMessage message = ChatMessageMapper.INSTANCE.toMessage(dto);
        Room room = roomService.getRoomById(dto.roomId());
        message.setRoom(room);
        room.getMessages().add(message);
        chatMessageRepository.save(message);
        roomService.updateRoom(room);
    }

}
