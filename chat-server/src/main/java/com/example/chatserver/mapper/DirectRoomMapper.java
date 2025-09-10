package com.example.chatserver.mapper;

import com.example.chatserver.DTO.DirectRoomDTO;
import com.example.chatserver.entity.DirectRoom;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DirectRoomMapper {

    DirectRoomMapper INSTANCE = Mappers.getMapper(DirectRoomMapper.class);

    @Mapping(target = "messages", ignore = true)
    DirectRoomDTO toDto(DirectRoom directRoom);

    @Mapping(source = "messages", target = "messages")
    DirectRoomDTO toDtoWithMessages(DirectRoom directRoom);

    @Mapping(target = "messages", ignore = true)
    DirectRoom toEntity(DirectRoomDTO directRoomDto);

    List<DirectRoomDTO> toDtoList(List<DirectRoom> directRooms);
    List<DirectRoom> toEntityList(List<DirectRoomDTO> directRoomDTOs);

}
