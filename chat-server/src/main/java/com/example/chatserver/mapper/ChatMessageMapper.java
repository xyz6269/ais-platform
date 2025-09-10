package com.example.chatserver.mapper;

import com.example.chatserver.DTO.ChatMessageDTO;
import com.example.chatserver.entity.ChatMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ChatMessageMapper {

    ChatMessageMapper INSTANCE = Mappers.getMapper(ChatMessageMapper.class);

    @Mapping(source = "room.id", target = "roomId")
    ChatMessageDTO toDTO(ChatMessage message);

    @Mapping(target = "room", ignore = true)
    ChatMessage toMessage(ChatMessageDTO dto);
}
