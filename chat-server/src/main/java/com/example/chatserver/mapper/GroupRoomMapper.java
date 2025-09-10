package com.example.chatserver.mapper;

import com.example.chatserver.DTO.GroupRoomDTO;
import com.example.chatserver.entity.GroupRoom;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface GroupRoomMapper {

    GroupRoomMapper INSTANCE = Mappers.getMapper(GroupRoomMapper.class);

    @Mapping(target = "messages", ignore = true)
    GroupRoomDTO toDto(GroupRoom groupRoom);

    @Mapping(source = "messages", target = "messages")
    GroupRoomDTO toDtoWithMessages(GroupRoom groupRoom);

    @Mapping(target = "messages", ignore = true)
    GroupRoom toEntity(GroupRoomDTO groupRoomDto);

    List<GroupRoomDTO> toDtoList(List<GroupRoom> groupRooms);
    List<GroupRoom> toEntityList(List<GroupRoomDTO> groupRoomDTOs);
}
